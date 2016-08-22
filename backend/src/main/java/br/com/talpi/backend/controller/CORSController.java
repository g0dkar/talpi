package br.com.talpi.backend.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Options;
import br.com.caelum.vraptor.Result;

@Controller
public class CORSController {
	@Inject
	private HttpServletResponse response;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private Result result;
	
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
