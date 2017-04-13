package main;

public abstract class MetaData {
	private String name;
	
	/**
	 * Elt�rolja az objektumb�l k�sz�lt p�ld�ny nev�t.
	 * @param name Az elt�rolt n�v
	 */
	public void setName(String name) {
		this.name = name; 
	};
	
	/**
	 * Visszaadja az objektum nev�t.
	 * @return Az objektum neve
	 */
	public String toString() {
		return name;
	}
}
