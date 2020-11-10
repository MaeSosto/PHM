package com.example.testmenu2.impostazioni;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;

import com.example.testmenu2.R;

public class ImpostazioniFragment extends PreferenceFragmentCompat {

    public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_impostazioni, rootKey);

        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(getContext());
        Boolean switchPref = sharedPref.getBoolean
                (KEY_PREF_EXAMPLE_SWITCH, false);
        Toast.makeText(getContext(), switchPref.toString(),
                Toast.LENGTH_SHORT).show();

    }
}