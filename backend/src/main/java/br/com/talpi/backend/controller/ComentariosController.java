package br.com.talpi.backend.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

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
import br.com.talpi.social.Comentario;
import br.com.talpi.social.Comentarios;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

@Controller
@Path("/comentarios")
@RequerAutenticacao
public class ComentariosController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	ComentariosController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public ComentariosController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado, final HttpServletRequest request, final HttpServletResponse response) {
		this.log = log;
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.usuarioLogado = usuarioLogado;

		if (response != null) {
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}
	}
	
	/**
	 * <p><code>GET /comentarios/comentario/{id}</code>
	 * 
	 * <p>Retorna um {@link Comentario} do banco de dados
	 * 
	 * @param id Identificador do {@link Comentario}
	 */
	@Get("/comentario/{id:\\d+}")
	public void comentario(final Long id) {
		final Comentario comentario = ps.find(Comentario.class, id);
		if (comentario != null) {
			result.use(Results.json()).withoutRoot().from(comentario).include("usuario").serialize();
		}
		else {
			result.notFound();
		}
	}
	
	/**
	 * <p><code>GET /comentarios/{id}</code>
	 * 
	 * <p>Retorna uma thread de {@link Comentarios} do banco de dados
	 * 
	 * @param id Identificador da thread de {@link Comentarios}
	 */
	@Get("/{id:\\d+}")
	public void thread(final Long id) {
		final Comentarios comentarios = ps.find(Comentarios.class, id);
		if (comentarios != null) {
			result.use(Results.json()).withoutRoot().from(comentarios).include("comentarios", "comentarios.usuario", "melhorComentario", "melhorComentario.usuario").serialize();
		}
		else {
			result.notFound();
		}
	}
	
	/**
	 * <p><code>POST /comentarios/{id}</code>
	 * 
	 * <p>Inclui ou altera um {@link Comentario} em uma thread de {@link Comentarios}
	 * 
	 * @param id Identificador da thread de {@link Comentarios}
	 * @param comentario O Comentário a ser incluído/alterado
	 */
	@Transactional
	@Post("/{id:\\d+}")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void salvarComentario(final Long id, Comentario comentario) {
		final Comentarios comentarios = ps.find(Comentarios.class, id);
		
		if (comentarios != null) {
			if (comentario.getId() != null) {
				final Comentario comentarioBanco = (Comentario) ps.createQuery("FROM Comentario WHERE id = :id AND pai = :pai").setParameter("id", comentario.getId()).setParameter("pai", comentarios).getSingleResult();
				
				if (comentarioBanco != null) {
					if (comentarioBanco.getUsuario().getId().equals(usuarioLogado.get().getId())) {
						comentario.setTimestamp(comentarioBanco.getTimestamp());
					}
					else {
						validator.add(new I18nMessage("error", "erro.comentario.naoAutorizado"));
					}
				}
				else {
					validator.add(new I18nMessage("error", "erro.comentario.naoEncontrado"));
				}
			}
			
			if (!validator.hasErrors()) {
				if (comentario.getPai() != null && comentario.getPai().getId() != null) {
					comentario.setPai(ps.find(Comentario.class, comentario.getPai().getId()));
				}
				else {
					comentario.setPai(null);
				}
				
				comentario.setUsuario(usuarioLogado.get());
				
				if (!validator.validate(comentario).hasErrors()) {
					try {
						if (comentario.getId() != null) {
							ps.persist(comentario);
						}
						else {
							comentario = ps.merge(comentario);
						}
						result.use(Results.json()).withoutRoot().from(comentarios).include("comentarios", "comentarios.usuario", "melhorComentario", "melhorComentario.usuario").serialize();
					} catch (final Exception e) {
						log.error("Erro ao salvar comentário", e);
						validator.add(new I18nMessage("error", "erro.comentario.persistir", e.getClass().getSimpleName()));
					}
				}
			}
		}
		else {
			result.notFound();
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
}
