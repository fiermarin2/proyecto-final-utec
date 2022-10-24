package com.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.DepartamentoDAO;
import com.entities.Departamento;
import com.exceptions.ServiciosException;
import com.services.dto.DepartamentoDTO;
import com.services.dto.UsuarioDTO;

/**
 * Session Bean implementation class DepartamentosBean
 */
@Stateless
@LocalBean
public class DepartamentosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EJB
	DepartamentoDAO departamentoDAO;
	
	public DepartamentosBean() {
		// TODO Auto-generated constructor stub
	}
	
	public Departamento mapDepartamento(DepartamentoDTO deptoDTO) throws ServiciosException {
		try {
			Departamento d = new Departamento();
			d.setNomDepartamento(deptoDTO.getNombre());		
			return d;
		} catch (Exception e) {
			throw new ServiciosException("error mapeo");
		}
	}
	
	public Departamento mapMergeDepartamento(DepartamentoDTO deptoDTO) throws ServiciosException {
		try {
			Departamento d = new Departamento();
			d.setIdDepartamento(deptoDTO.getId());
			d.setNomDepartamento(deptoDTO.getNombre());
			return d;
		} catch (Exception e) {
			throw new ServiciosException("error mapeo modificar");
		}
	}
	
	public DepartamentoDTO buscar(long id) throws ServiciosException {
		return mapInversoDepartamento(departamentoDAO.findById(id));
	}
	
	public DepartamentoDTO mapInversoDepartamento(Departamento deptoDTO) throws ServiciosException {
    	try {
    		DepartamentoDTO depto = new DepartamentoDTO();
    		
    		depto.setId(deptoDTO.getIdDepartamento());
    		depto.setNombre(deptoDTO.getNomDepartamento());
    		
        	return depto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error mapeo");
		}
    }
	
	public DepartamentoDTO crearDepartamento(DepartamentoDTO deptoDTO) throws ServiciosException {
		Departamento depto = this.mapDepartamento(deptoDTO);
		depto = departamentoDAO.create(depto);
		deptoDTO.setId(depto.getIdDepartamento());
		return deptoDTO;
	}

	public DepartamentoDTO mergeDepartamento(DepartamentoDTO deptoDTO) throws ServiciosException {
		try {
			Departamento depto = this.mapMergeDepartamento(deptoDTO);
			depto = departamentoDAO.merge(depto);
			return deptoDTO;
		} catch (Exception e) {
			throw new ServiciosException("error modificar departamento");
		}
	}

	public void borrarDepartamento(Long id) throws ServiciosException {

		try {
			departamentoDAO.remove(id);
		} catch (Exception e) {
			throw new ServiciosException("error modificar departamento");
		}

	}

	public List<DepartamentoDTO> obtenerTodos() throws ServiciosException {
		try {
			List<DepartamentoDTO> dto = new ArrayList<>();
			for(Departamento d: departamentoDAO.findAll())
				dto.add(this.mapInversoDepartamento(d));
			
			return dto;
		} catch (Exception e) {
			throw new ServiciosException("Error al listar");
		}
	}

	public List<DepartamentoDTO> obtenerTodosFiltro(String filtro) throws ServiciosException {
		 try {
			List<DepartamentoDTO> dto = new ArrayList<>();
			for(Departamento d: departamentoDAO.FindAllFiltered(filtro))
				dto.add(this.mapInversoDepartamento(d));
				
			return dto;
		} catch (Exception e) {
			throw new ServiciosException("error al listar por nombre");
		}

	}
}

