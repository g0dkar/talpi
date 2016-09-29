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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.talpi.estado.Estado;
import br.com.talpi.social.Comentarios;
import br.com.talpi.social.Votos;
import br.com.talpi.usuario.UsuarioProjeto;

/**Classe para gerenciar Requisitos e suas atribuições
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Requisito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Projeto a qual o requisito pertence */
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Projeto projeto;

	/** Usuario Criador do Requisito */
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto criador;

	/** Tempo de criação do requisito */

	@Column(nullable = false)
	private Instant timestampCriacao;

	/** Estado em que o requisito se encontra */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Estado estado;

	/** Comentários pertencentes aos requisitos */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Comentarios comentarios;

	/** Voto pertencentes aos requisitos */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Votos votos;

	/** Título do Requisito */
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String titulo;

	/** Descrição do Requisito */
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	/** Índice de Risco do Requisito */
	
	@Column(nullable = false)
	private double indiceRisco;

	/** Lista com as tarefas do requisito */
	
	@OneToMany(mappedBy = "requisito", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Tarefa> tarefas;

	/** Histico com as modificações do requisito */
	
	@Valid
	@NotEmpty
	@OrderBy("timestamp DESC")
	@OneToMany(mappedBy = "requisito", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
			indiceRisco = historico.size() / ((historico.get(historico.size() - 1).getTimestamp().getEpochSecond() - timestampCriacao.getEpochSecond()) / 86400);
		}
		
		if (comentarios == null) {
			comentarios = new Comentarios();
		}
		
		if (votos == null) {
			votos = new Votos();
		}
	}

	/** Método para retorno do Id
	 *   @return long - id*/

	public Long getId() {
		return id;
	}

	/** Método para setar id
	 * @param positivo long - Negativos*/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retornar o Projeto
	 * @return Projeto - projeto */

	public Projeto getProjeto() {
		return projeto;
	}

	/** Método para definir o Projeto
	 * @param projeto Projeto - projeto em que o requisito pertence */

	public void setProjeto(final Projeto projeto) {
		this.projeto = projeto;
	}

	/** Método para retornar o Usuario criado do Requisito
	 * @return UsuarioProjeto criador*/

	public UsuarioProjeto getCriador() {
		return criador;
	}

	/** Método para definir o Usuario criador do Projeto
	 * @param criador UsuarioProjeto - Usuario criador do Projeto*/

	public void setCriador(final UsuarioProjeto criador) {
		this.criador = criador;
	}

	/** Método para retornar o Instant de criação do Projeto
	 * @return Instant - timestampCriacao */

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	/** Método para definir o Instant de criação do Projeto
	 * @param timestampCriacao Instant - instant com a criação do projeto*/

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	/** Método para retornar os comentários relacioandos ao projeto
	 * @return Comentarios - comentarios */

	public Comentarios getComentarios() {
		return comentarios;
	}

	/** Método para definir os comentarios relacionados ao projeto
	 * @param comentarios Comentarios - comentarios do projeto */

	public void setComentarios(final Comentarios comentarios) {
		this.comentarios = comentarios;
	}

	/** Método para retornar o vostos no Projeto
	 * @return Votos - votos */

	public Votos getVotos() {
		return votos;
	}

	/** Método para definir os votos do projeto
	 * @param votos Votos - votos no projeto */

	public void setVotos(final Votos votos) {
		this.votos = votos;
	}

	/** Método para retonar o Titulo do Projeto
	 * @return String - titulo */

	public String getTitulo() {
		return titulo;
	}

	/** Método para definir o titulo do Projeto
	 * @param titulo String - titulo do projeto */

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

	/** Método para retonar a descrição do Requisito
	 * @return String descricao*/

	public String getDescricao() {
		return descricao;
	}

	/** Método para definir a descrição do projeto
	 * @param descricao String - Descrição do Projeto */

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/** Método para retonar o indice de risco do requisito
	 * @return double - indiceRisco */

	public double getIndiceRisco() {
		return indiceRisco;
	}

	/** Método para definir o indice de risco
	 * @param indiceRisco double - Indice de Risco do Projeto*/

	public void setIndiceRisco(final double indiceRisco) {
		this.indiceRisco = indiceRisco;
	}

	/** Método para retornar as tarefas do Requisito
	 * @return List<Tarefa> - Tarefas do Requisito */

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	/** Método para definir as tarefas do requisito
	 * @param tarefas List<Tarefa> - Tarefas do Requisitos */

	public void setTarefas(final List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	/** Método para retornar o historico do requisito
	 * @return List<HistoricoRequisito> - historico*/

	public List<HistoricoRequisito> getHistorico() {
		return historico;
	}

	/** Método para definir o historico do requisito
	 * @param historico List<HistoricoRequisito> - Historico do Requisito*/

	public void setHistorico(final List<HistoricoRequisito> historico) {
		this.historico = historico;
	}
}
