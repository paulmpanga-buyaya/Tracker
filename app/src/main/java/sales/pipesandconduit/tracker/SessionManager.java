package sales.pipesandconduit.tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Shared Pref
    SharedPreferences pref;
    SharedPreferences customerInfo;
    //Editor for shared pref
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String pref_name = "MgmtPrefs";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String key_name = "name";
    public static final String key_username = "username";
    public static final String key_stationID = "stationID";
    public static final String key_station_name = "stationName";

    public static final String customerKey = "customer_key";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(pref_name,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String username, String stationID, String stationName){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(key_name, name);
        editor.putString(key_username, username);
        editor.putString(key_stationID, stationID);
        editor.putString(key_station_name, stationName);
        editor.commit();
        Intent intent = new Intent(_context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public void createCustomerKey(String customerKey){
        editor.putString(customerKey, customerKey);
        editor.commit();
    }

    public HashMap<String, String> getCustomerKey(){
        HashMap<String, String> key = new HashMap<String, String>();
        key.put(customerKey, pref.getString(customerKey, null));
        return key;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(key_name, pref.getString(key_name, null));
        user.put(key_username, pref.getString(key_username, null));
        user.put(key_stationID, pref.getString(key_stationID, null));
        user.put(key_station_name, pref.getString(key_station_name, null));
        return user;
    }

    public void checkLogin(){
        if (!this.isLoggedIn()){
            //Intent intent = new Intent(_context, SignInActivity.class);
            Intent intent = new Intent(_context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        //Intent intent = new Intent(_context, SignInActivity.class);
        Intent intent = new Intent(_context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN,false);
    }

}
