package com.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.CasillasDAO;
import com.dao.RegistroDAO;
import com.entities.Casilla;
import com.entities.Registro;
import com.exceptions.ServiciosException;
import com.services.dto.AficionadoDTO;
import com.services.dto.RegistroDTO;
import com.services.dto.UsuarioDTO;

/**
 * Session Bean implementation class FormulariosBean
 */
@Stateless
public class RegistrosBean {

	@EJB
	private FormulariosBean beanFormularios;
	@EJB
	private CasillasBean beanCasillas;
	@EJB
	private UsuariosBean beanUsuarios;
	@EJB
	private EstacionesBean beanEstaciones;
	@EJB
	private RegistroDAO daor;
	@EJB
	private CasillasDAO daoc;

	public RegistrosBean() {

	}
	
	public Registro mapeo(RegistroDTO dtod) throws ServiciosException {
		Registro dato = new Registro();

		Map<Casilla,String> valores = new HashMap<>();
		
		for(String s: dtod.getValor().keySet()) {
			valores.put(daoc.findEvenBorrado(s), dtod.getValor().get(s));
		}
		
		dato.setValor(valores);
		dato.setEstacion(beanEstaciones.mapEstacion(dtod.getEstacion()).setIdEstacion(dtod.getEstacion().getId()));
		dato.setFormulario(beanFormularios.map(dtod.getFormulario()).setIdFormulario(dtod.getFormulario().getId()));
		dato.setUsuario(beanUsuarios.mapeo(dtod.getUsuario()).setId(dtod.getUsuario().getId()));
		
		return dato;
	}
	
	public RegistroDTO mapeo(Registro dato) throws ServiciosException {
		RegistroDTO dtod = new RegistroDTO();
		
		Map<String,String> valores = new HashMap<>();
		
		for(Casilla s: dato.getValor().keySet())	{
			valores.put(s.getNomCasilla(), dato.getValor().get(s));
		}
		
		dtod.setValor(valores);
		dtod.setEstacion(beanEstaciones.mapInversoEstacion(dato.getEstacion()));
		dtod.setFecha_Hora(dato.getFechaHora());
		dtod.setFormulario(beanFormularios.mapInverso(dato.getFormulario()));
		dtod.setUsuario(beanUsuarios.mapeo(dato.getUsuario()));
		dtod.setId(dato.getId());
		
		return dtod;
	}

	public RegistroDTO crear(RegistroDTO ddto) throws ServiciosException {
		try {
			Registro dato = mapeo(ddto);
			dato = daor.crear(dato);
			ddto.setId(dato.getId());
			
			return ddto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error alta registro");
		}
	}

	public RegistroDTO merge(RegistroDTO dto) throws ServiciosException {
		try {
			Registro d = mapeo(dto);
			d.setId(dto.getId());
			daor.merge(d);		

			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error modificar registro");
		}
	}

	public void borrarMedicion(Long id) throws ServiciosException {
		try {
			daor.remove(id);
		} catch (Exception e) {
			throw new ServiciosException("error eliminar medicion");
		}
	}
	
	public List<RegistroDTO> obtenerTodos(UsuarioDTO udto) {
		try {
			List<Registro> lista = (udto instanceof AficionadoDTO) ? 
					daor.findByUserId(udto.getId()) 
					: daor.findAll();
			
			List<RegistroDTO> listaDto = new ArrayList<>();
			for (Registro d : lista)
				listaDto.add(this.mapeo(d));

			return listaDto;
		} catch (ServiciosException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<RegistroDTO> obtenerPorFormulario(Long idFormulario) {
		try {
			List<RegistroDTO> lista = new ArrayList<>();

			for (Registro d : daor.findByFromulario(idFormulario))
				lista.add(this.mapeo(d));

			return lista;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<RegistroDTO> obtenerPorFechas(Date min, Date max) {
		try {
			List<RegistroDTO> lista = new ArrayList<>();
			for (Registro d : daor.findByFechas(min, max))
				lista.add(this.mapeo(d));

			return lista;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean obtenerExistenMediciones(long id) {
		try {
			return daor.findByEstaciones(id);
		} catch (ServiciosException e) {
			e.printStackTrace();
			return false;
		}
	}
}