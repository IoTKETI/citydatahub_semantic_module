package com.semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EntrySet<K1, K2, V> {

	private Map<K1, Map<K2, V>> mMap;
	
	public EntrySet() {
	    mMap = new HashMap<K1, Map<K2, V>>();
	}
	
	public V put(K1 key1, K2 key2, V value) {
	    
		Map<K2, V> map;
	    
		if (mMap.containsKey(key1)) {
			
	        map = mMap.get(key1);
	    } else {
	        
	    	map = new HashMap<K2, V>();
	        mMap.put(key1, map);
	    }

		//System.out.println("\nConsole LOG--> In EntrySet: count: \n" + count(key1));
		
	    return map.put(key2, value);
	}
	
	
	public Map<K2, V> update(K1 key1, K2 key2, V value) {
	
		Map<K2, V> map = null;
	    
		if (mMap.containsKey(key1)) {
			
	        map = mMap.get(key1);
	        
	        if( !map.containsKey(key2) ) {
	        	
	        	map.put(key2, value);
	        }    
	    }
		
		return mMap.put(key1, map); 
	}
	
	
	public Map<K2, V> get(K1 key1) {
	    
		if (mMap.containsKey(key1)) {
			
	        return mMap.get(key1);
	    } else {
	        return null;
	    }
	}
	
	
	public V get(K1 key1, K2 key2) {
	    if (mMap.containsKey(key1)) {
	        return mMap.get(key1).get(key2);
	    } else {
	        return null;
	    }
	}
	
	public boolean containsKeys(K1 key1, K2 key2) {
		
	    return mMap.containsKey(key1) && mMap.get(key1).containsKey(key2);
	}
	
	public boolean containsValue(K1 key1, String value) {
		
	    return mMap.containsKey(key1) && mMap.get(key1).containsValue(value);
	}
	
	public boolean containsKeys(K1 key1) {
		
	    return mMap.containsKey(key1);
	}
	
	
	public int numberOfEntries() {
		
		return mMap.size();
	}
	
	
	public int count(K1 key1) {
	
		int count = 0;
		
		if (mMap.containsKey(key1)) {
			
			if( !mMap.get(key1).isEmpty() ) {
				
				count = mMap.get(key1).size();
			}
	    }
		
		return count;
	}
	
	public Iterator getIterator() {
		
		return mMap.entrySet().iterator();
	}

	public void clear() {
	    mMap.clear();
	}
}
