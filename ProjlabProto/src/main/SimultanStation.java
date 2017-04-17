package main;

public class SimultanStation extends Station{
	
	/**
	 * Konstruktor. Parameterul kapja, hogy az allomas melyik sinnel all. Ezen meghivja a setStation(Station)
	 * fuggvenyt a sajat referenciajaval. A masodik parameter az allomas szine.
	 * @param railway Amelyik sinnel all
	 * @param color Az allomas szine
	 */
	public SimultanStation(Railway railway, Color color){
		super(railway,color);
	}
	
	/**
	 * Jelzoszinnel ter vissza, jelezve, hogyha tortent-e felszallas. Ha megteheto, lebonyolit egy felszallast, ugy,
	 * hogy lekerdezi a parameterul kapott vagon szinet, majd a Passengers attributumat, es ha azonos szinu, es ures
	 * a vagon, akkor beallitja true-ra a Passengers attributumot.
	 * @param toMe A parameterul kapott kocsi
	 * @return A kocsi szine
	 */
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
