package main;

public class CoalCart extends Cart {

	public CoalCart(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart){
		super(railwaySegment, previousRailwaySegment,nextCart, null, false);
	}
	@Override
	public boolean colorCheck(Station station) {
		boolean TODO = false;
		return TODO;
	}
	
}
