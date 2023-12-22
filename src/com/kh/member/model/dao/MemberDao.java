package com.kh.member.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.vo.Member;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	public MemberDao() {
		
		try {
			prop.loadFromXML(new FileInputStream(
					MemberDao.class.getResource("/sql/member/member-mapper.xml").getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Member loginMember(Connection conn, String userId, String userPwd) {
		//SELECT 문. ResultSet = > Member객체로 변환
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("loginMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member(
						rset.getInt("USER_NO"),
						rset.getString("USER_ID"),
						rset.getString("USER_PWD"),
						rset.getString("USER_NAME"),
						rset.getString("PHONE"),
						rset.getString("EMAIL"),
						rset.getString("ADDRESS"),
						rset.getString("INTEREST"),
						rset.getDate("ENROLL_DATE"),
						rset.getDate("MODIFY_DATE"),
						rset.getString("STATUS")
						);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		return m;
	}

	public int insertMember(Connection conn, Member m) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getPhone());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getAddress());
			pstmt.setString(7, m.getInterest());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		return result;
		
		
	}

	public int updateMember(Connection conn, Member m) {

		//update -> 처리된 행의 갯수를 반환
		int result =0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserName());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getEmail());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getInterest());
			pstmt.setString(6, m.getUserId());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public Member selectMember(Connection conn, String userId) {
		
		// select문 => 항상 ResultSet 으로 반환 => Member객체로 변환
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member(
						rset.getInt("USER_NO"),
						rset.getString("USER_ID"),
						rset.getString("USER_PWD"),
						rset.getString("USER_NAME"),
						rset.getString("PHONE"),
						rset.getString("EMAIL"),
						rset.getString("ADDRESS"),
						rset.getString("INTEREST"),
						rset.getDate("ENROLL_DATE"),
						rset.getDate("MODIFY_DATE"),
						rset.getString("STATUS")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return m;
	}

	public int deleteMember(Connection conn, String userId, String userPwd) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			result = pstmt.executeUpdate(); //실행
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		return result;
	}

	public int updatePwdMember(Connection conn, String userId, String userPwd, String updatePwd) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updatePwdMember"); //저장되어있는 쿼리문 꺼내준다?
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, updatePwd);
			pstmt.setString(2, userId);
			pstmt.setString(3, userPwd);
			
			result = pstmt.executeUpdate(); //실행  리절트에 담아준다.
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result; //객체 담아줄?
	}

	public int idCheck(Connection conn, String checkId) {
		int count = 0; //요안에다 값을 담아서 반환
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("idCheck"); //이게 쿼리문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, checkId);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				count = rset.getInt(1);
			}
					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return count;
		
		
	}

}
