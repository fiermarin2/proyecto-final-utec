package com.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.persistence.PersistenceException;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
	@Inject
	private UsuariosBean beanu;
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

	//@Consumes(MediaType.APPLICATION_JSON)
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginRest(UsuarioDTO credenciales) {
		
		try {
			UsuarioDTO usuario = new UsuarioDTO();
			usuario = beanu.mapeo(usuarioDAO.obtenerLogIn(credenciales.getUsuario(), credenciales.getContrasena()));
			usuario.setContrasena(null);

			return Response.ok(usuario).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.NETWORK_AUTHENTICATION_REQUIRED).entity("No autorizado").build();
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

	public boolean buscarDocument(int documento, String tipoUsuario) {
		try {
			Administrador a = null;
			Investigador i = null;
			if(tipoUsuario.equals("ADMINISTRADOR")) {
				a = (Administrador) usuarioDAO.findAdministradorByDocument(documento,0);
			}
			else
				i = (Investigador) usuarioDAO.findInvestigadorByDocument(documento,0);
			
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
		if(usuario == null) {
			usuario = new Administrador();
		}
		
		usuario.setApellido(udto.getApellido());
		usuario.setNombre(udto.getNombre());
		usuario.setMail(udto.getMail());
		usuario.setNombreUsuario(udto.getUsuario());
		if(udto.getContrasena() != null) {
			if(udto.getContrasena().length != 0) {
				usuario.setContrasena(udto.getContrasena());
			}
		}
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
	
	public UsuarioDTO usuarioLDAP(String usuarioLDAP, String contrasena) {

		UsuarioDTO usuario = new UsuarioDTO();

		try {
			String context = "com.sun.jndi.ldap.LdapCtxFactory";
			String provider = "ldap://hq.utec.uy:389";
			String returnedAtts[]={"givenName","sn","userPrincipalName", "samAccountName", "memberOf","info"};
			String searchBase = "dc=hq,dc=utec,dc=uy";
			String searchFilter = "(&(objectClass=user)"
					+ "(memberOf:1.2.840.113556.1.4.1941:=CN=GS_iAGRO-App,OU=Data Center,DC=hq,DC=utec,DC=uy)"
					+ "(sAMAccountName="+usuarioLDAP.split("\\\\")[1]+")"
					+ "(|(memberOf=CN=GS_Investigadores_iAGRO,OU=Data Center,DC=hq,DC=utec,DC=uy)"
					+ "(memberOf=CN=GS_Administradores_iAGRO,OU=Data Center,DC=hq,DC=utec,DC=uy)))";
			SearchControls searchCtls = new SearchControls();

			Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, context);
			ldapEnv.put(Context.PROVIDER_URL, provider);
			ldapEnv.put(Context.SECURITY_PRINCIPAL, usuarioLDAP);
			ldapEnv.put(Context.SECURITY_CREDENTIALS, contrasena);

			DirContext ldapContext = new InitialDirContext(ldapEnv);     

			searchCtls.setReturningAttributes(returnedAtts);
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

			Attributes attrs = (Attributes)answer.next().getAttributes();

			ldapContext.close();

			usuario = (attrs.get("memberOf").toString().contains("Administradores"))?
					new AdministradorDTO().setDocumento(Integer.parseInt((String) attrs.get("info").get(0)))
					:
					new InvestigadorDTO().setDocumento(Integer.parseInt((String) attrs.get("info").get(0)));
			
			usuario.setNombre((String) attrs.get("givenName").get(0));
			usuario.setApellido((String) attrs.get("sn").get(0));
			usuario.setMail((String)  attrs.get("userPrincipalName").get(0));
			usuario.setContrasena(contrasena.toCharArray());
			usuario.setUsuario(usuarioLDAP);

			if (!buscarUserName(usuario.getUsuario())) {
				crear(usuario);
			}
			else {
				usuario.setId(buscar(usuarioLDAP).getId());
				modificar(usuario);
			}
		}catch(AuthenticationException e) {
			System.out.println("Error de usuario o contraseña");

		}catch(CommunicationException c) {
			System.out.println("Problemas de conexión con el servidor");

		}catch(NoInitialContextException i) {
			System.out.println("error de integración LDAP");

		}catch(PartialResultException p) {
			System.out.println("no encuentro un atributo o usuario");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ServiciosException e) {
			System.out.println("Ërror al crear o actualizar el usuario");
		}
		return usuario;

	}
}