package com.digestivethinking.dtcomandas.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class Alergens {

    private static Alergens mInstance;

    private LinkedList<Alergen> mAlergens;

    public Alergens() {
        mAlergens = new LinkedList<>();
    }

    public static Alergens getInstance() {
        if (mInstance == null) {
            mInstance = new Alergens();
        }

        return mInstance;
    }

    public Alergen getAlergen(int index) {
        return mAlergens.get(index);
    }

    public Alergen getAlergenById(int id) {
        for (int i = 0; i < mAlergens.size(); i++) {
            Alergen item = mAlergens.get(i);
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }


    public LinkedList<Alergen> getAlergens() {
        return mAlergens;
    }

    public int getCount() {
        return mAlergens.size();
    }

    public void add(Alergen Alergen) {
        mAlergens.add(Alergen);
    }

    public void processJSONAlergens(String JSONString) {
        try {
            JSONArray jsonRoot = new JSONArray(JSONString);
            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject JSONAlergen = jsonRoot.getJSONObject(i);
                this.add(new Alergen(JSONAlergen));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
