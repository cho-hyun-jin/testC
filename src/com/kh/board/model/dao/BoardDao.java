package com.kh.board.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.board.model.vo.Reply;
import com.kh.common.model.vo.PageInfo;

import static com.kh.common.JDBCTemplate.*;

public class BoardDao {
	
	private Properties prop = new Properties();

	public BoardDao() {
		try {
			prop.loadFromXML(new FileInputStream(
					BoardDao.class.getResource("/sql/board/board-mapper.xml").getPath()
					));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int selectListCount(Connection conn) {
		
		//select문 => ResultSet => int
		int listCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectListCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				listCount = rset.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		
		
		return listCount;
	}

	public ArrayList<Board> selectList(Connection conn, PageInfo pi) {
		//SELECT문 = > ResultSet
		ArrayList<Board> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			/*
			 * boardLimit = 10
			 * curretPage = 1 => 1~10
			 * curretPage = 1 => 21~30
			 * 
			 * 시작값 =( curretPage -1)* boardLimit +1
			 * 끝값 = 시작값+boardLimit -1
			 * */
			
			int startRow = (pi.getCurrentPage()-1) * pi.getBoardLimit()+1;
			int endRow = startRow + pi.getBoardLimit()-1;
			
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Board b = new Board.Builder()
								.boardNo(rset.getInt("BOARD_NO"))
								.categoryName(rset.getString("CATEGORY_NAME"))
								.boardTitle(rset.getString("BOARD_TITLE"))
								.boardWriter(rset.getString("USER_ID"))
								.count(rset.getInt("COUNT"))
								.createDate(rset.getDate("CREATE_DATE"))
								.build();
				list.add(b);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int increaseCount(Connection conn, int boardNo) {
		//update문
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public Board selectBoard(Connection conn, int boardNo) {
		//Select => ResultSet => Board
		Board b = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				b = new Board.Builder()
						.categoryName(rset.getString(1))
						.boardTitle(rset.getString(2))
						.boardWriter(rset.getString(3))
						.boardContent(rset.getString(4))
						.createDate(rset.getDate(5))
						.boardNo(rset.getInt(6))
						.build(); //빌드함수로 보드타입객체호 반환
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		return b;
	}

	public Attachment selectAttachment(Connection conn, int boardNo) {
		//Select => ResultSet => Board
		Attachment at = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAttachment");
				
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo); //위치홀더를 치완??
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) { //다음행이 존재하는지 안하는지 먼저 확인해줌
				at = new Attachment();
				
				at.setFileNo(rset.getInt("FILE_NO"));
				at.setOriginName(rset.getString("ORIGIN_NAME"));
				at.setChangeName(rset.getString("CHANGE_NAME"));
				at.setFilePath(rset.getString("FILE_PATH"));
						
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return at;
	}

	public ArrayList<Category> selectCategoryList(Connection conn) {
		
		ArrayList<Category> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectCategoryList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Category c = new Category(
						rset.getInt(1),
						rset.getString(2)
						);
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int insertBoard(Connection conn, Board b) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getCategoryNo());
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setString(4, b.getBoardWriter());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int insertAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			
			result = pstmt.executeUpdate(); //실행 시킨다
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result; //실행한 결과값 반환시켜준다
	}

	public int updateBoard(Connection conn, Board b) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateBoard"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getCategoryNo());
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setInt(4, b.getBoardNo());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateAttachment"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName() );
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int insertNewAttachment(Connection conn, Attachment at) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertNewAttachment"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			//FILE_NO, REF_BNO, ORIGIN_NAME,CHANGE_NAME, FILE_PATH ???? 에 들어가는것
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteBoard(Connection conn, int boardNo) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteBoard"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		
		return result;
	}

	public void deleteAttachment(Connection conn, int boardNo) {
		//여긴 값을 반환해주지않기에 반환형이 필요없다. int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteAttachment"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
	}

	public ArrayList<Board> selectThumbnailList(Connection conn) {
		ArrayList<Board> list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectThumbnailList"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Attachment at = new Attachment();
				at.setFilePath(rset.getString("FILE_PATH"));
				at.setChangeName(rset.getString("CHANGE_NAME"));
				
				Board b = new Board.Builder()
						.boardNo(rset.getInt("BOARD_NO"))
						.boardTitle(rset.getString("BOARD_TITLE"))
						.count(rset.getInt("COUNT"))
						.attachment(at)
						.build();
				list.add(b);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	public int insertThumbnailBoard(Connection conn, Board b) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertThumbnailBoard"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setString(3, b.getBoardWriter()); 
			//위치홀더 치환 (변환)작업 끝
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
		
		
	}

	public int insertAttachmentList(Connection conn, ArrayList<Attachment> list) {
		int result = 1;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachmentList"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			for(Attachment at : list) {
				//반복문이 돌떄마다 미완성된 sql문을 담은 pstmt객체 생성
				pstmt = conn.prepareStatement(sql);

				// 완성 형태로 만들기
				pstmt.setString(1, at.getOriginName());
				pstmt.setString(2, at.getChangeName());
				pstmt.setString(3, at.getFilePath());
				pstmt.setInt(4, at.getFileLevel());
			
				//실행
				result *= pstmt.executeUpdate();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public ArrayList<Attachment> selectAttachmentList(Connection conn, int boardNo) {
		ArrayList<Attachment> list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAttachment"); //키값에 저장된 쿼리문 꺼내쓰는 구문
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				Attachment at = new Attachment();
				
				at.setChangeName(rset.getString("CHANGE_NAME"));
				at.setFilePath(rset.getString("FILE_PATH"));
				
				list.add(at);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
		
		
		
		
		
		
	}

	public int insertReply(Connection conn, Reply r) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertReply"); 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, r.getReplyContent());
			pstmt.setInt(2, r.getRefBoardNo());
			pstmt.setInt(3, r.getReplyWriter());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	public ArrayList<Reply> selectReplyList(Connection conn, int bno) {
		
		ArrayList<Reply> list = new ArrayList(); //반환시키고자하는 변수의값 가장먼저 빼줘야하고
		PreparedStatement pstmt = null; //두번째
		ResultSet rset = null; //셀렉트문 실행하고있으니깐 리절트셋 필요하겠고
		String sql = prop.getProperty("selectReplyList"); //쿼리문 가져오고
		
		try {
			pstmt = conn.prepareStatement(sql); //pstmt객체 생성
			pstmt.setInt(1, bno);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) { //여러개
				Reply r = new Reply(); 
				//순서에 맞춰서 넣어준다.
				r.setReplyNo(rset.getInt("REPLY_NO")); //첫번쨰로 조회한 데이터 숫자로 123으로 하면 순서대로 써야함
				r.setReplyContent(rset.getString("REPLY_CONTENT"));
				r.setUserId(rset.getString("USER_ID"));
				r.setCreateDate(rset.getDate("CREATE_DATE"));
				
				list.add(r);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
		
		
		
	}
}
