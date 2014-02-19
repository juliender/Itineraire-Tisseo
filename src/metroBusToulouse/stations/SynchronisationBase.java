package metroBusToulouse.stations;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Fait tourner la synchro de la liste dans un nouveau thread
 * @author J
 *
 */
public class SynchronisationBase implements Runnable {
		Context context;
		ProgressDialog pd;
		
		public SynchronisationBase(Context c){
			super();
			context = c; 
		}
		@Override
	    public void run() {
			//TODO pd = ProgressDialog.show(this, "", "Synchronisation en cours", true,false);
			int nb=0;
			try {
				nb = BaseStations.mettreDansPrefs();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(nb);
	    }
	    private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	                //pd.dismiss();
	        }
	    };
	}



