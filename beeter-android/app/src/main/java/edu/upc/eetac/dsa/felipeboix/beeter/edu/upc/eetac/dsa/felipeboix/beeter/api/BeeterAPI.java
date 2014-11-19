package edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api;

/**
 * Created by Felipe on 17/11/2014.
 */
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Map;
        import java.util.Properties;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.content.Context;
        import android.content.res.AssetManager;
        import android.util.Log;

public class BeeterAPI {
    private final static String TAG = BeeterAPI.class.getName();
    private static BeeterAPI instance = null;
    private URL url;

    private BeeterRootAPI rootAPI = null;

    private BeeterAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();// Cargar el asset config.properties
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("beeter.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static BeeterAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new BeeterAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new BeeterRootAPI();// modelo que guarda la respuesta a la raiz del servicio "/"
        HttpURLConnection urlConnection = null;//conexxion http
        try {
            urlConnection = (HttpURLConnection) url.openConnection();//abrir cnx
            urlConnection.setRequestMethod("GET");//metodo get
            urlConnection.setDoInput(true);//leer
            urlConnection.connect();//hace la petición
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Beeter API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();// con esto lee en resp. y se genera en un stringbuilder
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());// creas JSON a partir de esa cadena
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Beeter API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Beeter Root API");
        }

    }

    public StingCollection getStings() throws AppException {
        Log.d(TAG, "getStings()");
        StingCollection stings = new StingCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("stings").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Beeter API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, stings.getLinks());

            stings.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            stings.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonStings = jsonObject.getJSONArray("stings");
            for (int i = 0; i < jsonStings.length(); i++) {
                Sting sting = new Sting();
                JSONObject jsonSting = jsonStings.getJSONObject(i);
                sting.setAuthor(jsonSting.getString("author"));
                sting.setStingid(jsonSting.getInt("stingid"));
                sting.setLastModified(jsonSting.getLong("lastModified"));
                sting.setCreationTimestamp(jsonSting.getLong("creationTimestamp"));
                sting.setSubject(jsonSting.getString("subject"));
                sting.setUsername(jsonSting.getString("username"));
                jsonLinks = jsonSting.getJSONArray("links");
                parseLinks(jsonLinks, sting.getLinks());
                stings.getStings().add(sting);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Beeter API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Beeter Root API");
        }

        return stings;
    }

    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
    //pasamos array y un mapa
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");//
            for (String s : rels)
                map.put(s, link);
        }
    }
}