package com.digestivethinking.dtcomandas.model;

import org.json.JSONObject;

public class Alergen {
    private int mId;
    private String mName;
    private String mImage;

    public Alergen(int id, String name, String image) {
        mId = id;
        mName = name;
        mImage = image;
    }

    public Alergen(JSONObject jsonObject) {
        this(-1, "", "");
        try {
            mName = jsonObject.getString("name");
            mImage = jsonObject.getString("image");
            mId = jsonObject.getInt("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }
}
