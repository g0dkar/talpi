package br.com.talpi.backend.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
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
	
	@Post("/{id:\\d+}")
	public void postAtThread(final Long id) {
		final Comentarios comentarios = ps.find(Comentarios.class, id);
		result.use(Results.json()).withoutRoot().from(comentarios).include("comentarios", "comentarios.usuario", "melhorComentario", "melhorComentario.usuario").serialize();
	}
}
