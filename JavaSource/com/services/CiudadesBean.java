package com.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.CiudadDAO;
import com.dao.DepartamentoDAO;
import com.entities.Ciudad;
import com.exceptions.ServiciosException;
import com.services.dto.CiudadDTO;
import com.services.dto.DepartamentoDTO;

/**
 * Session Bean implementation class CiudadesBean
 */
@Stateless
@LocalBean
public class CiudadesBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CiudadDAO daoc;
	@EJB
	private DepartamentoDAO daod;

	public CiudadesBean() {

	}

	//Del DTO de Roles, traemos el paquete de datos, creando un objeto rol, y devolviendo.
	public Ciudad mapCiudad(CiudadDTO DTOciudad) throws ServiciosException {
		try {
			Ciudad ciudad = new Ciudad();
			ciudad.setNombreCiudad(DTOciudad.getNombre());

			ciudad.setDepartamento(daod.find(DTOciudad.getDepartamento().getNombre()));

			return ciudad;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error mapeo");
		}
	}

	//Del DTO de Funcionalidades, traemos el paquete de datos, creando un objeto funcionalidad, y devolviendo.
	public Ciudad mapCiudadMerge(CiudadDTO DTOciudad) throws ServiciosException {
		try {
			Ciudad ciudad = new Ciudad();
			ciudad.setIdCiudad(DTOciudad.getId());

			ciudad.setNombreCiudad(DTOciudad.getNombre());
			ciudad.setDepartamento(daod.find(DTOciudad.getDepartamento().getNombre()));

			return ciudad;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error mapeo");
		}
	}

	public CiudadDTO buscar(long id) throws ServiciosException {
		return mapInversoCiudad(daoc.findById(id));
	}

	public CiudadDTO mapInversoCiudad(Ciudad ciudad) throws ServiciosException {
		if(ciudad!=null) {
			try {
				CiudadDTO DTOciudad = new CiudadDTO();
				DTOciudad.setId(ciudad.getIdCiudad());
				DTOciudad.setNombre(ciudad.getNombreCiudad());
				//seteamos nombre de departamento para mostrar en la tabla.
				DepartamentoDTO depDTO = new DepartamentoDTO();
				depDTO.setId(ciudad.getDepartamento().getIdDepartamento());
				depDTO.setNombre(ciudad.getDepartamento().getNomDepartamento());
				DTOciudad.setDepartamento(depDTO);

				return DTOciudad;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiciosException("Error mapeo");
			}
		} else {
			return null;
		}
	}

	public CiudadDTO crearCiudad(CiudadDTO DTOciudad) throws ServiciosException {
		//try {
			Ciudad ciudad = this.mapCiudad(DTOciudad);
			ciudad = daoc.create(ciudad);
			DTOciudad.setId(ciudad.getIdCiudad());
			//ciudad.setIdCiudad(DTOciudad.getId());
			return DTOciudad;
/*		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error alta de ciudad");
		}*/

	}

	public CiudadDTO mergeCiudad(CiudadDTO DTOciudad) throws ServiciosException {
		try {
			Ciudad ciudad = this.mapCiudadMerge(DTOciudad);
			ciudad = daoc.merge(ciudad);
			return DTOciudad;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error modificar de ciudad");
		}

	}

	public void borrarCiudad(Long id) throws ServiciosException {

		try {
			daoc.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("error al eliminar ciudad");
		}

	}

	public Ciudad obtener(Long id) throws ServiciosException {
		try {
			return daoc.obtener(id); //
		} catch (Exception e) {

			throw new ServiciosException("Error al listar funcionalidades");
		}
	}

	public CiudadDTO obtenerDTO(Long id) throws ServiciosException {
		try {
			return mapInversoCiudad(daoc.obtener(id)); //
		} catch (Exception e) {

			throw new ServiciosException("Error al listar ciudades");
		}
	}

	public List<CiudadDTO> obtenerTodos() throws ServiciosException {
		try {

			List<CiudadDTO> devuelve = new ArrayList<>(); 

			for(Ciudad c: daoc.obtenerTodos()) 
				devuelve.add(mapInversoCiudad(c)); 

			return devuelve; //

		} catch (Exception e) {

			e.printStackTrace();
			throw new ServiciosException("Error al listar funcionalidades");
		}
	}

	public List<CiudadDTO> obtenerTodosFiltro(String filtro) throws ServiciosException {

		try {

			List<CiudadDTO> devuelve = new ArrayList<>(); 

			for(Ciudad c: daoc.obtenerTodosFiltro(filtro)) 
				devuelve.add(mapInversoCiudad(c)); 

			return devuelve; //

		} catch (Exception e) {

			e.printStackTrace();
			throw new ServiciosException("Error al listar funcionalidades");
		}
	}

	public List<CiudadDTO> obtenerTodosDepto(String depto) throws ServiciosException {
		try {
			List<CiudadDTO> devuelve = new ArrayList<>(); 
			for(Ciudad c: daoc.obtenerTodosDepto(depto)) 
				devuelve.add(mapInversoCiudad(c)); 
			return devuelve; 

		} catch (Exception e) {

			e.printStackTrace();
			throw new ServiciosException("Error al listar funcionalidades");
		}
	}
}
