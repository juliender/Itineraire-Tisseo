package metroBusToulouse.choixStation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;


public class Helper {

	
	public static boolean  connected(Context c) {
		boolean res = false;
		ConnectivityManager cm = ((ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE));
		
		res = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		
		if (res) {
			Toast.makeText(c, "Utilisation de la connexion "+cm.getActiveNetworkInfo().getTypeName(),Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(c, "Pas de connexion internet",Toast.LENGTH_LONG).show();
		}
		
		 return res;

		}
}
