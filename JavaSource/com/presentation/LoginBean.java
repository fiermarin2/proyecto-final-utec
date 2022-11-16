package com.presentation;


import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
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
import javax.servlet.http.HttpSession;

import com.dao.UsuarioDAO;
import com.entities.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.UsuariosBean;
import com.services.dto.AdministradorDTO;
import com.services.dto.InvestigadorDTO;
import com.services.dto.UsuarioDTO;

/**
 * Session Bean implementation class LoginBean

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
 *
 */
@Named(value="gestionLogin")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private UsuarioDAO daou;
	@Inject
	private UsuariosBean beanu;
	private static UsuarioDTO usuario;
	private String usr = "";
	private String pwd = "";
	static DirContext ldapContext;
	/**
	 * Default constructor. 
	 */
	@PostConstruct	
	public void init(){

		try {
			usuario = new AdministradorDTO();
			usuario.setNombre("Grupo 10");
			usuario.setApellido("Semana 4");
			usuario.setUsuario("admin");
			usuario.setContrasena("admin".toCharArray());
			usuario.setTipo(TipoUsuario.ADMINISTRADOR);
			((AdministradorDTO) usuario).setTelefono(11111111);
			((AdministradorDTO) usuario).setDocumento(11111111);
			((AdministradorDTO) usuario).setDomicilio("Domicilio 1111");
			((AdministradorDTO) usuario).setMail("admin@admin.adm");

			if (!beanu.buscarUserName(usuario.getUsuario())) {
				beanu.crear(usuario);
				System.out.println("Se creó el usuario");

				usuario.setId(1L);
				usuario.setNombre("Grupo 10");
				beanu.modificar(usuario);
			}

		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String checkLogIn() throws ServiciosException, IOException {
		
		if (usr.contains("\\")) loginLDAP(usr, pwd);
		
		usuario = beanu.mapeo(daou.obtenerLogIn(usr, pwd.toCharArray()));

		HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( true );
		ses.setAttribute("id", usuario.getId());
		ses.setAttribute("username", usr);

		if (usuario != null) {
			return "views/menuPrincipal.xhtml?faces-redirect=true";
		}
		return "Login";
	}

	public void logout() {

		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.invalidateSession();
			ec.redirect("../Login.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UsuarioDTO loginLDAP(String usrLDAP, String pwd) {

		UsuarioDTO usuario = new UsuarioDTO();

		try {
			String context = "com.sun.jndi.ldap.LdapCtxFactory";
			String provider = "ldap://192.168.122.51:389";
			String returnedAtts[]={"givenName","sn","userPrincipalName", "samAccountName", "memberOf","info"};
			String searchBase = "dc=hq,dc=utec,dc=uy";
			String searchFilter = "(&(objectClass=user)"
					+ "(memberOf:1.2.840.113556.1.4.1941:=CN=GS_iAGRO-App,OU=Data Center,DC=hq,DC=utec,DC=uy)"
					+ "(sAMAccountName="+usrLDAP.split("\\\\")[1]+")"
					+ "(|(memberOf=CN=GS_Investigadores_iAGRO,OU=Data Center,DC=hq,DC=utec,DC=uy)"
					+ "(memberOf=CN=GS_Administradores_iAGRO,OU=Data Center,DC=hq,DC=utec,DC=uy)))";
			SearchControls searchCtls = new SearchControls();

			Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, context);
			ldapEnv.put(Context.PROVIDER_URL, provider);
			ldapEnv.put(Context.SECURITY_PRINCIPAL, usrLDAP);
			ldapEnv.put(Context.SECURITY_CREDENTIALS, pwd);

			ldapContext = new InitialDirContext(ldapEnv);     

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
			usuario.setContrasena(pwd.toCharArray());
			usuario.setUsuario(usrLDAP);

			if (!beanu.buscarUserName(usuario.getUsuario())) {
				beanu.crear(usuario);
			}
			else {
				usuario.setId(beanu.buscar(usrLDAP).getId());
				beanu.modificar(usuario);
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

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		LoginBean.usuario = usuario;
	}

}