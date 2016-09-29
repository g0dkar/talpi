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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import br.com.talpi.usuario.UsuarioProjeto;

/**Classe para gerenciar o historico de requisitos de um projeto
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class HistoricoRequisito implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Requistos a este historico */

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requisito requisito;

	/** Usuario */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioProjeto usuario;

	/** Momento da criação */

	@Column(nullable = false)
	private Instant timestamp;

	/** Título do requisito */
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String titulo;

	/** Descrição do requisito */
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	/** Justificativa do requisito */
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String justificativa;

	/** Comprovação do requisito */
	
	@URL
	@Size(max = 2048)
	@NotBlank
	@Column(nullable = false, length = 2048)
	private String comprovacao;
	
	@PrePersist
	public void beforeSave() {
		if (timestamp == null) {
			timestamp = Instant.now();
		}
		
		titulo = requisito.getTitulo();
		descricao = requisito.getDescricao();
	}
	
	@PreUpdate
	public void beforeUpdate() {
		titulo = requisito.getTitulo();
		descricao = requisito.getDescricao();
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

	/** Método para retorno do Requisito
	 *   @return Requisito - requisito*/

	public Requisito getRequisito() {
		return requisito;
	}

	/** Método para setar o Requisito
	 * @param requisito Requisito - Requisito*/

	public void setRequisito(final Requisito requisito) {
		this.requisito = requisito;
	}

	/** Método para retorno do Usuário Criador
	 *   @return UsuarioProjeto - criador*/

	public UsuarioProjeto getUsuario() {
		return usuario;
	}

	/** Método para setar o Usuário Criador
	 * @param criador UsuarioProjeto - Requisito*/

	public void setUsuario(final UsuarioProjeto usuario) {
		this.usuario = usuario;
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

	/** Método para retorno da Descrição
	 *   @return String - descricao*/

	public String getDescricao() {
		return descricao;
	}

	/** Método para setar a Descrição
	 * @param descricao String - Descrição*/

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/** Método para retorno da Comprovação
	 *   @return String - comprovacao*/

	public String getComprovacao() {
		return comprovacao;
	}

	/** Método para setar a Comprovação
	 * @param comprovacao String - Comprovação*/

	public void setComprovacao(final String comprovacao) {
		this.comprovacao = comprovacao;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
}
