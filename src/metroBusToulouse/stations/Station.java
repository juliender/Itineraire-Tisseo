package metroBusToulouse.stations;

import java.util.ArrayList;

import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Station {
	
	private String nom;
	private String ville;
	private Location location; 
	private ArrayList<String> listeLignes;
	
	
	public Station(String n, String v){
		setNom(n);
		setVille(v);
		location = new Location("");
		listeLignes = new ArrayList<String>();
	}
	
	public Station(String n, String v, float lat,float lng){
		setNom(n);
		setVille(v);
		location = new Location("");
		location.setLatitude(lat);
		location.setLongitude(lng);
		listeLignes = new ArrayList<String>();
	}
	
	public Station(String n, String v, float lat,float lng,ArrayList<String> lignes){
		setNom(n);
		setVille(v);
		location = new Location("");
		location.setLatitude(lat);
		location.setLongitude(lng);
		listeLignes=lignes;
	}

	public Station(String n, String v, float lat, float lng, String listeStr) {
		setNom(n);
		setVille(v);
		location = new Location("");
		location.setLatitude(lat);
		location.setLongitude(lng);
		listeLignes=this.convListeLignes(listeStr);
	}

	public GeoPoint getPoint(){
		return new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
	}
	
	public ArrayList<String> getListeLignes(){
		return (ArrayList<String>) this.listeLignes;
		
	}

	public ArrayList<String> convListeLignes(String listeStr){
	ArrayList<String> res = new ArrayList<String>();
	
	String ligne = null;
	if (listeStr!=null && !listeStr.equals("")){
		
		int debut=0, fin=listeStr.indexOf(',');
		int cpt=0;
		while (fin!=-1 && cpt<10){
			ligne = listeStr.substring(debut,fin);
			debut = fin+1;
			fin = listeStr.indexOf(',',debut+1);

			res.add(ligne);
			cpt++;
		}
		ligne = listeStr.substring(debut);
		res.add(ligne);
	
	}
	return res;
	
}

public float getDistance(Location loc){
	float res=999999;
	if (this.location!=null && loc!=null){
		res = this.location.distanceTo(loc);
	}
	else{
		Log.e("pas de loc!",this.nom);
	}
	return res;
}


public void setNom(String nom) {
	this.nom = nom;
}

public String getNom() {
	return nom;
}

public void setVille(String ville) {
	this.ville = ville;
}

public String getVille() {
	return ville;
}


}




