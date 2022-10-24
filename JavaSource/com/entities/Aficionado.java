package com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the AFICIONADOS database table.
 * 
 */
@Entity
@Table(name="AFICIONADOS")
@NamedQuery(name="Aficionado.findAll", query="SELECT a FROM Aficionado a")
@PrimaryKeyJoinColumn(referencedColumnName = "ID_USUARIO" , name = "ID_AFICIONADO")
public class Aficionado extends Usuario {
	private static final long serialVersionUID = 1L;
/*
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_AFICIONADO", unique=true, nullable=false, precision=38)
	private long idAficionado;
*/
	@Column(length=50)
	private String ocupacion;
	
	public Aficionado() {
	}
/*	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private Usuario usuario;



	public long getIdAficionado() {
		return this.idAficionado;
	}

	public void setIdAficionado(long idAficionado) {
		this.idAficionado = idAficionado;
	}
*/
	public String getOcupacion() {
		return this.ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
/*
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
*/
}