package ui;

import java.util.Objects;

public class Pair<K, V> {
	K first;
	V second;

	public Pair(K first, V second) {
		super();
		this.first = first;
		this.second = second;
	}

	public K getFirst() {
		return first;
	}

	public void setFirst(K first) {
		this.first = first;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}
}
