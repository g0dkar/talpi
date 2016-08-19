package br.com.talpi.backend.controller;

import java.util.List;

import javax.inject.Inject;
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
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

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
	ProjetoController() { this(null, null, null, null, null); }
	
	@Inject
	public ProjetoController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado) {
		this.log = log;
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.usuarioLogado = usuarioLogado;
	}
	
	private Projeto get(final Long id) {
		return (Projeto) ps.createQuery("FROM Projeto WHERE criador = :criador AND id = :id").setParameter("criador", usuarioLogado.get().getId()).setParameter("id", id).getSingleResult();
	}
	
	@Get({ "/projetos", "/projetos/{pagina:\\d+}", "/projetos/{pagina:\\d+}/{itens:\\d+}" })
	public void projetos(final Integer pagina, final Integer itens) {
		final int resultados = itens != null ? Math.max(Math.min(itens, 50), 5) : 10;
		final int offset = pagina == null ? 0 : pagina * resultados;
		final List<Projeto> projetos = ps.createQuery("FROM Projeto WHERE criador = :criador").setMaxResults(resultados).setFirstResult(offset).setParameter("criador", usuarioLogado.get().getId()).getResultList();
		result.use(Results.json()).withoutRoot().from(projetos).serialize();
	}
	
	@Get("/projeto/{id:\\d+}")
	public void projeto(final Long id) {
		final Projeto projeto = get(id);
		
		if (projeto != null) {
			result.use(Results.json()).withoutRoot().from(projeto).serialize();
		}
		else {
			result.notFound();
		}
	}
	
	@Transactional
	@Post("/projeto")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void perfil(final Projeto projeto) {
		if (projeto.getId() == null) {
			if (!usuarioLogado.get().isPremium()) {
				final int projetos = ((Number) ps.createQuery("SELECT count(*) FROM Projeto WHERE criador = :criador").setParameter("criador", usuarioLogado.get().getId()).getSingleResult()).intValue();
				
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
				projeto.setCriador(usuarioLogado.get());
				projeto.setEstado(new Estado());
				projeto.getEstado().setEstado(EstadoEnum.INICIADO);
				
				try {
					ps.persist(projeto);
					result.use(Results.json()).withoutRoot().from(projeto).serialize();
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
					result.use(Results.json()).withoutRoot().from(salvo).serialize();
				}
			}
			else {
				validator.add(new I18nMessage("error", "erro.projeto.naoEncontrado", projeto.getNome()));
			}
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
}
