package br.com.talpi.backend.controller;

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
import br.com.talpi.requisito.Requisito;
import br.com.talpi.requisito.Tarefa;
import br.com.talpi.social.Comentarios;
import br.com.talpi.social.Votos;
import br.com.talpi.usuario.PapelUsuarioProjetoEnum;
import br.com.talpi.usuario.UsuarioProjeto;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

/**
 * Gerencia operações sob {@link Tarefa}
 * @author Rafael M. Lins
 *
 */
@Controller
@Path("/tarefa")
@RequerAutenticacao
public class TarefaController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	TarefaController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public TarefaController(final Logger log, final Result result, final Validator validator, final PersistenceService ps, final UsuarioLogado usuarioLogado, final HttpServletRequest request, final HttpServletResponse response) {
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
	 * @param projeto Projeto cujo usuário está tentando mexer
	 * @return {@link UsuarioProjeto} do usuário atual para o {@link Projeto}, se ele existir
	 */
	private UsuarioProjeto getUsuarioProjetoAtual(final Projeto projeto) {
		return (UsuarioProjeto) ps.createQuery("FROM UsuarioProjeto WHERE usuario = :usuarioLogado AND projeto = :projeto").setParameter("usuarioLogado", usuarioLogado.get()).setParameter("projeto", projeto).getSingleResult();
	}
	
	/**
	 * @param id Identificador do Projeto
	 * @param pm O usuário logado deve ser um PM para ter acesso ao projeto?
	 * @return {@link Projeto} especificado pelo {@code id} desde que ele pertença ao usuário logado atualmente ou o usuário logado seja {@link PapelUsuarioProjetoEnum#PM Project Manager} no projeto
	 */
	private Projeto getProjeto(final Long id, final boolean pm) {
		if (pm) {
			return (Projeto) ps.createQuery("SELECT p FROM Projeto p JOIN p.usuarios up WHERE (p.criador = :criador OR (up.id = :criador AND up.papel = :enumPM)) AND p.id = :id").setParameter("criador", usuarioLogado.get().getId()).setParameter("enumPM", PapelUsuarioProjetoEnum.PM).setParameter("id", id).getSingleResult();
		}
		else {
			return (Projeto) ps.createQuery("SELECT p FROM Projeto p JOIN p.usuarios up WHERE (p.criador = :criador OR up.id = :criador) AND p.id = :id").setParameter("criador", usuarioLogado.get().getId()).setParameter("id", id).getSingleResult();
		}
	}
	
	/**
	 * @param projeto Projeto a qual o requisito pertence
	 * @param id Identificador do Projeto
	 * @return Requisito, desde que exista no Projeto
	 */
	private Requisito getRequisito(final Projeto projeto, final Long id) {
		return (Requisito) ps.createQuery("FROM Requisito WHERE projeto = :projeto AND id = :id").setParameter("projeto", projeto).setParameter("id", id).getSingleResult();
	}
	
