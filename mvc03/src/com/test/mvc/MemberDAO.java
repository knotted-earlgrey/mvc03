package com.test.mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class MemberDAO implements IMemberDAO
{
	// ※ Connection 객체에 대한 의존성 주입을 위한 준비
	// ① 인터페이스 형태의 데이터타입을 취하는 속성 구성
	private DataSource dataSource;
	
	// ② setter 구성
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	
	// ※ IMemberDAO 인터페이스의 메소드 오버라이딩
	@Override
	public int add(MemberDTO member) throws SQLException
	{
		int result=0;
		
		Connection conn = dataSource.getConnection();
		// dataSource의 getConnection()에 대한 설정은 아래처럼 우리가 직접 설정해주는게 아니고
		// 서블릿에 등록해놓으면 서블릿 컨테이너가 알아서 연결해줌
		
		//Class.forName("oracle.jdbc.driver.OracleDriver");
		//Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@211.238.142.165:1521:xe", "scott", "tiger");
		
		String sql = "INSERT INTO TBL_MEMBERLIST(MID, NAME, TELEPHONE) VALUES(MEMBERLISTSEQ.NEXTVAL, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member.getName());
		pstmt.setString(2, member.getTelephone());
		
		result = pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		return result;
	}

	@Override
	public ArrayList<MemberDTO> lists() throws SQLException
	{
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
		
		Connection conn = dataSource.getConnection();
		
		String sql = "SELECT MID, NAME, TELEPHONE FROM TBL_MEMBERLIST ORDER BY MID";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
		{
			MemberDTO dto = new MemberDTO();
			
			dto.setMid(rs.getInt(1));
			dto.setName(rs.getString(2));
			dto.setTelephone(rs.getString(3));
			
			result.add(dto);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return result;
	}

	@Override
	public int count() throws SQLException
	{
		int result=0;
		
		Connection conn = dataSource.getConnection();
		
		String sql = "SELECT COUNT(*) AS COUNT FROM TBL_MEMBERLIST";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			result = rs.getInt(1);
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return result;
	}

}
