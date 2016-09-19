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
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.talpi.estado.Estado;
import br.com.talpi.social.Comentarios;
import br.com.talpi.social.Votos;
import br.com.talpi.usuario.UsuarioProjeto;

@Entity
public class Requisito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Projeto projeto;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto criador;
	
	@Column(nullable = false)
	private Instant timestampCriacao;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private HistoricoRequisito ultimaAlteracao;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Estado estado;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Comentarios comentarios;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Votos votos;
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String titulo;
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;
	
	@Column(nullable = false)
	private double indiceRisco;
	
	@OneToMany(mappedBy = "requisito", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Tarefa> tarefas;
	
	@NotEmpty
	@OrderBy("timestamp DESC")
	@OneToMany(mappedBy = "requisito", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<HistoricoRequisito> historico;
	
	@PrePersist
	public void beforeSave() {
		timestampCriacao = Instant.now();
	}
	
	/**
	 * Atualiza a data/hora de última alteração e recalcula o {@link #indiceRisco índice de risco}
	 */
	@PreUpdate
	public void beforeUpdate() {
		if (historico.size() > 1) {
			// Nosso Índice de Risco é a quantidade de mudanças divida pela idade do requisito em dias
			indiceRisco = historico.size() / ((ultimaAlteracao.getTimestamp().getEpochSecond() - timestampCriacao.getEpochSecond()) / 86400);
		}
	}

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

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(final Projeto projeto) {
		this.projeto = projeto;
	}

	public UsuarioProjeto getCriador() {
		return criador;
	}

	public void setCriador(final UsuarioProjeto criador) {
		this.criador = criador;
	}

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	public HistoricoRequisito getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(final HistoricoRequisito ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public Comentarios getComentarios() {
		return comentarios;
	}

	public void setComentarios(final Comentarios comentarios) {
		this.comentarios = comentarios;
	}

	public Votos getVotos() {
		return votos;
	}

	public void setVotos(final Votos votos) {
		this.votos = votos;
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

	public double getIndiceRisco() {
		return indiceRisco;
	}

	public void setIndiceRisco(final double indiceRisco) {
		this.indiceRisco = indiceRisco;
	}

<<<<<<< Updated upstream
=======
	/** Método para retornar as tarefas do Requisito
	 * @return {@link List} - Tarefas do Requisito */

>>>>>>> Stashed changes
	public List<Tarefa> getTarefas() {
		return tarefas;
	}

<<<<<<< Updated upstream
=======
	/** Método para definir as tarefas do requisito
	 * @param tarefas {@link List} - Tarefas do Requisitos */

>>>>>>> Stashed changes
	public void setTarefas(final List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

<<<<<<< Updated upstream
=======
	/** Método para retornar o historico do requisito
	 * @return {@link List} - historico*/

>>>>>>> Stashed changes
	public List<HistoricoRequisito> getHistorico() {
		return historico;
	}

<<<<<<< Updated upstream
=======
	/** Método para definir o historico do requisito
	 * @param historico {@link List} - Historico do Requisito*/

>>>>>>> Stashed changes
	public void setHistorico(final List<HistoricoRequisito> historico) {
		this.historico = historico;
	}
}
