package metroBusToulouse.liste;

import java.util.List;

import metroBusToulouse.stations.Station;
import metroBusToulouse.stations.ListeStation.ListeStations;
import android.content.Context;
import android.widget.ListView;

public class Liste {

	private List<Station> listeStations;
	private ListView lv;
	Context context;
	private StationAdapter adapter;
	
	public Liste(Context c,ListView l) {
		context = c;
		lv = l;
		adapter=new StationAdapter(context, listeStations);
	}
	
	
	public void chargerListe(List<Station> liste){
		this.listeStations = liste;
		adapter.maj(liste);
		lv.setAdapter(adapter);
	}


	public ListView getLV(){
		return lv;
	}


	public void chargerListe(ListeStations stationCourante, Station stationDepart) {
		chargerListe(stationCourante.getArrivees(stationDepart));		
	}

	public List<Station> getListeStations() {
		return listeStations;
	}
	
	

}
