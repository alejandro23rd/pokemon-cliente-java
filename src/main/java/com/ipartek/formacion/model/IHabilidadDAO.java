package com.ipartek.formacion.model;

import java.util.List;

public interface IHabilidadDAO<I> {
	
		
	/**
	 * Obtiene todos los datos
	 * @return lista del pojo de habilidad
	 */
	List<I> getAll();

}
