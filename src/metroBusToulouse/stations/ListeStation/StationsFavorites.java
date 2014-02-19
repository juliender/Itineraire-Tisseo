package metroBusToulouse.stations.ListeStation;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import julien.android.transports.R;

import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.Station;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class StationsFavorites implements ListeStations{
	private static String FILENAME = "Favoris.txt";
	private Context context;
	
	public StationsFavorites(Context c){
		context = c;
	}
	
	@Override
	public void afficherMenuCont(ContextMenu menu, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		if (this.getListe().get(info.position).getVille().length()>0){
			menu.setHeaderTitle(this.getListe().get(info.position).getNom());
			menu.add(Menu.NONE, 0, 0, "Supprimer");
		}
		menu.add(Menu.NONE, 0, 0, "Ajouter station");
	}

	private List<Station> getListe() {
		return recupererFavoris();
	}

	@Override
	public void reagirMenuContextuel(final MenuItem item, ChoixStation classe) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		String titre = (String) item.getTitle();
		if (titre.equals("Ajouter station")){
			LayoutInflater factory = LayoutInflater.from(classe);
            final View textEntryView = factory.inflate(R.layout.ajout_station, null);
//TODO AUTOCOMPLETE!!
            final AutoCompleteTextView etn = initialisationAutoComplete();
			AlertDialog.Builder dialog = new AlertDialog.Builder(classe);
			dialog.setView(textEntryView)
			.setMessage("Entrez le nom de la station à ajouter")
		    .setPositiveButton("Ajouter", new DialogInterface.OnClickListener(){
		    	public void onClick(DialogInterface dialog, int whichButton){
		    		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		    		ajouterFavoris(etn.getText().toString());//,info.position);
		    	}
		    })
		    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int whichButton) {
		    	}
		    })
		    .show();
		}
		else if (titre.equals("Supprimer")){
			//TODO suppr(info.position);
		}		
	}

	private AutoCompleteTextView initialisationAutoComplete(){
		
		final AutoCompleteTextView textView = (AutoCompleteTextView) ((Activity) context).findViewById(R.id.autocomplete);
        final ArrayList<String> listeNoms = BaseStations.recupererNoms();
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(context,R.layout.list_item, listeNoms);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
            	//on cherche la ville de l'arret selectionné (position dans la grande liste:perdue)
            	textView.setText( (String) adapter.getItem(i));
            }
           }	
        );
        return textView;
        
	}
	@Override
	public List<Station> getDeparts() {
		return recupererFavoris();
	}

	@Override
	public List<Station> getArrivees(Station depart) {
		return recupererFavoris();
	}

	@Override
	public CharSequence getType() {
		return "Favorites";
	}
	
	private void ajouterFavoris(String nom){
		String ligneAEcrire = nom+"\n";
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			//Impossible sinon créé
			e.printStackTrace();
		}
		try {
			fos.write(ligneAEcrire.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private List<Station> recupererFavoris(){
		ArrayList<Station> res = new ArrayList<Station>();
		
		 // try opening the myfilename.txt
		  try {
		    // open the file for reading
		    InputStream instream = context.openFileInput(this.FILENAME);
		 
		    // if file the available for reading
		    if (instream != null) {
		      // prepare the file for reading
		      InputStreamReader inputreader = new InputStreamReader(instream);
		      BufferedReader buffreader = new BufferedReader(inputreader);
		 
		      String line;
		 
		      // read every line of the file into the line-variable, on line at the time
		      while (( line = buffreader.readLine()) != null) {
		    	  Station s = BaseStations.trouveStation(line);
		    	  if (s!=null){
		    		  res.add(s);  
		    	  }
		    	  
		      }
		 
		    }
		 
		    // close the file again
		    instream.close();
		  } catch (java.io.FileNotFoundException e) {
		    // do something if the myfilename.txt does not exits
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  if (res.size()==0){
			  res.add(new Station("Long clic pour ajouter:","" ));
		  }
		return res;
	}
	
}