	/**
	 * <p><code>GET /tarefa/{projeto}/{requisito}/lista, GET /tarefa/{projeto}/{requisito}/lista/{pagina}, GET /tarefa/{projeto}/{requisito}/lista/{pagina}/{itens}</code>
	 * 
	 * <p>Retorna as Tarefas de um Requisito de um Projeto
	 * 
	 * @param requisito Requisito do qual se quer a lista de tarefas
	 * @param projeto Projeto a qual pertence o Requisito
	 * @param pagina Qual página de resultados o usuário quer? (0+, default: 0)
	 * @param itens Quantos itens por página serão buscados? (5-50, default: 10)
	 */
	@Get({ "/{projeto:\\d+}/{requisito:\\d+}/lista", "/{projeto:\\d+}/{requisito:\\d+}/lista/{pagina:\\d+}", "/{projeto:\\d+}/{requisito:\\d+}/lista/{pagina:\\d+}/{itens:\\d+}" })
	public void tarefas(final Long requisito, final Long projeto, final Integer pagina, final Integer itens) {
		final Projeto proj = getProjeto(projeto, false);
		
		if (proj != null) {
			final Requisito req = getRequisito(proj, requisito);
			
			if (req != null) {
				final int resultados = itens != null ? Math.max(Math.min(itens, 50), 5) : 10;
				final int offset = pagina == null ? 0 : pagina * resultados;
				final List<Tarefa> tarefas = ps.createQuery("FROM Tarefa WHERE requisito = :requisito").setMaxResults(resultados).setFirstResult(offset).setParameter("requisito", requisito).getResultList();
				result.use(Results.json()).withoutRoot().from(tarefas).include("estado", "comentarios", "votos").serialize();
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
		}
	}
	
	/**
	 * <p><code>GET /tarefa/{projeto}/{requisito}/{id}</code>
	 * 
	 * <p>Retorna todos os dados de uma {@link Tarefa} que pertence ao {@link Requisito} especificado, o qual pertence ao {@link Projeto} especificado
	 * 
	 * @param requisito Requisito do qual se quer a lista de tarefas
	 * @param projeto Projeto a qual pertence o Requisito
	 * @param id ID da Tarefa
	 */
	@Get("/{projeto:\\d+}/{requisito:\\d+}/{id:\\d+}")
	public void tarefa(final Long requisito, final Long projeto, final Long id) {
		final Projeto proj = getProjeto(projeto, false);
		
		if (proj != null) {
			final Requisito req = getRequisito(proj, requisito);
			
			if (req != null) {
				final Tarefa tarefa = (Tarefa) ps.createQuery("FROM Tarefa WHERE id = :id AND requisito = :requisito").setParameter("id", id).setParameter("requisito", requisito).getSingleResult();
				result.use(Results.json()).withoutRoot().from(tarefa).include("estado", "comentarios", "votos").serialize();
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
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
	@Post("/{projeto:\\d+}/{requisito:\\d+}")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void membros(final Long requisito, final Long projeto, Tarefa tarefa) {
		final Projeto proj = getProjeto(projeto, false);
		
		if (proj != null) {
			final Requisito req = getRequisito(proj, requisito);
			
			if (req != null) {
				final UsuarioProjeto up = getUsuarioProjetoAtual(proj);
				
				if (tarefa.getId() == null) {
					if (up.getPapel().equals(PapelUsuarioProjetoEnum.PM)) {
						tarefa.setRequisito(req);
						tarefa.setComentarios(new Comentarios());
						tarefa.setVotos(new Votos());
						tarefa.setCriador(up);
						tarefa.setEstado(new Estado());
						tarefa.getEstado().setEstado(EstadoEnum.CRIADO);
						
						if (!validator.validate(tarefa).hasErrors()) {
							try {
								ps.persist(tarefa);
								result.use(Results.json()).withoutRoot().from(tarefa).include("estado", "comentarios", "votos").serialize();
							} catch (final Exception e) {
								log.error("Erro ao persistir Tarefa", e);
							}
						}
					}
					else {
						validator.add(new I18nMessage("error", "erro.tarefa.semPermissaoCriar"));
					}
				}
				else {
					final Tarefa tarefaBanco = (Tarefa) ps.createQuery("FROM Tarefa WHERE id = :id AND requisito = :requisito").setParameter("id", tarefa.getId()).setParameter("requisito", requisito).getSingleResult();
					
					if (tarefaBanco != null) {
						if (tarefaBanco.getCriador().getId().equals(up.getId()) || up.getPapel().equals(PapelUsuarioProjetoEnum.PM)) {
							tarefa.setComentarios(tarefaBanco.getComentarios());
							tarefa.setCriador(tarefaBanco.getCriador());
							tarefa.setRequisito(req);
							tarefa.setTempoTrabalhado(tarefaBanco.getTempoTrabalhado());
							tarefa.setTimestamp(tarefaBanco.getTimestamp());
							tarefa.setVotos(tarefaBanco.getVotos());
							
							if (!validator.validate(tarefa).hasErrors()) {
								try {
									tarefa = ps.merge(tarefa);
									result.use(Results.json()).withoutRoot().from(tarefa).include("estado", "comentarios", "votos").serialize();
								} catch (final Exception e) {
									log.error("Erro ao persistir Tarefa", e);
								}
							}
						}
						else {
							validator.add(new I18nMessage("error", "erro.tarefa.semPermissaoEditar"));
						}
					}
					else {
						result.notFound();
					}
				}
			}
			else {
				result.notFound();
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
