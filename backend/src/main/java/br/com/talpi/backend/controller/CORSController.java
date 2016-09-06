package br.com.talpi.backend.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Options;
import br.com.caelum.vraptor.Result;

/**
 * Implementa de forma bem simples e direta o protocolo CORS (Cross-Origin Resource Sharing)
 * @author Rafael Lins
 * @see https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS
 *
 */
@Controller
public class CORSController {
	@Inject
	private HttpServletResponse response;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private Result result;
	
	/**
	 * <p><code>OPTIONS /*</code>
	 * <p>Qualquer requisição feita de outro domínio vai, primeiro, fazer um {@code OPTIONS} na URL
	 * para saber quais as permissões de CORS. Esse método é o <em>catch-all</em> para qualquer chamada {@code OPTIONS}
	 * em qualquer URL. Ele responde com os cabeçalhos necessários e nada mais.
	 * 
	 * @see Options
	 */
	@Options("/*")
	public void cors() {
		response.setHeader("Access-Control-Max-Age", "1000");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Origin, Authorization, Accept, Client-Security-Token, Accept-Encoding");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		result.nothing();
	}
}
