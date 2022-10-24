package com.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Departamento;
import com.entities.Formulario;
import com.exceptions.ServiciosException;

/**
 * Session Bean implementation class FormularioDAO
 */
@Stateless
@LocalBean
public class FormularioDAO {

	@PersistenceContext
	
	private EntityManager em;
	
    public FormularioDAO() {
        // TODO Auto-generated constructor stub
    }
    
    public Formulario crear(Formulario form) {
    	
    		Formulario f= null;
			try {
				f = find(form.getNomFormulario(),1);
			} catch (ServiciosException e) {
				new ServiciosException("No existe formulario");
			}
    		
    		if(f == null) {
    			em.persist(form);
    			em.flush();
    			em.refresh(form);
    			
    		}else {
    			form.setIdFormulario(f.getIdFormulario());
    			form.setBorrado(0);
    			form.setFecHora(Date.valueOf(LocalDate.now()));
    			em.merge(form);
    			em.flush();
    		}
    		return form;

    }
    
    public Formulario merge(Formulario form) throws ServiciosException{
    	//try {
			em.merge(form);
			em.flush();
			return form;
			/*
		} catch (Exception e) {
			e.getMessage();
			throw new ServiciosException("Error modificar formulario");
		} */
    }
    
    public Formulario findById(Long id) throws ServiciosException{
		try {
			TypedQuery<Formulario> query = em
					.createNamedQuery("Formulario.findByID", Formulario.class)
					.setParameter("id", id)
					;
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}
    
    public void remove(Long id) throws ServiciosException{
    	try {
			Formulario frm = em.find(Formulario.class, id);
				frm.setBorrado(1);
				em.merge(frm);
				em.flush();
		} catch (Exception e) {
			throw new ServiciosException("Error eliminar formulario");
		}
    }
    
    public Formulario find(String nombre, int borrado) throws ServiciosException{
    	try {
			TypedQuery<Formulario> query = em.createNamedQuery("Formulario.findAllFiltered",Formulario.class)
					.setParameter("nombre", nombre)
					.setParameter("borrado", borrado);
			 return query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error no existe formulario");
		}
    }
    
    
    public boolean findBooleanName(String nombre) throws ServiciosException{
    	try {
			TypedQuery<Formulario> query = em.createNamedQuery("Formulario.findAllFiltered",Formulario.class)
					.setParameter("borrado", 0)
					.setParameter("nombre", nombre);
			 if(query.getResultList().size() >= 1) {
				 return true;
			 }
			 return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error no existe formulario!!");
		}
    }
    
    public List<Formulario> findAll() throws ServiciosException{
    	try {
    		TypedQuery<Formulario> query = em.createNamedQuery("Formulario.findAll",Formulario.class);
    		return query.getResultList();
		} catch (Exception e) {
			throw new ServiciosException("Error al listar formulario"); 
		}
    }

}
