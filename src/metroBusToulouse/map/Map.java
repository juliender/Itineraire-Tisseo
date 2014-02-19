package metroBusToulouse.map;

import java.util.ArrayList;
import java.util.List;

import metroBusToulouse.stations.BaseStations;
import metroBusToulouse.stations.Station;


import julien.android.transports.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
/** Affiche la carte et permet le choix de la station.
 * 
 * @author Julien
 *
 */
public class Map extends MapActivity {
    MapController mc;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
        mc = mapView.getController();
        mc.setZoom(16); 
        
        Location loc = ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	try{
    	GeoPoint center = new GeoPoint((int)(loc.getLatitude()*1E6),(int)(loc.getLongitude()*1E6)); 
        mc.setCenter(center);
    	}
    	catch(NullPointerException e){
    		Toast.makeText(this,"Localisation non trouvée",800);
    	}
        
        
	    Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    ArrayList<MyItemizedOverlay> itemizedOverlays = new ArrayList<MyItemizedOverlay>();

	    BaseStations ls = new BaseStations(this);
	    
	    ArrayList<Station> liste = ls.recupererListe();  

	    for(int i=0;i<liste.size();i++){
	    	itemizedOverlays.add(new MyItemizedOverlay(drawable,this));
	    	
	    	itemizedOverlays.get(i).addOverlay(new OverlayItem(liste.get(i).getPoint()
	    										,liste.get(i).getNom() , liste.get(i).getVille())
	    										);
	    	mapOverlays.add(itemizedOverlays.get(i));
	    }
	        
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}


}
