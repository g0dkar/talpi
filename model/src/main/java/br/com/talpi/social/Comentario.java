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

/**Classe para objetos do tipo Comentário, onde serão contidos, valores e métodos para o mesmo.
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Comentario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Lista de Comentários a qual este objeto Comentario pertence */
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Comentarios thread;

	/** Usuario que criou este comentário */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario usuario;

	/** Horário de criação do comentário */
	
	@Column(nullable = false)
	private Instant timestamp;

	/** Conteúdo do comentário, texto do comentário.*/
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String texto;

	/** Comentário Pai caso o comentário possui um Pai*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Comentario pai;

	/** Método para ser executado antes do objeto ser persistido
	 * neste metódo o timestamp de criação é definido com o Instant atual
	 * ou seja, o Instant.now()*/
	
	@PrePersist
	public void beforeSave() {
		timestamp = Instant.now();
	}

	/** Método para retorno do Id do Comentário
	 *   @return Long - id*/

	public Long getId() {
		return id;
	}

	/** Método para definir o Id do Comentário
	 * @param id Long - Id*/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do Usuario do Comentário
	 *   @return Usuario - usuario*/

	public Usuario getUsuario() {
		return usuario;
	}

	/** Método para definir o Usuário do Comentário
	 * @param usuario Usuario - Usuário*/

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	/** Método para retorno o timestamp da definição do Comentário
	 *   @return Instant - timestamp*/

	public Instant getTimestamp() {
		return timestamp;
	}

	/** Método para definir o timestamp do Comentário a partir de um paramentro do mesmo tipo
	 * @param timestamp Instant - Timestamp*/

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	/** Método para retorno o texto do Comentário
	 *   @return String - texto*/

	public String getTexto() {
		return texto;
	}

	/** Método para definir o texto do comentário
	 * @param texto String - Texto do comentário*/

	public void setTexto(final String texto) {
		this.texto = texto;
	}

	/** Método para retorno do comentário "Pai"
	 *   @return Comentário - pai*/

	public Comentario getPai() {
		return pai;
	}

	/** Método para definir o "pai" do comentário
	 * @param pai Comentario - Comentário Pai*/

	public void setPai(final Comentario pai) {
		this.pai = pai;
	}
}
