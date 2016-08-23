package br.com.talpi.seguranca;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;
import br.com.talpi.util.RequerAutenticacao;
import br.com.talpi.util.UsuarioLogado;

/**
 * Garante que o acesso a classes e métodos anotados com {@link RequerAutenticacao} só será feito se houver um usuario logado
 * @author g0dkar
 *
 */
@Intercepts
@RequestScoped
public class AutenticacaoInterceptor {
	private final Logger log;
	private final Result result;
	private final UsuarioLogado usuarioLogado;
	
	/** @deprecated CDI */ @Deprecated
	AutenticacaoInterceptor() { this(null, null, null); }
	
	@Inject
	public AutenticacaoInterceptor(final Logger log, final Result result, final UsuarioLogado usuarioLogado) {
		this.log = log;
		this.result = result;
		this.usuarioLogado = usuarioLogado;
	}
	
	@AroundCall
    public void intercept(final SimpleInterceptorStack stack) {
		if (usuarioLogado.get() == null) {
			log.error("Acesso não autorizado!");
			result.use(Results.http()).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else {
			stack.next();
		}
	}
	
	@Accepts
	public boolean accepts(final ControllerMethod method) {
	    return method.containsAnnotation(RequerAutenticacao.class) || method.getController().getType().isAnnotationPresent(RequerAutenticacao.class);
	}
}
