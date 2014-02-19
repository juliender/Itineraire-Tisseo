package metroBusToulouse.liste;

import java.util.ArrayList;

import metroBusToulouse.stations.Station;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
/**
 * Décrit graphiquement une station de la liste.
 * @author Julien
 *
 */
class StationLayout extends LinearLayout{
	Context context;
	private Station station;
    private TextView mTitle;
    private ArrayList<Button> mBoutons;   
	
	
    public StationLayout(final Context context_,Station station_) {
        super(context_);
        context = context_;
        setStation(station_);

        this.setOrientation(VERTICAL);
        
        mTitle = new TextView(context);
        mTitle.setText(getStation().getNom());
        mTitle.setTextSize(22);
        mTitle.setTextColor(Color.WHITE);
        addView(mTitle, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        TableLayout table = new TableLayout(context);
        TableRow boutonsLayout = new TableRow(context);
        TableRow boutonsLayout2 = new TableRow(context);

        // Création d'une liste de boutons pour afficher les lignes.
        mBoutons = new ArrayList<Button>();
        for (int i=0;i<getStation().getListeLignes().size();i++){
        	Button mBouton = new Button(context);
        	mBouton.setFocusable(false);
        	mBouton.setTextSize(14);
        	mBouton.setTextColor(Color.WHITE);
        	mBouton.setHighlightColor(Color.BLUE);
        	mBouton.setPadding(8, 6, 8, 6);
        	mBouton.setBackgroundColor(Color.DKGRAY);
        	mBouton.setEnabled(false);
        	mBoutons.add(mBouton);
        	mBoutons.get(i).setText(getStation().getListeLignes().get(i));
        	
        	LinearLayout.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	params.setMargins(0, 0, 4, 0);
        	
        	 
        	if (i>5){
        		boutonsLayout.
            	addView(mBoutons.get(i), params);

        	}
        	else {
        		boutonsLayout2.
            	addView(mBoutons.get(i), params);
        	}
        }
        
        //table.
        addView(boutonsLayout2, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        addView(boutonsLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

    	//addView(table, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }
   
    public void setTitre(String titre) {
        mTitle.setText(titre);
    }

	public void setStation(Station station) {
		this.station = station;
	}

	public Station getStation() {
		return station;
	}
}
