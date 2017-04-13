package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Locomotive extends MovingObject {
	
	private int Speed;
	
	/**
	 * Konstruktor. L�trehozza a mozdonyt.
	 * @param railwaySegment Jelenlegi s�n, ahol a mozdony �ll.
	 * @param previousRailwaySegment El�z� s�n, ahonnal a mozdony j�tt.
	 * @param nextCart Mozdony �ltal k�zvetlen�l h�zott kocsi.
	 * @param speed Mozdony sebess�ge
	 */
	public Locomotive(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, int speed) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.Speed = speed;
	}
	
	/**
	 * Megh�v�sakor a mozdony l�p egyet el�re.
	 */
	public void move() {
		
		Railway nextRailwaySegment = CurrentRailwaySegment.next(PreviousRailwaySegment);
		step(nextRailwaySegment);
		PreviousRailwaySegment = CurrentRailwaySegment;
		CurrentRailwaySegment = nextRailwaySegment;
		
	}
	
	/**
	 * �llom�shoz �rkez�skor h�v�dik. Jelzi a m�g� k�t�tt vagonnak, hogy �llom�shoz �rkezett a vonat.
	 * @param station Az az �llom�s, ahova meg�rkezett a mozdony.
	 */
	public void ArrivedAtStation(Station station) {
		
		if(Pulls.colorCheck(station) == true) {
			//Application.win();
		}
		
	}
}
