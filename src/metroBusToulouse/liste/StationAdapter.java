package metroBusToulouse.liste;

import java.util.ArrayList;
import java.util.List;

import metroBusToulouse.stations.Station;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StationAdapter extends BaseAdapter{

	private Context context;	
	private ArrayList<StationLayout> listeStationLayout;
	private List<Station> listeStations;
	
	public StationAdapter(Context c, List<Station> listeStations_) {
		context = c;
		listeStationLayout = new ArrayList<StationLayout>();
		listeStations = listeStations_;
	}
	
	@Override
	public int getCount() {
		  return listeStationLayout.size();
	}

	@Override
	public Object getItem(int position) {
		  return listeStationLayout.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StationLayout itemLayout;
		if (listeStationLayout.size()>position){
				itemLayout = listeStationLayout.get(position);
		}
		else{
			itemLayout = new StationLayout(context,listeStations.get(position));
			listeStationLayout.add(itemLayout);
		}
		return itemLayout;
	}

	
	public void maj(List<Station> liste) {
		this.listeStations = liste;
		
		ArrayList<StationLayout> remplace = new ArrayList<StationLayout>();
		ArrayList<Station> existantes = new ArrayList<Station>();
		for (StationLayout i : listeStationLayout){
			existantes.add(i.getStation());
		}
		for (int i=0;i<listeStations.size();i++){
			if (existantes.contains(listeStations.get(i))){
				remplace.add(listeStationLayout.get(i));
			}
			else {
				//ArrayList<String> listeLignes = listeStations.get(i).getListeLignes();
				remplace.add(new StationLayout(context,listeStations.get(i)));
			}
		}
		this.listeStationLayout = remplace;

		
	}
}
