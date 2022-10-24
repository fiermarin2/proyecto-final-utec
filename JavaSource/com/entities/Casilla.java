package com.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the CASILLAS database table.
 * 
 */
@Entity
@Table(name="CASILLAS")						
@NamedQuery(name="Casilla.findAll", query="SELECT c FROM Casilla c WHERE c.borrado = 0 ORDER BY c.id ASC")
@NamedQuery(name="Casilla.findCasillaFilter", query="SELECT c FROM Casilla c WHERE c.nombre LIKE :nombre AND c.borrado = 0")
@NamedQuery(name="Casilla.findCasillaFilterID", query="SELECT c FROM Casilla c WHERE c.id = :id AND c.borrado = 0")
public class Casilla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_CASILLA", unique=true, nullable=false, precision=38)
	private long id;

	@Column(precision=1)
	private int borrado;

	@Column(length=50)
	private String descripcion;

	@Column(name="NOM_CASILLA", nullable=false, length=50 , unique = true)
	private String nombre;

	@Column(name="TIPO_INPUT", nullable=false, length=50)
	private String tipoInput;
	
	@Column(name="OBLIGATORIA", nullable=false, precision = 1, columnDefinition = "NUMBER DEFAULT 0")
	private int obligatoria;

	@Column(nullable=false, precision=20)
	private String ubicacion;

	@Column(name="UNI_MEDIDA", nullable=false, length=50)
	private String unidadMedida;
	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private Usuario usuario;

	public Casilla() {
	}

	public long getIdCasilla() {
		return this.id;
	}

	public void setIdCasilla(long idCasilla) {
		this.id = idCasilla;
	}

	public int getBorrado() {
		return this.borrado;
	}

	public void setBorrado(int borrado) {
		this.borrado = borrado;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNomCasilla() {
		return this.nombre;
	}

	public void setNomCasilla(String nomCasilla) {
		this.nombre = nomCasilla;
	}

	public String getTipoInput() {
		return this.tipoInput;
	}

	public void setTipoInput(String tipoInput) {
		this.tipoInput = tipoInput;
	}

	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getUniMedida() {
		return this.unidadMedida;
	}

	public void setUniMedida(String uniMedida) {
		this.unidadMedida = uniMedida;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getObligatoria() {
		return obligatoria;
	}

	public void setObligatoria(int obligatoria) {
		this.obligatoria = obligatoria;
	}

}