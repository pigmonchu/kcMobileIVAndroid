package com.digestivethinking.dtcomandas.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class CourseTypes {
    private static CourseTypes mInstance;

    private LinkedList<CourseType> mCourseTypes;

    public CourseTypes() {
        mCourseTypes = new LinkedList<>();
    }

    public static CourseTypes getInstance() {
        if (mInstance == null) {
            mInstance = new CourseTypes();
        }

        return mInstance;
    }

    public LinkedList<CourseType> getCourseTypes() {
        return mCourseTypes;
    }

    public int getCount() {
        return mCourseTypes.size();
    }

    public void add(CourseType courseType) {
        mCourseTypes.add(courseType);
    }

    public CourseType getCourseType(int index) {
        return mCourseTypes.get(index);
    }

    public CourseType getCourseTypeById(int id) {
        for (int i = 0; i < mCourseTypes.size(); i++) {
            CourseType item = mCourseTypes.get(i);
            if (item.getId() == id) {
                return item;
            }
        }
        return null;

    }

    public void processJSON(String JSONString) {
        try {
            JSONArray jsonRoot = new JSONArray(JSONString);

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject JSONCourseType = jsonRoot.getJSONObject(i);
                this.add(new CourseType(JSONCourseType));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
