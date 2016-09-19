package br.com.talpi.usuario;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import br.com.caelum.vraptor.serialization.SkipSerialization;

/**Classe para objetos do tipo Usuario, onde serão contidos, valores e métodos para o mesmo.
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	/** Nome do Usuario */
	@NotBlank
	@Column(nullable = false, length = 128)
	private String nome;

	/** Email do Usuario */
	
	@Email
	@NotBlank
	@Column(nullable = false, unique = true, length = 128)
	private String email;

	/** Senha do Usuário */
	
	@NotBlank
	@Size(min = 6)
	@Column(nullable = false, length = 60)
	@SkipSerialization
	private String senha;

	/** Momento de criação */
	
	@SkipSerialization
	@Column(nullable = false)
	private Instant timestampCriacao;

	/** Momento do ultimo login */

	@SkipSerialization
	private Instant timestampUltimoLogin;

	/** Atributo para sinalizar se o usário é Premium ou não */
	
	@Column(nullable = false)
	private boolean premium;

	/** Método para ser executado antes do objeto ser persistido
	 * neste metódo o timestamp de criação é definido com o Instant atual
	 * ou seja, o Instant.now()
	 * */
	
	@PrePersist
	public void beforeSave() {
		timestampCriacao = Instant.now();
	}

	/** Método para retorno do Id
	*   @return long - id*/

	public Long getId() {
		return id;
	}

	/** Método para setar id
	 * 	@param id long - Negativos
	 **/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do Nome
 	*  	@return String - nome*/

	public String getNome() {
		return nome;
	}

	/** Método para setar Nome
	 * @param nome String - Nome do usuário*/

	public void setNome(final String nome) {
		this.nome = nome;
	}

	/** Método para retorno do Email
	 *  @return String - email*/

	public String getEmail() {
		return email;
	}

	/** Método para setar Email
	 * @param email String - Email do usuário*/

	public void setEmail(final String email) {
		this.email = email;
	}

	/** Método para retorno da senha
	 *   @return String - senha*/

	public String getSenha() {
		return senha;
	}

	/** Método para setar a senha do usuário
	 * @param senha String - senha do usuário*/

	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/** Método para retorno do Instant de criação do Usuario
	 *   @return Instant - timestampCriacao*/

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	/** Método para setar o timestamp de criacao
	 * @param timestampCriacao Instant - Timestamp da Criação*/

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	/** Método para verificar se o usuário é Premium
	 *   @return boolean - premium*/

	public boolean isPremium() {
		return premium;
	}

	/** Método para setar se o usuário pe premium
	 * @param premium boolean - Usuário premium?*/

	public void setPremium(final boolean premium) {
		this.premium = premium;
	}

	/** Método para retorno do Instant do último login do Usuário
	 *   @return Instant - timestampUltimoLogin*/

	public Instant getTimestampUltimoLogin() {
		return timestampUltimoLogin;
	}

	/** Método para setar o Instant do último login do usuário
	 * @param timestampUltimoLogin Instant - Timestamp do útimo login do usuário*/

	public void setTimestampUltimoLogin(final Instant timestampUltimoLogin) {
		this.timestampUltimoLogin = timestampUltimoLogin;
	}
}
