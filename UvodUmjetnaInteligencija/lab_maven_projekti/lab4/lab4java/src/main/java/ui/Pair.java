package ui;

public class Pair<K,V> {
	K first;
	V second;
	
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public void setFirst(K first) {
		this.first = first;
	}
	
	public K getFirst() {
		return this.first;
	}
	
	public void setSecond(V second) {
		this.second = second;
	}
	
	public V getSecond() {
		return this.second;
	}
}
