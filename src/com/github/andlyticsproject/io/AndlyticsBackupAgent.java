package com.github.andlyticsproject.io;

import java.io.IOException;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class AndlyticsBackupAgent extends BackupAgentHelper {

	private static final String TAG = AndlyticsBackupAgent.class.getSimpleName();

	private static final String PREFS = "andlytics_pref";
	private static final String PREFS_BACKUP_KEY = "prefs";
	private static final String STATS_BACKUP_KEY = "stats";

	private Object fileLock = new Object();

	@Override
	public void onCreate() {
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
		addHelper(PREFS_BACKUP_KEY, helper);
		// addHelper(STATS_BACKUP_KEY, new StatsDbBackupHelper(this));
		addHelper(STATS_BACKUP_KEY, new DbBackupHelper(this, "andlytics"));
	}

	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
			ParcelFileDescriptor newState) throws IOException {
		Log.d(TAG, "onBackup");
		synchronized (fileLock) {
			super.onBackup(oldState, data, newState);
		}
	}

	@Override
	public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState)
			throws IOException {
		Log.d(TAG, "onRestore appVersionCode = " + appVersionCode);

		synchronized (fileLock) {
			Log.d(TAG, "onRestore in-lock");
			super.onRestore(data, appVersionCode, newState);
		}
	}

}
