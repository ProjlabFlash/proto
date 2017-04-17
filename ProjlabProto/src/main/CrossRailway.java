package main;

import java.util.ArrayList;

public class CrossRailway extends Railway {
	private ArrayList<Railway> ThatNeighbour2 = new ArrayList<>();
	private ArrayList<Railway> ThisNeighbour2 = new ArrayList<>();
	public ArrayList<Railway> getThatNeighbour2(){
		return ThatNeighbour2;
	}
	public ArrayList<Railway> getThisNeighbour2(){
		return ThisNeighbour2;
	}
	public CrossRailway(Railway this1 ,Railway that1 ,Railway this2 ,Railway that2){
		super(this1);
		ThatNeighbour.add(that1);
		ThisNeighbour2.add(this2);
		ThatNeighbour2.add(that2);		
	}
	public CrossRailway(Railway this1) {
		super(this1);
	}
	@Override
	public Railway next(Railway previousRailway) 
	{
		if (previousRailway == null) 
		{			
			if (ThisNeighbour.size() != 0)
			{
				return ThisNeighbour.get(0);
			}
			if (ThatNeighbour.size() != 0)
			{
				return ThatNeighbour.get(0);
			}
			if (ThisNeighbour2.size() != 0)
			{
				return ThisNeighbour2.get(0);
			}
			if (ThatNeighbour2.size() != 0)
			{
				return ThatNeighbour2.get(0);
			}
			return null;
		}
		if(ThisNeighbour.contains(previousRailway)) 
		{
			if(ThatNeighbour.size() == 0){
				return null;
			}
			else
			{
				return ThatNeighbour.get(0);
			}
					
		}
		if(ThatNeighbour.contains(previousRailway))
		{
			if(ThisNeighbour.contains(previousRailway)) 
			{
				if(ThisNeighbour.size() == 0){
					return null;
				}
				else
				{
					return ThisNeighbour.get(0);
				}
			}
			
		}
		if(ThisNeighbour2.contains(previousRailway)) 
		{
			if(ThatNeighbour2.size() == 0){
				return null;
			}
			else
			{
				return ThatNeighbour2.get(0);
			}
					
		}
		if(ThatNeighbour2.contains(previousRailway))
		{
			if(ThisNeighbour2.contains(previousRailway)) 
			{
				if(ThisNeighbour2.size() == 0){
					return null;
				}
				else
				{
					return ThisNeighbour2.get(0);
				}
			}
			
		}
		return null;
	}
}
