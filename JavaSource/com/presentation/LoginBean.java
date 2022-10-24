package com.presentation;


import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpSession;

import com.dao.UsuarioDAO;
import com.entities.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.CiudadesBean;
import com.services.DepartamentosBean;
import com.services.UsuariosBean;
import com.services.dto.AdministradorDTO;
import com.services.dto.CiudadDTO;
import com.services.dto.DepartamentoDTO;
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
	@Inject
	private DepartamentosBean beand;
	@Inject
	private CiudadesBean beanc;
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
				System.out.println("Se creï¿½ el usuario");
				
				usuario.setId(1L);
				usuario.setNombre("Grupo 100");
				beanu.modificar(usuario);
			}
			
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String checkLogIn() throws ServiciosException, IOException {
		usuario = beanu.mapeo(daou.obtenerLogIn(usr, pwd.toCharArray()));
	
		HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( true );
		ses.setAttribute("id", usuario.getId());
		ses.setAttribute("username", usr);
		
		if (usuario != null) {
			return "views/menuPrincipal.xhtml?faces-redirect=true";
		}
		return "Login";
	}
	
	public String logout() {
	    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	    session.invalidate();
	    return "/Login.xhtml?faces-redirect=true";
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

	public void login() {
		try{
	      System.out.println("Début du test Active Directory");

	      Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
	      ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	      
	      ldapEnv.put(Context.PROVIDER_URL,  "ldap://dom.fr:389");
	      ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	      //ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=administrateur,cn=users,dc=societe,dc=fr");
	      ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=jean paul blanc,ou=MonOu,dc=dom,dc=fr");
	      ldapEnv.put(Context.SECURITY_CREDENTIALS, "pwd");
	      //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
	      //ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
	      ldapContext = new InitialDirContext(ldapEnv);

	      // Create the search controls         
	      SearchControls searchCtls = new SearchControls();

	      //Specify the attributes to return
	      String returnedAtts[]={"sn","givenName", "samAccountName"};
	      searchCtls.setReturningAttributes(returnedAtts);

	      //Specify the search scope
	      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

	      //specify the LDAP search filter
	      String searchFilter = "(&(objectClass=user))";

	      //Specify the Base for the search
	      String searchBase = "dc=dom,dc=fr";
	      //initialize counter to total the results
	      int totalResults = 0;

	      // Search for objects using the filter
	      NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

	      //Loop through the search results
	      while (answer.hasMoreElements())
	      {
	        SearchResult sr = (SearchResult)answer.next();

	        totalResults++;

	        System.out.println(">>>" + sr.getName());
	        Attributes attrs = sr.getAttributes();
	        System.out.println(">>>>>>" + attrs.get("samAccountName"));
	      }

	      System.out.println("Total results: " + totalResults);
	      ldapContext.close();
	    }catch (Exception e){
	      System.out.println(" Search error: " + e);
	      e.printStackTrace();
	      System.exit(-1);
	    }
	}
}