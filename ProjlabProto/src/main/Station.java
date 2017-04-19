package main;

public class Station extends MetaData {
	
	protected Color color;
	protected Railway railway;
	
	/**
	 * Konstruktor. Beallitja az allomas szinet, es hozzarendeli
	 * egy sinhez, aminel a leszallas tortenik.
	 * @param railway A sin, amihez hozzarendeltuk
	 * @param color Az allomas szine
	 */
	public Station(Railway railway, Color color) {
		railway.setStation(this);
		this.color = color;
		this.railway = railway;
	}
	
	/**
	 * Visszaadja az allomas szinet.
	 * @return
	 */
	public Color getColor(Cart toMe) {		
		return this.color;
	}
}
