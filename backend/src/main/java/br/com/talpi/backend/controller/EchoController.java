package br.com.talpi.backend.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class EchoController {
	@Inject
	private Result result;
	
	@Path("/echo")
	public void echo(final String e) {
		result.use(Results.http()).body(e);
	}
}
