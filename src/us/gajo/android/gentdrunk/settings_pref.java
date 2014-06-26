package us.gajo.android.gentdrunk;
 
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

 
public class settings_pref extends PreferenceActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.layout.settings);
 
                Preference customPref = (Preference) findPreference("newep_notif_cb");
                
               
                customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    
                                        public boolean onPreferenceClick(Preference preference) {
                                                //Toast.makeText(getBaseContext(),"The custom preference has been clicked",Toast.LENGTH_LONG).show();
                                                
                                                SharedPreferences customSharedPreference = getSharedPreferences(
                                                                "GDPrefsFile", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = customSharedPreference
                                                                .edit();
                                                
                                                CheckBoxPreference newep = (CheckBoxPreference) findPreference("newep_notif_cb");
                                                if(newep.isChecked())
                                                {

	                                                editor.putBoolean("newep_notify", true);
	                                              //  Toast.makeText(getBaseContext(),"true" ,Toast.LENGTH_LONG).show();
                                                
                                                }
                                                else
                                                {

	                                                editor.putBoolean("newep_notify", false);
	                                               // Toast.makeText(getBaseContext(),"false" ,Toast.LENGTH_LONG).show();
	                                                
                                                }
        
                                                editor.commit();
                                            
                                                
                                                return true;
                                        }
 
                                });
                                
        }
}