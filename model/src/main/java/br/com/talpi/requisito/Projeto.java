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
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.talpi.estado.Estado;
import br.com.talpi.usuario.Usuario;
import br.com.talpi.usuario.UsuarioProjeto;

/**Classe para gerenciar o Projeto!
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */


@Entity
public class Projeto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Lista com requisitos pertecentes ao Projeto */

	@OneToMany(mappedBy = "projeto", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Requisito> requisitos;

	/** Usuário criador do Projeto */
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Usuario criador;

	/** Momento da criação do Projeto */
	
	@Column(nullable = false)
	private Instant timestampCriacao;

	/** Estado em que se encontra o Projeto */
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Estado estado;

	/** Nome do Projeto */
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String nome;

	/** Descrição do Projeto */
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	/** Atributo para identificar se o projeto está congelado ou não */
	
	@Column(nullable = false)
	private boolean congelado;

	/** Lista com usuários pertecentes ao projeto */
	
	@NotEmpty
	@Size(min = 1)
	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<UsuarioProjeto> usuarios;

	/** Método para ser executado antes do objeto ser persistido
	 * neste metódo o timestamp de criação é definido com o Instant atual
	 * ou seja, o Instant.now()*/

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
	 * @param id long - Negativos*/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do usuário criador do Projeto
	 *   @return {@link Usuario} - criador*/

	public Usuario getCriador() {
		return criador;
	}

	/** Método para setar o usuario criador do projeto
	 * @param criador {@link Usuario} - Negativos*/

	public void setCriador(final Usuario criador) {
		this.criador = criador;
	}

	/** Método para retorno do Instant de criação
	 *   @return Instant - timestampCriacao*/

	public Instant getTimestampCriacao() {
		return timestampCriacao;
	}

	/** Método para setar o Instant de criação
	 * @param timestampCriacao Instant - Instant com a criação do Projeto*/

	public void setTimestampCriacao(final Instant timestampCriacao) {
		this.timestampCriacao = timestampCriacao;
	}

	/** Método para retorno do Nome
	 *   @return String - nome*/

	public String getNome() {
		return nome;
	}

	/** Método para setar o Nome
	 * @param nome String - Nome do Projeto*/

	public void setNome(final String nome) {
		this.nome = nome;
	}

	/** Método para verificar se o projeto encontra-se congelado
	 * @return boolean - congelado */

	public boolean isCongelado() {
		return congelado;
	}

	/** Método para modificar o estado do Congelado do projeto
	 * @param congelado boolean - Estado de Congelado do Projeto */

	public void setCongelado(final boolean congelado) {
		this.congelado = congelado;
	}

	/**  Método para retornar a lista de requisitos do projetos
	 * @return {@link List} - requisitos*/

	public List<Requisito> getRequisitos() {
		return requisitos;
	}

	/** Método para definir a lista de requisitos do projeto
	 * @param requisitos {@link List} - Lista com os requisitos de um projeto */

	public void setRequisitos(final List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}

	/** Método para retornar o Estado do projeto
	 * @return Estado - restado */

	public Estado getEstado() {
		return estado;
	}

	/** Método para definir o estado do projeto
	 * @param estado {@link Estado} - estado do projeto */

	public void setEstado(final Estado estado) {
		this.estado = estado;
	}

	/** Método para retornar o retorno da descrição da Descricao
	 * @return String - descricao */

	public String getDescricao() {
		return descricao;
	}

	/** Método para definir a descrição do projeto
	 * @param descricao String - descrição do projeto */

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/** Método para retornar os usuários do projeto
	 * @return {@link List}  - descricao*/

	public List<UsuarioProjeto> getUsuarios() {
		return usuarios;
	}

	/** Método definir a lista de usuários do Projeto
	 * @param usuarios {@link List} - usuarios do Projeto*/

	public void setUsuarios(final List<UsuarioProjeto> usuarios) {
		this.usuarios = usuarios;
	}
}
