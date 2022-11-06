package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OBTI_DATOS database table.
 * 
 */
@Entity
@Table(name="OBTI_DATOS")
@NamedQuery(name="Registro.findAll", query="SELECT o FROM Registro o ORDER BY o.fechaHora DESC")
@NamedQuery(name="Registro.findByUserID", query="SELECT a FROM Registro a WHERE a.usuario.id = :id")
@NamedQuery(name="Registro.findAllPorFormulario", query="SELECT o FROM Registro o WHERE o.formulario.id = :formulario")
@NamedQuery(name="Registro.findAllPorFecha", query="SELECT o FROM Registro o WHERE o.fechaHora >= :min and o.fechaHora <= :max")
public class Registro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_OBTIENE", unique=true, nullable=false, precision=38)
	private long id;

	
	@Column(name="FEC_HORA", nullable=false, insertable = false, updatable = false, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	@Temporal(value = TemporalType.DATE)
	private Date fechaHora;
	
	@ElementCollection
	@CollectionTable(name="PARAMETROS", joinColumns = @JoinColumn(name="ID_PARAMETROS"))
	@MapKeyJoinColumn(name="FK_CASILLA")
	@Column(name = "VALORES", length = 50)
	private Map<Casilla, String> valor;

	//bi-directional many-to-one association to Casilla
	@ManyToOne()
	@JoinColumn(name="ID_FORMULARIO", nullable=false)
	private Formulario formulario;
	
	
	//bi-directional many-to-one association to Estacion
	@ManyToOne
	@JoinColumn(name="ID_ESTACION", nullable=false)
	private Estacion estacion;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private Usuario usuario;

	public Registro() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Map<Casilla, String> getValor() {
		return valor;
	}

	public void setValor(Map<Casilla, String> valor) {
		this.valor = valor;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
/*
	public Casilla getCasilla() {
		return casilla;
	}

	public void setCasilla(Casilla casilla) {
		this.casilla = casilla;
	}
*/
	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


}