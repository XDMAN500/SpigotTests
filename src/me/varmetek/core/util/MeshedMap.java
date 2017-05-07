package me.varmetek.core.util;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by XDMAN500 on 2/11/2017.
 */
public class MeshedMap<K,V1,V2>
{
	private HashMap<K,V1> map1 = Maps.newHashMap();
	private HashMap<K,V2> map2 = Maps.newHashMap();


	public boolean isEmpty(){
			return map1.isEmpty() || map2.isEmpty();
	}

	public boolean containsKey(K val){
		return map1.containsKey(val) || map2.containsKey(val);
	}

	public boolean containsValue1(V1 val){
		return map1.containsValue(val) ;
	}

	public boolean containsValue2(V2 val){
		return map2.containsValue(val) ;
	}

	public void put(K key, V1 val, V2 val2){
		map1.put(key,val);
		map2.put(key,val2);
	}

	public void remove(K key){
		map1.remove(key);
		map2.remove(key);
	}

	public void clear(){
		map1.clear();
		map2.clear();

	}
	public V1 getV1(K key){
		return map1.get(key);
	}

	public V2 getV2(K key){
		return map2.get(key);
	}
	public Collection<K> keys(){
		return map1.keySet();
	}

	public Collection<V1> values1(){
		return map1.values();
	}

	public Collection<V2> values2(){
		return map2.values();
	}
	public int size(){
		return keys().size();
	}
	public void putAll(MeshedMap<K,V1,V2> ez){
		for(K key: ez.keys()){
			put(key,ez.getV1(key), ez.getV2(key));
		}
	}
}
