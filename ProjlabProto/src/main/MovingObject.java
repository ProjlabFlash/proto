package main;

import java.util.ArrayList;

public abstract class MovingObject extends MetaData {
	
	protected Railway CurrentRailwaySegment;
	protected Railway PreviousRailwaySegment;
	protected Cart Pulls;
	
	/**
	 * Konstruktor. Beállítja a kezdeti értékeket.
	 * @param railwaySegment Jelenlegi sín, ahol áll.
	 * @param previousRailwaySegment Elõzõ sín, ahol állt.
	 * @param nextCart Általa húzott kocsi.
	 */
	public MovingObject(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart) {
		railwaySegment.setOnMe(this);
		this.CurrentRailwaySegment = railwaySegment;
		this.PreviousRailwaySegment = previousRailwaySegment;
		this.Pulls = nextCart;
	}
	
	/**
	 * Egyet lép a vonatelem. A metódus átállítja a CurrentRailwaySegment
	 * és a PreviousRailwaySegment értékét, illetve gondoskodik, hogy a Railway-ek tudjanak róla,
	 * hogy ez az elem elhagyta, illetve rálépett az adott Railwayre.
	 * @param toHere Az a sín, ahova lépnie kell a vonatelemnek.
	 */
	public void step(Railway toHere) {
		
		
		CurrentRailwaySegment.setOnMe(null);
		toHere.setOnMe(this);
		if(Pulls != null) Pulls.step(CurrentRailwaySegment);
		
	}
	
	/**
	 * Jelzi az Application-nek, hogy a vonatelem ütközött egy másik vonatelemmel.
	 */
	public void crash() {
		
		//Application.lose()
	}
}
