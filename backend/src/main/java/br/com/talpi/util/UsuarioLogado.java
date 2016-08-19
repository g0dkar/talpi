package br.com.talpi.util;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import br.com.talpi.usuario.Usuario;

@SessionScoped
public class UsuarioLogado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public Usuario get() {
		return usuario;
	}
	
	public void set(final Usuario usuario) {
		this.usuario = usuario;
	}
}
