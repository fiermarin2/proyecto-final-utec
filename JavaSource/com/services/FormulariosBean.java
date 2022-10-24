package com.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.FormularioDAO;
import com.dao.UsuarioDAO;
import com.entities.Casilla;
import com.entities.Formulario;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.dto.CasillaDTO;
import com.services.dto.DepartamentoDTO;
import com.services.dto.FormularioDTO;

/**
 * Session Bean implementation class FormulariosBean
 */
@Stateless
@LocalBean
public class FormulariosBean {

    @EJB
    private FormularioDAO daof;
    @EJB
    private CasillasBean beanCasillas;
    @EJB
    private UsuarioDAO daou;
    @EJB
    private UsuariosBean beanUsuarios;
	
    public FormulariosBean() {
       
    }
    
	public Formulario map(FormularioDTO dtof) throws ServiciosException {
		Formulario f = new Formulario();
		
		f.setNomFormulario(dtof.getNombre());
		f.setComentarios(dtof.getComentario());
		
		Usuario u = beanUsuarios.mapeo(dtof.getUsuario());
		u.setId(dtof.getUsuario().getId());
		f.setUsuario(u);
		
		Set<Casilla> casillas = new LinkedHashSet<>();
		for (CasillaDTO dtoc: dtof.getCasillas().values()) {
			Casilla c = beanCasillas.map(dtoc);
			c.setIdCasilla(dtoc.getId());
			
			casillas.add(c);
		}
		
		f.setCasillas(casillas);
		
		return f;
	}
    
	public FormularioDTO buscar(long id) throws ServiciosException {
		return mapInverso(daof.findById(id));
	}
	
	public Formulario mapMerge(FormularioDTO frm) throws ServiciosException {
		return map(frm).setFecHora(Date.valueOf(LocalDate.now()));
	}
    
	public FormularioDTO mapInverso(Formulario frm) throws ServiciosException {
		try {
			FormularioDTO dto = new FormularioDTO();
		
			dto.setId(frm.getIdFormulario());
			dto.setNombre(frm.getNomFormulario());
			dto.setFecha_hora(frm.getFecHora());
			dto.setComentario(frm.getComentarios());
			
			dto.setUsuario(beanUsuarios.mapeo(frm.getUsuario()));
			
			Map<String, CasillaDTO> casillas = new LinkedHashMap<>();
			for (Casilla c: frm.getCasillas()) {
				CasillaDTO dtoc = beanCasillas.mapInverso(c);
				casillas.put(dtoc.getNombre(),dtoc);
			}
			dto.setCasillas(casillas);
			
			return dto;
		} catch (Exception e) {
			throw new ServiciosException("Error al leer de la base de datos");
		}
	}
    
	public FormularioDTO crearFormulario(FormularioDTO frm) throws ServiciosException {
		try {
			Formulario formNuevo = this.map(frm);
			formNuevo = daof.crear(formNuevo);
			return frm;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error alta formulario");
		}
	}

	public FormularioDTO mergeFormulario(FormularioDTO frm) throws ServiciosException {
		try {
			Formulario f = this.mapMerge(frm);
			f.setIdFormulario(frm.getId());
			
			daof.merge(f);
			return frm;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("error modificar formulario");
		}
	}

	public void borrarFormulario(Long id) throws ServiciosException {
		try {
			daof.remove(id);
		} catch (Exception e) {
			throw new ServiciosException("error eliminar formulario");
		}
	}

	public List<FormularioDTO> obtenerTodos(){
		try {
			List<FormularioDTO> listaFrm = new ArrayList<>();
			for(Formulario f: daof.findAll())
				listaFrm.add(this.mapInverso(f));
			return listaFrm;
		} catch (ServiciosException e) {
			e.printStackTrace();
			return null;
		}
	}

	public FormularioDTO obtenerFormulario(String nombre){
		try {
			FormularioDTO frmEncuentra = new FormularioDTO();
			frmEncuentra = this.mapInverso(daof.find(nombre, 0));
			return frmEncuentra;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean findBooleanName(String nombre) {
		try {
			return daof.findBooleanName(nombre);		
		} catch (Exception e) {
			e.printStackTrace();	
			return false;
		}
	}
}