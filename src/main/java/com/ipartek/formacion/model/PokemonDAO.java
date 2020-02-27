package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.mysql.jdbc.Statement;

public class PokemonDAO implements IDAO<Pokemon> {

	private static PokemonDAO INSTANCE;

	private final static Logger LOG = LogManager.getLogger(PokemonDAO.class);

	//CORRECTAS
	private final String SLQ_GET_ALL = "SELECT p.id 'pokemon_Id', p.nombre 'pokemon_Nombre', p.imagen 'pokemon_Imagen', h.id 'habilidad_Id', h.nombre 'habilidad_Nombre' FROM ( pokemon p LEFT JOIN pokemon_habilidades phh ON p.id = phh.id_pokemon ) LEFT JOIN habilidad h ON h.id = phh.id_habilidad ORDER BY p.id ASC LIMIT 500;";
	private final String SQL_GET_BYID = "SELECT p.id 'pokemon_Id', p.nombre 'pokemon_Nombre', p.imagen 'pokemon_Imagen', h.id 'habilidad_Id', h.nombre 'habilidad_Nombre' FROM habilidad h, pokemon p, pokemon_habilidades phh WHERE phh.id_pokemon = p.id AND phh.id_habilidad = h.id AND p.id  = ? ORDER BY h.id ASC LIMIT 500;";
	
	//CORRECTAS
	private final String SQL_INSERT = "INSERT INTO pokemon (nombre, imagen) VALUES (?,?);";
	private final String SQL_UPDATE = "UPDATE pokemon SET nombre = ?, imagen = ? WHERE id = ?;";
	private final String SQL_DELETE = "DELETE FROM pokemon p WHERE p.id=?;";
	
	private final String SQL_INSERT_HABILIDAD = "INSERT INTO pokemon_habilidades (id_pokemon, id_habilidad) VALUES (?,?);";
	private final String SQL_DELETE_HABILIDAD = "DELETE from pokemon_habilidades where id_pokemon = ?;";
	
	//arreglar
	private final String SQL_GET_BYNAME = "SELECT p.id 'pokemonId', p.nombre 'pokemonNombre' , h.id 'habilidadId', h.nombre 'habilidadNombre' FROM habilidad h, pokemon p, pokemon_has_habilidad phh WHERE phh.pokemonId = p.id AND phh.habilidadId = h.id AND p.nombre LIKE ? ORDER BY p.id ASC LIMIT 500;";
	


	private PokemonDAO() {
		super();
	}

