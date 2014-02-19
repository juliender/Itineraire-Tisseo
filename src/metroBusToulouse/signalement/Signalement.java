package metroBusToulouse.signalement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import julien.android.transports.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/** Activité listant les signalements postés, et permettant aussi d'en poster.
 *
 */
public class Signalement extends ListActivity{

	String xml;
	final static String signaler = "http://j.derenty.free.fr/appliAndroid/tisseo/signaler.php";
	ProgressDialog pd;
	Dialog dialog;
	Document doc;

	NodeList listeNoeuds;
	ArrayList<String> liste;
	Context c;
	
	Handler toastTeller;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		
		c = getApplicationContext();
				
		Calendar cal = Calendar.getInstance();
		String jour = cal.get(Calendar.DAY_OF_MONTH)+"";
		if (jour.length()==1){
			jour = "0".concat(jour);
		}
		String annee = cal.get(Calendar.YEAR)+"";
		String an = annee.substring(2);
		String mois = cal.get(Calendar.MONTH)+1+"";
		if (mois.length()==1){
			mois = "0".concat(mois);
		}
		String date = jour+mois+an;
		 xml = "http://j.derenty.free.fr/appliAndroid/tisseo/base"+date+".xml";

		 
		 
		liste = new ArrayList<String>();
		try {
			pd = ProgressDialog.show(Signalement.this, "", "Récupération signalements en cours", true,false);
			new Thread(new Runnable() {
			      public void run() { 
			      try {
			    	  liste = majListe();
			      } catch (Exception e) {} 

			      runOnUiThread(new Runnable() {
			    	  @Override
			    	  public void run() {
			    		  pd.dismiss();
			    		  if (doc==null || liste==null){
			    			  zeroSignalement();
			    		  }
			    		 try{
			    			 setListAdapter(new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, liste));
			    		 }
			    		 catch (NullPointerException e){
			    			 zeroSignalement();
			    		 }
			    	  }
			      });
			      }}).start();

			
		} catch (Exception e1) {
			Toast.makeText(Signalement.this, "Echec Récupération signalements", Toast.LENGTH_LONG).show();
		}
		
		Button boutonAjout = (Button)findViewById(R.id.boutonAjout);
		boutonAjout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id){
		case 0:
		Context mContext = Signalement.this;
		dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.ajoutsign);

		final EditText inputStation = (EditText) dialog.findViewById(R.id.signStation);
		final EditText inputCom = (EditText) dialog.findViewById(R.id.signCom);
		Button valider = (Button) dialog.findViewById(R.id.boutonAjoutValider);

		
		valider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Editable station = inputStation.getText();
				Editable com = inputCom.getText();
				String s_station = URLEncoder.encode(station.toString());
				String s_com = URLEncoder.encode(com.toString());
				String url = "http://j.derenty.free.fr/appliAndroid/tisseo/signaler.php?station="+s_station.toString()+"&comment="+s_com.toString();
				
				Log.e("", url);
				HttpClient httpclient = new DefaultHttpClient();
		        try {
		            HttpGet httpget = new HttpGet(url);

		            ResponseHandler<String> responseHandler = new BasicResponseHandler();
		            String responseBody = httpclient.execute(httpget, responseHandler);
		 
		            if (responseBody.equals("1\n")){
		            	Toast.makeText(c, "Signalement ajouté !", Toast.LENGTH_LONG).show();
		            }
		            else{
		            	
		            	Toast.makeText(c, "Erreur d'ajout :"+responseBody, Toast.LENGTH_LONG).show();
		            }

		 
		        } catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
		            // When HttpClient instance is no longer needed,
		            // shut down the connection manager to ensure
		            // immediate deallocation of all system resources
		            httpclient.getConnectionManager().shutdown();
		        }
				
				dialog.dismiss();
				onCreate(null);
			}
		  }
		);
		break;
		
		}

	    return dialog;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String coms;
		try {
			Element elt = (Element) listeNoeuds.item(listeNoeuds.getLength()-position-1);
			coms = elt.getLastChild().getNodeValue();
		}
		catch (Exception e){
			coms = "Vide";
		}
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setMessage(coms)
        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.cancel();
            }
        })
        .create();
    dialog.show();
	}
	
	public void zeroSignalement(){
		AlertDialog.Builder adb = new AlertDialog.Builder(Signalement.this);
		adb.setTitle("")
	    .setMessage("Pas de signalement aujourd'hui encore.\nEn créer un ?")
	    .setPositiveButton("OUI", new DialogInterface.OnClickListener()
	    {
	    public void onClick(DialogInterface dialog, int whichButton)
	    {
	    	runOnUiThread(new Runnable() {
		           @Override
		           public void run() {
		        	   Signalement.this.showDialog(0);
		            }
		        });
	    	
	    }
	    }
	    )
	    .setNegativeButton("NON", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
	    	finish();
	    }
	    })
	    .show();

	}
	

	private ArrayList<String>  majListe() throws ClientProtocolException, IOException, URISyntaxException{
		doc = null;
		ArrayList<String> res = new ArrayList<String>();
		int nb;

		doc = getDocument(xml);
		
		if(doc!=null){
			listeNoeuds = doc.getElementsByTagName("observation");
			nb = listeNoeuds.getLength();
			for(int i = nb-1;i>=0;i--){
				Element elt = (Element) listeNoeuds.item(i);
				String station = elt.getAttribute("station");
				String time = elt.getAttribute("time");
				res.add(time+" "+station);
				
			}
		}

		return res;
	}

	public static Document getDocument(String url) throws ClientProtocolException, IOException, URISyntaxException{
			Document document = null;
			BufferedReader bufferedReader = null;
			InputStream inputStream = null;

				//Création d'un DefaultHttpClient et un HttpGet permettant d'effectuer une requête HTTP de type GET
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();

				//Création de l'URI et on l'affecte au HttpGet
				URI uri = new URI(url);
				httpGet.setURI(uri);

				//Execution du client HTTP avec le HttpGet
				HttpResponse httpResponse = httpClient.execute(httpGet);

				//On récup�re la r�ponse dans un InputStream
				inputStream = httpResponse.getEntity().getContent();
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					document = builder.parse(inputStream);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
				//Dans tous les cas on ferme le bufferedReader s'il n'est pas null
				if (bufferedReader != null){
					try{
						bufferedReader.close();
					}catch(IOException e){
						
					}
				}
				}
			
			return document; 
		}


}
