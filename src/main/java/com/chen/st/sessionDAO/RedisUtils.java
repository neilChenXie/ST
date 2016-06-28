package com.chen.st.sessionDAO;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @describe
 *
 * @author chen
 * @date Jun 27, 2016
 */
public class RedisUtils {

	// jedis pool properties, default value
	private JedisPoolConfig jedisPoolConfig = null;
	private String host = "127.0.0.1";
	private int port = 6379;
	private int timeout = 2000;
	private String password = null;

	// jedis properties
	private int expire = 0;

	private static JedisPool jedisPool = null;

	public RedisUtils() {
		// for Spring to create bean
	}

	/**
	 * @describe 初始化redisPool
	 *
	 * @author
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	private JedisPool getPool() {
		if (jedisPool == null) {
			if (jedisPoolConfig == null) {
				// 如果没有注入，生成默认配置
				setJedisPoolConfig(new JedisPoolConfig());
			}
			// jedisPool = new JedisPool(getJedisPoolConfig(), getHost(),
			// getPort(), getTimeout(), getPassword());
			jedisPool = new JedisPool(getJedisPoolConfig(), getHost(), getPort());
		}
		return jedisPool;
	}

	/**
	 * @describe 关闭一个jedis连接
	 *
	 * @author chen
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	private void returnJedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
		return value;
	}

	/**
	 * @describe 创建redis k-v,并设置全局的expire值
	 *
	 * @author chen
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	public void set(byte[] key, byte[] value) {
		set(key, value, getExpire());
	}

	/**
	 * @describe 创建redis k-v,并设置传入的expire值
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param key,
	 *            value, expire
	 * @return
	 */
	public void set(byte[] key, byte[] value, int expire) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
	}

	/**
	 * @describe 删除一个k-v
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param key
	 * @return
	 */
	public void del(byte[] key) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.del(key);
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
	}

	/**
	 * @describe 查看所有符合正则的Keys
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param pattern
	 * @return
	 */
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			keys = jedis.keys(pattern.getBytes());
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
		return keys;
	}

	/**
	 * @describe flush redis
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	public void flunshDB() {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.flushDB();
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
	}

	/**
	 * @describe 查看DB大小
	 *
	 * @author neil_xie
	 * @date Jun 27, 2016
	 * @param
	 * @return
	 */
	public long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			dbSize = jedis.dbSize();
		} catch (Exception e) {
			throw new JedisException("redis连接错误");
		} finally {
			returnJedis(jedis);
		}
		return dbSize;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public static JedisPool getJedisPool() {
		return jedisPool;
	}

	public static void setJedisPool(JedisPool jedisPool) {
		RedisUtils.jedisPool = jedisPool;
	}
}
