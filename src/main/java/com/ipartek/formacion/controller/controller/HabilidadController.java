package com.ipartek.formacion.controller.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.controller.utils.Utilidades;
import com.ipartek.formacion.model.HabilidadDAO;
import com.ipartek.formacion.model.pojo.Habilidad;

/**
 * Servlet implementation class PokemonController
 */
@WebServlet("/api/habilidad/*")
public class HabilidadController extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static HabilidadDAO dao;
	private final static Logger LOG = LogManager.getLogger(HabilidadController.class);

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = HabilidadDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		dao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		super.service(request, response);

	} //service

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		int id = -1;
		int status = 200;

		Object objetoRespuesta = null;
		try{
			id = Utilidades.obtenerId(request.getPathInfo());
			
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			List<Habilidad> habilidades;	
			habilidades = dao.getAll();
			
			if (habilidades.isEmpty()){
				status = HttpServletResponse.SC_NO_CONTENT;
				response.setStatus(status);
				


			}else {
				objetoRespuesta = habilidades;
				PrintWriter out = response.getWriter();
				Gson json = new Gson();
				out.print(json.toJson(objetoRespuesta));
				out.flush();
			}
		}	
	} //doGet
} //HabilidadController
