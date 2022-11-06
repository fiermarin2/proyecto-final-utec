package com.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Formulario;
import com.entities.Registro;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.dto.UsuarioDTO;

/**
 * Session Bean implementation class ObtieneDatoDAO
 */
@Stateless
@LocalBean
public class RegistroDAO {
	
	@PersistenceContext
	private EntityManager em;
	
    public RegistroDAO() {
        // TODO Auto-generated constructor stub
    }
    
    
    public Registro crear(Registro dato) throws ServiciosException {
    	try {
			em.persist(dato);
			em.flush();
			em.refresh(dato);
			return dato;
		} catch (Exception e) {
			throw new ServiciosException("error al persistir registro de datos.");
		}
    }
    
    public void remove(Long id) throws ServiciosException{
    	try {
			Registro reg = em.find(Registro.class, id);
			em.remove(reg);
		} catch (Exception e) {
			throw new ServiciosException("Error eliminar medicion");
		}
    }
       
	public Registro merge(Registro form) throws ServiciosException{
    	try {
			em.merge(form);
			em.flush();
			return form;
			
		} catch (Exception e) {
			e.getMessage();
			throw new ServiciosException("Error modificar registro de datos");
		} 
    }
    
	public List<Registro> findByUserId(Long id) throws ServiciosException{
		try {
			TypedQuery<Registro> query = em
					.createNamedQuery("Registro.findByUserID", Registro.class)
					.setParameter("id", id)
					;
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}
    
    //FIXME: arreglar para que ande
    public List<Registro> findByFromulario(Long idFormulario) throws ServiciosException{
    	try {
			TypedQuery<Registro> query = em.createNamedQuery("Registro.findAllPorFormulario",Registro.class)
					.setParameter("formulario", idFormulario);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new ServiciosException("No existen registros");
		}
    }
    
    
    //FIXME: arreglar para que ande
    public List<Registro> findByFechas(Date min, Date max) throws ServiciosException{
    	try {
			TypedQuery<Registro> query = em.createNamedQuery("Registro.findAllPorFecha",Registro.class)
					.setParameter("min", min)
					.setParameter("max", max);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new ServiciosException("No existen registros");
		}
    }

    
    public List<Registro> findAll() throws ServiciosException{
    	try {
    		TypedQuery<Registro> query = em.createNamedQuery("Registro.findAll",Registro.class);
    		return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("Error al obtener todos los registros"); 
		}
    }
    
    public boolean findByEstaciones(Long id) throws ServiciosException{
    	try {
    		TypedQuery<Registro> query = em.createQuery("SELECT o FROM Registro o WHERE o.estacion.id = :id",Registro.class)
    			.setParameter("id", id);
    		if(query.getResultList().size()>0)
    			return true;
    		else 
    			return false;
		} catch (Exception e) {
			throw new ServiciosException("Error al obtener todos los registros por estaecion");
		}
    }

    
    
}
