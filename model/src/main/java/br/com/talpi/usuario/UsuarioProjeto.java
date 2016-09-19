package br.com.talpi.usuario;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.talpi.requisito.Projeto;

@Entity
@Table(indexes = { @Index(name = "unique_user_per_project", columnList = "usuario_id, projeto_id", unique = true) })
public class UsuarioProjeto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario criador;
	
	@Column(nullable = false)
	private Instant timestampCriacao;
	
	@NotNull
	@JoinColumn(name = "usuario_id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario usuario;
	
	@NotNull
	@JoinColumn(name = "projeto_id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Projeto projeto;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private PapelUsuarioProjetoEnum papel;
	
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

	public Usuario getCriador() {
		return criador;
	}

	public void setCriador(final Usuario criador) {
		this.criador = criador;
	}

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(final Projeto projeto) {
		this.projeto = projeto;
	}

	public PapelUsuarioProjetoEnum getPapel() {
		return papel;
	}

	public void setPapel(final PapelUsuarioProjetoEnum papel) {
		this.papel = papel;
	}
}
