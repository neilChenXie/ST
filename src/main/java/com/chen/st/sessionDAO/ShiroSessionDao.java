package com.chen.st.sessionDAO;
import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.st.utils.JedisUtil;

public class ShiroSessionDao extends CachingSessionDAO {

	private static final Logger logger = LoggerFactory.getLogger(ShiroSessionDao.class);
	
    // 保存到Redis中key的前缀 prefix+sessionId  
    private String prefix = "";  
  
    // 设置会话的过期时间  
    private int seconds = 0;  
  
    // 特殊配置 只用于没有Redis时 将Session放到EhCache中  
    private Boolean onlyEhCache;  
  
    @Autowired  
    private JedisUtil jedisUtils;  
    
	@Override
	protected void doDelete(Session session) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUpdate(Session session) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Serializable doCreate(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Session doReadSession(Serializable serializable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		Session cached = null;  
        try {  
            cached = super.getCachedSession(sessionId);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        if (onlyEhCache) {  
            return cached;  
        }  
        // 如果缓存不存在或者缓存中没有登陆认证后记录的信息就重新从Redis中读取  
        if (cached == null || cached.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {  
            try {  
                cached = this.doReadSession(sessionId);  
                if (cached == null) {  
                    throw new UnknownSessionException();  
                } else {  
                    // 重置Redis中缓存过期时间并缓存起来 只有设置change才能更改最后一次访问时间  
                    ((ShiroSession) cached).setChanged(true);  
                    super.update(cached);  
                }  
            } catch (Exception e) {  
                logger.warn("There is no session with id [" + sessionId + "]");  
            }  
        }  
        return cached;		
	}

}
