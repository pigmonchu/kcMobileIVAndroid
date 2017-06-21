package com.digestivethinking.dtcomandas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.digestivethinking.dtcomandas.model.Alergen;
import com.digestivethinking.dtcomandas.model.Alergens;
import com.digestivethinking.dtcomandas.model.Course;
import com.digestivethinking.dtcomandas.model.CourseTypes;
import com.digestivethinking.dtcomandas.model.MainMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TableStatusActivity extends AppCompatActivity {

    private static final String TAG = TableStatusActivity.class.getSimpleName();
    public static final String URL_IMAGENES = "http://comandas.digestivethinking.com/";
    public static final int PAQ_SIZE = 1024;

    private boolean isFirstExecution;

    public TableStatusActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_status);

        Log.v(TAG, ">>>>>-----> onCreate <-----<<<<<");

        isFirstExecution = !PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(this.getString(R.string.Server_data_downloaded), false);;

        if (MainMenu.getInstance().getCount() == 0) {
            Log.v(TAG, isFirstExecution ? ">>>>>-----> Descargando de la nube <-----<<<<<" : ">>>>>-----> Cargando datos de local <-----<<<<<");
            menuMaker.execute(isFirstExecution);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.v(TAG, "Por aquí pasa");
    }

    private AsyncTask<Boolean, Integer, CourseTypes> menuMaker = new AsyncTask<Boolean, Integer, CourseTypes>() {
        @Override
        protected CourseTypes doInBackground(Boolean... params) {

            if (params[0]) { //isFirstExecution
                return downloadAndSaveCourseTypes();
            } else {
                return loadLocalCourseTypes();
            }

        }

        @Override
        protected void onPostExecute(CourseTypes courseTypes) {
            super.onPostExecute(courseTypes);

            alergensMaker.execute(isFirstExecution);
        }
    };

    private AsyncTask<Boolean, Integer, Alergens> alergensMaker = new AsyncTask<Boolean, Integer, Alergens>() {
        @Override
        protected Alergens doInBackground(Boolean... params) {
            if (params[0]) { // isFirstExecution
                return downloadAndSaveAlergens();
            } else {
                return loadLocalAlergens();
            }
        }

        @Override
        protected void onPostExecute(Alergens alergens) {
            super.onPostExecute(alergens);

            courseListMaker.execute(isFirstExecution);
        }
    };

    private AsyncTask<Boolean, Integer, MainMenu> courseListMaker = new AsyncTask<Boolean, Integer, MainMenu>() {
        @Override
        protected MainMenu doInBackground(Boolean... params) {
            if (params[0]) { // isFirstExecution
                return downloadAndSaveCourses();
            } else {
                return loadLocalCourses();
            }
        }

        @Override
        protected void onPostExecute(MainMenu mainMenu) {
            super.onPostExecute(mainMenu);

            PreferenceManager.getDefaultSharedPreferences(TableStatusActivity.this)
                    .edit()
                    .putBoolean(TableStatusActivity.this.getString(R.string.Server_data_downloaded), true)
                    .apply();
        }
    };

    private CourseTypes downloadAndSaveCourseTypes() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_course_types));
            saveFile(this.getString(R.string.File_course_types), JSONString);
            CourseTypes courseTypes = CourseTypes.getInstance();
            courseTypes.processJSON(JSONString);

            return courseTypes;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private CourseTypes loadLocalCourseTypes() {
        try {
            String JSONString = readFile(this.getString(R.string.File_course_types));
            CourseTypes courseTypes = CourseTypes.getInstance();
            courseTypes.processJSON(JSONString);

            return courseTypes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Alergens downloadAndSaveAlergens() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_alergenos));
            saveFile(this.getString(R.string.File_alergenos), JSONString);
            Alergens alergens = Alergens.getInstance();
            alergens.processJSONAlergens(JSONString);
            return alergens;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Alergens loadLocalAlergens() {
        try {
            String JSONString = readFile(this.getString(R.string.File_alergenos));
            Alergens alergens = Alergens.getInstance();
            alergens.processJSONAlergens(JSONString);
            return alergens;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private MainMenu downloadAndSaveCourses() {
        try {
            String JSONString = universalDownload(this.getString(R.string.Url_courses));
            saveFile(this.getString(R.string.File_courses), JSONString);
            return processJSONCourses(JSONString);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private MainMenu loadLocalCourses() {
        try {
            String JSONString = readFile(this.getString(R.string.File_courses));
            return processJSONCourses(JSONString);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private MainMenu processJSONCourses(String JSONString) {
        try {
            JSONArray jsonRoot = new JSONArray(JSONString);
            MainMenu courses = MainMenu.getInstance();

            for (int i = 0; i < jsonRoot.length(); i++) {

                JSONObject JSONCourse = jsonRoot.getJSONObject(i);

                Course course = new Course(JSONCourse);

                int courseType = JSONCourse.getInt("type");
                JSONArray alergens = JSONCourse.getJSONArray("alergenos");

                course.setCourseType(CourseTypes.getInstance().getCourseTypeById(courseType));

                for (int j = 0; j < alergens.length(); j++) {
                    int alergenId = alergens.getJSONObject(j).getInt("id");
                    Alergen alergen = Alergens.getInstance().getAlergenById(alergenId);
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
            byte data[] = new byte[PAQ_SIZE];
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

    private void saveFile(String name, String jsonText) {
        try {

            FileOutputStream fos = openFileOutput(name, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeBytes(jsonText);
            fos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private String readFile(String name) {
        byte data[] = new byte[PAQ_SIZE];
        int downloadedBytes;

        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = openFileInput(name);
            DataInputStream dis = new DataInputStream(fis);

            while ((downloadedBytes = dis.read(data)) != -1) {
                sb.append(new String(data, 0, downloadedBytes));
            }

            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
}
