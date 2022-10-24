package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the DEPARTAMENTOS database table.
 * 
 */
@Entity
@Table(name="DEPARTAMENTOS")
@NamedQuery(name="Departamento.findAll", query="SELECT d FROM Departamento d")
@NamedQuery(name="Departamento.findByID", query="SELECT u FROM Departamento u WHERE u.id = :id")
@NamedQuery(name="Departamento.findAllFilter", query="SELECT d FROM Departamento d WHERE d.nombre LIKE :nombre")
@NamedQuery(name="Departamento.findDeptoFilter", query="SELECT d FROM Departamento d WHERE d.nombre LIKE :nombre")
public class Departamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_DEPARTAMENTO", unique=true, nullable=false, precision=38)
	private long id;

	@Column(name="NOM_DEPARTAMENTO", unique = true, nullable = false, length=50)
	private String nombre;
/*
	//bi-directional many-to-one association to Ciudad
	@OneToMany//(mappedBy="departamento")
	private List<Ciudad> ciudades;

	//bi-directional many-to-one association to Estacion
	@OneToMany(mappedBy="departamento")
	private List<Estacion> estaMediciones;
*/
	public Departamento() {
	}

	public long getIdDepartamento() {
		return this.id;
	}

	public void setIdDepartamento(long idDepartamento) {
		this.id = idDepartamento;
	}

	public String getNomDepartamento() {
		return this.nombre;
	}

	public void setNomDepartamento(String nomDepartamento) {
		this.nombre = nomDepartamento;
	}
/*
	public List<Ciudad> getCiudades() {
		return this.ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	public Ciudad addCiudade(Ciudad ciudade) {
		getCiudades().add(ciudade);
		ciudade.setDepartamento(this);

		return ciudade;
	}

	public Ciudad removeCiudade(Ciudad ciudade) {
		getCiudades().remove(ciudade);
		ciudade.setDepartamento(null);

		return ciudade;
	}

	public List<Estacion> getEstaMediciones() {
		return this.estaMediciones;
	}

	public void setEstaMediciones(List<Estacion> estaMediciones) {
		this.estaMediciones = estaMediciones;
	}

	public Estacion addEstaMedicione(Estacion estaMedicione) {
		getEstaMediciones().add(estaMedicione);
		estaMedicione.setDepartamento(this);

		return estaMedicione;
	}

	public Estacion removeEstaMedicione(Estacion estaMedicione) {
		getEstaMediciones().remove(estaMedicione);
		estaMedicione.setDepartamento(null);

		return estaMedicione;
	}
*/
}