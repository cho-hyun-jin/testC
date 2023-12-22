package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet("/logout.me")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// a태그를 통한 페이지이동은 get으로 요청을 보내는것.
		// 로그아웃 요청처리 => session데이터 만료시키기
		
		// request.getSession().removeAttribute("loginUser");
		request.getSession().invalidate(); //세션을 무효화 시키는 키벨류형태의 데이터가 싹다날라간다.
		
		//응답페이지는 메인페이지로 페이지 재요청 보내기
		response.sendRedirect(request.getContextPath());
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
