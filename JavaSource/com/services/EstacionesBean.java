package com.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.EstacionesDAO;
import com.dao.UsuarioDAO;
import com.entities.Estacion;
import com.exceptions.ServiciosException;
import com.services.dto.EstacionDTO;


@Stateless
@LocalBean
public class EstacionesBean {

    @EJB
    private EstacionesDAO daoe;
    @EJB
    private UsuarioDAO daou;
    @EJB
    private DepartamentosBean beanDepto;
	
    public EstacionesBean() {
        
    }
    
    public Estacion mapEstacion(EstacionDTO estacionDTO) throws ServiciosException{
    	try {
			Estacion estacion = new Estacion();
			estacion.setCalAgua(estacionDTO.getCalidad_agua());
			estacion.setHumRelativa(estacionDTO.getHumedad_relativa());
			
			double d = estacionDTO.getLatitud();
			float f = (float) d;
			estacion.setLatitud(f);
			
			double dd = estacionDTO.getLongitud();
			float ff = (float) dd;
			estacion.setLongitud(ff);
			
			estacion.setNomEstacion(estacionDTO.getNombre());
			
			estacion.setDepartamento(beanDepto.mapMergeDepartamento(estacionDTO.getDepartamento()));

			estacion.setUsuario(daou.findByUserName(estacionDTO.getNombre_de_usuario(), 0));
			
			return estacion;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("error mapeo");
		}
    }
    
    public Estacion mapEstacionMerge(EstacionDTO estacionDTO) throws ServiciosException{
    	try {
			Estacion estacion = new Estacion();
			estacion.setIdEstacion(estacionDTO.getId());
			estacion.setCalAgua(estacionDTO.getCalidad_agua());
			estacion.setHumRelativa(estacionDTO.getHumedad_relativa());

			double d = estacionDTO.getLatitud();
			float f = (float) d;
			estacion.setLatitud(f);
			
			double dd = estacionDTO.getLongitud();
			float ff = (float) dd;
			estacion.setLongitud(ff);
			
			
			estacion.setNomEstacion(estacionDTO.getNombre());
			estacion.setDepartamento(beanDepto.mapMergeDepartamento(estacionDTO.getDepartamento()));
			
			estacion.setUsuario(daou.findByUserName(estacionDTO.getNombre_de_usuario(), 0));
			
			return estacion;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("error mapeo");
		}
    }
    
    public EstacionDTO mapInversoEstacion(Estacion estacion) throws ServiciosException{
    	try {
    		EstacionDTO estacionDTO = new EstacionDTO();
    		
    		estacionDTO.setId(estacion.getIdEstacion());
    		estacionDTO.setCalidad_agua(estacion.getCalAgua());
    		estacionDTO.setHumedad_relativa(estacion.getHumRelativa());
    		//estacionDTO.setLatitud((float) estacion.getLatitud());
    		estacionDTO.setLatitud((double) estacion.getLatitud());
    		//estacionDTO.setLongitud((float) estacion.getLongitud());
    		estacionDTO.setLongitud((double) estacion.getLongitud());
    		estacionDTO.setNombre(estacion.getNomEstacion());
    		
    		estacionDTO.setDepartamento(beanDepto.mapInversoDepartamento(estacion.getDepartamento()));
    		
    		estacionDTO.setNombre_de_usuario(estacion.getUsuario().getNombreUsuario());
    		
    		return estacionDTO;
    		
		} catch (Exception e) {
			throw new ServiciosException("error al mapear listado");
		}
    }
    
	public EstacionDTO crearEstacion(EstacionDTO estacionDTO) throws ServiciosException {
		try {
			Estacion estacion = this.mapEstacion(estacionDTO);
			estacion = daoe.create(estacion);
			return estacionDTO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error aalta estacion");
		}
	}

	public EstacionDTO mergeEstacion(EstacionDTO estacionDTO) throws ServiciosException {
		try {
			Estacion estacion = this.mapEstacionMerge(estacionDTO);
			estacion = daoe.merge(estacion);
			return estacionDTO;
		} catch (Exception e) {
			throw new ServiciosException("error modificacion estacion");
		}
	}

	public void borrarEstacion(Long id) throws ServiciosException {
		try {
			daoe.remove(id);
		} catch (Exception e) {
			throw new ServiciosException("error al eliminar estacion");
		}
	}

	public List<EstacionDTO> obtenerTodos() throws ServiciosException {
		try {
			List<EstacionDTO> devuelve = new ArrayList<>();
			if(daoe.findAll().size()>0) {
				for(Estacion e: daoe.findAll())
					devuelve.add(this.mapInversoEstacion(e));
			}
			return devuelve;
		} catch (Exception e) {
			throw new ServiciosException("error al listar");
		}
	}
	
	public List<EstacionDTO> obtenerTodosFiltro(String nombre) throws ServiciosException {
		try {
			List<EstacionDTO> devuelve = new ArrayList<>();
			
			for(Estacion e: daoe.findAllFiltered(nombre))
				devuelve.add(this.mapInversoEstacion(e));
				
				return devuelve;	
				
		} catch (Exception e) {
			throw new ServiciosException("Error al listar estaciones");
		}
	}
	
	public EstacionDTO obtenerPorID(long id) throws ServiciosException {
		try {
			return mapInversoEstacion(daoe.findByID(id));
				
		} catch (Exception e) {
			throw new ServiciosException("Error al listar estaciones");
		}
	}
	
	public EstacionDTO obtenerPorNombre(String nombre) throws ServiciosException {
		try {
			return mapInversoEstacion(daoe.findByName(nombre));
				
		} catch (Exception e) {
			throw new ServiciosException("Error al listar estaciones");
		}
	}
}