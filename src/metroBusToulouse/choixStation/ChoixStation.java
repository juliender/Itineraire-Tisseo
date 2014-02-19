package metroBusToulouse.choixStation;

import java.util.ArrayList;

import julien.android.transports.R;
import metroBusToulouse.controleur.Controleur;
import metroBusToulouse.liste.Liste;
import metroBusToulouse.map.Map;
import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.SynchronisationBase;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Classe définissant l'activité de choix d'une station dans une liste, avec l'interface associée.
 * 
 */
public abstract class ChoixStation extends ListActivity {
	
	Liste liste;
	Controleur controleur;
	SharedPreferences prefs;
	
	//UI
	Button boutonTypeStation;
	
	//Ret activities
	int MAP_RET=1;
	
	public abstract void initialiserControleur();
	public abstract void afficherAide();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choix_station);
		
		//Initialisation attributs
        prefs = getSharedPreferences(ChoixStationDepart.PREF_FILE_NAME,MODE_PRIVATE);
        liste = new Liste(this.getApplicationContext(),getListView());
        initialiserControleur();
        
        chargementUI();    
        
        //Menu contextuel
        registerForContextMenu(getListView());
    }
	
		
	@Override
	public void onResume(){
		super.onResume();
		((AutoCompleteTextView) findViewById(R.id.autocomplete)).setText("");
		controleur.raffraichirListe();
		Controleur.lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60*1000, 50, Controleur.mlocListener);		//listener sur nouvelle location
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Controleur.lm.removeUpdates(Controleur.mlocListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		if (v.getId()==getListView().getId()) {
			controleur.afficherMenuCont(menu,menuInfo);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem menuItem){
		controleur.reagirMenuContextuel(menuItem);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	    controleur.go(position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == MAP_RET) {
			if(resultCode == RESULT_OK) {
	    		 controleur.go(data.getStringExtra("Nom"),data.getStringExtra("Ville"));
	    	}
	    }
	    else if (resultCode == RESULT_CANCELED) {
	    	Toast.makeText(this, "Opération annulée", Toast.LENGTH_SHORT).show();
	    }
	}

   @Override
   	public boolean onOptionsItemSelected(MenuItem item) {
	   return reagirMenu(item);
    }
    

	private void chargementUI() {
        //UI
        boutonTypeStation = (Button) findViewById(R.id.button);
        Button buttonMap = (Button) findViewById(R.id.ButtonMap);
        Button boutonAide= (Button) findViewById(R.id.BoutonAide);
        
        //*** Listeners ***
        boutonTypeStation.setOnClickListener(
            	new OnClickListener() {
            		@Override
            		public void onClick(View v) {
            			controleur.switcherListe();
            		}});
        buttonMap.setOnClickListener(
            	new OnClickListener() {
            		@Override
            		public void onClick(View v) {
            			Intent mapint = new Intent(ChoixStation.this, Map.class);
            			try{
            				startActivityForResult(mapint, MAP_RET);
            			}
            			catch (Exception e){
            	        	Toast.makeText(getApplicationContext(),"Impossible d'afficher la carte", 1500).show();
            			}
            		}});
        
        boutonAide.setOnClickListener(
            	new OnClickListener() {
            		@Override
            		public void onClick(View v) {
            			afficherAide();
            		}
            	});
        
        initialisationAutoComplete();
		
	}
	

	private boolean reagirMenu(MenuItem item) {
		   int index = (item.getItemId());
		   switch (index) {
		   case R.id.choixAff:
	    		break;
	    		
		   case R.id.changeTime:
			   afficherTimePicker();
	    		break;
	    	case R.id.syncStations:
	    		new SynchronisationBase(this).run();
			break;
	    	case R.id.ajout:
	    		String url = "http://onlinux.free.fr/map/bus.php";
	    		Intent i = new Intent(Intent.ACTION_VIEW);
	    		i.setData(Uri.parse(url));
	    		startActivity(i);
	    		break;
	    		
	    	case R.id.sign:
	    		controleur.sign();
	    		break;
	    	default:
	    		return false;
		   }
		   return true;
	
}

	private void afficherTimePicker() {
		LayoutInflater factory =  LayoutInflater.from(this);
        View TimePickerView = factory.inflate(R.layout.choix_heure, null);
        final TimePicker tp = (TimePicker) TimePickerView.findViewById(R.id.TimePick);
        AlertDialog dialog = new AlertDialog.Builder(this) 
            .setView(TimePickerView)
            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
             	   controleur.changerHoraire(tp.getCurrentHour(),tp.getCurrentMinute());
	        		Toast toast = Toast.makeText(getApplicationContext(),
	        				"Horaire changé :"+tp.getCurrentHour()+"h"+tp.getCurrentMinute(),1000);
	        		toast.show();
                    dialog.cancel();
                }
            })
            .create();
        dialog.show();
        //tp = (TimePicker) dialog.findViewById(R.id.TimePick);
        tp.setIs24HourView(true);
	
}


	public void raffraichirListe() {
		this.controleur.raffraichirListe();
	}
	
	public void texteBouton(String texte){
		this.boutonTypeStation.setText(texte);
	}
	
	private void initialisationAutoComplete(){
		
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        final BaseStations ls = new BaseStations(this);
        final ArrayList<String> listeNoms = ls.recupererNoms();
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.list_item, listeNoms);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
            	//on cherche la ville de l'arret selectionné (position dans la grande liste:perdue)
            	String nom = (String) adapter.getItem(i);
            	int index = listeNoms.indexOf(nom);
            	String ville = ls.recupererListe().get(index).getVille();
    		    controleur.go(nom,ville);    			
            }
           }	
        );
        
	}
}
