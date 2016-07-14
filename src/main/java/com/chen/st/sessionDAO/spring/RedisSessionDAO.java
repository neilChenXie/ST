package com.chen.st.sessionDAO.spring;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

public class RedisSessionDAO extends AbstractSessionDAO {

	@Override
	public void delete(Session paramSession) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Session> getActiveSessions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Session paramSession) throws UnknownSessionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Serializable doCreate(Session paramSession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Session doReadSession(Serializable paramSerializable) {
		// TODO Auto-generated method stub
		return null;
	}

}
