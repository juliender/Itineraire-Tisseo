package metroBusToulouse.stations.ListeStation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.Station;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

public class StationsUtilisees implements ListeStations{

	private static ContextWrapper context;
	private static String FILENAME="StationsUtilisees";

	
	public StationsUtilisees(Context applicationContext) {
		context = (ContextWrapper) applicationContext; 
	}

	@Override
	public void afficherMenuCont(ContextMenu menu, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		menu.setHeaderTitle(this.recupererFavoris().get(info.position).getNom());
		menu.add(Menu.NONE, 0, 0, "Supprimer");
	}

	@Override
	public void reagirMenuContextuel(MenuItem item, ChoixStation classe) {
		int pos =((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
		supprimer(recupererFavoris().get(pos).getNom());
		classe.raffraichirListe();
	}

	private void supprimer(String nom) {
		String all="";
		
		try {
			InputStream instream = context.openFileInput(FILENAME);
		    if (instream != null) {
		    	InputStreamReader inputreader = new InputStreamReader(instream);
		    	BufferedReader buffreader = new BufferedReader(inputreader);
		    	
		    	String line;
		    	while ( (line = buffreader.readLine()) != null)  {
		    		if (!recupereNom(line).equals(nom)){
		    			all=all+line+"\n";		    			
		    		}		    		
		    	}
			    instream.close();
		    }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			//Impossible sinon créé
			e.printStackTrace();
		}
		try {
			fos.write(all.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Station> getDeparts() {
		ArrayList<Station> res = (ArrayList<Station>) recupererFavoris();
		return res;
	}

	@Override
	public List<Station> getArrivees(Station depart) {
		ArrayList<Station> res = (ArrayList<Station>) recupererFavoris();
		return res;
	}

	@Override
	public CharSequence getType() {
		return "Déjà utilisées";
	}
	
	
	public static void incremente(String nom){
		int valeur = 0;

    	String all="";
		
		try {
			InputStream instream = context.openFileInput(FILENAME);
		    if (instream != null) {
		    	InputStreamReader inputreader = new InputStreamReader(instream);
		    	BufferedReader buffreader = new BufferedReader(inputreader);
		    	
		    	String line;
		    	String nomLu="";
		    	while ( (line = buffreader.readLine()) != null)  {
		    		if (!recupereNom(line).equals(nom)){
		    			all=all+line+"\n";		    			
		    		}
		    		else{
		    			valeur = recupereValeurDeLigne(line);
		    			valeur++;
		    		}
		    		nomLu = recupereNom(line);
		    		
		    	}
			    instream.close();
		    }
			String ligneAEcrire = nom+"_"+(valeur)+"\n";
		    all=all+ligneAEcrire;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			//Impossible sinon créé
			e.printStackTrace();
		}
		try {
			fos.write(all.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static List<Station> recupererFavoris(){
		ArrayList<Station> res = new ArrayList<Station>();
		try {	
			InputStream instream = context.openFileInput(FILENAME);
		    if (instream != null) {
		    	InputStreamReader inputreader = new InputStreamReader(instream);
		    	BufferedReader buffreader = new BufferedReader(inputreader);
		    	
		    	String line;
		    	ArrayList<Integer> valeurs = new ArrayList<Integer>();
		    	while (( line = buffreader.readLine()) != null) {
		    		String nom = recupereNom(line);
		    		int valeur = recupereValeurDeLigne(line);
		    		
		    		int i=0;
		    		for (int j=0;j<res.size();j++){
		    			if (valeurs.get(j)>valeur){
		    				i=j;
		    			}
		    		}
		    		res.add(i,BaseStations.trouveStation(nom));
			    	valeurs.add(i,valeur);
		    	}
		    	
		    }
		    instream.close();
		    
		} catch (java.io.FileNotFoundException e) {
			// TODO
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private static int recupereValeurDeLigne(String line) {
		int res = 0;
		res = Integer.parseInt(line.substring(line.lastIndexOf('_')+1));
		return res;
	}

	private static int recupereValeurDeNom(String nom) {
		int res = 0;
		try {
			InputStream instream = context.openFileInput(FILENAME);
		    if (instream != null) {
		    	InputStreamReader inputreader = new InputStreamReader(instream);
		    	BufferedReader buffreader = new BufferedReader(inputreader);
		 
		    	String line = "";
		    	String nomLu;
		    	boolean trouve = false;
		    	while (!trouve && (line = buffreader.readLine()) != null) {
		    		nomLu = recupereNom(line);
		    		trouve = nomLu.equals(nom);
		    	}
		    	try{
		    		res = Integer.parseInt(line.substring(0,line.lastIndexOf('_')));
		    	}
		    	catch(Exception e){
		    	}
		    }
		    instream.close();
		} catch (java.io.FileNotFoundException e) {
			// TODO
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private static String recupereNom(String line) {
		String res = line;
		res = res.substring(0,line.lastIndexOf('_'));
		return res;
	}
	


}
