package br.com.talpi.social;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**Classe para gerenciar uma lista de comentários.
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Comentarios implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /** Momento do primeiro comentário */

	private Instant primeiroComentario;

    /** Momento do ultimo comentário */

	private Instant ultimoComentario;

    /** Quantidade total de comentários */

	private long totalComentarios;

    /** Melhor comentário */

	@ManyToOne(fetch = FetchType.EAGER)
	private Comentario melhorComentario;

    /** Lista com os comentarios (Comentario) pertecentes*/
	
	@OrderBy("pai DESC, timestamp DESC")
	@OneToMany(mappedBy = "thread", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comentario> comentarios;

	/** Método para retorno do Id da Lista Comentários
	 *   @return Long - id*/

	public Long getId() {
		return id;
	}

	/** Método para definir o Id da Lista Comentários
	 * @param id Long - Id*/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno do Primeiro Comentário
	 *   @return Instant - primeiroComentário*/

	public Instant getPrimeiroComentario() {
		return primeiroComentario;
	}

	/** Método para definir o primeiroComentario
	 * @param primeiroComentario Instant - Primeiro Comentário Insant*/

	public void setPrimeiroComentario(final Instant primeiroComentario) {
		this.primeiroComentario = primeiroComentario;
	}

    /** Método para retorno do Ultimo Comentário
     *   @return Instant - ultimoComentario*/

	public Instant getUltimoComentario() {
		return ultimoComentario;
	}

    /** Método para definir o ultimoComentario
     * @param ultimoComentario Instant - Ultimo Comentário Insant*/

	public void setUltimoComentario(final Instant ultimoComentario) {
		this.ultimoComentario = ultimoComentario;
	}

    /** Método para retornar o total de comentário
     *   @return Long - totalComentarios*/

	public long getTotalComentarios() {
		return totalComentarios;
	}

    /** Método para definir o total de Comentários
     * @param totalComentarios Long - Total de comentários*/

	public void setTotalComentarios(final long totalComentarios) {
		this.totalComentarios = totalComentarios;
	}

    /** Método para retornar o melhor comentário
     *   @return {@link Comentario} - melhorComentario
     * */

	public Comentario getMelhorComentario() {
		return melhorComentario;
	}

    /** Método para definir o melhor Comentário
     * @param melhorComentario {@link Comentario} - O Melhor comentário
     * */

	public void setMelhorComentario(final Comentario melhorComentario) {
		this.melhorComentario = melhorComentario;
	}

    /** Método para retornar uma lista com todos os comentários
     *   @return {@link List} - comentarios
     * */

	public List<Comentario> getComentarios() {
		return comentarios;
	}

    /** Método para definir a listra de comentários
     * @param comentarios {@link List} - Lista de comentários
     * */

	public void setComentarios(final List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
}
