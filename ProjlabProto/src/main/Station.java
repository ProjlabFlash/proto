package main;

public class Station extends MetaData {
	
	private Color color;
	
	/**
	 * Konstruktor. Be�ll�tja az �llom�s sz�n�t, �s hozz�rendeli
	 * egy s�nhez, amin�l a lesz�ll�s t�rt�nik.
	 * @param railway A s�n, amihez hozz�rendelt�k
	 * @param color Az �llom�s sz�ne
	 */
	public Station(Railway railway, Color color) {
		railway.setStation(this);
		this.color = color;
	}
	
	/**
	 * Visszaadja az �llom�s sz�n�t.
	 * @return
	 */
	public Color getColor() {
		
		return this.color;
	}
}
