package com.chen.st.sessionListener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MyListener implements SessionListener {

	@Override
	public void onExpiration(Session session) {
		System.out.println("session expired:"+session.getId());

	}

	@Override
	public void onStart(Session session) {
		System.out.println("session created: "+session.getId());
	}

	@Override
	public void onStop(Session session) {
		System.out.println("session stopped: "+session.getId());
	}

}
