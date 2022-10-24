package com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * The persistent class for the INVESTIGADORES database table.
 * 
 */
@Entity
@Table(name="INVESTIGADORES")
@NamedQuery(name="Investigador.findDocument", query="SELECT i FROM Investigador i WHERE i.documento = :documento AND i.borrado = :borrado")
@PrimaryKeyJoinColumn(referencedColumnName = "ID_USUARIO" , name = "ID_INVESTIGADOR")
public class Investigador extends Usuario {
	private static final long serialVersionUID = 1L;
/*
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_INVESTIGADOR", unique=true, nullable=false, precision=38)
	private long idInvestigador;
*/
	@Column(precision=8, unique = true)
	private int documento;

	@Column(length=50)
	private String domicilio;

	@Column(precision = 14)
	private int telefono;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="ID_CIUDAD")
	private Ciudad ciudad;

	public Investigador() {
	}
/*
	public long getIdInvestigador() {
		return this.idInvestigador;
	}

	public void setIdInvestigador(long idInvestigador) {
		this.idInvestigador = idInvestigador;
	}
*/
	public int getDocumento() {
		return this.documento;
	}

	public void setDocumento(int documento) {
		this.documento = documento;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public int getTelefono() {
		return this.telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public Ciudad getCiudade() {
		return this.ciudad;
	}

	public void setCiudade(Ciudad ciudade) {
		this.ciudad = ciudade;
	}

}