package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the CIUDADES database table.
 * 
 */
@Entity
@Table(name="CIUDADES")
@NamedQuery(name="Ciudad.findId", query="SELECT c FROM Ciudad c WHERE c.id = :id")
@NamedQuery(name="Ciudad.findAll", query="SELECT c FROM Ciudad c")
@NamedQuery(name="Ciudad.findAllFilter", query="SELECT c FROM Ciudad c WHERE c.nombre LIKE :nombre")
@NamedQuery(name="Ciudad.findByDepartamento", query="SELECT a FROM Ciudad a WHERE a.departamento.nombre = :departamento")
public class Ciudad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_CIUDAD", unique=true, nullable=false, precision=38)
	private long id;

	@Column(name="NOM_CIUDAD", length=50)
	private String nombre;
	
	//bi-directional many-to-one association to Departamento
	@ManyToOne
	@JoinColumn(name="ID_DEPARTAMENTO", nullable=false)
	private Departamento departamento;

	public Ciudad() {
	}

	public long getIdCiudad() {
		return this.id;
	}

	public void setIdCiudad(long idCiudad) {
		this.id = idCiudad;
	}

	public String getNombreCiudad() {
		return this.nombre;
	}

	public void setNombreCiudad(String nomCiudad) {
		this.nombre = nomCiudad;
	}
	
	public Departamento getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
}