package com.digestivethinking.dtcomandas.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Course {
    private int mId;
    private String mName;
    private String mDescription;
    private Double mPrice;
    private CourseType mCourseType;
    private Alergens mAlergens;

    public Course(int id, String name, String description, Double price) {
        mId = id;
        mName = name;
        mDescription = description;
        mPrice = price;

        mCourseType = null;
        mAlergens = new Alergens();
    }

    public Course(JSONObject jsonObject) {
        this(-1, "", "", 0.0);
        try {
            mName = jsonObject.getString("name");
            mDescription = jsonObject.getString("description");
            mPrice = jsonObject.getDouble("price");
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

    public CourseType getCourseType() {
        return mCourseType;
    }

    public void setCourseType(CourseType courseType) {
        mCourseType = courseType;
    }

    public void setAlergens(Alergens alergens) {
        mAlergens = alergens;
    }

    public void addAlergen(Alergen alergen) {

        if (mAlergens.getAlergenById(alergen.getId()) == null) {
            mAlergens.add(alergen);

        }
    }
}
