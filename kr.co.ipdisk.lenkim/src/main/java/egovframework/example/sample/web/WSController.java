package egovframework.example.sample.web;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;
import javax.websocket.OnError;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.util.Collections;

import org.springframework.stereotype.Controller;



@ServerEndpoint(value="/ws")
@Controller
@Singleton
public class WSController {
	private static String rtnMsg = "";
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	
	@OnOpen
	public void onOpen(Session session){
		System.out.println("세션ID : " + session.getId());
		
		try {
			final Basic basic = session.getBasicRemote();
			basic.sendText("연결됬다.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		sessions.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session){
		System.out.println("세션ID : " + session.getId() + ", 내용 : " + message);
		rtnMsg = rtnMsg + message + "1";
		
		sendAll(session, rtnMsg);
		try {
			final Basic basic = session.getBasicRemote();
			basic.sendText("응답 : " + rtnMsg);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		sessions.add(session);
	}
	
	@OnError
	public void onError(Throwable t){
		t.printStackTrace();
	}
	
	public void sendAll(Session ss, String message){
		
		try {
			for(Session session : WSController.sessions){
				if(!ss.getId().equals(session.getId())){
					session.getBasicRemote().sendText("전체응답 : " + message);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	

}
