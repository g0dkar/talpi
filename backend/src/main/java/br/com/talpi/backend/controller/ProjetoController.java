package br.com.talpi.backend.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import br.com.talpi.estado.Estado;
import br.com.talpi.estado.EstadoEnum;
import br.com.talpi.requisito.Projeto;
import br.com.talpi.usuario.PapelUsuarioProjetoEnum;
import br.com.talpi.usuario.Usuario;
import br.com.talpi.usuario.UsuarioProjeto;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

/**
 * Gerencia operações relacionadas diretamente aos Projetos
 * 
 * @author Rafael Lins
 *
 */
@Controller
@Path("/projeto")
@RequerAutenticacao
public class ProjetoController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	ProjetoController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public ProjetoController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado, final HttpServletRequest request, final HttpServletResponse response) {
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
	 * @param id Identificador do Projeto
	 * 
	 * @return {@link Projeto} especificado pelo {@code id} desde que ele pertença ao usuário logado atualmente ou o usuário logado seja {@link PapelUsuarioProjetoEnum#PM Project Manager} no projeto
	 */
	private Projeto get(final Long id) {
		return (Projeto) ps.createQuery("FROM Projeto p LEFT JOIN UsuarioProjeto up ON up.projeto = p WHERE (p.criador = :criador OR (up.id = :criador AND up.papel = :enumPM)) AND p.id = :id").setParameter("criador", usuarioLogado.get().getId()).setParameter("enumPM", PapelUsuarioProjetoEnum.PM).setParameter("id", id).getSingleResult();
	}
	
	/**
	 * <p><code>GET /projeto/lista, GET /projeto/lista/{pagina}, GET /projeto/lista/{pagina}/{itens}</code>
	 * 
	 * <p>Retorna Projetos aos quais o usuário logado tem acesso
	 * 
	 * @param pagina Qual página de resultados o usuário quer? (0+, default: 0)
	 * @param itens Quantos itens por página serão buscados? (5-50, default: 10)
	 */
	@Get({ "/lista", "/lista/{pagina:\\d+}", "/lista/{pagina:\\d+}/{itens:\\d+}" })
	public void projetos(final Integer pagina, final Integer itens) {
		final int resultados = itens != null ? Math.max(Math.min(itens, 50), 5) : 10;
		final int offset = pagina == null ? 0 : pagina * resultados;
		final List<Projeto> projetos = ps.createQuery("FROM Projeto p LEFT JOIN UsuarioProjeto up ON up.projeto = p WHERE p.criador = :criador OR (up.id = :criador AND up.papel = :enumPM)").setMaxResults(resultados).setFirstResult(offset).setParameter("criador", usuarioLogado.get()).getResultList();
		result.use(Results.json()).withoutRoot().from(projetos).serialize();
	}
	
	/**
	 * <p><code>GET /projeto/{id}</code>
	 * 
	 * <p>Retorna todos os dados de um {@link Projeto}, desde que o usuário tenha acesso ao mesmo
	 * 
	 * @param id ID do Projeto
	 */
	@Get("/{id:\\d+}")
	public void projeto(final Long id) {
		final Projeto projeto = get(id);
		
		if (projeto != null) {
			result.use(Results.json()).withoutRoot().from(projeto).include("estado").serialize();
		}
		else {
			result.notFound();
		}
	}
	
	/**
	 * <p><code>POST /projeto/editar</code>
	 * 
	 * <p>Cria ou edita {@link Projeto projetos}. Edição só é permitida se o usuário for {@link PapelUsuarioProjetoEnum#PM Project Manager} ou o criador do projeto
	 * 
	 * @param pagina Qual página de resultados o usuário quer? (0+, default: 0)
	 * @param itens Quantos itens por página serão buscados? (5-50, default: 10)
	 */
	@Transactional
	@Post("/editar")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void projeto(final Projeto projeto, final List<UsuarioProjeto> membros) {
		if (projeto.getId() == null) {
			final UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
			usuarioProjeto.setCriador(usuarioLogado.get());
			usuarioProjeto.setPapel(PapelUsuarioProjetoEnum.PM);
			usuarioProjeto.setProjeto(projeto);
			usuarioProjeto.setUsuario(usuarioLogado.get());
			
			final List<UsuarioProjeto> membrosProjeto = new ArrayList<>(1);
			membrosProjeto.add(usuarioProjeto);
			
			projeto.setUsuarios(membrosProjeto);
			projeto.setCriador(usuarioLogado.get());
			
			if (!usuarioLogado.get().isPremium()) {
				final int projetos = ((Number) ps.createQuery("SELECT count(*) FROM Projeto WHERE criador = :criador").setParameter("criador", usuarioLogado.get()).getSingleResult()).intValue();
				
				if (projetos >= 5) {
					validator.add(new I18nMessage("error", "erro.maisDeCincoProjetos"));
				}
				else {
					validator.validate(projeto);
				}
			}
			else {
				validator.validate(projeto);
			}
			
			if (!validator.hasErrors()) {
				projeto.setEstado(new Estado());
				projeto.getEstado().setEstado(EstadoEnum.INICIADO);
				
				try {
					ps.persist(projeto);
					
					if (membros != null && !membros.isEmpty()) {
						result.forwardTo(this).membros(projeto.getId(), membros);
					}
					else {
						result.use(Results.json()).withoutRoot().from(projeto).include("estado").serialize();
					}
				} catch (final Exception e) {
					log.error("Erro ao salvar Projeto", e);
					validator.add(new I18nMessage("error", "erro.projeto.persistir", e.getClass().getSimpleName()));
				}
			}
		}
		else {
			final Projeto db = get(projeto.getId());
			if (db != null) {
				db.setNome(projeto.getNome());
				db.setCongelado(projeto.isCongelado());
				db.setDescricao(projeto.getDescricao());
				
				if (!validator.validate(db).hasErrors()) {
					final Projeto salvo = ps.merge(db);
					result.use(Results.json()).withoutRoot().from(salvo).include("estado").serialize();
				}
			}
			else {
				validator.add(new I18nMessage("error", "erro.projeto.naoEncontrado"));
			}
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	/**
	 * <p><code>POST /projeto/{id}/membros</code>
	 * 
	 * <p>Altera a lista de membros de um {@link Projeto}. O criador do projeto sempre será um {@link PapelUsuarioProjetoEnum#PM Project Manager} do projeto
	 * 
	 * @param id Id do Projeto
	 * @param membros Lista <strong>completa</strong> de membros do projeto
	 */
	@Transactional
	@Post("/{id:\\d+}/membros")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void membros(final Long id, final List<UsuarioProjeto> membros) {
		if (id != null) {
			Projeto projeto = get(id);
			
			if (projeto != null) {
				if (!contemUsuario(membros, projeto.getCriador())) {
					final UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
					usuarioProjeto.setPapel(PapelUsuarioProjetoEnum.PM);
					usuarioProjeto.setUsuario(usuarioLogado.get());
					membros.add(usuarioProjeto);
				}
				
				for (final Iterator<UsuarioProjeto> iterator = membros.iterator(); iterator.hasNext();) {
					final UsuarioProjeto usuarioProjeto = iterator.next();
					
					if (usuarioProjeto.getPapel() != null) {
						usuarioProjeto.setCriador(usuarioLogado.get());
						usuarioProjeto.setProjeto(projeto);
						usuarioProjeto.setUsuario(ps.find(Usuario.class, usuarioProjeto.getUsuario().getId()));
					}
					else {
						iterator.remove();
					}
				}
				
				projeto.setUsuarios(membros);
				try {
					projeto = ps.merge(projeto);
					result.use(Results.json()).withoutRoot().from(projeto).include("estado").serialize();
				} catch (final Exception e) {
					log.error("Erro ao modificar membros do projeto", e);
					validator.add(new I18nMessage("error", "erro.projeto.erroMembros"));
				}
			}
			else {
				validator.add(new I18nMessage("error", "erro.projeto.naoEncontrado"));
			}
		}
		else {
			result.notFound();
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	/**
	 * @return {@code true} se o usuário especificado existe na lista de membros
	 */
	private boolean contemUsuario(final List<UsuarioProjeto> membros, final Usuario criador) {
		for (final UsuarioProjeto usuarioProjeto : membros) {
			if (usuarioProjeto.getId().equals(criador.getId())) {
				return true;
			}
		}
		
		return false;
	}
}
