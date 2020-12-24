package com.propertyeager.realestate.Utils;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;


public class SessionManager {
    SharedPreferences pref;

    Context context;

    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;

    private final static String PREF_NAME = "JhongaraAppSessions";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }




    private static final String IS_CLIENT_LOGIN = "IsClientLoggedIn";
    public static final String KEY_CLIENT_ID = "id";
    public static final String KEY_CLIENT_NAME = "name";
    public static final String KEY_CLIENT_EMAIL = "email";
    public static final String KEY_CLIENT_PHONE = "phone";
    public static final String KEY_CLIENT_PASSWORD = "password";
    public static final String KEY_CLIENT_REMEMBER_EMAIL = "remember_client_email";
    public static final String KEY_CLIENT_REMEMBER_PASSWORD = "remember_client_password";


    private static final String IS_AGENT_LOGIN = "IsAgentLoggedIn";
    public static final String KEY_AGENT_ID = "id";
    public static final String KEY_AGENT_NAME = "name";
    public static final String KEY_AGENT_EMAIL = "email";
    public static final String KEY_AGENT_PHONE = "phone";
    public static final String KEY_AGENT_IMAGE = "image";
    public static final String KEY_AGENT_PASSWORD = "password";
    public static final String KEY_AGENCY_NAME = "agency_name";
    public static final String KEY_AGENCY_DESCRIPTION = "agency_description";
    public static final String KEY_AGENT_REMEMBER_EMAIL = "remember_agent_email";
    public static final String KEY_AGENT_REMEMBER_PASSWORD = "remember_agent_password";


    public void CreateClientSession(String id, String name, String email, String phone, String password) {

        editor.putBoolean(IS_CLIENT_LOGIN, true);
        editor.putString(KEY_CLIENT_ID, id);
        editor.putString(KEY_CLIENT_NAME, name);
        editor.putString(KEY_CLIENT_EMAIL, email);
        editor.putString(KEY_CLIENT_PHONE, phone);
        editor.putString(KEY_CLIENT_PASSWORD, password);
        editor.putString(KEY_CLIENT_REMEMBER_EMAIL, email);
        editor.putString(KEY_CLIENT_REMEMBER_PASSWORD, password);
        editor.commit();
    }

    public HashMap<String, String> getAgent() {
        HashMap<String, String> admin = new HashMap<>();
        admin.put(KEY_AGENT_ID, pref.getString(KEY_AGENT_ID, ""));
        admin.put(KEY_AGENT_NAME, pref.getString(KEY_AGENT_NAME, ""));
        admin.put(KEY_AGENT_EMAIL, pref.getString(KEY_AGENT_EMAIL, ""));
        admin.put(KEY_AGENT_PHONE, pref.getString(KEY_AGENT_PHONE, ""));
        admin.put(KEY_AGENT_IMAGE, pref.getString(KEY_AGENT_IMAGE, ""));
        admin.put(KEY_AGENT_PASSWORD, pref.getString(KEY_AGENT_PASSWORD, ""));
        admin.put(KEY_AGENCY_NAME, pref.getString(KEY_AGENCY_NAME, ""));
        admin.put(KEY_AGENCY_DESCRIPTION, pref.getString(KEY_AGENCY_DESCRIPTION, ""));

        return admin;
    }

    public HashMap<String, String> getClient() {
        HashMap<String, String> admin = new HashMap<>();
        admin.put(KEY_CLIENT_ID, pref.getString(KEY_CLIENT_ID, ""));
        admin.put(KEY_CLIENT_NAME, pref.getString(KEY_CLIENT_NAME, ""));
        admin.put(KEY_CLIENT_EMAIL, pref.getString(KEY_CLIENT_EMAIL, ""));
        admin.put(KEY_CLIENT_PHONE, pref.getString(KEY_CLIENT_PHONE, ""));
        admin.put(KEY_CLIENT_PASSWORD, pref.getString(KEY_CLIENT_PASSWORD, ""));

        return admin;
    }


    public void CreateAgentSession(String id, String name, String email, String phone, String password, String image, String agency_name, String agency_description) {

        editor.putBoolean(IS_AGENT_LOGIN, true);
        editor.putString(KEY_AGENT_ID, id);
        editor.putString(KEY_AGENT_NAME, name);
        editor.putString(KEY_AGENT_EMAIL, email);
        editor.putString(KEY_AGENT_PHONE, phone);
        editor.putString(KEY_AGENT_PASSWORD, password);
        editor.putString(KEY_AGENT_IMAGE, image);
        editor.putString(KEY_AGENCY_NAME, agency_name);
        editor.putString(KEY_AGENCY_DESCRIPTION, agency_description);
        editor.commit();
    }

    public void RememberAgentCredencial(String remember_agent_email, String remember_agent_password) {

        editor.putString(KEY_AGENT_EMAIL, remember_agent_email);
        editor.putString(KEY_AGENT_PASSWORD, remember_agent_password);
        editor.commit();
    }

    public void RememberClientCredencial(String remember_email, String remember_password) {

        editor.putString(KEY_CLIENT_EMAIL, remember_email);
        editor.putString(KEY_CLIENT_PASSWORD, remember_password);
        editor.commit();
    }

    public void AgentProfilePicChanged(String path){
        editor.putString(KEY_AGENT_IMAGE,path);
        editor.commit();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        //  Intent i = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        //   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        //  context.startActivity(i);
        //  ((LoginActivity)context).finish();


    }

    public void logoutGuestUser() {

        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        //   Intent i = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        //    context.startActivity(i);
        //  ((UserDashboardActivity)context).finish();


    }


    public String GetAgentId() {

        return pref.getString(KEY_AGENT_ID, "");
    }

    public String GetRememberAgentEmail() {
        return pref.getString(KEY_AGENT_REMEMBER_EMAIL, "");
    }

    public String GetRememberAgentPassword() {
        return pref.getString(KEY_AGENT_REMEMBER_PASSWORD, "");
    }

    public String GetRememberClientEmail() {
        return pref.getString(KEY_CLIENT_REMEMBER_EMAIL, "");
    }

    public String GetRememberClientPassword() {
        return pref.getString(KEY_CLIENT_REMEMBER_PASSWORD, "");
    }

  /*  public String GetAccessToken(){
        return pref.getString(KEY_AGENT_ACCESS_TOKEN,"");
    }*/

    public String GetAgentName() {
        return pref.getString(KEY_AGENT_NAME, "");
    }

   /* public String GetAgentProfileImage(){
        return pref.getString(KEY_AGENT_IMAGE,"");
    }*/

    public void logoutAgent() {

        editor.remove(KEY_AGENT_NAME);
        //editor.remove(KEY_AGENT_ACCESS_TOKEN);
        editor.remove(KEY_AGENT_ID);
        //  editor.remove(KEY_AGENT_BRANCH_ID);
        //  editor.remove(KEY_CUSTOMER_FORM_ID);
        //  editor.remove(KEY_AGENT_IMAGE);

        editor.putBoolean(IS_AGENT_LOGIN, false);
        editor.remove(KEY_AGENT_EMAIL);
        editor.remove(KEY_AGENT_IMAGE);
        editor.remove(KEY_AGENT_PASSWORD);
        editor.commit();
    }

    public void logoutClient() {

        editor.remove(KEY_CLIENT_EMAIL);
        editor.remove(KEY_CLIENT_ID);

        editor.putBoolean(IS_CLIENT_LOGIN, false);
        editor.remove(KEY_CLIENT_NAME);
        editor.remove(KEY_AGENT_IMAGE);
        editor.remove(KEY_AGENT_PASSWORD);
        editor.commit();

        // After logout redirect user to Loing Activity
        //   Intent i = new Intent(context, LoginActivity.class);

        // Closing all the Activities
        //     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        // context.startActivity(i);
        //  ((AdminDashboard)context).finish();


    }


    // Get Login State
    public boolean isClientLoggedIn() {
        return pref.getBoolean(IS_CLIENT_LOGIN, false);
    }


    public boolean isAgentLoggedin() {
        return pref.getBoolean(IS_AGENT_LOGIN, false);
    }

}

