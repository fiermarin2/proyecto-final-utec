package com.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Departamento;
import com.entities.Usuario;
import com.exceptions.ServiciosException;


@Stateless
@LocalBean
public class DepartamentoDAO {

	@PersistenceContext
	private EntityManager em;
	
	public DepartamentoDAO() {
		// TODO Auto-generated constructor stub
	}

	public Departamento create(Departamento depto) throws ServiciosException {
		//try {
			em.persist(depto);
			em.flush();
			em.refresh(depto);
			return depto;
		//} catch (Exception e) {
		//	throw new ServiciosException("Error alta Departamento");
		//}

	}

	public Departamento findById(Long id) throws ServiciosException{
		try {
			TypedQuery<Departamento> query = em
					.createNamedQuery("Departamento.findByID", Departamento.class)
					.setParameter("id", id)
					;
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}
	
	public Departamento merge(Departamento depto) throws ServiciosException {

		try {
			em.merge(depto);
			em.flush();
			return depto;
		} catch (Exception e) {
			throw new ServiciosException("Error al modificar Departamento");
		}

	}

	public void remove(Long id) throws ServiciosException {

		try {
			Departamento depto = em.find(Departamento.class, id);
			em.remove(depto);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException("Error al borrar departamento");
		}

	}

	public List<Departamento> findAll() throws ServiciosException {
		try {
			TypedQuery<Departamento> query = em.createNamedQuery("Departamento.findAll",Departamento.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros");
		}

	}

	public List<Departamento> FindAllFiltered(String filtro) throws ServiciosException {
		try {
			TypedQuery<Departamento> query = em.createNamedQuery("Departamento.findAllFilter", Departamento.class)
					.setParameter("nombre", filtro);
			return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros con el filtro solicitado");
		}
	}
	
	public Departamento find(String nombre) throws ServiciosException{
		try {
			TypedQuery<Departamento> query = em.createNamedQuery("Departamento.findDeptoFilter", Departamento.class)
					.setParameter("nombre", nombre);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ServiciosException("No existen registros con el filtro solicitado");
		}
	}

}
