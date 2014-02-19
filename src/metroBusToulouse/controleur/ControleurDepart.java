package metroBusToulouse.controleur;

import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.choixStation.ChoixStationArrivee;
import metroBusToulouse.liste.Liste;
import metroBusToulouse.stations.ListeStation.ListeStations;
import metroBusToulouse.stations.ListeStation.StationsFavorites;
import metroBusToulouse.stations.ListeStation.StationsProches;
import metroBusToulouse.stations.ListeStation.StationsUtilisees;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

public class ControleurDepart extends Controleur {
	
	public ControleurDepart(ChoixStation c,SharedPreferences prefs, Liste l){
		super(c, prefs, l);
		bundle = new Bundle();
		
		int listeRecuperee = prefs.getInt("ListeDepartCourante", 0);
		if (listeRecuperee>1){listeRecuperee=0;} 
		stationCourante = listeTypes.get(listeRecuperee);
		while(ite.hasNext() && ite.next().getClass()!=stationCourante.getClass()){			
		}
	}
	
	public void remplirListeTypeStation(SharedPreferences prefs){
		listeTypes.add(new StationsProches(classe.getApplicationContext()));
		listeTypes.add(new StationsUtilisees(classe.getApplicationContext()));
		listeTypes.add(new StationsFavorites(classe));
	}
	public void switcherListe(){
		if (!ite.hasNext()){
			ite = listeTypes.iterator();
		}
		stationCourante = (ListeStations) ite.next();
		int listeCourante = listeTypes.indexOf(stationCourante);

		Editor editor = prefs.edit();
		editor.putInt("ListeDepartCourante", listeCourante);
		editor.commit();
		raffraichirListe();	
	}
	
	public void raffraichirListe(){
		liste.chargerListe(stationCourante.getDeparts());
		renommerBouton();
	}

	public void go(String depart,String ville){
		try{
		Intent myIntent = new Intent(classe, ChoixStationArrivee.class);
	     if (depart.length()<35){
	    	 Toast.makeText(classe,"Départ choisi: "+depart+" à "+ville, Toast.LENGTH_LONG).show();
	    	 bundle.putString("depart",depart);
	    	 bundle.putString("departVille",ville);
	    	 myIntent.putExtras(bundle);
	    	 classe.startActivity(myIntent);
	     }
	     else {
	    	 Toast.makeText(classe,"Liste vide!",Toast.LENGTH_SHORT).show();
	     }
		}
		catch(Exception e){
			Toast.makeText(classe,"Impossible de continuer",Toast.LENGTH_SHORT).show();
		}
	}

	public void go(int position){
		String depart = null;
		String ville = null;
		try{
	     depart = liste.getListeStations().get(position).getNom();
	     ville = liste.getListeStations().get(position).getVille();
		}
		catch(Exception e){
		}
	     this.go(depart,ville);
	}
}
