package br.com.talpi.util;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import br.com.talpi.usuario.Usuario;

/**
 * Mantém na {@link HttpSession sessão} qual usuário está logado
 * 
 * @author Rafael Lins
 *
 */
@SessionScoped
public class UsuarioLogado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Que {@link Usuario} está logado */
	private Usuario usuario;
	
	@Inject
	private Logger log;
	
	@PostConstruct
	public void criou() {
		if (log.isDebugEnabled()) { log.debug("Criou UsuarioLogado"); }
	}
	
	public Usuario get() {
		return usuario;
	}
	
	public void set(final Usuario usuario) {
		if (log.isDebugEnabled()) { log.debug("Setou o UsuarioLogado: {} ({})", usuario.getId(), usuario); }
		this.usuario = usuario;
	}
	
	/**
	 * Verifica se o usuário logado é igual ao usuário especificado
	 * @param usuario Usuário para verificar se é o usuário logado
	 * @return {@code true} se houver um usuário logado e ambos tiverem o mesmo {@link Usuario#getId()}
	 */
	public boolean equals(final Usuario usuario) {
		return usuario != null && this.usuario != null && usuario.getId().equals(this.usuario.getId());
	}
}
