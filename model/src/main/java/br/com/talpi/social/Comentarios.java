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

@Entity
public class Comentarios implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant primeiroComentario;
	private Instant ultimoComentario;
	private long totalComentarios;

	@ManyToOne(fetch = FetchType.EAGER)
	private Comentario melhorComentario;

	@OneToMany(mappedBy = "thread", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comentario> comentarios;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Instant getPrimeiroComentario() {
		return primeiroComentario;
	}

	public void setPrimeiroComentario(final Instant primeiroComentario) {
		this.primeiroComentario = primeiroComentario;
	}

	public Instant getUltimoComentario() {
		return ultimoComentario;
	}

	public void setUltimoComentario(final Instant ultimoComentario) {
		this.ultimoComentario = ultimoComentario;
	}

	public long getTotalComentarios() {
		return totalComentarios;
	}

	public void setTotalComentarios(final long totalComentarios) {
		this.totalComentarios = totalComentarios;
	}

	public Comentario getMelhorComentario() {
		return melhorComentario;
	}

	public void setMelhorComentario(final Comentario melhorComentario) {
		this.melhorComentario = melhorComentario;
	}

<<<<<<< Updated upstream
=======
    /** Método para retornar uma lista com todos os comentários
     *   @return {@link List} - comentarios*/

>>>>>>> Stashed changes
	public List<Comentario> getComentarios() {
		return comentarios;
	}

<<<<<<< Updated upstream
=======
    /** Método para definir a listra de comentários
     * @param comentarios {@link List} - Lista de comentários*/

>>>>>>> Stashed changes
	public void setComentarios(final List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
}
