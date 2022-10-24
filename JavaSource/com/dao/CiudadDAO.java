package com.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Ciudad;
import com.entities.Departamento;
import com.exceptions.ServiciosException;


@Stateless
@LocalBean
public class CiudadDAO {

	@PersistenceContext
	private EntityManager em;
	
	public CiudadDAO() {
		
	}
	
	public Ciudad create(Ciudad ciudad) throws ServiciosException {
		try {
			em.persist(ciudad);
			em.flush();
			em.refresh(ciudad);
			return ciudad;
		} catch (Exception e) {
			throw new ServiciosException("Error al crear Ciudad");
		}
	}

	public Ciudad findById(Long id) throws ServiciosException{
		try {
			TypedQuery<Ciudad> query = em
					.createNamedQuery("Ciudad.findId", Ciudad.class)
					.setParameter("id", id)
					;
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}
	
	public Ciudad merge(Ciudad ciudad) throws ServiciosException {
		try {
			em.merge(ciudad);
			em.flush();
			return ciudad;
		} catch (Exception e) {
			throw new ServiciosException("Error al actualizar Ciudad");
		}
	}

	public void remove(Long id) throws ServiciosException {
		try {
			Ciudad ciudad = em.find(Ciudad.class, id);
			em.remove(ciudad);
		} catch (Exception e) {
			throw new ServiciosException("Error al borrar Ciudad");
		}

	}

	public List<Ciudad> obtenerTodos() throws ServiciosException {
		try {
			TypedQuery<Ciudad> query = em.createNamedQuery("Ciudad.findAll",Ciudad.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros");
		}
	}

	public List<Ciudad> obtenerTodosFiltro(String filtro) throws ServiciosException {
		try {

			TypedQuery<Ciudad> query = em.createNamedQuery("Ciudad.findAllFilter",Ciudad.class)
					.setParameter("nombre", filtro);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("Error al buscar con filtro");
		}
	}
	
	public Ciudad find(String nombre) throws ServiciosException{
		try {
			TypedQuery<Ciudad> query = em.createNamedQuery("Ciudad.findAllFilter", Ciudad.class)
					.setParameter("nombre", nombre);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros con el filtro solicitado");
		}
	}
	
	public List<Ciudad> obtenerTodosDepto(String depto) throws ServiciosException{
		//Hay que hacerla para que devuleva por departamento, ahora esta devolviendo todo
		try {
			TypedQuery<Ciudad> query = em.createNamedQuery("Ciudad.findByDepartamento", Ciudad.class)
					.setParameter("departamento", depto);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros");
		}
	}

	
	public Ciudad obtener(Long id) throws ServiciosException{
		try {
			TypedQuery<Ciudad> query = em.createNamedQuery("Ciudad.findId", Ciudad.class)
					.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros con el id solicitado");
		}
	}

}
