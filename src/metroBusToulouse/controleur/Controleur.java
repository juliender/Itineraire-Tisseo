package metroBusToulouse.controleur;

import java.util.ArrayList;
import java.util.Iterator;


import metroBusToulouse.MyLocationListener;
import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.liste.Liste;
import metroBusToulouse.signalement.Signalement;
import metroBusToulouse.stations.ListeStation.ListeStations;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Défini le comportement des boutons de l'activité de choix d'une station
 *
 */
public abstract class Controleur {
	protected ChoixStation classe;
	protected SharedPreferences prefs;
	protected int compteurClic;
	protected ArrayList<ListeStations> listeTypes;
	protected Iterator<ListeStations> ite;
	protected ListeStations stationCourante;
	protected Liste liste;
	protected Bundle bundle;
	public static LocationManager lm;
	public static LocationListener mlocListener;
	
	public Controleur(ChoixStation c,SharedPreferences prefs_,Liste l){
		prefs = prefs_;
		liste = l;
		classe = c;
		listeTypes = new ArrayList<ListeStations>();
		remplirListeTypeStation(prefs);
		ite = listeTypes.iterator();
		stationCourante = this.listeTypes.get(0);
		
		lm=(LocationManager)classe.getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener(this);	//acc�s aux m�thodes
    	lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 60*1000, 50, mlocListener);		//listener sur nouvelle location
	}
	
	public abstract void raffraichirListe();
	public abstract void go(String depart,String ville);
	public abstract void go(int position);
	public abstract void remplirListeTypeStation(SharedPreferences prefs);
	public abstract void switcherListe();


	public void renommerBouton(){
		classe.texteBouton((String) stationCourante.getType());
	}
	public void sign() {
		Intent myIntent = new Intent(classe, Signalement.class);
		try{
			classe.startActivity(myIntent);	
		}
		catch(Exception e){
			Toast.makeText(classe.getApplicationContext(),e.toString(),1000).show();
	}
	}

	public void afficherMenuCont(ContextMenu menu, ContextMenuInfo menuInfo) {
		stationCourante.afficherMenuCont(menu, menuInfo);
	}

	public void reagirMenuContextuel(MenuItem item) {
		stationCourante.reagirMenuContextuel(item,classe);		
	}

	public void changerHoraire(Integer currentHour, Integer currentMinute) {
       	bundle.putString("heure", ""+currentHour);
   		bundle.putString("minute",""+currentMinute);
	}
	protected void sauverListe(){
		int listeCourante = listeTypes.indexOf(stationCourante);
		Editor editor = prefs.edit();
		editor.putInt("ListeArriveeCourante", listeCourante);
		editor.commit();
		
	}

	public void locationMaj(Location loc) {
		if (stationCourante.getType().equals("A proximité")){
			this.raffraichirListe();
		}
	}
		
}
	

