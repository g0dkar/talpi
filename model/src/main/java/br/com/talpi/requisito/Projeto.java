package br.com.talpi.requisito;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.talpi.estado.Estado;
import br.com.talpi.usuario.Usuario;

@Entity
public class Projeto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy = "projeto", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Requisito> requisitos;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario criador;
	
	@Column(nullable = false)
	private Instant timestampCriacao;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Estado estado;
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String nome;
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;
	
	@Column(nullable = false)
	private boolean congelado;
	
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

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public boolean isCongelado() {
		return congelado;
	}

	public void setCongelado(final boolean congelado) {
		this.congelado = congelado;
	}

	public List<Requisito> getRequisitos() {
		return requisitos;
	}

	public void setRequisitos(final List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(final Estado estado) {
		this.estado = estado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}
}
