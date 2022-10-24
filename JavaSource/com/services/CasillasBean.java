package com.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.CasillasDAO;
import com.dao.UsuarioDAO;
import com.entities.Casilla;
import com.exceptions.ServiciosException;
import com.services.dto.CasillaDTO;

/**
 * Session Bean implementation class CasillasBean
 */
@Stateless
@LocalBean
public class CasillasBean implements Serializable {
	
	@EJB
	private CasillasDAO casillasDAO;
	@EJB
    private UsuarioDAO daou;
    
    public CasillasBean() {
    }

	public Casilla map(CasillaDTO casilla) throws ServiciosException {
		try {
			Casilla c = new Casilla();
			c.setNomCasilla(casilla.getNombre()); 
			c.setDescripcion(casilla.getDescripcion());
			c.setTipoInput(casilla.getTipo());
			c.setUbicacion(casilla.getUbicacion());
			c.setUniMedida(casilla.getUnidad_de_medida());
			c.setObligatoria(casilla.getObligatoria() ? 1 : 0);
			
			c.setUsuario(daou.findByUserName(casilla.getUsuario(), 0));
			
			return c;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error mapeo casilladto");
		}
	}

	public CasillaDTO mapInverso(Casilla casilla) throws ServiciosException {
		try {
			CasillaDTO dto = new CasillaDTO();
			
			dto.setId(casilla.getIdCasilla());
			dto.setNombre(casilla.getNomCasilla());
			dto.setDescripcion(casilla.getDescripcion());
			dto.setTipo(casilla.getTipoInput());
			dto.setUbicacion(casilla.getUbicacion());
			dto.setUnidad_de_medida(casilla.getUniMedida());
			dto.setObligatoria(casilla.getObligatoria() == 1);
			//dto.setId_formulario(casilla.get);
			
			dto.setUsuario(casilla.getUsuario().getNombreUsuario());
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error mapeo casillaDto");
		}
	}

	public CasillaDTO crear(CasillaDTO casilla) throws ServiciosException {
		try {
			Casilla c = this.map(casilla);
			c = casillasDAO.create(c);
			return casilla;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error crea casilla");
		}
	}

	public void eliminar(Long id) throws ServiciosException {
		// TODO Auto-generated method stub
		try {
			casillasDAO.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error eliminar casilla");
		}
	}

	public List<CasillaDTO> obtenerTodos() throws ServiciosException {
		//try {
			List<CasillaDTO> dto = new ArrayList<>();
			for(Casilla c: casillasDAO.findAll())
				dto.add(this.mapInverso(c));
			
			return dto;
		//} catch (Exception e) {
		//	e.printStackTrace();
		//	throw new ServiciosException("Error mapeo casilladto");
		//}
	}
	
	//busca casilla por ID
	public CasillaDTO buscar(Long id) throws ServiciosException {
		try {
			CasillaDTO dto = new CasillaDTO();
			Casilla c = casillasDAO.findbyID(id);
			
			dto = this.mapInverso(c);
			
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error al obtener casillas");
		}
	}

	public CasillaDTO obtenerCasilla(String nombre) throws ServiciosException {
		try {
			CasillaDTO dto = new CasillaDTO();
			Casilla c = casillasDAO.find(nombre);
			
			dto = this.mapInverso(c);
			
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiciosException("Error al obtener casillas");
		}
	}

	public boolean findBooleanCasilla(String nombre) {
		try {
			return casillasDAO.findBooleanCasilla(nombre);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			return false;
		}
	}
}
