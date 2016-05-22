package org.gary.comm.utils;
import java.util.LinkedHashMap;

public class KeyValue<V> extends LinkedHashMap<String, V> {
	private static final long serialVersionUID = 3667650976055949141L;

	@Override
	public V put(String key, V value) {
		return super.put(key == null ? null : key.toLowerCase(), value);
	}

	@Override
	public V get(Object key) {
		return super.get(key == null ? null : key.toString().toLowerCase());
	}

}
