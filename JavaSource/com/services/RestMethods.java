package com.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dao.UsuarioDAO;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.dto.FormularioDTO;
import com.services.dto.RegistroDTO;
import com.services.dto.UsuarioDTO;

@Path("methods")
public class RestMethods {
	
	@EJB
	private UsuarioDAO usuarioDAO;
	@Inject
	private UsuariosBean userBean;
	@Inject
	private RegistrosBean registroBean;
	@Inject
	private FormulariosBean formBean;
	
	@GET
	@Path("listarUsuarios")
	@Produces("application/json")
	public List<UsuarioDTO> encontrarTodos() throws ServiciosException {
		List<UsuarioDTO> dto = new ArrayList<UsuarioDTO>();
		
		for(Usuario u: usuarioDAO.findAll()) {
			dto.add(userBean.mapeo(u));
		}
		return dto;
	}
	
	//@Consumes(MediaType.APPLICATION_JSON)
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginRest(UsuarioDTO credenciales) {
		
		try {
			UsuarioDTO usuario = new UsuarioDTO();
			usuario = userBean.mapeo(usuarioDAO.obtenerLogIn(credenciales.getUsuario(), credenciales.getContrasena()));
			usuario.setContrasena(null);

			return Response.ok(usuario).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.NETWORK_AUTHENTICATION_REQUIRED).entity("No autorizado").build();
		}

	}
	
	
	@GET
	@Path("listar/{userId}")
	@Produces("application/json")
	public ArrayList<RegistroDTO> listarMediciones(@PathParam(value = "userId") String userId) {
		
		 ArrayList<RegistroDTO> listaMediciones = new ArrayList<>(); 
		 try {
			  long id = Long.parseLong(userId);
			  listaMediciones = (ArrayList<RegistroDTO>) registroBean.obtenerTodos(userBean.buscar(id)); 
			  return listaMediciones;
		 } catch (ServiciosException e) {
			 e.printStackTrace();
			 return null; 
		 } 
		 
	}
	
	@GET
	@Path("forms")
	@Produces("application/json")
	public ArrayList<FormularioDTO> listarForms() {
		ArrayList<FormularioDTO> lista = new ArrayList<FormularioDTO>();
		try {
			lista = (ArrayList<FormularioDTO>) formBean.obtenerTodos();
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("form/{formId}")
	@Produces("application/json")
	public FormularioDTO obtenerForm(@PathParam(value = "formId") String formId) {
		FormularioDTO form = new FormularioDTO();
		try {
			form = formBean.buscar(Long.parseLong(formId));
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
