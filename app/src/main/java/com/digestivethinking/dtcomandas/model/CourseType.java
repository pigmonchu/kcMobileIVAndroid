package com.digestivethinking.dtcomandas.model;


public class CourseType {
    private long mId;
    private String mType;
    private String mShortType;
    private String mImage;

    public CourseType(long id, String type, String shortType, String image) {
        mId = id;
        mType = type;
        mShortType = shortType;
        mImage = image;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getShortType() {
        return mShortType;
    }

    public void setShortType(String shortType) {
        mShortType = shortType;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }
}
