package br.com.talpi.backend.controller;

import java.util.ArrayList;
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
import br.com.talpi.requisito.HistoricoRequisito;
import br.com.talpi.requisito.Projeto;
import br.com.talpi.requisito.Requisito;
import br.com.talpi.social.Comentarios;
import br.com.talpi.social.Votos;
import br.com.talpi.usuario.PapelUsuarioProjetoEnum;
import br.com.talpi.usuario.UsuarioProjeto;
import br.com.talpi.util.PersistenceService;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

@Controller
@Path("/requisito/{pid:\\d+}")
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
	
	/**
	 * Simplifica o envio de um {@link Requisito} como resposta a uma requisição
	 * @param requisito Requsito a ser enviado
	 */
	private void respostaRequisito(final Requisito requisito) {
		result.use(Results.json()).withoutRoot().from(requisito).include("criador", "estado", "comentarios", "votos").serialize();
	}
	
	/**
	 * @param id Identificador do Projeto
	 * @return {@link Projeto} especificado pelo {@code id} desde que ele pertença ao usuário logado atualmente ou o usuário logado seja {@link PapelUsuarioProjetoEnum#PM Project Manager} no projeto
	 */
	private Projeto getProjeto(final Long id) {
		return getProjeto(id, true);
	}
	
	/**
	 * @param id Identificador do Projeto
	 * @param pm O usuário logado deve ser um PM para ter acesso ao projeto?
	 * @return {@link Projeto} especificado pelo {@code id} desde que ele pertença ao usuário logado atualmente ou o usuário logado seja {@link PapelUsuarioProjetoEnum#PM Project Manager} no projeto
	 */
	private Projeto getProjeto(final Long id, final boolean pm) {
		if (pm) {
			return (Projeto) ps.createQuery("SELECT p FROM Projeto p JOIN p.usuarios up WHERE (p.criador = :criador OR (up.id = :criador AND up.papel = :enumPM)) AND p.id = :id").setParameter("criador", usuarioLogado.get()).setParameter("enumPM", PapelUsuarioProjetoEnum.PM).setParameter("id", id).getSingleResult();
		}
		else {
			return (Projeto) ps.createQuery("SELECT p FROM Projeto p JOIN p.usuarios up WHERE (p.criador = :criador OR up.id = :criador) AND p.id = :id").setParameter("criador", usuarioLogado.get()).setParameter("id", id).getSingleResult();
		}
	}
	
	/**
	 * @param projeto Projeto a qual o requisito pertence
	 * @param id Identificador do Projeto
	 * @return Requisito, desde que exista no Projeto
	 */
	private Requisito get(final Projeto projeto, final Long id) {
		return (Requisito) ps.createQuery("FROM Requisito WHERE projeto = :projeto AND id = :id").setParameter("projeto", projeto).setParameter("id", id).getSingleResult();
	}
	
	/**
	 * <p><code>GET /requisito/{pid}/lista, GET /requisito/{pid}/lista/{pagina}, GET /requisito/{pid}/lista/{pagina}/{itens}</code>
	 * 
	 * <p>Responde com uma listagem dos requisitos de um projeto
	 * 
	 * @param pid ID do Projeto
	 * @param pagina Qual página de resultados o usuário quer
	 * @param itens Quantidade de itens por página
	 */
	@Get({ "/lista", "/lista/{pagina:\\d+}", "/lista/{pagina:\\d+}/{itens:\\d+}" })
	public void requisitos(final Long pid, final Integer pagina, final Integer itens) {
		final Projeto projeto = getProjeto(pid);
		
		if (projeto != null) {
			final int resultados = itens != null ? Math.max(Math.min(itens, 50), 5) : 20;
			final int offset = pagina == null ? 0 : pagina * resultados;
			final List<Requisito> requisitos = ps.createQuery("FROM Requisito WHERE projeto = :projeto").setParameter("projeto", projeto).setMaxResults(resultados).setFirstResult(offset).getResultList();
			result.use(Results.json()).withoutRoot().from(requisitos).include("votos").serialize();
		}
		else {
			result.notFound();
		}
	}
	
	/**
	 * <p><code>GET /requisito/{pid}/{id}</code>
	 * 
	 * <p>Retorna todas as informações de um requisito
	 * 
	 * @param pid ID do Projeto
	 * @param id ID do Requisito (deve obrigatoriamente pertencer ao projeto especificado)
	 */
	@Get("/{id:\\d+}")
	public void requisito(final Long pid, final Long id) {
		final Projeto projeto = getProjeto(pid);
		
		if (projeto != null) {
			final Requisito requisito = get(projeto, id);
			
			if (requisito != null) {
				respostaRequisito(requisito);
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
		}
	}

	@Transactional
	@Post("/editar")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void editar(final Long pid, Requisito requisito) {
		final Projeto projeto = getProjeto(pid);
		
		if (projeto != null) {
			if (projeto.isCongelado()) {
				validator.add(new I18nMessage("error", "erro.requisito.projetoCongelado"));
			}
			else {
				final UsuarioProjeto up = getUsuarioProjetoAtual(projeto);
				
				if (up != null && up.getPapel().equals(PapelUsuarioProjetoEnum.PM)) {
					if (requisito.getUltimaAlteracao() != null) {
						requisito.getUltimaAlteracao().setRequisito(requisito);
						requisito.getUltimaAlteracao().setId(null);
						requisito.getUltimaAlteracao().setUsuario(up);
					}
					
					// Novo
					if (requisito.getId() == null) {
						final List<HistoricoRequisito> historico = new ArrayList<>(1);
						historico.add(requisito.getUltimaAlteracao());
						
						requisito.setProjeto(projeto);
						requisito.setComentarios(new Comentarios());
						requisito.setVotos(new Votos());
						requisito.setCriador(up);
						requisito.setHistorico(historico);
						
						if (!validator.validate(requisito).hasErrors()) {
							try {
								ps.persist(requisito);
								respostaRequisito(requisito);
							} catch (final Exception e) {
								log.error("Erro ao persistir novo requisito", e);
								validator.add(new I18nMessage("error", "erro.requisito.criacaoException"));
							}
						}
					}
					// Alteração
					else {
						final Requisito requisitoBanco = (Requisito) ps.createQuery("FROM Requisito WHERE id = :id AND projeto = :projeto").setParameter("id", requisito.getId()).setParameter("projeto", projeto).getSingleResult();
						
						if (requisitoBanco != null) {
							requisito.setProjeto(projeto);
							requisito.setComentarios(requisitoBanco.getComentarios());
							requisito.setVotos(requisitoBanco.getVotos());
							requisito.setHistorico(requisitoBanco.getHistorico());
							requisito.setCriador(requisitoBanco.getCriador());
							requisito.setTarefas(requisitoBanco.getTarefas());
							
							requisito.getHistorico().add(requisito.getUltimaAlteracao());
							
							if (!validator.validate(requisito).hasErrors()) {
								try {
									requisito = ps.merge(requisito);
									respostaRequisito(requisito);
								} catch (final Exception e) {
									log.error("Erro ao alterar requisito", e);
									validator.add(new I18nMessage("error", "erro.requisito.alteracaoException"));
								}
							}
						}
						else {
							validator.add(new I18nMessage("error", "erro.requisito.semAutorizacao"));
						}
					}
				}
				else {
					validator.add(new I18nMessage("error", "erro.requisito.semAutorizacao"));
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
	
	private UsuarioProjeto getUsuarioProjetoAtual(final Projeto projeto) {
		return (UsuarioProjeto) ps.createQuery("FROM UsuarioProjeto WHERE usuario = :usuarioLogado AND projeto = :projeto").setParameter("usuarioLogado", usuarioLogado.get()).setParameter("projeto", projeto).getSingleResult();
	}
}
