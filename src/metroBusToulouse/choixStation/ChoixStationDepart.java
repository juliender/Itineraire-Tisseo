package metroBusToulouse.choixStation;
import julien.android.transports.R;
import metroBusToulouse.controleur.ControleurDepart;
import metroBusToulouse.stations.BaseStations;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ChoixStationDepart extends ChoixStation {
	public static final String PREF_FILE_NAME = "PrefFile";
	public static final String version = "v0.8";
	

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Départ");
        premierLancement();
    }
	
	
	public void initialiserControleur(){
        controleur = new ControleurDepart(this,prefs,liste);
	}

	public void afficherAide(){
		   new AlertDialog.Builder(this)
		   .setTitle("Aide")
		   .setMessage("Vous pouvez choisir votre station de départ en cliquant sur une station de la liste." +
		   		"\nVous pouvez aussi entrer le nom de la station en haut à droite, et cliquer sur celle que vous désirez choisir.")
		   .show();
	}

    private void premierLancement() {
    	SharedPreferences prefs = getSharedPreferences(PREF_FILE_NAME,MODE_PRIVATE);
       final SharedPreferences.Editor editor = prefs.edit();
	   boolean firstLaunchAfterUpdate = prefs.getBoolean(version, true);

	   if (firstLaunchAfterUpdate){
		   editor.putBoolean(version,false);
		   
		   try{
			   BaseStations.mettreDansPrefs();   
		   }
		   catch(Exception e){
			   new AlertDialog.Builder(this)
			   .setTitle("Erreur liste stations")
			   .setMessage("La liste des  stations n'a pas été synchronisée, vous pouvez le faire manuellement à partir du menu.")
			   .show();
		   }

		   
	   }
	   else {
	   if (!prefs.getBoolean("note",true)){
		   AlertDialog.Builder adb = new AlertDialog.Builder(ChoixStationDepart.this);
		   //Message nouvelle version
		   adb.setTitle("Notez l'appli !")
		   .setMessage("Allez sur le market et donnez votre avis !" +
		   		"\n(Activez aussi la mise à jour automatique pour avoir les dernières fonctionnalités)")
		   .setPositiveButton("Noter",new OnClickListener(){
			   @Override
			   public void onClick(DialogInterface arg0, int arg1) {
				   editor.putBoolean("note", true);
				   editor.commit();
				   String url = "market://details?id=julien.android.transports";
				   Intent i = new Intent(Intent.ACTION_VIEW);
				   i.setData(Uri.parse(url));
				   startActivity(i);
			   }
		   });
		   adb.setNegativeButton("Fermer", new OnClickListener(){
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			   }
		   })
		   .show();
	   }
	   }
	   
	   
	   boolean firstLaunch = prefs.getBoolean("firstLaunch", true);
	   if (firstLaunch){
   			editor.putBoolean("firstLaunch",false);
   			AlertDialog.Builder adb = new AlertDialog.Builder(ChoixStationDepart.this);
   			adb.setTitle("Première utilisation ...")
   			//message première utilisation
   			.setMessage("Explications: \n\n" +
   					"Vous choisissez d'abord une station de départ parmi les stations proches, ou parmi les plus utilisées en cliquant sur le bouton \"Stations proches\"." +
   					"Vous tapez votre station d'arrivée en haut à droite." +
   	    			"Vous avez les horaires !")
   	    	.show();
	   }
	   editor.commit();
   }
}