package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zonesion.hadoop.base.util.Rest;

public class RestServlet extends HttpServlet {

	private static final long serialVersionUID = -7751517020504894974L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = request.getParameter("key");
		try {
			String resultJSON = Rest.doRest("GET",url,null);
			PrintWriter out = response.getWriter();
			out.println(resultJSON);// 向客户端输出JSONObject字符串
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String url = "http://192.168.100.10:8096/client/api?command=listZones&showcapacities=true&response=json";
		try {
			String resultJSON = Rest.doRest("GET",url,null);
			System.out.println(resultJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
