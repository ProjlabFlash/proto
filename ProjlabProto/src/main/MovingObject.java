package main;

public abstract class MovingObject extends MetaData {
	
	protected Railway CurrentRailwaySegment;
	protected Railway PreviousRailwaySegment;
	protected Cart Pulls;
	
	/**
	 * Konstruktor. Be�ll�tja a kezdeti �rt�keket.
	 * @param railwaySegment Jelenlegi s�n, ahol �ll.
	 * @param previousRailwaySegment El�z� s�n, ahol �llt.
	 * @param nextCart �ltala h�zott kocsi.
	 */
	public MovingObject(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart) {
		railwaySegment.setOnMe(this);
		this.CurrentRailwaySegment = railwaySegment;
		this.PreviousRailwaySegment = previousRailwaySegment;
		this.Pulls = nextCart;
	}
	
	/**
	 * Egyet l�p a vonatelem. A met�dus �t�ll�tja a CurrentRailwaySegment
	 * �s a PreviousRailwaySegment �rt�k�t, illetve gondoskodik, hogy a Railway-ek tudjanak r�la,
	 * hogy ez az elem elhagyta, illetve r�l�pett az adott Railwayre.
	 * @param toHere Az a s�n, ahova l�pnie kell a vonatelemnek.
	 */
	public void step(Railway toHere) {
		
		
		CurrentRailwaySegment.setOnMe(null);
		toHere.setOnMe(this);
		if(Pulls != null) Pulls.step(CurrentRailwaySegment);
		
	}
	
	/**
	 * Jelzi az Application-nek, hogy a vonatelem �tk�z�tt egy m�sik vonatelemmel.
	 */
	public void crash() {
		
		//Application.lose()
	}
}
