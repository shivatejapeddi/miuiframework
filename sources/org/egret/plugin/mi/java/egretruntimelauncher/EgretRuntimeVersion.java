package org.egret.plugin.mi.java.egretruntimelauncher;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class EgretRuntimeVersion {
    private static final String JSON_LIBRARY = "library";
    private static final String JSON_RUNTIME = "runtime";
    private static final String JSON_URL = "url";
    private ArrayList<Library> libraryList = new ArrayList();

    public void fromString(String content) {
        ArrayList<Library> result = new ArrayList();
        try {
            JSONObject runtime = new JSONObject(new JSONTokener(content)).getJSONObject("runtime");
            String url = runtime.getString("url");
            JSONArray libs = runtime.getJSONArray(JSON_LIBRARY);
            for (int i = 0; i < libs.length(); i++) {
                result.add(new Library((JSONObject) libs.get(i), url));
            }
            this.libraryList = result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Library> getLibraryList() {
        return this.libraryList;
    }
}
