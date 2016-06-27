package com.chen.st.sessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.junit.experimental.theories.Theories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisDAO.class);

	private String keyPrefix = "shiro_session:";

	private RedisUtils redisUtils;

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (validSession(session)) {
			this.saveSession(session);
		}
	}

	@Override
	public void delete(Session session) {
		if (validSession(session)) {
			redisUtils.del(getRedisKey(session.getId()));
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();

		Set<byte[]> keys = redisUtils.keys(this.keyPrefix + "*");
		if (keys != null && keys.size() > 0) {
			for (byte[] key : keys) {
				Session s = (Session) SerializeUtils.deserialize(redisUtils.get(key));
				sessions.add(s);
			}
		}

		return sessions;
	}

	/*
	 * 通过generateSessionId得到新的ID，并与session组合存入redis
	 * 
	 * @see
	 * org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doCreate(org.apache.
	 * shiro.session.Session)
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		}

		Session session = (Session) SerializeUtils.deserialize(redisUtils.get(getRedisKey(sessionId)));
		return session;
	}

	/**
	 * @describe 验证是否为有效session
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	private boolean validSession(Session session) {
		if (session == null || session.getId() == null) {
			logger.error("session or session id 为空");
			return false;
		}
		return true;
	}

	/**
	 * @describe 向redis中写入session
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	private void saveSession(Session session) {
		if (validSession(session)) {
			byte[] key = getRedisKey(session.getId());
			byte[] value = SerializeUtils.serialize(session);
			session.setTimeout(redisUtils.getExpire() * 1000);
			redisUtils.set(key, value, redisUtils.getExpire());
		}
	}

	/**
	 * @describe 组装存入redis的key
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	private byte[] getRedisKey(Serializable sessionId) {
		String preKey = this.keyPrefix + sessionId;
		return preKey.getBytes();
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public void setRedisUtils(RedisUtils redisUtils) {
		this.redisUtils = redisUtils;
	}
}
