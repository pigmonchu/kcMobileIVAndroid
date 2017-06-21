package com.digestivethinking.dtcomandas.model;

import java.util.LinkedList;

public class MainMenu {
    private static MainMenu mInstance;

    private LinkedList<Course> mMainMenu;

    public MainMenu() {
        mMainMenu = new LinkedList<>();
    }

    public static MainMenu getInstance() {
        if (mInstance == null) {
            mInstance = new MainMenu();
        }

        return mInstance;
    }

    public Course getCourse(int index) {
        return mMainMenu.get(index);
    }

    public Course getCourseById(int id) {
        for (int i = 0; i < mMainMenu.size(); i++) {
            Course item = mMainMenu.get(i);
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public MainMenu getCoursesByType(int type) {
        MainMenu menuByType = new MainMenu();

        for (int i = 0; i < mMainMenu.size(); i++) {
            Course item = mMainMenu.get(i);
            if (item.getCourseType().getId() == type) {
                menuByType.add(item);
            }
        }
        return menuByType;
    }

    public LinkedList<Course> getMainMenu() {
        return mMainMenu;
    }

    public int getCount() {
        return mMainMenu.size();
    }

    public void add(Course Course) {
        mMainMenu.add(Course);
    }


}
