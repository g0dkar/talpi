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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import br.com.talpi.usuario.UsuarioProjeto;

@Entity
public class HistoricoRequisito implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requisito requisito;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto usuario;
	
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
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String justificativa;
	
	@URL
	@Size(max = 2048)
	@NotBlank
	@Column(nullable = false, length = 2048)
	private String comprovacao;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(final Requisito requisito) {
		this.requisito = requisito;
	}

	public UsuarioProjeto getUsuario() {
		return usuario;
	}

	public void setUsuario(final UsuarioProjeto usuario) {
		this.usuario = usuario;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getComprovacao() {
		return comprovacao;
	}

	public void setComprovacao(final String comprovacao) {
		this.comprovacao = comprovacao;
	}
}
