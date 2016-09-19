package br.com.talpi.estado;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**Classe para objetos do tipo Estado, onde serão contidos, valores e métodos para o mesmo.
		* @author Rafael Lins
		* @version 0.1
		* @since Beta-release
		*/

@Entity
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /** Estado da instancia deste objeto do tipo {@link EstadoEnum} */

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 24)
	private EstadoEnum estado;

    /** Estado da instancia anterior deste objeto do tipo {@link EstadoEnum} */

	@Enumerated(EnumType.STRING)
	@Column(length = 24)
	private EstadoEnum estadoAnterior;

    /** Momento da criação deste objeto */

	@Column(nullable = false)
	private Instant timestamp;

    /** Método para retorno do Id do Estado
     *   @return Long - id*/

	public Long getId() {
		return id;
	}

    /** Método para definir o Id do Estado
     * @param id Long - Id*/

	public void setId(final Long id) {
		this.id = id;
	}

    /** Método para retorno do Nome do Estado
     *   @return {@link EstadoEnum} - estado*/

	public EstadoEnum getEstado() {
		return estado;
	}


    /**Método para definir o Estado,
     * neste método o timestamp do objeto também é definido, a partir do Instant.now()
     * @param  estado {@link EstadoEnum} - Estado.
     */

	public void setEstado(final EstadoEnum estado) {
		setEstadoAnterior(this.estado);
		timestamp = Instant.now();
		this.estado = estado;
	}

    /** Método para retorno o timestamp da definição do Estado
     *   @return Instant - timestamp*/

	public Instant getTimestamp() {
		return timestamp;
	}

    /** Método para definir o timestamp do Estado a partir de um paramentro do mesmo tipo
     * @param timestamp Instant - Timestamp*/

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

    /** Método para retorno do Estado Anterior
     *   @return {@link EstadoEnum} - estadoAnterior*/

	public EstadoEnum getEstadoAnterior() {
		return estadoAnterior;
	}

    /** Método para definir o estado anterior
     * @param estadoAnterior {@link EstadoEnum} - Estado Anterior*/

	public void setEstadoAnterior(final EstadoEnum estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
}
