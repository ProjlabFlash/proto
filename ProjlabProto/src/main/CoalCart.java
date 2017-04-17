package main;

public class CoalCart extends Cart {

	/**
	 * Konstruktor. Rendre beallitja a railwaySegment, a previousRailwaySegment, 
	 * es a Pulls parameterek erteket.
	 * @param railwaySegment A jelenlegi sin, ahol a szeneskocsi tartozkodik
	 * @param previousRailwaySegment Az elozo sin, ahol a szeneskocsi volt
	 * @param nextCart A szeneskocsi altal huzott masik kocsi
	 */
	public CoalCart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart){
		super(railwaySegment, previousRailwaySegment,nextCart, null, false);
	}
	
	/* (non-Javadoc)
	 * @see main.Cart#colorCheck(main.Station)
	 */
	@Override
	public boolean colorCheck(Station station) {
		if (Pulls != null)
		{
			Pulls.colorCheck(station);
		}
		return false;
	}
	
}
