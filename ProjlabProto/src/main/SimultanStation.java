package main;

public class SimultanStation extends Station{
	public SimultanStation(Railway railway, Color color){
		super(railway,color);
	}
	@Override
	public Color getColor(Cart toMe){ 
		
		if(toMe.getColor().equals(this.color))
		{
			if(!toMe.getPassengers())
			{
				toMe.setPassengers();
				return Color.FELSZALLTAK;
			}
			else
			{
				return Color.HORROR;
			}
		}
		return Color.HORROR;			
	}
}
