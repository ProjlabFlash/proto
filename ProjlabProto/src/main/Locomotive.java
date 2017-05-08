package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Locomotive extends MovingObject {
	
	protected final int Speed;
	
	/**
	 * Konstruktor. Letrehozza a mozdonyt.
	 * @param railwaySegment Jelenlegi sin, ahol a mozdony all.
	 * @param previousRailwaySegment Elozo sin, ahonnal a mozdony jott.
	 * @param nextCart Mozdony altal kozvetlenul huzott kocsi.
	 * @param speed Mozdony sebessege
	 */
	public Locomotive(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, int speed) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.Speed = speed;
	}
	
	/**
	 * Meghivasakor a mozdony lep egyet elore.
	 */
	public void move() {
		
		Railway nextRailwaySegment = CurrentRailwaySegment.next(PreviousRailwaySegment);
		step(nextRailwaySegment);
		
	}
	
	/**
	 * Allomashoz erkezeskor hivodik. Jelzi az utana kotott vagonnak, hogy allomashoz erkezett a vonat.
	 * @param station Az az allomas, ahova megerkezett a mozdony.
	 */
	public void ArrivedAtStation(Station station) {
		Controller.sendMessage("Elertuk a san allomast.", station);
		if(Pulls.colorCheck(station) == true) {
			//Application.win();
		}
		
	}
	
}
