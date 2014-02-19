package metroBusToulouse.map;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * Défini l'affichage et la réaction des icones station sur la carte.
 */
public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	
	public MyItemizedOverlay(Drawable defaultMarker) {
		   super(boundCenterBottom(defaultMarker));
		 }

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(defaultMarker);
		  mContext = context;
		 }
	
	
	@Override
	public void draw(Canvas canvas, MapView myMapView, boolean shadow) {
		super.draw(canvas, myMapView, shadow);
		// Log.e("MapViewDemo", "MapDemoOverlay...draw");
		// Setup our "brush"/"pencil"/ whatever...
		Paint paint = new Paint();
		paint.setTextSize(16);

		// Create a Point that represents our GPS-Location
		int lat = mOverlays.get(0).getPoint().getLatitudeE6();//43.5 * 1E6;
		int lng = mOverlays.get(0).getPoint().getLongitudeE6();//1.5 * 1E6;
		GeoPoint geoPoint = new GeoPoint(lat, lng);
		Projection myprojection = myMapView.getProjection();
		Point point = new Point();
		myprojection.toPixels(geoPoint, point);
		
		// Setup a color for our location
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.BLACK);
		
		// Draw our name
		if (myMapView.getZoomLevel()>15){
			canvas.drawText(mOverlays.get(0).getTitle(),
				point.x - 5*mOverlays.get(0).getTitle().length()+5, point.y-10, paint);
		}
		paint.setStrokeWidth(0);
		paint.setARGB(255, 0, 0, 0);
		int rayon;
		int zoom = myMapView.getZoomLevel();
		if (zoom>15){
			rayon = 7;
		}
		else if (zoom>4){
			rayon = zoom/3;
		}
		else{
			rayon=3;
		}
		
		canvas.drawCircle(point.x, point.y, rayon, paint);
		// Log.e("MapViewDemo", "MapDemoOverlay...draw
		paint.setStyle(Style.FILL);
	}

		 public void addOverlay(OverlayItem overlay) {
		     mOverlays.add(overlay);
		     populate();
		 }

		 @Override
		 protected OverlayItem createItem(int i) {
		   return mOverlays.get(i);
		 }

		 @Override
		 public int size() {
		   return mOverlays.size();
		 }

		 @Override
		 protected boolean onTap(int index) {
		   final OverlayItem item = mOverlays.get(index);
		   AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		   dialog.setTitle(item.getTitle())
		   .setMessage("Choisir cette station de départ ?")
		    .setPositiveButton("OUI", new DialogInterface.OnClickListener()
		    {
		    	public void onClick(DialogInterface dialog, int whichButton)
		    	{   
		    		MapActivity ma = (MapActivity) mContext;	
					Intent intent = new Intent();
					intent.putExtra("Nom",item.getTitle());
					intent.putExtra("Ville",item.getSnippet());
					ma.setResult(Activity.RESULT_OK, intent);
				    ma.finish();

		    	}
		    })
		    .setNegativeButton("NON", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int whichButton) {
		    	}
		    })
		    .show();
		   
		   return true;
		 }

		}

