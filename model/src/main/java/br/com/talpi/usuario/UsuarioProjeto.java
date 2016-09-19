package br.com.talpi.usuario;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.talpi.requisito.Projeto;


/**Classe para gerenciar os Usuários de um Projeto
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
@Table(indexes = { @Index(name = "unique_user_per_project", columnList = "usuario, projeto", unique = true) })
public class UsuarioProjeto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Usuario criador do projeto */
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario criador;

	/** Momento de criação */
	
	@Column(nullable = false)
	private Instant timestampCriacao;

	/** Usuarios do projeto */
	
	@NotNull
	@JoinColumn(name = "usuario_id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario usuario;

	/** Projeto */
	
	@NotNull
	@JoinColumn(name = "projeto_id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Projeto projeto;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private PapelUsuarioProjetoEnum papel;

	/** Método para ser executado antes do objeto ser persistido
	 * neste metódo o timestamp de criação é definido com o Instant atual
	 * ou seja, o Instant.now()
	 * */

	@PrePersist
	public void beforeSave() {
		timestampCriacao = Instant.now();
	}

	/** Método para retorno do Id
	 *   @return long - id
	 * */

	public Long getId() {
		return id;
	}

	/** Método para setar id
	 * @param id long - Negativos
	 * */

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do usuário criador do Projeto
	 *   @return {@link Usuario} - criador
	 * */

	public Usuario getCriador() {
		return criador;
	}

	/** Método para setar o usuario criador do projeto
	 * @param criador {@link Usuario} - Negativos
	 * */

	public void setCriador(final Usuario criador) {
		this.criador = criador;
	}

	/** Método para retorno do Instant de criação
	 *   @return Instant - timestampCriacao
	 * */

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	/** Método para setar o Instant de criação
	 * @param timestampCriacao Instant - Instant com a criação do Projeto*/

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	/** Método para retorno do Usuario
	 *   @return {@link Usuario} - usuario*/

	public Usuario getUsuario() {
		return usuario;
	}

	/** Método para setar o Usuario
	 * @param usuario {@link Usuario} - Usuario*/

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	/** Método para retorno do Projeto
	 *   @return {@link Projeto} - projeto*/

	public Projeto getProjeto() {
		return projeto;
	}

	/** Método para setar o Projeto
	 * @param projeto {@link Projeto} - Projeto*/

	public void setProjeto(final Projeto projeto) {
		this.projeto = projeto;
	}

	/** Método para retorno do Papel do Usuário no Projeto
	 *   @return {@link PapelUsuarioProjetoEnum} - papel*/

	public PapelUsuarioProjetoEnum getPapel() {
		return papel;
	}

	/** Método para setar o Papel do Usuário no Projeto
	 * @param papel {@link PapelUsuarioProjetoEnum} - Papel do usuário*/

	public void setPapel(final PapelUsuarioProjetoEnum papel) {
		this.papel = papel;
	}
}
