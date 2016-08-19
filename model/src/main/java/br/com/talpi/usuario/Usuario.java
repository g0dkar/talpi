package br.com.talpi.usuario;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 128)
	private String nome;
	
	@Email
	@NotBlank
	@Column(nullable = false, unique = true, length = 128)
	private String email;
	
	@NotBlank
	@Column(nullable = false, length = 96)
	private String senha;
	
	@Column(nullable = false)
	private Instant timestampCriacao;
	
	@Column(nullable = false)
	private boolean premium;
	
	@PrePersist
	public void beforeSave() {
		timestampCriacao = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(final String senha) {
		this.senha = senha;
	}

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(final boolean premium) {
		this.premium = premium;
	}
}
