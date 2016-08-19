package br.com.talpi.social;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;

@Entity
public class Votos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(0)
	@Column(nullable = false)
	private long positivos;
	
	@Min(0)
	@Column(nullable = false)
	private long negativos;
	
	@Column(nullable = false)
	private boolean alerta;
	
	@Column(nullable = false)
	private boolean alertaAutomatico;
	
	@PreUpdate
	public void beforeUpdate() {
		if (!alerta && positivos > 0 && negativos > 0) {
			// Entidade entra em alerta se 70+% dos votos forem negativos
			alerta = negativos / positivos + negativos > .7;
			alertaAutomatico = alerta;
		}
		else {
			alertaAutomatico = false;
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public long getPositivos() {
		return positivos;
	}

	public void setPositivos(final long positivos) {
		this.positivos = positivos;
	}

	public long getNegativos() {
		return negativos;
	}

	public void setNegativos(final long negativos) {
		this.negativos = negativos;
	}
}
