package br.com.talpi.social;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.talpi.usuario.Usuario;

@Entity
public class Comentario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Comentarios thread;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario usuario;
	
	@Column(nullable = false)
	private Instant timestamp;
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String texto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Comentario pai;
	
	@PrePersist
	public void beforeSave() {
		timestamp = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(final String texto) {
		this.texto = texto;
	}

	public Comentario getPai() {
		return pai;
	}

	public void setPai(final Comentario pai) {
		this.pai = pai;
	}
}
