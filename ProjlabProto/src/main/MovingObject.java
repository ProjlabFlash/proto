package main;

public abstract class MovingObject extends MetaData {
	
	protected Railway CurrentRailwaySegment;
	protected Railway PreviousRailwaySegment;
	protected final Cart Pulls;
	
	/**
	 * Konstruktor. Beallitja a kezdeti ertekeket.
	 * @param railwaySegment Jelenlegi sin, ahol all.
	 * @param previousRailwaySegment Elozo sin, ahol allt.
	 * @param nextCart altala huzott kocsi.
	 */
	public MovingObject(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart) {
		railwaySegment.setOnMe(this);
		this.CurrentRailwaySegment = railwaySegment;
		this.PreviousRailwaySegment = previousRailwaySegment;
		this.Pulls = nextCart;
	}
	
	/**
	 * Egyet lep a vonatelem. A metodus atallitja a CurrentRailwaySegment
	 * es a PreviousRailwaySegment erteket, illetve gondoskodik, hogy a Railway-ek tudjanak rola,
	 * hogy ez az elem elhagyta, illetve ralepett az adott Railwayre.
	 * @param toHere Az a sin, ahova lepnie kell a vonatelemnek.
	 */
	public void step(Railway toHere) {
		
		if (toHere == null) {
			Controller.lose();
			return;
		}
		
		CurrentRailwaySegment.setOnMe(null);
		toHere.setOnMe(this);
		if(Pulls != null) Pulls.step(CurrentRailwaySegment);
		PreviousRailwaySegment = CurrentRailwaySegment;
		CurrentRailwaySegment = toHere;
		
	}
	
	/**
	 * Jelzi az Application-nek, hogy a vonatelem utkozott egy masik vonatelemmel.
	 */
	public void crash() {
		Controller.sendMessage("Miert nincs 2 sinpar egy vonalon...? utkozes tortent.");
		Controller.lose();
	}
	public abstract void ArrivedAtStation(Station station);
}
