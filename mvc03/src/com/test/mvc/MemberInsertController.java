/*
 * MemberInsertController.java
 * 사용자 정의 컨트롤러
 * 회원 데이터 추가 액션 처리 클래스
 * DAO 객체에 대한 의존성 주입 준비
 * → setter 메소드 추가
 */

package com.test.mvc;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

// 『implements Controller』 혹은 『extends AbstractController』
// → 서블릿에서 HttpServlet을 상속받은 서블릿 객체 역할
public class MemberInsertController implements Controller
{
	private IMemberDAO dao;

	public void setDao(IMemberDAO dao)
	{
		this.dao = dao;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ModelAndView mav = new ModelAndView();
		
		// 캐릭터 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		
		// 이전 페이지로부터 넘어온 데이터 수신
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		
		try
		{
			MemberDTO member = new MemberDTO();
			member.setName(name);
			member.setTelephone(telephone);
			
			dao.add(member);
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		// sendRedirect()
		// redirect: 로 리다이렉트함을 알려줄 수 있음
		mav.setViewName("redirect:memberlist.action");
		// 리다이렉트로 컨트롤러를 요청 → 컨트롤러가 필요한 count, list와 함께 뷰를 재요청
		
		// ※ mav.setViewName("/WEB-INF/view/MemberList.jsp"); 으로 해버리면 count, list가 출력되지 않음
		// 이는 뷰를 지정만 하고... 어떤 것도 뿌려주지 않음
		
		return mav;
	}

}
