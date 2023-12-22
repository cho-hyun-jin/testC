package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Reply;
import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class replyInsertController
 */
@WebServlet("/insert.re")
public class AjaxReplyInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxReplyInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String replyContent = request.getParameter("content");
		int refBno = Integer.parseInt(request.getParameter("bno")); //리퀘스트는 항상 반환하는값이 문자열이기때문에 파싱을해줘여함
		int userNo = ((Member) request.getSession().getAttribute("loginUser")).getUserNo(); //getUserNo값까지 같이 가져오기
		
		Reply r = new Reply();
		r.setReplyContent(replyContent);
		r.setRefBoardNo(refBno);
		r.setReplyWriter(userNo);
		
	
		int result =  new BoardService().insertReply(r);
		
		response.getWriter().print(result);;
		
		
		
		
	
	}

}
