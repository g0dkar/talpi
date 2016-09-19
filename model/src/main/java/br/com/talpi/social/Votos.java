package br.com.talpi.social;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;

/**Classe para objetos do tipo Votos, onde serão contidos, valores e métodos para o mesmo.
 * @author Rafael Lins
 * @version 0.1
 * @since Beta-release
 */

@Entity
public class Votos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /** Quantidade de votos positivos */

	@Min(0)
	@Column(nullable = false)
	private long positivos;

    /** Quantidade de Votos Negativos */

	@Min(0)
	@Column(nullable = false)
	private long negativos;

    /** Caso os votos negativos sejam mais que 70% do total o voto fica em Alerta */
	
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

	/** Método para retorno do Id
	 *   @return long - id*/
	
	public Long getId() {
		return id;
	}

	/** Método para setar o id
	 * @param id long - Id*/

	public void setId(final Long id) {
		this.id = id;
	}

	/** Método para retorno dos votos Positivos
	 *   @return long - Positivos*/

	public long getPositivos() {
		return positivos;
	}

	/** Método para setar o votos Positivos
	 * @param positivos long - Positivos*/

	public void setPositivos(final long positivos) {
		this.positivos = positivos;
	}

	/** Método para retorno dos votos Negativos
	 *   @return long - Negativos*/

	public long getNegativos() {
		return negativos;
	}

	/** Método para setar o votos Negativos
	 * @param negativos long - Negativos*/

	public void setNegativos(final long negativos) {
		this.negativos = negativos;
	}
}
