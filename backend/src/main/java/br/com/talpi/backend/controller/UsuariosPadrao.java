package br.com.talpi.backend.controller;

import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.mindrot.jbcrypt.BCrypt;

import br.com.talpi.usuario.Usuario;

@Singleton
@Startup
public class UsuariosPadrao {
	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
	public void init() {
		Usuario rafael;
		try {
			rafael = (Usuario) em.createQuery("FROM Usuario WHERE email = 'rafael.lins777@gmail.com'").getSingleResult();
		} catch (final NoResultException nre) {
			rafael = new Usuario();
			rafael.setEmail("rafael.lins777@gmail.com");
			rafael.setNome("Rafael Lins");
			rafael.setTimestampCriacao(Instant.now());
			rafael.setSenha(BCrypt.hashpw("rafael", BCrypt.gensalt()));
			em.persist(rafael);
		}
	}
}
