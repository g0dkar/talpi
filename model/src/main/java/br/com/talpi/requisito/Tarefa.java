package br.com.talpi.requisito;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.talpi.estado.Estado;
import br.com.talpi.usuario.UsuarioProjeto;

@Entity
public class Tarefa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requisito requisito;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto criador;
	
	@Column(nullable = false)
	private Instant timestamp;
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String titulo;
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Estado estado;
	
	/** Tempo trabalhado na tarefa em segundos */
	@Column(nullable = false)
	private long tempoTrabalhado;

	public Long getId() {
		return id;
	}

<<<<<<< Updated upstream
=======
	/** Método para setar id
	 * @param id long - Negativos*/

>>>>>>> Stashed changes
	public void setId(final Long id) {
		this.id = id;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(final Requisito requisito) {
		this.requisito = requisito;
	}

	public UsuarioProjeto getCriador() {
		return criador;
	}

	public void setCriador(final UsuarioProjeto criador) {
		this.criador = criador;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(final Estado estado) {
		this.estado = estado;
	}

	public long getTempoTrabalhado() {
		return tempoTrabalhado;
	}

	public void setTempoTrabalhado(final long tempoTrabalhado) {
		this.tempoTrabalhado = tempoTrabalhado;
	}
}
