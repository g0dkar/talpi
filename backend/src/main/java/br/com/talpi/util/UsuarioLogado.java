package br.com.talpi.util;

import javax.enterprise.context.SessionScoped;

import br.com.talpi.usuario.Usuario;

@SessionScoped
public class UsuarioLogado {
	private Usuario usuario;
	
	public Usuario get() {
		return usuario;
	}
	
	public void set(final Usuario usuario) {
		this.usuario = usuario;
	}
}