	public static synchronized PokemonDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PokemonDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Pokemon> getAll() {

		List<Pokemon> resul = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SLQ_GET_ALL);
				ResultSet rs = pst.executeQuery() ) {
			resul = mapper(rs);
		} catch (Exception e) {
			// TODO: LOG
			e.printStackTrace();
		}

		return resul;
	}

	@Override
	public Pokemon getById(int id) {
		Pokemon resul = null;
		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BYID);
				) {
			pst.setInt(1, id);
			try(ResultSet rs = pst.executeQuery()) {
				if(rs.next()) {
					resul = new Pokemon();
					resul.setId(rs.getInt("pokemon_Id"));
					resul.setNombre(rs.getString("pokemon_Nombre"));
					resul.setImagen(rs.getString("pokemon_Imagen"));

					List<Habilidad> habilidades = resul.getHabilidades();
					Habilidad habilidad;
					while (rs.next()){
						habilidad = new Habilidad();
						habilidad.setId(rs.getInt("habilidad_Id"));
						habilidad.setNombre(rs.getString("habilidad_Nombre"));
						habilidades.add(habilidad);
					}
				}
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		// TODO Auto-generated method stub
		return resul;
	}

	@Override
	public Pokemon delete(int id) throws Exception {
		Pokemon resul = getById(id);

		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE)) {
			pst.setInt(1, id);

			int affectedRows = pst.executeUpdate();

			if(affectedRows == 1) {
				LOG.info("Pokemon borrado correctamente de la BD");
			} else {
				LOG.info("Parece que se ha borrado " + affectedRows + " pokemons");
				resul = null;
			}

		} catch (Exception e) {
			LOG.error(e);
		}
		return resul;
	}

	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		LOG.debug("Entra en update");		
		
		 Pokemon resul = null;
		    Connection con = null;
		    try{
		        con = ConnectionManager.getConnection();
		        con.setAutoCommit(false);
		        PreparedStatement pstPokemon = con.prepareStatement(SQL_UPDATE);
		        
		        pstPokemon.setString(1, pojo.getNombre());
		        pstPokemon.setInt(3, pojo.getId());
		        pstPokemon.setString(2, pojo.getImagen());
		        
		        
				LOG.trace(pstPokemon);
		        
		        int affectedRows = pstPokemon.executeUpdate();
		        if(affectedRows == 1) {	
		        	
		        	//paso borrar habilidades del pokemon
		        	PreparedStatement pstHabilidad1 = con.prepareStatement(SQL_DELETE_HABILIDAD);
	        		pstHabilidad1.setInt(1, pojo.getId());
	        		LOG.trace(pstHabilidad1);
	        		pstHabilidad1.executeUpdate();
		            
	        		// paso introducir habilidades actuales
		        	ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
		        	for(Habilidad h : habilidades){
		        
		        		PreparedStatement pstHabilidad2 = con.prepareStatement(SQL_INSERT_HABILIDAD);
		        		pstHabilidad2.setInt(1, pojo.getId());
		        		pstHabilidad2.setInt(2, h.getId());
		        		LOG.trace(pstHabilidad2);
		        		pstHabilidad2.executeUpdate();
		        
		        	}
		            resul = pojo;
		            con.commit();
		        }
		    } catch (Exception e) {
		        con.rollback();
		        throw e;
		    } finally {
		        if(con != null) {
		            con.close();
		        }
		    }
		    return resul;

	}

	
	//create en forma de trassacion automica
	@Override
	public Pokemon create(Pokemon pojo) throws Exception {

	    LOG.debug("Entra en create");
	    
	    Pokemon resul = null;
	    Connection con = null;
	    try{
	        con = ConnectionManager.getConnection();
	        con.setAutoCommit(false);
	        PreparedStatement pstPokemon = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
	        pstPokemon.setString(1, pojo.getNombre());
	        pstPokemon.setString(2, pojo.getImagen());
	        int affectedRows = pstPokemon.executeUpdate();
	        if(affectedRows == 1) {
	            ResultSet rs = pstPokemon.getGeneratedKeys();

	            resul = pojo;
	            rs.next();

	            resul.setId(rs.getInt(1));
	            
	            ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
	            for(Habilidad h : habilidades){
	        
	                PreparedStatement pstHabilidad = con.prepareStatement(SQL_INSERT_HABILIDAD);
	                
	                
	                
	                pstHabilidad.setInt(1, resul.getId());
	                pstHabilidad.setInt(2, h.getId());
	                
	                LOG.trace(pstHabilidad);
	                
	                pstHabilidad.executeUpdate();
	                con.commit();
	        
	            }
	            
	            con.commit();
	        }
	    } catch (Exception e) {
	        con.rollback();
	        throw e;
	    } finally {
	        if(con != null) {
	            con.close();
	        }
	    }
	    return resul;
	}

	public List<Pokemon> getByName(String nombreP) {

		List<Pokemon> resul = null;
		try( Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BYNAME);) {

			pst.setString(1, "%" + nombreP + "%");

			try( ResultSet rs = pst.executeQuery();) {
				resul = mapper(rs);
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return resul;
	}

	private List<Pokemon> mapper( ResultSet rs) throws SQLException {

		Map<Integer, Pokemon> mapPokemons = new HashMap<Integer, Pokemon>();

		while( rs.next() ) {
			// TODO mapper
			int id = rs.getInt("pokemon_Id");
			String nombre = rs.getString("pokemon_Nombre");
			String imagen = rs.getString("pokemon_Imagen");
			LOG.trace(id + " " + nombre);

			Pokemon p;
			if(mapPokemons.containsKey(id)) {
				// Como el pokemon esta, lo obtenemos y le añadimos la habilidad
				p = mapPokemons.get(id);
			} else {
				// Si el pokemon no esta, lo crea
				p = new Pokemon();
				p.setId(id);
				p.setNombre(nombre);
				p.setImagen(imagen);

				mapPokemons.put(id, p);
			}

			Habilidad habilidad = new Habilidad();
			habilidad.setId(rs.getInt("habilidad_Id"));
			habilidad.setNombre(rs.getString("habilidad_Nombre"));

			if(habilidad.getNombre() != null) {
				p.getHabilidades().add(habilidad);
			}
		}

		return new ArrayList<Pokemon>(mapPokemons.values());

	}

}
