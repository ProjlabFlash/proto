package main;

public abstract class MetaData {
	private String name;
	
	/**
	 * Eltarolja az objektumbol keszult peldany nevet.
	 * @param name Az eltarolt nev
	 */
	public void setName(String name) {
		this.name = name; 
	};
	
	/**
	 * Visszaadja az objektum nevet.
	 * @return Az objektum neve
	 */
	public String toString() {
		return name;
	}
}
