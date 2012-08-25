/* 
 * AndroBOINC - BOINC Manager for Android
 * Copyright (C) 2010, Pavol Michalec
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package sk.boinc.androboinc;

import sk.boinc.androboinc.debug.Logging;
import sk.boinc.androboinc.util.NetStatsStorage;
import sk.boinc.androboinc.util.PreferenceName;
import sk.boinc.androboinc.util.ScreenOrientationHandler;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class AppPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String TAG = "AppPreferencesActivity";

	private static final int DIALOG_NETSTATS_DISCLAIMER = 0;

	private ScreenOrientationHandler mScreenOrientation;

	private StringBuilder mAuxString = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Screen orientation handler
		mScreenOrientation = new ScreenOrientationHandler(this);

		// Initializes the preference activity.
		addPreferencesFromResource(R.xml.preferences);

		ListPreference listPref;
		CheckBoxPreference cbPref;

		// Screen Rotation
		listPref = (ListPreference)findPreference(PreferenceName.SCREEN_ORIENTATION);
		listPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				ListPreference listPref = (ListPreference)preference;
				int idx = listPref.findIndexOfValue((String)newValue);
				CharSequence[] allDesc = listPref.getEntries();
				listPref.setSummary(allDesc[idx]);
				return true;
			}
		});

		// Automatic refresh interval for WiFi
		listPref = (ListPreference)findPreference(PreferenceName.AUTO_UPDATE_WIFI);
		listPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				ListPreference listPref = (ListPreference)preference;
				int idx = listPref.findIndexOfValue((String)newValue);
				CharSequence[] allDesc = listPref.getEntries();
				if (idx == 0) {
					// Automatic updates are disabled
					listPref.setSummary(R.string.disabled);
				}
				else {
					listPref.setSummary(getString(R.string.prefAutoUpdateIntervalSummary) + " " + allDesc[idx]);
				}
				return true;
			}
		});

		// Automatic refresh interval for Mobile
		listPref = (ListPreference)findPreference(PreferenceName.AUTO_UPDATE_MOBILE);
		listPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				ListPreference listPref = (ListPreference)preference;
				int idx = listPref.findIndexOfValue((String)newValue);
				CharSequence[] allDesc = listPref.getEntries();
				if (idx == 0) {
					// Automatic updates are disabled
					listPref.setSummary(R.string.disabled);
				}
				else {
					listPref.setSummary(getString(R.string.prefAutoUpdateIntervalSummary) + " " + allDesc[idx]);
				}
				return true;
			}
		});

		cbPref = (CheckBoxPreference)findPreference(PreferenceName.COLLECT_STATS);
		cbPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				CheckBoxPreference cbPref = (CheckBoxPreference)preference;
				if (cbPref.isChecked()) {
					// It was enabled and it is going to be disabled
					// Nothing to do now, just disable it
					return true;
				}
				else {
					// It was disabled and it's going to be enabled
					// Display also disclaimer (netstats not reliable) if applicable
					SharedPreferences netStats = getSharedPreferences(NetStatsStorage.NET_STATS_FILE, MODE_PRIVATE);
					boolean warningDisabled = netStats.getBoolean(NetStatsStorage.NET_STATS_WARNING, false);
					if (warningDisabled) {
						// Warning has been disabled; just turn on statistics
						return true;
					}
					else {
						// Warning active - show dialog first, do NOT turn on statistics yet
						// (Back button of the dialog can still leave netstats turned off)
						showDialog(DIALOG_NETSTATS_DISCLAIMER);
						return false;
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// We are in foreground now

		mScreenOrientation.setOrientation();

		ListPreference listPref;
		int auiIdx;

		// Screen Rotation
		listPref = (ListPreference)findPreference(PreferenceName.SCREEN_ORIENTATION);
		listPref.setSummary(listPref.getEntry());

		// Automatic refresh interval for WiFi
		listPref = (ListPreference)findPreference(PreferenceName.AUTO_UPDATE_WIFI);
		auiIdx = listPref.findIndexOfValue(listPref.getValue());
		if (auiIdx == 0) {
			// Automatic updates are disabled
			listPref.setSummary(R.string.disabled);
		}
		else {
			listPref.setSummary(getString(R.string.prefAutoUpdateIntervalSummary) + " " + listPref.getEntry());
		}

		// Automatic refresh interval for Mobile
		listPref = (ListPreference)findPreference(PreferenceName.AUTO_UPDATE_MOBILE);
		auiIdx = listPref.findIndexOfValue(listPref.getValue());
		if (auiIdx == 0) {
			// Automatic updates are disabled
			listPref.setSummary(R.string.disabled);
		}
		else {
			listPref.setSummary(getString(R.string.prefAutoUpdateIntervalSummary) + " " + listPref.getEntry());
		}

		// Register to receive changes in statistics
		SharedPreferences netStats = getSharedPreferences(NetStatsStorage.NET_STATS_FILE, MODE_PRIVATE);
		netStats.registerOnSharedPreferenceChangeListener(this);
		// And update current numbers in display of network statistics
		updateNetworkStats();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences netStats = getSharedPreferences(NetStatsStorage.NET_STATS_FILE, MODE_PRIVATE);
		netStats.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mScreenOrientation = null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// We handle only orientation changes
		if (Logging.ON) Log.d(TAG, "onConfigurationChanged(), newConfig=" + newConfig.toString());
		mScreenOrientation.setOrientation();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		View v;
		TextView text;
		switch (id) {
		case DIALOG_NETSTATS_DISCLAIMER:
			v = LayoutInflater.from(this).inflate(R.layout.opt_out_dialog, null);
			text = (TextView)v.findViewById(R.id.dialogText);
			text.setText(R.string.prefNetworkUsageDisclaimer);
			return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.notice)
				.setView(v)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Confirmed; turn on network statistics now
						CheckBoxPreference cbPref = (CheckBoxPreference)findPreference(PreferenceName.COLLECT_STATS);
						cbPref.setChecked(true);
						// Additionally, if the opt-out checkbox was selected, store property to
						// disable this dialog for future
						AlertDialog ad = (AlertDialog)dialog;
						CheckBox optOut = (CheckBox)ad.findViewById(R.id.optOutSetting);
						if (optOut.isChecked()) {
							if (Logging.ON) { Log.d(TAG, "Disabling notice for future"); }
							SharedPreferences netStats = getSharedPreferences(NetStatsStorage.NET_STATS_FILE, MODE_PRIVATE);
							SharedPreferences.Editor editor = netStats.edit();
							editor.putBoolean(NetStatsStorage.NET_STATS_WARNING, true);
							editor.commit();
						}
					}
				})
				.create();
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_NETSTATS_DISCLAIMER:
			CheckBox optOutOption = (CheckBox)dialog.findViewById(R.id.optOutSetting);
			optOutOption.setChecked(false);
			break;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(NetStatsStorage.NET_STATS_TOTAL_RCVD)) {
			updateNetworkStats();
		}
		else if (key.equals(NetStatsStorage.NET_STATS_TOTAL_SENT)) {
			updateNetworkStats();
		}
	}

	private void updateNetworkStats() {
		SharedPreferences netStats = getSharedPreferences(NetStatsStorage.NET_STATS_FILE, MODE_PRIVATE);
		long totalRcvd = netStats.getLong(NetStatsStorage.NET_STATS_TOTAL_RCVD, 0);
		long totalSent = netStats.getLong(NetStatsStorage.NET_STATS_TOTAL_SENT, 0);

		mAuxString.setLength(0);
		mAuxString.append(getString(R.string.received));
		mAuxString.append(": ");
		if (totalRcvd/1000000 > 0) {
			// More than 1M
			double mRcvd = totalRcvd/1000000.0d;
			mAuxString.append(String.format("%.1fMB, ", mRcvd));
		}
		else if (totalRcvd/1000 > 0) {
			// More than 1K
			double kRcvd = totalRcvd/1000.0d;
			mAuxString.append(String.format("%.1fKB, ", kRcvd));
		}
		else {
			// Just bytes
			mAuxString.append(totalRcvd);
			mAuxString.append("B, ");
		}
		mAuxString.append(getString(R.string.sent));
		mAuxString.append(": ");
		if (totalSent/1000000 > 0) {
			// More than 1M
			double mSent = totalSent/1000000.0d;
			mAuxString.append(String.format("%.1fMB", mSent));
		}
		else if (totalSent/1000 > 0) {
			// More than 1K
			double kSent = totalSent/1000.0d;
			mAuxString.append(String.format("%.1fKB", kSent));
		}
		else {
			mAuxString.append(totalSent);
			mAuxString.append("B");
		}

		CheckBoxPreference pref = (CheckBoxPreference)findPreference(PreferenceName.COLLECT_STATS);
		pref.setSummaryOn(mAuxString.toString());
	}
}