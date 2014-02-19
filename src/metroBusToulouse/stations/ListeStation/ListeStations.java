package metroBusToulouse.stations.ListeStation;

import java.util.List;

import metroBusToulouse.choixStation.ChoixStation;
import metroBusToulouse.stations.Station;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;

/**
 * Méthodes d'une liste de stations.
 * @author Julien
 *
 */
public interface ListeStations {

	/**
	 * gere l'affichage de la listview (affichage distances ou compteur 
	 * et rempli le champ de  de la Liste avec les stations utilisables pour l'iti.
	 * @param liste
	 * @param button
	 */
	public abstract void afficherMenuCont(ContextMenu menu, ContextMenuInfo menuInfo);

	public abstract void reagirMenuContextuel(MenuItem item, ChoixStation classe);

	public abstract List<Station> getDeparts();

	public abstract List<Station> getArrivees(Station depart);

	public abstract CharSequence getType();



}
