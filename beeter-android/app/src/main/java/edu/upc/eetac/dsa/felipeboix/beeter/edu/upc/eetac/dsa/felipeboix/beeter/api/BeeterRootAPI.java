package edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api;

/**
 * Created by Felipe on 17/11/2014.
 */
import java.util.HashMap;
import java.util.Map;

public class BeeterRootAPI {

    private Map<String, Link> links;

    public BeeterRootAPI() {
        links = new HashMap<String, Link>();
    }

    public Map<String, Link> getLinks() {
        return links;
    }

}
