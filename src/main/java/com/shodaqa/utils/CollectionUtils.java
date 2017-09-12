package com.shodaqa.utils;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {
	
	public static <K, V> Map<K, V> newHashMap(Map.Entry<K, V>... entries) {
		Map<K, V> map = new HashMap<K, V>();
		for (Map.Entry<K, V> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	
	public static <K, V> Map.Entry<K, V> mapEntry(final K key, final V value) {
		return new MapEntry<K, V>(key, value);
	}
	
	
	public static class MapEntry<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;
		
		public MapEntry(final K key, final V value) {
			this.key = key;
			this.value = value;
		}
		
		public K getKey() {
			return key;
		}
		
		public V getValue() {
			return value;
		}
		
		public V setValue(final V value) {
			this.value = value;
			return value;
		}
	}
}
