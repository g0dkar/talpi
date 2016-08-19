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

@Entity
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 24)
	private EstadoEnum estado;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 24)
	private EstadoEnum estadoAnterior;
	
	@Column(nullable = false)
	private Instant timestamp;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public EstadoEnum getEstado() {
		return estado;
	}

	public void setEstado(final EstadoEnum estado) {
		setEstadoAnterior(this.estado);
		this.estado = estado;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	public EstadoEnum getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(final EstadoEnum estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
}
