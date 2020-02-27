package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;

public class HabilidadDAO implements IHabilidadDAO<Habilidad> {

	private static HabilidadDAO INSTANCE;

	private final static Logger LOG = LogManager.getLogger(HabilidadDAO.class);

	//CORRECTAS
	private final String SLQ_GET_ALL_HABILIDAD = "SELECT id, nombre FROM habilidad ORDER BY id ASC LIMIT 500;";

	private HabilidadDAO() {
		super();
	}

	public static synchronized HabilidadDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HabilidadDAO();
		}
		return INSTANCE;
	}

	//GET ALL
	@Override
	public List<Habilidad> getAll() {

	    LOG.debug("Entra en getAll");

	    ArrayList<Habilidad> resultado = new ArrayList<Habilidad>();

	    try (Connection con = ConnectionManager.getConnection();
	            PreparedStatement pst = con.prepareStatement(SLQ_GET_ALL_HABILIDAD);
	            ResultSet rs = pst.executeQuery()) {

	        LOG.trace(pst);

	        while (rs.next()) {
	                
	                resultado.add(mapperHabilidad(rs));

	        }

	    } catch (Exception e) {
	        LOG.error(e);
	    }

	    return resultado;
	}

	//MAPPER
	private Habilidad mapperHabilidad(ResultSet rs) throws SQLException {

	    Habilidad h = new Habilidad();
	    h.setId(rs.getInt("id"));
	    h.setNombre(rs.getString("nombre"));

	    return h;
	}

}
