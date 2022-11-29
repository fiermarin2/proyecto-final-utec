package com.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the ESTA_MEDICIONES database table.
 * 
 */
@Entity
@Table(name="ESTA_MEDICIONES")
@NamedQuery(name="Estacion.findAll", query="SELECT e FROM Estacion e")
@NamedQuery(name = "Estacion.findByID", query = "SELECT e FROM Estacion e WHERE e.id LIKE :id")
@NamedQuery(name = "Estacion.findByName", query = "SELECT e FROM Estacion e WHERE e.nombre = :nombre")
@NamedQuery(name = "Estacion.findAllFiltered", query = "SELECT e FROM Estacion e WHERE e.nombre LIKE :nombre")
public class Estacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ESTACION", unique=true, nullable=false, precision=38)
	private long id;

	@Column(name="CAL_AGUA", length=50)
	private String calidadAgua;

	@Column(name="HUM_RELATIVA", length=50)
	private String humedadRelativa;

	@Column(name="NOM_ESTACION", nullable=false, length=50)
	private String nombre;

	//bi-directional many-to-one association to Ubicacion
	@Column(nullable=false, precision=126)
	private float latitud;

	@Column(nullable=false, precision=126)
	private float longitud;

	//bi-directional many-to-one association to Departamento
	@ManyToOne
	@JoinColumn(name="ID_DEPARTAMENTO", nullable=false)
	private Departamento departamento;
	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private Usuario usuario;
/*
	//bi-directional many-to-one association to ObtieneDato
	@OneToMany(mappedBy="estaMedicione")
	private List<ObtieneDato> obtiDatos;
*/
	public Estacion() {
	}

	public long getIdEstacion() {
		return this.id;
	}

	public Estacion setIdEstacion(long idEstacion) {
		this.id = idEstacion;
		return this;
	}

	public String getCalAgua() {
		return this.calidadAgua;
	}

	public void setCalAgua(String calAgua) {
		this.calidadAgua = calAgua;
	}

	public String getHumRelativa() {
		return this.humedadRelativa;
	}

	public void setHumRelativa(String humRelativa) {
		this.humedadRelativa = humRelativa;
	}

	public String getNomEstacion() {
		return this.nombre;
	}

	public void setNomEstacion(String nomEstacion) {
		this.nombre = nomEstacion;
	}

	public Departamento getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public float getLatitud() {
		return this.latitud;
	}

	public void setLatitud(float double1) {
		this.latitud = double1;
	}
	
	public float getLongitud() {
		return this.longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
/*
	public List<ObtieneDato> getObtiDatos() {
		return this.obtiDatos;
	}

	public void setObtiDatos(List<ObtieneDato> obtiDatos) {
		this.obtiDatos = obtiDatos;
	}

	public ObtieneDato addObtiDato(ObtieneDato obtiDato) {
		getObtiDatos().add(obtiDato);
		obtiDato.setEstaMedicione(this);

		return obtiDato;
	}

	public ObtieneDato removeObtiDato(ObtieneDato obtiDato) {
		getObtiDatos().remove(obtiDato);
		obtiDato.setEstaMedicione(null);

		return obtiDato;
	}
*/
}