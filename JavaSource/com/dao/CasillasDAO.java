package com.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Casilla;
import com.exceptions.ServiciosException;

@Stateless
@LocalBean
public class CasillasDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	
	 public Casilla create(Casilla casilla) {

			Casilla c = null;
			try {
				c = findBorrado(casilla.getNomCasilla());
			} catch (ServiciosException e) {
				new ServiciosException("No se encontró Casilla");
			}
			
			if( c == null) {
				em.persist(casilla);
				em.flush();
				em.refresh(casilla);
				return casilla;
			}else {
				casilla.setIdCasilla(c.getIdCasilla());
				casilla.setBorrado(0);
				em.merge(casilla);
				em.flush();
				return casilla;
			}
		

	}
	 
	 public Casilla merge(Casilla casilla) throws ServiciosException {
			try {
				em.merge(casilla);
				em.flush();
				return casilla;
			} catch (Exception e) {
				throw new ServiciosException("Error modificar casilla");
			}

		}
	 
	 public void remove(Long id) throws ServiciosException {

			try {
				Casilla casilla = em.find(Casilla.class, id);
				//em.remove(casilla);
						casilla.setBorrado(1);
				em.merge(casilla);
				em.flush();
			} catch (Exception e) {
				throw new ServiciosException("Error al borrar casilla");
			}

		}
	 
	 public List<Casilla> findAll() throws ServiciosException {
			try {
				TypedQuery<Casilla> query = em.createNamedQuery("Casilla.findAll",Casilla.class);
				return query.getResultList();
			} catch (Exception e) {
				throw new ServiciosException("No existen registros");
			}

		} 
	 //findbyID
	 public Casilla findbyID(Long id) throws ServiciosException{
			try {
				TypedQuery<Casilla> query = em.createNamedQuery("Casilla.findCasillaFilterID", Casilla.class)
						.setParameter("id", id);
				return query.getSingleResult();
			} catch (Exception e) {
				throw new ServiciosException("No existen registros con el filtro solicitado");
			}
		}
	 
	 public Casilla find(String nombre) throws ServiciosException{
			try {
				TypedQuery<Casilla> query = em.createNamedQuery("Casilla.findCasillaFilter", Casilla.class)
						.setParameter("nombre", nombre);
				return query.getSingleResult();
			} catch (Exception e) {
				throw new ServiciosException("No existen registros con el filtro solicitado");
			}
		}
	 
	 public Casilla findBorrado(String nombre) throws ServiciosException{
			try {
				TypedQuery<Casilla> query = em.createQuery("SELECT c FROM Casilla c WHERE c.nombre LIKE :nombre AND c.borrado = 1", Casilla.class)
						.setParameter("nombre", nombre);
				return query.getSingleResult();
			} catch (Exception e) {
				throw new ServiciosException("No existen registros con el filtro solicitado");
			}
		}
	 
	 public Casilla findEvenBorrado(String nombre) throws ServiciosException{
			try {
				TypedQuery<Casilla> query = em.createQuery("SELECT c FROM Casilla c WHERE c.nombre LIKE :nombre", Casilla.class)
						.setParameter("nombre", nombre);
				return query.getSingleResult();
			} catch (Exception e) {
				throw new ServiciosException("No existen registros con el filtro solicitado");
			}
		}
	 
	 public boolean findBooleanCasilla(String nombre){
			try {
//				TypedQuery<Casilla> query = em.createNamedQuery("Casilla.findCasillaFilter", Casilla.class)
//						.setParameter("nombre", nombre);
				TypedQuery<Casilla> query = em.createQuery("SELECT c FROM Casilla c WHERE c.nombre = :nombre", Casilla.class)
						.setParameter("nombre", nombre);
				System.out.println(query.getResultList().size());
				if(query.getResultList().size()>0) {
					return true;
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}
	 
	
}
