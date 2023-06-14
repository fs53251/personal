package hr.fer.zemris.java.servleti.model;

import java.util.Objects;

/**
 * Model koji prestavlja podatke o nekom bendu.
 * Čuva id, ime, url pjesme i broj glasova o nekom bendu.
 * 
 * Razred nudi osnovne gettere i settere za sve atribute.
 * Nadjačani su toString, equals i compareTo kako bi mogao manupulirati
 * objektima tipa Band.
 * @author Filip
 *
 */
public class Band implements Comparable<Band> {
	/**
	 * Id nekog banda
	 */
	long id;

	/**
	 * Ime benda
	 */
	String name;

	/**
	 * Url na pjesmu koju ima neki band.
	 */
	String songURL;

	/**
	 * Broj glasova nekog benda
	 */
	long votes;

	/**
	 * Konstruktor koji postavlja sve parametre i zasebno inicijalizira glasove 
	 * benda na 0.
	 * @param id
	 * @param name
	 * @param songURL
	 */
	public Band(long id, String name, String songURL) {
		this.id = id;
		this.name = name;
		this.songURL = songURL;
		this.votes = 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSongURL() {
		return songURL;
	}

	public void setSongURL(String songURL) {
		this.songURL = songURL;
	}

	public void addVote() {
		this.votes++;
	}

	public long getVotes() {
		return votes;
	}

	public void setVotes(long votes) {
		this.votes = votes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Band other = (Band) obj;
		return id == other.id && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Band [id=" + id + ", name=" + name + ", songURL=" + songURL + "]";
	}

	@Override
	public int compareTo(Band o) {
		return Long.compare(this.id, o.getId());
	}

}
