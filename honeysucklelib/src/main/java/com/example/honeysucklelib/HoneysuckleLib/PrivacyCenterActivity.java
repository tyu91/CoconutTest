package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.honeysucklelib.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PrivacyCenterActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_settings);
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        PrivacyPreferenceFragment privacyPreferenceFragment = new PrivacyPreferenceFragment();
        if (intent != null && intent.hasExtra(PrivacyPreferenceFragment.DATA_USE_KEY)) {
            String title;
            String key = intent.getStringExtra(PrivacyPreferenceFragment.DATA_USE_KEY);
            if (key == null) {
                return;
            }
            AnnotationInfo aggregatedAnnotationInfo = HSStatus.getMyAnnotationInfoMap().getAnnotationInfoByID(key);
            if (aggregatedAnnotationInfo == null) {
                return;
            }
            title = aggregatedAnnotationInfo.purposes[0];
            Bundle b = new Bundle();
            b.putString(PrivacyPreferenceFragment.TITLE, title);
            b.putString(PrivacyPreferenceFragment.DATA_USE_KEY, key);

            privacyPreferenceFragment.setArguments(b);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, privacyPreferenceFragment).commit();
    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        Bundle b = new Bundle();
        b.putString(PrivacyPreferenceFragment.TITLE, pref.getTitle().toString());
        b.putString(PrivacyPreferenceFragment.DATA_USE_KEY, pref.getKey());

        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(b);
        fragment.setTargetFragment(caller, 0);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }
}
