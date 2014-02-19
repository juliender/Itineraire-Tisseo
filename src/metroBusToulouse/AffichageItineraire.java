package metroBusToulouse;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import julien.android.transports.R;
import metroBusToulouse.choixStation.ChoixStationDepart;
import metroBusToulouse.stations.ListeStation.StationsUtilisees;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class AffichageItineraire extends Activity {
	
	//Heure du départ
	private String jour;
	private String heure;
	private String minutes;

    @Override()
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.affichage_resultat);
    	
    	WebView wv = (WebView) findViewById(R.id.WebView);
    	
    	//extraction infos bundle
    	Bundle extras = this.getIntent().getExtras();
    	String depart = extras.getString("depart");
    	String departVille = extras.getString("departVille");
    	String arrivee = extras.getString("arrivee");
    	String arriveeVille = extras.getString("arriveeVille");
	

    	if(!arrivee.equalsIgnoreCase(depart) && !depart.equalsIgnoreCase(arrivee) ){
    		
    		calculerTemps();
    
    		//test si horaire perso
    		if (extras.containsKey("heure") ) {
    			heure = extras.getString("heure");
    			minutes = extras.getString("minute");
    		}

    		
    		SharedPreferences prefs = getSharedPreferences(ChoixStationDepart.PREF_FILE_NAME,MODE_PRIVATE);
//    		wv.loadUrl(
//    				genererURL(
//    				prefs.getInt("choixAff",0),
//    				depart,
//    				departVille,
//    				arrivee,
//    				arriveeVille,
//    				jour,
//    				heure,
//    				minutes));
    		
    		//incr�mentation de la liste
    		StationsUtilisees.incremente(depart);
    		StationsUtilisees.incremente(arrivee);
    		
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(genererURL(
    				prefs.getInt("choixAff",0),
    				depart,
    				departVille,
    				arrivee,
    				arriveeVille,
    				jour,
    				heure,
    				minutes)));
    		startActivity(browserIntent);
    		this.finish();
    	}
    	else{
    		wv.loadData("Arrivee identique au Depart !!", "text/html" , "UTF-8");
    	}
    }

    public String genererURL(
    		int choixAff,
    		String depart,
    		String departVille,
    		String arrivee, 
    		String arriveeVille,
    		String jour,
    		String heure,
    		String minutes)
    {
    	String url = null;
    	Calendar cal = new GregorianCalendar();
    	int g = Integer.parseInt(heure);
    	Log.e("g", ""+g+cal.getTimeZone().toString()+" "+cal.getTime().toString());
    	if (cal.getTimeZone().inDaylightTime(cal.getTime())){
			if (g!=0){
				g--;
			}
			else{
				g=23;
			}
	}
    	heure = ""+g;
    	
    	String charset = "ISO-8859-1";
    	try{
    		depart = depart+" ("+departVille+")";
    		String departure = URLEncoder.encode(depart,charset);
    		arrivee = arrivee+" ("+arriveeVille+")";
    		String arrival = URLEncoder.encode(arrivee,charset);
    		switch(choixAff){
			
		//		case 1:
		//			url = "http://j.derenty.free.fr/appliAndroid/tisseo/extract.php?villedepart="+departVille+"&depart="+depart+"&villearrivee="+arriveeVille+"&arrivee="+arrivee+"&jour="+jour+"&heure="+heure+"&minute="+minutes;
		//			break;
				default:
					url="http://mobi.tisseo.fr/recherche/resultat?ush=23&usm=45&prz=15479&departure="+departure+"&key_departure="+departure+"&arrival="+arrival+"&key_arrival="+arrival+"&dy=2014-02-18&sh=23&sm=45&tm=17451448556060673&dac=0";
					//url="http://www.tisseo.fr/recherche/resultat?ush="+heure+"&usm="+minutes+"&prz=0&departure="+departure+"&key_departure="+departure+"&arrival="+arrival+"&key_arrival="+arrival+"&dy=2014-02-19&sh="+heure+"&sm="+minutes+"&dac=0";
					//url="http://mobi.tisseo.fr/recherche/resultat?ush="+heure+"&usm="+minutes+"&prz=15479&departure="+departure+"&key_departure="+departure+"&arrival="+arrival+"&key_arrival="+arrival+"&dac=0dy=2014-02-19&sh=23&sm=45&tm=17451448556060673";
					break;
			}
    	} catch(Exception e) {
    	}
		Log.e("", heure+"h"+minutes);
		Log.e("URL",url);

		return url;
    }
    
    public void calculerTemps(){
	Calendar cal = new GregorianCalendar();
	
	//jour
	long t1 = cal.getTimeInMillis() + 3600;
	long c = t1/86400000; 
	long d = c*86400 - 3600;
	jour = String.valueOf(d);
	
	//heure
	int g =  cal.get(Calendar.HOUR_OF_DAY);
	heure = String.valueOf(g);
	
	//min	
	int f =  cal.get(Calendar.MINUTE);
	f = (f/15)*15;
	minutes = String.valueOf(f);
}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    }
}
