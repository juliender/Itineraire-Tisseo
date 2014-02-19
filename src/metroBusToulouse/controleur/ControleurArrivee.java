package metroBusToulouse.controleur;

import metroBusToulouse.AffichageItineraire;
import metroBusToulouse.choixStation.ChoixStationArrivee;
import metroBusToulouse.choixStation.Helper;
import metroBusToulouse.liste.Liste;
import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.Station;
import metroBusToulouse.stations.ListeStation.ListeStations;
import metroBusToulouse.stations.ListeStation.StationsFavorites;
import metroBusToulouse.stations.ListeStation.StationsProches;
import metroBusToulouse.stations.ListeStation.StationsUtilisees;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ControleurArrivee extends Controleur {
	
	private Station depart;
	
	public ControleurArrivee(ChoixStationArrivee c, SharedPreferences prefs, Liste l){
		super(c, prefs, l);
		super.bundle = c.getIntent().getExtras();
		depart = BaseStations.trouveStation(bundle.getString("depart"));
		int listeRecuperee = prefs.getInt("ListeArriveeCourante", 0);
		if (listeRecuperee>1){listeRecuperee=0;} 
		stationCourante = listeTypes.get(listeRecuperee);
		
		//on place l'ité au bon endroit aussi
		while(ite.hasNext() && ite.next().getClass()!=stationCourante.getClass()){			
		}
	}
	
	public void remplirListeTypeStation(SharedPreferences prefs){		
		listeTypes.add(new StationsProches(classe));
		listeTypes.add(new StationsUtilisees(classe));
		listeTypes.add(new StationsFavorites(this.classe));		
	}
	
	public void switcherListe(){
		if (!ite.hasNext()){
			ite = listeTypes.iterator(); 
		}
		stationCourante = (ListeStations) ite.next();
		sauverListe();
		raffraichirListe();
	}
	
	public void raffraichirListe(){
		try{
			liste.chargerListe(stationCourante,new Station(this.bundle.getString("depart"),this.bundle.getString("departVille")));
			renommerBouton();
			}
		catch(Exception e){
			Toast.makeText(classe,"Impossible d'afficher la liste",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void go(String arrivee,String ville) {
        Helper.connected(this.classe);
        
		try{
		Intent myIntent = new Intent(classe, AffichageItineraire.class);
		if (arrivee.length()<35){			
	    	 bundle.putString("arrivee",arrivee);
	    	 bundle.putString("arriveeVille",ville);
	    	 myIntent.putExtras(bundle);
	    	 //Toast.makeText(classe,"Arrivée choisie: "+arrivee+" à "+ville, Toast.LENGTH_LONG).show();
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
	

	public void go(int position) {
	    String arrivee=liste.getListeStations().get(position).getNom();
	    String ville=liste.getListeStations().get(position).getVille();
	    go(arrivee,ville);    
	}
}
