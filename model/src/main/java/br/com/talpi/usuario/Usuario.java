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

@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 128)
	private String nome;
	
	@Email
	@NotBlank
	@Column(nullable = false, unique = true, length = 128)
	private String email;
	
	@NotBlank
	@Size(min = 6)
	@Column(nullable = false, length = 60)
	@SkipSerialization
	private String senha;
	
	@SkipSerialization
	@Column(nullable = false)
	private Instant timestampCriacao;
	
	@SkipSerialization
	private Instant timestampUltimoLogin;
	
	@Column(nullable = false)
	private boolean premium;
<<<<<<< Updated upstream
=======

	/** Método para ser executado antes do objeto ser persistido
	 * neste metódo o timestamp de criação é definido com o Instant atual
	 * ou seja, o Instant.now()
	 * */
>>>>>>> Stashed changes
	
	@PrePersist
	public void beforeSave() {
		timestampCriacao = Instant.now();
	}

<<<<<<< Updated upstream
=======
	/** Método para retorno do Id
	*   @return long - id*/

>>>>>>> Stashed changes
	public Long getId() {
		return id;
	}

<<<<<<< Updated upstream
=======
	/** Método para setar id
	 * 	@param id long - Negativos
	 **/

>>>>>>> Stashed changes
	public void setId(final Long id) {
		this.id = id;
	}

<<<<<<< Updated upstream
=======
	/** Método para retorno do Nome
 	*  	@return String - nome*/

>>>>>>> Stashed changes
	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

<<<<<<< Updated upstream
=======
	/** Método para retorno do Email
	 *  @return String - email*/

>>>>>>> Stashed changes
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(final String senha) {
		this.senha = senha;
	}

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(final boolean premium) {
		this.premium = premium;
	}

	public Instant getTimestampUltimoLogin() {
		return timestampUltimoLogin;
	}

	public void setTimestampUltimoLogin(final Instant timestampUltimoLogin) {
		this.timestampUltimoLogin = timestampUltimoLogin;
	}
}
