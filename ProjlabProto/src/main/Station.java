package main;

public class Station extends MetaData {
	
	private Color color;
	
	/**
	 * Konstruktor. Beallitja az allomas szinet, es hozzarendeli
	 * egy sinhez, aminel a leszallas tortenik.
	 * @param railway A sin, amihez hozzarendeltuk
	 * @param color Az allomas szine
	 */
	public Station(Railway railway, Color color) {
		railway.setStation(this);
		this.color = color;
	}
	
	/**
	 * Visszaadja az allomas szinet.
	 * @return
	 */
	public Color getColor() {
		
		return this.color;
	}
}
