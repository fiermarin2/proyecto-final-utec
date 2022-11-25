package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Administrador;
import com.entities.Investigador;
import com.entities.Usuario;
import com.exceptions.ServiciosException;

@Stateless
public class UsuarioDAO {

	@PersistenceContext
	private EntityManager em;

	public UsuarioDAO() {
		// TODO Auto-generated constructor stub
	}

	public Usuario create(Usuario usuario) throws ServiciosException {
		Usuario u = findByUserName(usuario.getNombreUsuario(), 1);
		
		if(u != null) {
			
			delete(u.getId());

		}
		em.persist(usuario);
		em.flush();
		em.refresh(usuario);
		
		return usuario;
	}

	public void merge(Usuario usuario) throws ServiciosException {

		em.merge(usuario);
		em.flush();
	}

	public void remove(Long id) throws ServiciosException {

		try {
			Usuario usuario = em.find(Usuario.class, id);
					usuario.setBorrado(1);
			em.merge(usuario);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException("Error al borrar usuario");
		}
	}

	public List<Usuario> findAll() throws ServiciosException{
		try {
			TypedQuery<Usuario> query = em
					.createNamedQuery("Usuario.findAll", Usuario.class)
					;
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}

	public Usuario findById(Long id) throws ServiciosException{
		try {
			TypedQuery<Usuario> query = em
					.createNamedQuery("Usuario.findByID", Usuario.class)
					.setParameter("id", id)
					;
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("NO EXISTEN REGISTROS");
		}

	}

	public Usuario findByUserName(String nombreUsuario, int borrado) /*throws ServiciosException*/{
		try {										
			TypedQuery<Usuario> query = em
					//.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario and u.borrado = 0",Usuario.class)
					.createNamedQuery("Usuario.findbyUserName",Usuario.class)
					.setParameter("nombreUsuario", nombreUsuario)
					.setParameter("borrado", borrado)
					;			
			return query.getSingleResult();
		} catch (Exception e) {
			
			return null;
			//throw new ServiciosException("NO EXISTEN REGISTROS");
		}
	}
	
	public Object findInvestigadorByDocument(int documento, int borrado) /*throws ServiciosException*/{
		try {
				TypedQuery<Investigador> query = em.createNamedQuery("Investigador.findDocument",Investigador.class)
						.setParameter("documento", documento)
						.setParameter("borrado", borrado)
						;
						return query.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			//throw new ServiciosException("NO EXISTEN REGISTROS");
		}
	}
	
	public Administrador findAdministradorByDocument(int documento, int borrado) /*throws ServiciosException*/{
		try {										
			TypedQuery<Administrador> query = em
					//.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario and u.borrado = 0",Usuario.class)
					.createNamedQuery("Administrador.findDocument",Administrador.class)
					.setParameter("documento", documento)
					.setParameter("borrado", borrado)
					;			
			return (Administrador) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
			//throw new ServiciosException("NO EXISTEN REGISTROS");
		}
	}
	
	
	public boolean findBooleanByUserName(String nombreUsuario, int borrado) /*throws ServiciosException*/{
		//try {										
			TypedQuery<Usuario> query = em
					//.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario and u.borrado = 0",Usuario.class)
					.createNamedQuery("Usuario.findbyUserName",Usuario.class)
					.setParameter("nombreUsuario", nombreUsuario)
					.setParameter("borrado", borrado)
					;

			
				 if(query.getResultList().size() >= 1)
					 return true;
				 else
					return false;
		/*} catch (Exception e) {
			e.printStackTrace();
			return false;
			//throw new ServiciosException("NO EXISTEN REGISTROS");
		}*/
	}
	
	public boolean findByMail(String mail) /*throws ServiciosException*/{
		//try {
			TypedQuery<Usuario> query = em
					//.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario and u.borrado = 0",Usuario.class)
					.createNamedQuery("Usuario.findByMail",Usuario.class)
					.setParameter("mail", mail)
					;
			//System.out.println(query.getSingleResult());
			if(query.getResultList().size() >= 1) {
				return true;
				}
			return false;

		/*} catch (Exception e) {
			e.printStackTrace();
			return false;
			//throw new ServiciosException("NO EXISTEN REGISTROS");
		}*/
	}

	public Usuario obtenerLogIn(String usuario, char[] contrasenia) throws ServiciosException {
		try {
    		TypedQuery<Usuario> query = em.createQuery(
    				"SELECT u FROM Usuario u WHERE u.nombreUsuario = :usuario AND u.contrasena = :contrasenia AND borrado = 0", Usuario.class)
    				.setParameter("usuario", usuario)
    				.setParameter("contrasenia", contrasenia);
    		
    		if(query.getSingleResult() != null)
    			return query.getSingleResult();
    		else
    			return null;
			
    	}catch(NoResultException n) {
    		return null;
		} catch (Exception e) {
			
			throw new ServiciosException("Listar " + e.getMessage());

		}
	}
	
	public void delete(long id) throws ServiciosException{
		try {
			Usuario u = em.find(Usuario.class, id);
			em.remove(u);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException("Error al delete permanente" + e.getMessage());
		}
	}

}
