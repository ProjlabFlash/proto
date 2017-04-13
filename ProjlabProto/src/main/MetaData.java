package main;

public abstract class MetaData {
	private String name;
	
	/**
	 * Eltárolja az objektumból készült példány nevét.
	 * @param name Az eltárolt név
	 */
	public void setName(String name) {
		this.name = name; 
	};
	
	/**
	 * Visszaadja az objektum nevét.
	 * @return Az objektum neve
	 */
	public String toString() {
		return name;
	}
}
