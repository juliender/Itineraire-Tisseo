
package metroBusToulouse.stations.ListeStation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.controleur.Controleur;
import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.Station;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;

/**
 * Permet de récupérer les stations à proximité.
 * @author Julien
 *
 */
public class StationsProches implements ListeStations{
	Context context;
	
	public StationsProches(Context c) {
		context = c;
	}
	
	/**
	 * Renvoie les premières stations de la liste complète triée par distance.
	 * @param nbmax
	 * @return
	 */
	public List<Station> getListe(int nbmax) {
		List<Station> res = new ArrayList<Station>();
		List<Station> listeComplete = getListe();
		if (listeComplete.size()>=nbmax){
			res = listeComplete.subList(0, nbmax);
		}
		else{
			res = listeComplete;
		}
		Log.d("GetListe Stationsproches", ""+res.size());
		return res;
	}
	
	/**
	 * Renvoie la liste complète des stations triée par distance
	 * @return liste complète des stations triée par distance
	 */
	public ArrayList<Station> getListe() {
		ArrayList<Station> res = new ArrayList<Station>();
		
		//creation liste stations	
		BaseStations base = new BaseStations(context);
		ArrayList<Station> listeNonTriee = base.recupererListe(); 
		
		//location
		Location loc = Controleur.lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		//tri du tableau
		Collections.sort(listeNonTriee,new ComparateurStationsParDistance(loc));
		res = listeNonTriee;
		
		return res;
	}	

	public class ComparateurStationsParDistance implements Comparator<Station>{
		private Location compLocation;
		public ComparateurStationsParDistance(Location loc){
			compLocation = loc;
		}

		@Override
		public int compare(Station s0, Station s1) {
			int res = 0;
			float d0 = s0.getDistance(compLocation);
			float d1 = s1.getDistance(compLocation);
			
			if (d0 > d1){
				res = 1;
			}
			else if (d0 < d1){
				res = -1;
			}
			else{
				res = 0;
			}
			return res;
		}
		
	}

	@Override
	public void afficherMenuCont(ContextMenu menu, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reagirMenuContextuel(MenuItem item, ChoixStation classe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Station> getDeparts() {
		return this.getListe(10);
	}

	@Override
	public List<Station> getArrivees(Station depart) {
		return this.getListe(10);
	}

	@Override
	public CharSequence getType() {
		return "A proximité";
	}

}
