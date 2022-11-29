package com.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Estacion;
import com.exceptions.ServiciosException;

@Stateless
@LocalBean
public class EstacionesDAO {

	@PersistenceContext
	private EntityManager em;

	public EstacionesDAO() {

	}

	public Estacion create(Estacion estacion) throws ServiciosException {

		try {
			em.persist(estacion);
			em.flush();
			em.refresh(estacion);
			return estacion;
		} catch (Exception e) {
			throw new ServiciosException("Error al dar alta de estaci�n");
		}
	}
	
	public Estacion merge(Estacion estacion) throws ServiciosException {

		try {
			em.merge(estacion);
			em.flush();
			return estacion;
		} catch (Exception e) {
			throw new ServiciosException("Error al dar modificar de estaci�n");
		}
	}
	
	public void remove(Long id) throws ServiciosException {

		try {
			Estacion estacion = em.find(Estacion.class, id);
			em.remove(estacion);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException("Error al borrar estaci�n");
		}
	}
	
	public List<Estacion> findAll() throws ServiciosException {

		try {
			TypedQuery<Estacion> query = em.createNamedQuery("Estacion.findAll", Estacion.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("Error al buscar de estaci�n");
		}
		
	}
	
	public List<Estacion> findAllFiltered(String nombre) throws ServiciosException {

		try {
			TypedQuery<Estacion> query = em.createNamedQuery("Estacion.findAll", Estacion.class)
					.setParameter("nombre", nombre);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("Error al buscar de estaci�n");
		}
		
	}

	public Estacion findByID(long id)  throws ServiciosException {
		try {
			TypedQuery<Estacion> query = em.createNamedQuery("Estacion.findByID", Estacion.class)
					.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ServiciosException("Error al buscar de estaci�n");
		}
	}
	
	public Estacion findByName(String nombre)  throws ServiciosException {
		try {
			TypedQuery<Estacion> query = em.createNamedQuery("Estacion.findByName", Estacion.class)
					.setParameter("nombre", nombre);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ServiciosException("Error al buscar de estaci�n");
		}
	}
}
