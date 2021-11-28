package com.example.pedometer;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Initialize variable
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {

        // Initialize shared preferences
        sharedPreferences = context.getSharedPreferences("AppKey", 0);

        // Initialize editor
        editor = sharedPreferences.edit();

        // Apply editor
        editor.apply();
    }

    public Boolean getFlag() {
        return sharedPreferences.getBoolean("KEY_FLAG", false);
    }

    public void setFlag(Boolean flag) {
        editor.putBoolean("KEY_FLAG", flag);
        editor.commit();
    }

    public String getCurrentTime() {
        return sharedPreferences.getString("KEY_TIME", "");
    }

    public void setCurrentTime(String currentTime) {
        editor.putString("KEY_TIME", currentTime);
        editor.commit();
    }

}
