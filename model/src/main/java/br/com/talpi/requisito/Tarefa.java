package br.com.talpi.requisito;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.talpi.estado.Estado;
import br.com.talpi.social.Comentarios;
import br.com.talpi.social.Votos;
import br.com.talpi.usuario.UsuarioProjeto;

/**Classe para objetos do tipo Tarefa, onde serão contidos, valores e métodos para o mesmo.
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Tarefa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /** Requisito a qual a tarefa pertencete */

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requisito requisito;

    /** Usuario criador desta tarefa */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto criador;

    /** Momento da criação */
	
	@Column(nullable = false)
	private Instant timestamp;

    /** Título da Tarefa */

	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String titulo;

    /** Descrição da Tarefa */

	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

    /** Estado da Tarefa */
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Estado estado;

    /** Comentários relacioandos a esta tarefa */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Comentarios comentarios;

    /** Votos relacioandos a esta tarefa */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Votos votos;
	
	/** Tempo trabalhado na tarefa em segundos */

	@Column(nullable = false)
	private long tempoTrabalhado;

    /** Este método é executo logo antes da persitencia do Objeto,
     * Aqui caso os comentários ou votos sejam novos, instancias para
     * cada um serão feitas.*/
	
	@PrePersist
	public void beforeSave() {
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
	 * @param id long 
	 * */

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do Requisito
	 *   @return {@link Requisito} - requisito
	 * */

	public Requisito getRequisito() {
		return requisito;
	}

	/** Método para setar o Requisito
	 * @param requisito {@link Requisito} - Requisito
	 * */

	public void setRequisito(final Requisito requisito) {
		this.requisito = requisito;
	}

    /** Método para retorno do Usuário Criador da Tarefa
     *   @return {@link UsuarioProjeto} - criador
     * */

	public UsuarioProjeto getCriador() {
		return criador;
	}

    /** Método para setar o Usuário Criador da Tarefa
     * @param criador {@link UsuarioProjeto} - Requisito
     * */

	public void setCriador(final UsuarioProjeto criador) {
		this.criador = criador;
	}

    /** Método para retorno do Instant de criação
     *   @return Instant - timestamp*/

	public Instant getTimestamp() {
		return timestamp;
	}

    /** Método para setar o Instant da criação
     * @param timestamp Instant - Instant da criação*/

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

    /** Método para retorno do Titulo da Tarefa
     *   @return String - titulo*/

	public String getTitulo() {
		return titulo;
	}

    /** Método para setar o Titulo da Tarefa
     * @param titulo String - Titulo da Tarefa*/

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

    /** Método para retorno da Descrição da Tarefa
     *   @return String - descricao*/

	public String getDescricao() {
		return descricao;
	}

    /** Método para setar a Descrição da Tarefa
     * @param descricao String - Descrição da Tarefa*/

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

    /** Método para retorno do Estado da Tarefa
     *   @return {@link Estado} - estado
     * */

	public Estado getEstado() {
		return estado;
	}

    /** Método para setar o Estado da Tarefa
     * @param estado {@link Estado} - Estado da Tarefa
     * */

	public void setEstado(final Estado estado) {
		this.estado = estado;
	}

    /** Método para retorno do Tempo Trabalhado
     *   @return long - tempoTrabalhado*/

	public long getTempoTrabalhado() {
		return tempoTrabalhado;
	}

    /** Método para setar o Tempo Trabalhado na Tarefa
     * @param tempoTrabalhado long - Tempo Trabalhado na Tarefa
     * */

	public void setTempoTrabalhado(final long tempoTrabalhado) {
		this.tempoTrabalhado = tempoTrabalhado;
	}

    /** Método para retorno dos Comentários
     *   @return {@link Comentarios} - comentários
     * */

	public Comentarios getComentarios() {
		return comentarios;
	}

    /** Método para setar os Comentáarios da Tarefa
     * @param comentarios {@link Comentarios} - Comentários da Tarefa
     * */

	public void setComentarios(final Comentarios comentarios) {
		this.comentarios = comentarios;
	}

    /** Método para retorno dos Votos
     *   @return {@link Votos} - votos
     * */

	public Votos getVotos() {
		return votos;
	}

    /** Método para setar os Votos
     * @param votos {@link Votos} - Votos da Tarefa
     * */

	public void setVotos(final Votos votos) {
		this.votos = votos;
	}
}
