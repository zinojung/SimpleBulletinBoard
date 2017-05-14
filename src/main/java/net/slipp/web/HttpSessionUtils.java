package net.slipp.web;

import javax.servlet.http.HttpSession;

import net.slipp.domain.User;

public class HttpSessionUtils {
	public static final String USER_SESSION_KEY = "sessionedUser";
	
	public static boolean isLoginUser(HttpSession session){
		Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionedUser == null) {
			return false;
		}
		return true;
	}
	
	public static User getUserFromSession(HttpSession session) {
		if(!isLoginUser(session)){
			return null;
		}
		return (User)session.getAttribute(USER_SESSION_KEY);
	}

	//세션유저와 작성자가 같은지 검사.
	public static boolean isMatchWriterAndSessionUser(HttpSession session, User writer){
		User sessionedUser = getUserFromSession(session);
		if(sessionedUser.matchId(writer.getId())) {
			return true;
		}
		return false;
	}
}
