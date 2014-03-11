package metroBusToulouse.stations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.util.Log;


public class BaseStations {
	
	private static String url = "http://onlinux.free.fr/map/phpsqlajax_genxml.php";
	private static SharedPreferences prefs;
	private static ArrayList<Station> liste;
	
	public BaseStations(Context c){
		prefs = c.getSharedPreferences("BaseStations", 0);
		try {

			InputStream input = c.getAssets().open("metro.json");
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();
			String text = new String(buffer);

	
	      	JSONObject jObject = new JSONObject(text); 
	      	Log.e("json",((JSONObject) jObject.getJSONArray("features").getJSONObject(0).get("properties")).getString("NOM"));
	      	
	      	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		
	public static ArrayList<String> recupererNoms(){
		ArrayList<String> res = new ArrayList<String>();
		
		for(Station s : recupererListe()){
			res.add(s.getNom());
		}
			
		return res;
	}
	public static ArrayList<Station> recupererListe(){
		ArrayList<Station> res = new ArrayList<Station>();
		int nb = prefs.getInt("nb", 0);
		
		for (int i=0; i<nb;i++){
			res.add(
					new Station(
							prefs.getString("nom"+i,"erreur"),
							prefs.getString("ville"+i,"erreur"),
							prefs.getFloat("lat"+i, 0),
							prefs.getFloat("lng"+i, 0),
							prefs.getString("line"+i, "Pas de ligne")
					)
			);
		}
		return res;
	}
	
	
	public static int mettreDansPrefs() throws ClientProtocolException, IOException, URISyntaxException {
		SharedPreferences.Editor editor = prefs.edit();
		Document doc = null;
		
		doc = getDocument(url);
		
		NodeList liste = doc.getElementsByTagName("marker");

		int nb=liste.getLength();
		editor.putInt("nb",nb);

		for(int i = 0;i<nb;i++){
			Element elt = (Element) liste.item(i);
			String name = elt.getAttribute("name");
			String ville = elt.getAttribute("city");
			String line = elt.getAttribute("line");

			float dlat = Float.parseFloat(elt.getAttribute("lat"));
			float dlng = Float.parseFloat(elt.getAttribute("lng"));
			
			/*int lat = (int) (dlat*10E6);
			int lng = (int) (dlng*10E6);*/
			
			editor.putString("nom"+i,name);
			editor.putString("ville"+i, ville);
			editor.putString("line"+i, line);
			editor.putFloat("lat"+i, dlat);
			editor.putFloat("lng"+i, dlng);
		}
		editor.commit();
		return nb;
	}
	
	public static Document getDocument(String url) throws ClientProtocolException, IOException, URISyntaxException{
		Document document = null;
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;

			//Cr���ation d'un DefaultHttpClient et un HttpGet permettant d'effectuer une requ���te HTTP de type GET
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet();

			//Cr���ation de l'URI et on l'affecte au HttpGet
			URI uri = new URI(url);
			httpGet.setURI(uri);

			//Execution du client HTTP avec le HttpGet
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			//On r���cup���re la r���ponse dans un InputStream
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


	public static Station trouveStation(String nom) {
		Station res = null;
		ArrayList<String> noms = recupererNoms();
		int n=noms.indexOf(nom);
		if (n>-1){
			res = recupererListe().get(n);
		}
		return res;
	}
	
}
