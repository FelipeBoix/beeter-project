package edu.upc.eetac.dsa.felipeboix.beeter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api.AppException;
import edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api.BeeterAPI;
import edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api.Sting;
import edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api.StingCollection;

public class BeeterMainActivity extends ListActivity {

    private class FetchStingsTask extends
            AsyncTask<Void, Void, StingCollection> {
        private ProgressDialog pd;

        @Override
        protected StingCollection doInBackground(Void... params) {
            StingCollection stings = null;
            try {
                stings = BeeterAPI.getInstance(BeeterMainActivity.this)
                        .getStings();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return stings;
        }

        @Override
        protected void onPostExecute(StingCollection result) {
            ArrayList<Sting> stings = new ArrayList<Sting>(result.getStings());
            for (Sting s : stings) {
                Log.d(TAG, s.getStingid() + "-" + s.getSubject());
            }
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BeeterMainActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }
    private final static String TAG = BeeterMainActivity.class.toString();
    private static final String[] items = { "lorem", "ipsum", "dolor", "sit",
            "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel",
            "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam", "vel",
            "erat", "placerat", "ante", "porttitor", "sodales", "pellentesque",
            "augue", "purus" };
    private ArrayAdapter<String> adapter;//adapter de la lista

    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beeter_main);
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alicia", "alicia"
                        .toCharArray());
            }
        });
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
        (new FetchStingsTask()).execute();
    }
}
