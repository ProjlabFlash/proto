package main;

import java.util.ArrayList;
import java.util.List;

public class CrossRailway extends Railway {
	private ArrayList<Railway> ThatNeighbour2 = new ArrayList<>();
	private ArrayList<Railway> ThisNeighbour2 = new ArrayList<>();
	
	
	/**
	 * Getter a ThatNeighbour2 attributumra
	 * @return A masodik sinpar 2. szomszedja
	 */
	public ArrayList<Railway> getThatNeighbour2(){
		return ThatNeighbour2;
	}
	
	/**
	 * Getter a ThisNeighbour2 attributumra
	 * @return A masodik sinpar 1. szomszedja
	 */
	public ArrayList<Railway> getThisNeighbour2(){
		return ThisNeighbour2;
	}
	
	/**
	 * Konstruktor. Rendre beallitja az elso sinpar 2 szomszedjat (elso ket parameter),
	 * majd a masodik sinpar szomszedjait (3. es 4. parameter)
	 * @param this1 Elso sinpar 1. szomszedja
	 * @param that1 Elso sinpar 2. szomszedja
	 * @param this2 Masodik sinpar 1. szomszedja
	 * @param that2 Masodik sinpar 2. szomszedja
	 */
	public CrossRailway(Railway this1 ,Railway that1 ,Railway this2 ,Railway that2){
		super(this1);
		ThatNeighbour.add(that1);
		ThisNeighbour2.add(this2);
		ThatNeighbour2.add(that2);		
	}
	
	/**
	 * Konstruktor.
	 * @param this1
	 */
	public CrossRailway(Railway this1) {
		super(this1);
	}
	
	/* (non-Javadoc)
	 * @see main.Railway#next(main.Railway)
	 */
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
				if(ThisNeighbour.size() == 0){
					return null;
				}
				else
				{
					return ThisNeighbour.get(0);
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
				if(ThisNeighbour2.size() == 0){
					return null;
				}
				else
				{
					return ThisNeighbour2.get(0);
				}
		}
		return null;
	}
	
	/**Ellenorzi hogy ez a sin hozzakotheto-e a parametrben kapott sinhez
	 * 
	 * @param newNeighbour: Az uj sin amit hozza akarunk kotni
	 * @param where: A ket keresztezo sin kozul melyikre
	 * @return Hozzakotheto-e(true), vagy sem(false)
	 */
	public boolean checkInsertNeighbour(Railway newNeighbour, int where) {
		
		if (ThisNeighbour.contains(newNeighbour) || ThisNeighbour2.contains(newNeighbour) ||
				ThatNeighbour.contains(newNeighbour) || ThatNeighbour2.contains(newNeighbour))
			return false;
		
		boolean result = true;
		
		switch (where) {
		
		case 1:
			if (ThisNeighbour.size() == 0);
			else if (ThatNeighbour.size() == 0);
			else result = false;
			break;
			
		case 2:
			if (ThisNeighbour2.size() == 0);
			else if (ThatNeighbour2.size() == 0);
			else result = false;
			break;
			
		default:
			result = false;
		}
		return result;
	}
	
	/**Hozzakoti a parameterben kapott sint ehhez a sinhez
	 * 
	 * @param newNeighbour: Az uj sin amit hozza akarunk kotni
	 * @param where: A ket keresztezo sin kozul melyikre
	 */
	public void insertNeighbour(Railway newNeighbour, int where) {

		switch (where) {
		
		case 1:
			if (ThisNeighbour.size() == 0) ThisNeighbour.add(newNeighbour);
			else if (ThatNeighbour.size() == 0) ThatNeighbour.add(newNeighbour);
			break;
			
		case 2:
			if (ThisNeighbour2.size() == 0) ThisNeighbour2.add(newNeighbour);
			else if (ThatNeighbour2.size() == 0) ThatNeighbour2.add(newNeighbour);
			break;
			
		default:
		}
	}
	
	@Override
	public List<Railway> getNeighbours() {
		List<Railway> result = super.getNeighbours();
		result.addAll(ThatNeighbour2);
		result.addAll(ThisNeighbour2);
		return result;
	}
	
	public List<Railway> get2ndNeighbours() {
		List<Railway> result = ThatNeighbour2;
		result.addAll(ThisNeighbour2);
		return result;
	}
	
	@Override
	public boolean deleteNeighbour(Railway tbDeleted) {
		boolean result = super.deleteNeighbour(tbDeleted);
		if (!result) result = ThisNeighbour2.remove(tbDeleted);
		if (!result) result = ThatNeighbour2.remove(tbDeleted);
		return result;
	}
}
