package com.squattingsasquatches.lucidity.activities;


import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.R.array;
import com.squattingsasquatches.lucidity.objects.University;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

public class SettingsScreen extends PreferenceActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setPreferenceScreen(createPreferenceHierarchy());
	    }

	    private PreferenceScreen createPreferenceHierarchy() {
	        // Root
	        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

	        // Inline preferences
	        PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
	        inlinePrefCat.setTitle("Preferences");
	        root.addPreference(inlinePrefCat);
//
////	        // Checkbox preference
//	        CheckBoxPreference checkboxPref = new CheckBoxPreference(this);
//	        checkboxPref.setKey("checkbox_preference");
//	        checkboxPref.setTitle("Checkbox Title");
//	        checkboxPref.setSummary("Checkbox Summary");
//	        inlinePrefCat.addPreference(checkboxPref);
//
	        // Switch preference
//	        SwitchPreference switchPref = new SwitchPreference(this);
//	        switchPref.setKey("switch_preference");
//	        switchPref.setTitle(R.string.title_switch_preference);
//	        switchPref.setSummary(R.string.summary_switch_preference);
//	        inlinePrefCat.addPreference(switchPref);
//
//	        // Dialog based preferences
//	        PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);
//	        dialogBasedPrefCat.setTitle(R.string.dialog_based_preferences);
//	        root.addPreference(dialogBasedPrefCat);
//
//	        // Edit text preference
//	        EditTextPreference editTextPref = new EditTextPreference(this);
//	        editTextPref.setDialogTitle(R.string.dialog_title_edittext_preference);
//	        editTextPref.setKey("edittext_preference");
//	        editTextPref.setTitle(R.string.title_edittext_preference);
//	        editTextPref.setSummary(R.string.summary_edittext_preference);
//	        dialogBasedPrefCat.addPreference(editTextPref);
//
	        
	        ListPreference listPref = new ListPreference(this);
	        listPref.setEntries(R.array.master_servers);
	        listPref.setEntryValues(R.array.master_servers);
	        listPref.setDialogTitle("Select Master Server");
	        listPref.setKey("master_server");
	        listPref.setTitle("Master Server");
	        listPref.setSummary("Select a different master server if the primary is having trouble connecting.");
	        inlinePrefCat.addPreference(listPref);
	        
	        EditTextPreference editTextPref = new EditTextPreference(this);
	        editTextPref.setDialogTitle("Set Master Server Port");
	        editTextPref.setKey("master_server_port");
	        editTextPref.setTitle("Master Server Port");
	        editTextPref.setSummary("Use this to set the master server port if it is something other than 80.");
	        inlinePrefCat.addPreference(editTextPref);
//
//	        // Launch preferences
//	        PreferenceCategory launchPrefCat = new PreferenceCategory(this);
//	        launchPrefCat.setTitle(R.string.launch_preferences);
//	        root.addPreference(launchPrefCat);
//
//	        /*
//	         * The Preferences screenPref serves as a screen break (similar to page
//	         * break in word processing). Like for other preference types, we assign
//	         * a key here so that it is able to save and restore its instance state.
//	         */
//	        // Screen preference
//	        PreferenceScreen screenPref = getPreferenceManager().createPreferenceScreen(this);
//	        screenPref.setKey("screen_preference");
//	        screenPref.setTitle(R.string.title_screen_preference);
//	        screenPref.setSummary(R.string.summary_screen_preference);
//	        launchPrefCat.addPreference(screenPref);
//
//	        /*
//	         * You can add more preferences to screenPref that will be shown on the
//	         * next screen.
//	         */
//
//	        // Example of next screen toggle preference
//	        CheckBoxPreference nextScreenCheckBoxPref = new CheckBoxPreference(this);
//	        nextScreenCheckBoxPref.setKey("next_screen_toggle_preference");
//	        nextScreenCheckBoxPref.setTitle(R.string.title_next_screen_toggle_preference);
//	        nextScreenCheckBoxPref.setSummary(R.string.summary_next_screen_toggle_preference);
//	        screenPref.addPreference(nextScreenCheckBoxPref);
//
//	        // Intent preference
//	        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
//	        intentPref.setIntent(new Intent().setAction(Intent.ACTION_VIEW)
//	                .setData(Uri.parse("http://www.android.com")));
//	        intentPref.setTitle(R.string.title_intent_preference);
//	        intentPref.setSummary(R.string.summary_intent_preference);
//	        launchPrefCat.addPreference(intentPref);
//
//	        // Preference attributes
//	        PreferenceCategory prefAttrsCat = new PreferenceCategory(this);
//	        prefAttrsCat.setTitle(R.string.preference_attributes);
//	        root.addPreference(prefAttrsCat);
//
//	        // Visual parent toggle preference
//	        CheckBoxPreference parentCheckBoxPref = new CheckBoxPreference(this);
//	        parentCheckBoxPref.setTitle(R.string.title_parent_preference);
//	        parentCheckBoxPref.setSummary(R.string.summary_parent_preference);
//	        prefAttrsCat.addPreference(parentCheckBoxPref);
//
//	        // Visual child toggle preference
//	        // See res/values/attrs.xml for the <declare-styleable> that defines
//	        // TogglePrefAttrs.
//	        TypedArray a = obtainStyledAttributes(R.styleable.TogglePrefAttrs);
//	        CheckBoxPreference childCheckBoxPref = new CheckBoxPreference(this);
//	        childCheckBoxPref.setTitle(R.string.title_child_preference);
//	        childCheckBoxPref.setSummary(R.string.summary_child_preference);
//	        childCheckBoxPref.setLayoutResource(
//	                a.getResourceId(R.styleable.TogglePrefAttrs_android_preferenceLayoutChild,
//	                        0));
//	        prefAttrsCat.addPreference(childCheckBoxPref);
//	        a.recycle();

	        return root;
	    }
}
