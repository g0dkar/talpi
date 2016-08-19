package br.com.talpi.backend.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.com.talpi.usuario.Usuario;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.UsuarioLogado;

@Controller
@Path("/usuario")
public class UsuarioController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	UsuarioController() { this(null, null, null, null, null); }
	
	@Inject
	public UsuarioController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado) {
		this.log = log;
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.usuarioLogado = usuarioLogado;
	}
	
	@Get({ "/perfil", "/perfil/{id:\\d+}" })
	public void perfil(final Long id) {
		if (usuarioLogado.get() != null) {
			if (id == null) {
				result.use(Results.json()).withoutRoot().from(usuarioLogado.get()).serialize();
			}
			else {
				final Usuario usuario = ps.find(Usuario.class, id);
				if (usuario != null) {
					result.use(Results.json()).withoutRoot().from(usuario).serialize();
				}
				else {
					result.notFound();
				}
			}
		}
		else {
			result.use(Results.http()).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@Transactional
	@Post("/perfil")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void perfil(Usuario usuario) {
		if (usuario != null && !validator.validate(usuario).hasErrors()) {
			if (usuario.getId() != null && usuarioLogado.get() != null) {
				// Evita que um usuário edite outro via injeção de ID
				usuario.setId(usuarioLogado.get().getId());
				usuario.setTimestampCriacao(usuarioLogado.get().getTimestampCriacao());
				
				if (usuario.getSenha() != null) {
					usuario.setSenha(BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()));
				}
				else {
					usuario.setSenha(usuarioLogado.get().getSenha());
				}
			}
			else if (usuario.getId() == null) {
				usuario.setSenha(BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()));
			}
			else {
				validator.add(new I18nMessage("error", "erros.semAutorizacao"));
			}
			
			if (!validator.hasErrors()) {
				final boolean emailExiste;
				
				if (usuario.getId() == null) {
					emailExiste = ((Number) ps.createQuery("SELECT count(*) FROM Usuario WHERE LOWER(email) = LOWER(:email)").setParameter("email", usuario.getEmail().trim()).getSingleResult()).intValue() == 0;
				}
				else {
					emailExiste = ((Number) ps.createQuery("SELECT count(*) FROM Usuario WHERE id <> :id AND LOWER(email) = LOWER(:email)").setParameter("id", usuario.getId()).setParameter("email", usuario.getEmail().trim()).getSingleResult()).intValue() == 0;
				}
				
				if (emailExiste) {
					validator.add(new I18nMessage("error", "erros.usuario.emailExiste", usuario.getEmail()));
				}
				
				try {
					if (usuario.getId() == null) {
						ps.persist(usuario);
					}
					else {
						usuario = ps.merge(usuario);
					}
					
					result.use(Results.json()).withoutRoot().from(usuario).serialize();
				} catch (final Exception e) {
					log.error("Erro ao salvar Usuário", e);
					validator.add(new I18nMessage("error", "erros.usuario.exception" + (usuario.getId() == null ? "Persist" : "Merge"), e.getClass().getSimpleName()));
				}
			}
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	@Transactional
	@Post("/login")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void login(final String email, final String senha) {
		if (email != null && email.trim().length() > 0 && senha != null && senha.trim().length() > 0) {
			final Usuario usuario = (Usuario) ps.createQuery("FROM Usuario WHERE LOWER(email) = LOWER(:email)").setParameter("email", email.trim()).getSingleResult();
			
			if (usuario != null) {
				if (BCrypt.checkpw(senha, usuario.getSenha())) {
					usuarioLogado.set(usuario);
					ps.createQuery("UPDATE Usuario SET timestampUltimoLogin = NOW() WHERE id = :id").setParameter("id", usuario.getId()).executeUpdate();
					result.forwardTo(this).perfil((Long) null);
				}
				else {
					validator.add(new I18nMessage("error", "erro.login.usuarioNaoExiste"));
				}
			}
			else {
				validator.add(new I18nMessage("error", "erro.login.usuarioNaoExiste"));
			}
		}
		else {
			validator.add(new I18nMessage("error", "erro.login.usuarioNaoExiste"));
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	public void logout() {
		usuarioLogado.set(null);
		result.nothing();
	}
}
