package com.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.dao.UsuarioDAO;
import com.entities.Administrador;
import com.entities.Aficionado;
import com.entities.Ciudad;
import com.entities.Investigador;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.dto.AdministradorDTO;
import com.services.dto.AficionadoDTO;
import com.services.dto.InvestigadorDTO;
import com.services.dto.UsuarioDTO;

/**
 * Session Bean implementation class DepartamentosBean
 */
@Path("usuarios")
@Stateless
@LocalBean
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private CiudadesBean ciudadesBean;

	public UsuariosBean() {

	}

	public UsuarioDTO crear(UsuarioDTO udto) throws ServiciosException {
		try {
			Usuario usuario = mapeo(udto);
					usuario.setBorrado(0);
					
			usuario = usuarioDAO.create(usuario);
			udto.setId(usuario.getId());

			return udto;
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el usuario");
		}
	}
	
	public UsuarioDTO modificar(UsuarioDTO udto) throws ServiciosException{
		try {
			Usuario u = mapeo(buscar(udto.getId()));
					
			Usuario usuario = mapeo(udto).setId(udto.getId());
			
			if(usuario.getContrasena() == null)
				usuario.setContrasena(u.getContrasena());
			
			//trae id y remueve fuerte.
			this.delete(udto.getId());
			
			usuarioDAO.merge(usuario);
			
			return udto;
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el usuario");
		}
	}
	
	public void eliminar(long idUsuario) throws ServiciosException{
		try {
			usuarioDAO.remove(idUsuario);
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo eliminar el usuario");
		}
	}

	@GET
	@Path("listarUsuarios")
	@Produces("application/json")
	public List<UsuarioDTO> encontrarTodos() throws ServiciosException {
		List<UsuarioDTO> dto = new ArrayList<UsuarioDTO>();
		
		for(Usuario u: usuarioDAO.findAll()) {
			dto.add(mapeo(u));
		}
		return dto;
	}

	public UsuarioDTO buscar(long id) throws ServiciosException {
		return mapeo(usuarioDAO.findById(id));
	}

	public UsuarioDTO buscar(String nombreDeUsuario) /*throws ServiciosException*/ {
		try {
			return mapeo(usuarioDAO.findByUserName(nombreDeUsuario, 0));
		} catch (ServiciosException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean buscarMail(String mail) /*throws ServiciosException*/{
		try {
			return usuarioDAO.findByMail(mail);		
		} catch (Exception e) {
			e.printStackTrace();	
			return false;
		}
	}

	public boolean buscarDocument(int documento) {
		try {
			Administrador a = usuarioDAO.findAdministradorByDocument(documento,0);
			Investigador i = usuarioDAO.findInvestigadorByDocument(documento,0);
			if(a != null || i != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean buscarUserName(String nombreDeUsuario) /*throws ServiciosException*/ {
		try {
			return usuarioDAO.findBooleanByUserName(nombreDeUsuario,0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Usuario mapeo(UsuarioDTO udto) throws ServiciosException {
		Usuario usuario = null;

		if (udto instanceof AdministradorDTO) {
			Ciudad ciudad = new Ciudad();
			if(((AdministradorDTO) udto).getCiudad() != null) {
				ciudad = ciudadesBean.obtener(((AdministradorDTO) udto).getCiudad().getId());
			}
			usuario = new Administrador();

			((Administrador) usuario).setDocumento(((AdministradorDTO) udto).getDocumento());
			((Administrador) usuario).setDomicilio(((AdministradorDTO) udto).getDomicilio());
			((Administrador) usuario).setTelefono(((AdministradorDTO) udto).getTelefono());
			if(((AdministradorDTO) udto).getCiudad() != null) {
				((Administrador) usuario).setCiudade(ciudad);
			}
		}

		if (udto instanceof InvestigadorDTO) {
			Ciudad ciudad = new Ciudad();
			
			if(((InvestigadorDTO) udto).getCiudad() != null) {
				ciudad = ciudadesBean.obtener(((InvestigadorDTO) udto).getCiudad().getId());
			}
			
			usuario = new Investigador();

			((Investigador) usuario).setDocumento(((InvestigadorDTO) udto).getDocumento());
			((Investigador) usuario).setDomicilio(((InvestigadorDTO) udto).getDomicilio());
			((Investigador) usuario).setTelefono(((InvestigadorDTO) udto).getTelefono());
			if(((InvestigadorDTO) udto).getCiudad() != null) {
				((Investigador) usuario).setCiudade(ciudad);
			}
		}

		if (udto instanceof AficionadoDTO) {
			usuario = new Aficionado();

			((Aficionado) usuario).setOcupacion(((AficionadoDTO) udto).getOcupacion());
		}

		usuario.setApellido(udto.getApellido());
		usuario.setNombre(udto.getNombre());
		usuario.setMail(udto.getMail());
		usuario.setNombreUsuario(udto.getUsuario());
		if(udto.getContrasena().length != 0)
		usuario.setContrasena(udto.getContrasena());
		usuario.setTipo(udto.getTipo());

		return usuario;
	}

	public UsuarioDTO mapeo(Usuario usuario) throws ServiciosException {
		UsuarioDTO udto = null;

		if (usuario instanceof Administrador) {
			udto = new AdministradorDTO();

			((AdministradorDTO) udto)
			.setDocumento(((Administrador) usuario).getDocumento())
			.setDomicilio(((Administrador) usuario).getDomicilio())
			.setTelefono(((Administrador) usuario).getTelefono())
			.setCiudad(ciudadesBean.mapInversoCiudad(((Administrador) usuario).getCiudade()));
		}
		if (usuario instanceof Investigador) {
			udto = new InvestigadorDTO();

			((InvestigadorDTO) udto)
			.setDocumento(((Investigador) usuario).getDocumento())
			.setDomicilio(((Investigador) usuario).getDomicilio())
			.setTelefono(((Investigador) usuario).getTelefono())
			.setCiudad(ciudadesBean.mapInversoCiudad(((Investigador) usuario).getCiudade()));
		}
		if (usuario instanceof Aficionado) {
			udto = new AficionadoDTO();

			((AficionadoDTO) udto)
			.setOcupacion(((Aficionado) usuario).getOcupacion());
		}

		udto.setId(usuario.getId());
		udto.setApellido(usuario.getApellido());
		udto.setNombre(usuario.getNombre());
		udto.setMail(usuario.getMail());
		udto.setUsuario(usuario.getNombreUsuario());
		udto.setContrasena(usuario.getContrasena());
		udto.setTipo(usuario.getTipo());

		return udto;
	}
	
	public void delete(long idUsuario) throws ServiciosException {
		usuarioDAO.delete(idUsuario);	
	}
}