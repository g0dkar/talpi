package br.com.talpi.util;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.talpi.usuario.Usuario;

@SessionScoped
public class UsuarioLogado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	@Inject
	private Logger log;
	
	@PostConstruct
	public void criou() {
		log.info("Criou o UsuarioLogado");
	}
	
	public Usuario get() {
		return usuario;
	}
	
	public void set(final Usuario usuario) {
		log.info("Setou o UsuarioLogado: {} ({})", usuario.getId(), usuario);
		this.usuario = usuario;
	}
}
