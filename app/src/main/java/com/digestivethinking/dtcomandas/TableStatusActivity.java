package com.digestivethinking.dtcomandas;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.digestivethinking.dtcomandas.model.Alergen;
import com.digestivethinking.dtcomandas.model.Alergens;
import com.digestivethinking.dtcomandas.model.Course;
import com.digestivethinking.dtcomandas.model.CourseType;
import com.digestivethinking.dtcomandas.model.CourseTypes;
import com.digestivethinking.dtcomandas.model.MainMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TableStatusActivity extends AppCompatActivity {

    private static final String TAG = TableStatusActivity.class.getSimpleName();
    public static final String URL_IMAGENES = "http://comandas.digestivethinking.com/";

    private CourseTypes mainCoursesTypes;
    private Alergens menuAlergens;
    private MainMenu theMenu;

    public TableStatusActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_status);

        if (!isDataSaved()) {
            Log.d(TAG, "Descarga de todo");

            menuDownloader.execute("");

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "Por aquí pasa");
    }

    private boolean isDataSaved() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(this.getString(R.string.Server_data_downloaded), false);
    }

    private AsyncTask<String, Integer, CourseTypes> menuDownloader = new AsyncTask<String, Integer, CourseTypes>() {
        @Override
        protected CourseTypes doInBackground(String... params) {
            return downloadCourseTypes();
        }

        @Override
        protected void onPostExecute(CourseTypes courseTypes) {
            super.onPostExecute(courseTypes);

            mainCoursesTypes = courseTypes;
            alergensDownloader.execute("");
        }
    };

    private AsyncTask<String, Integer, Alergens> alergensDownloader = new AsyncTask<String, Integer, Alergens>() {
        @Override
        protected Alergens doInBackground(String... params) {
            return downloadAlergens();
        }

        @Override
        protected void onPostExecute(Alergens alergens) {
            super.onPostExecute(alergens);

            menuAlergens = alergens;
            coursesDownloader.execute("");
        }
    };

    private AsyncTask<String, Integer, MainMenu> coursesDownloader = new AsyncTask<String, Integer, MainMenu>() {
        @Override
        protected MainMenu doInBackground(String... params) {
            return downloadCourses();
        }

        @Override
        protected void onPostExecute(MainMenu mainMenu) {
            super.onPostExecute(mainMenu);

            theMenu = mainMenu;
            PreferenceManager.getDefaultSharedPreferences(TableStatusActivity.this)
                    .edit()
                    .putBoolean(TableStatusActivity.this.getString(R.string.Server_data_downloaded), true)
                    .apply();

        }
    };

    private CourseTypes downloadCourseTypes() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_course_types));

            JSONArray jsonRoot = new JSONArray(JSONString);
            CourseTypes courseTypes = new CourseTypes();

            for (int i = 0; i < jsonRoot.length(); i++) {

                JSONObject JSONCourseType = jsonRoot.getJSONObject(i);
                String type = JSONCourseType.getString("type");
                String short_type = JSONCourseType.getString("short_type");
                String image = JSONCourseType.getString("image");
                int id = JSONCourseType.getInt("id");

                CourseType courseType = new CourseType(id, type, short_type, image);
                courseTypes.add(courseType);

            }
            return courseTypes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private Alergens downloadAlergens() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_alergenos));

            JSONArray jsonRoot = new JSONArray(JSONString);
            Alergens alergens = new Alergens();

            for (int i = 0; i < jsonRoot.length(); i++) {

                JSONObject JSONAlergen = jsonRoot.getJSONObject(i);
                String name = JSONAlergen.getString("name");
                String image = JSONAlergen.getString("image");
                int id = JSONAlergen.getInt("id");

                Alergen alergen = new Alergen(id, name, image);
                alergens.add(alergen);

            }
            return alergens;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private MainMenu downloadCourses() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_courses));

            JSONArray jsonRoot = new JSONArray(JSONString);
            MainMenu courses = new MainMenu();

            for (int i = 0; i < jsonRoot.length(); i++) {

                JSONObject JSONCourse = jsonRoot.getJSONObject(i);
                String name = JSONCourse.getString("name");
                String description = JSONCourse.getString("description");
                Double price = JSONCourse.getDouble("price");
                int id = JSONCourse.getInt("id");
                int courseType = JSONCourse.getInt("type");
                JSONArray alergens = JSONCourse.getJSONArray("alergenos");

                Course course = new Course(id, name, description, price);
                course.setCourseType(mainCoursesTypes.getCourseTypeById(courseType));

                for (int j = 0; j < alergens.length(); j++) {
                    int alergenId = alergens.getJSONObject(j).getInt("id");
                    Alergen alergen = menuAlergens.getAlergenById(alergenId);
                    course.addAlergen(alergen);
                }

                courses.add(course);
            }
            return courses;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private String universalDownload(String strUrl) {
        URL url = null;
        InputStream input = null;

        try {
            url = new URL(strUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            byte data[] = new byte[1024];
            int downloadedBytes;
            input = con.getInputStream();
            StringBuilder sb = new StringBuilder();
            while ((downloadedBytes = input.read(data)) != -1) {
                sb.append(new String(data, 0, downloadedBytes));
            }

            return sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
