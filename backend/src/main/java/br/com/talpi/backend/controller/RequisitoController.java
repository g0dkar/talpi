package br.com.talpi.backend.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.com.talpi.requisito.Requisito;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

@Controller
@Path("/requisito/{projeto:\\d+}")
@RequerAutenticacao
public class RequisitoController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	RequisitoController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public RequisitoController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado, final HttpServletRequest request, final HttpServletResponse response) {
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
	
	private Requisito get(final Long projeto, final Long id) {
		return (Requisito) ps.createQuery("FROM Requisito WHERE projeto = :projeto AND id = :id").setParameter("projeto", projeto).setParameter("id", id).getSingleResult();
	}
	
	@Get({ "/lista", "/lista/{pagina:\\d+}", "/lista/{pagina:\\d+}/{itens:\\d+}" })
	public void requisitos(final Long projeto, final Integer pagina, final Integer itens) {
		final int resultados = itens != null ? Math.max(Math.min(itens, 50), 5) : 20;
		final int offset = pagina == null ? 0 : pagina * resultados;
		final List<Requisito> requisitos = ps.createQuery("FROM Requisito WHERE projeto = :projeto").setMaxResults(resultados).setFirstResult(offset).setParameter("criador", usuarioLogado.get().getId()).getResultList();
		result.use(Results.json()).withoutRoot().from(requisitos).include("votos", "comentarios").serialize();
	}
	
//	@Get("/{id:\\d+}")
//	public void projeto(final Long id) {
//		final Projeto projeto = get(id);
//
//		if (projeto != null) {
//			result.use(Results.json()).withoutRoot().from(projeto).serialize();
//		}
//		else {
//			result.notFound();
//		}
//	}
//
//	@Transactional
//	@Post("/editar")
//	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
//	public void projeto(final Projeto projeto) {
//		if (projeto.getId() == null) {
//			if (!usuarioLogado.get().isPremium()) {
//				final int projetos = ((Number) ps.createQuery("SELECT count(*) FROM Projeto WHERE criador = :criador").setParameter("criador", usuarioLogado.get().getId()).getSingleResult()).intValue();
//
//				if (projetos >= 5) {
//					validator.add(new I18nMessage("error", "erro.maisDeCincoProjetos"));
//				}
//				else {
//					validator.validate(projeto);
//				}
//			}
//			else {
//				validator.validate(projeto);
//			}
//
//			if (!validator.hasErrors()) {
//				projeto.setCriador(usuarioLogado.get());
//				projeto.setEstado(new Estado());
//				projeto.getEstado().setEstado(EstadoEnum.INICIADO);
//
//				try {
//					ps.persist(projeto);
//
//					final UsuarioProjeto usuarioProjeto = new UsuarioProjeto();
//					usuarioProjeto.setCriador(usuarioLogado.get());
//					usuarioProjeto.setPapel(PapelUsuarioProjetoEnum.PM);
//					usuarioProjeto.setProjeto(projeto);
//					usuarioProjeto.setUsuario(usuarioLogado.get());
//					ps.persist(usuarioProjeto);
//
//					result.use(Results.json()).withoutRoot().from(projeto).serialize();
//				} catch (final Exception e) {
//					log.error("Erro ao salvar Projeto", e);
//					validator.add(new I18nMessage("error", "erro.projeto.persistir", e.getClass().getSimpleName()));
//				}
//			}
//		}
//		else {
//			final Projeto db = get(projeto.getId());
//			if (db != null) {
//				db.setNome(projeto.getNome());
//				db.setCongelado(projeto.isCongelado());
//				db.setDescricao(projeto.getDescricao());
//
//				if (!validator.validate(db).hasErrors()) {
//					final Projeto salvo = ps.merge(db);
//					result.use(Results.json()).withoutRoot().from(salvo).serialize();
//				}
//			}
//			else {
//				validator.add(new I18nMessage("error", "erro.projeto.naoEncontrado", projeto.getNome()));
//			}
//		}
//
//		if (validator.hasErrors()) {
//			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
//		}
//	}
}
