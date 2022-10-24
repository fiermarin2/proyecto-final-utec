package com.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * The persistent class for the FORMULARIOS database table.
 * 
 */
@Entity
@Table(name="FORMULARIOS") //"SELECT c FROM Casilla c WHERE c.nombre LIKE :nombre"
@NamedQuery(name="Formulario.findByID", query="SELECT u FROM Formulario u WHERE u.id = :id")
@NamedQuery(name="Formulario.findAllFiltered", query="SELECT f FROM Formulario f WHERE f.nombre = :nombre AND f.borrado = :borrado")
@NamedQuery(name="Formulario.findAll", query="SELECT f FROM Formulario f WHERE f.borrado = 0 ORDER BY f.id ASC")
public class Formulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_FORMULARIO", unique=true, nullable=false, precision=38)
	private long id;

	@Column(precision=1)
	private int borrado;

	@Column(length=50)
	private String comentario;

	@Column(name="FEC_HORA", nullable=false, insertable = false, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	@Temporal(value = TemporalType.DATE)
	private Date fechaHora;
/*
	@Column(name="MET_MEDICION", length=50)
	private String metodoMedicion;
*/
	@Column(name="NOM_FORMULARIO", nullable=false, length=50 , unique = true)
	private String nombre;

	//bi-directional many-to-one association to Casilla
	@ManyToMany(cascade = {CascadeType.ALL})// orphanRemoval = true)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "ID_FORMULARIO")},
			inverseJoinColumns = {@JoinColumn(name = "ID_CASILLA")}
			)
	@JoinColumn(name="ID_CASILLAS")
	private Set<Casilla> casillas = new LinkedHashSet<>();

	//bi-directional many-to-one association to Usuario
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	public Formulario() {
		
	}

	public long getIdFormulario() {
		return this.id;
	}

	public Formulario setIdFormulario(long idFormulario) {
		this.id = idFormulario;
		return this;
	}

	public int getBorrado() {
		return this.borrado;
	}

	public void setBorrado(int borrado) {
		this.borrado = borrado;
	}

	public String getComentarios() {
		return this.comentario;
	}

	public void setComentarios(String comentarios) {
		this.comentario = comentarios;
	}

	public Date getFecHora() {
		return this.fechaHora;
	}

	public Formulario setFecHora(Date fecHora) {
		this.fechaHora = fecHora;
		return this;
	}
/*
	public String getMetMedicion() {
		return this.metodoMedicion;
	} 

	public void setMetMedicion(String metMedicion) {
		this.metodoMedicion = metMedicion;
	}
*/
	public String getNomFormulario() {
		return this.nombre;
	}

	public void setNomFormulario(String nomFormulario) {
		this.nombre = nomFormulario;
	}

	public Set<Casilla> getCasillas() {
		return this.casillas;
	}

	public void setCasillas(Set<Casilla> casillas) {
		this.casillas = casillas;
	}

	public Casilla addCasilla(Casilla casilla) {
		getCasillas().add(casilla);
		//casilla.setFormulario(this);
		return casilla;
	}

	public Casilla removeCasilla(Casilla casilla) {
		getCasillas().remove(casilla);
		//casilla.setFormulario(null);
		return casilla;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}