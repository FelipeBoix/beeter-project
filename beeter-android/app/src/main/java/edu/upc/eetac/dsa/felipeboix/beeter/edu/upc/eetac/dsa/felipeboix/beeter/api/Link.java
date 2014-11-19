package edu.upc.eetac.dsa.felipeboix.beeter.edu.upc.eetac.dsa.felipeboix.beeter.api;

/**
 * Created by Felipe on 17/11/2014.
 */
import java.util.HashMap;
import java.util.Map;

public class Link {

    private String target;
    private Map<String, String> parameters;

    public Link() {
        parameters = new HashMap<String, String>();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
