package com.ipartek.formacion.controller.utils;

public class Utilidades  {

	/**
	 * Obtenemos el id de la URI
	 * @param pathInfo parte de la URI donde debemos buscar un numero
	 * @return numero id
	 * @throws Exception si el path info esta mal formado
	 *
	 * <br>ejemplos:
	 * <ol>
	 * 	<li> / URL valida </li>
	 * 	<li> /2 URL valida </li>
	 * 	<li> /2/ URL valida </li>
	 * 	<li> /2/2 URL esta mal formado </li>
	 * 	<li> /2/otracosa/34/ URL esta mal formado </li>
	 * </ol>
	 */
	public static int obtenerId(String pathInfo) throws Exception {
		String[] datosURL = getPathSplitted(pathInfo);
		int numero = 0;
		switch (datosURL.length) {
			case 0:
				numero = -1;
				break;
			case 1:
				if(datosURL[0].matches("^\\d+$")) {
					numero = Integer.parseInt(datosURL[0]);
				} else {
					throw new Exception("Esta mal formado");
				}
				break;
			default:
				throw new Exception("Tiene demasiados parametros");
		}

		return numero;
	}

	public static String[] getPathSplitted(String pathInfo) {
		String[] resul = null;
		String[] emptyArray = new String[0];
		if (pathInfo == null) {
			resul = emptyArray;
		} else if (pathInfo.length() == 1) {
			resul = emptyArray;
		} else {
			pathInfo = pathInfo.substring(1);
			String[] splitted = pathInfo.split("/");
			resul = splitted;
		}
		return resul;
	}

	public static int contarPalabras(String frase) {
		int resul = 0;
		if(frase != null) {
			frase = frase.replaceAll("[\\W\\_]", " ");
			frase = frase.trim();
			if (frase.length() != 0) {
			String[] spliteada = frase.split("(\\s)+");
			resul = spliteada.length;
			}
		}
		return resul;
	}
}