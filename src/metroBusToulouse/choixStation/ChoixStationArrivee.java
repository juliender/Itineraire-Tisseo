package metroBusToulouse.choixStation;
import julien.android.transports.R;
import metroBusToulouse.controleur.ControleurArrivee;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

public class ChoixStationArrivee extends ChoixStation {

	@Override()
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Arrivée");
    }
	
	public void initialiserControleur(){
		
        controleur = new ControleurArrivee(this,prefs,liste);
	}
	
	public void afficherAide(){
		new AlertDialog.Builder(this)
		   .setTitle("Aide")
		   .setMessage("Vous pouvez choisir votre station d'arrivée entrant le nom de la station en haut à droite et en cliquant sur celle que vous désirez choisir.")
		   .show();
	}

}