package com.example.chiachen.camerapractice.Util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.example.chiachen.camerapractice.Globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by chiachen on 2017/6/19.
 */

public class FileExporter {
	public static final String TAG = "FileExporter";
	public static final String EXPORT_FOLDER_NAME = "Camera_Practice";
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault());
	public static final String IMAGE_EXTENSION = ".jpg";

	public static String getDcimFolderPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory());
		sb.append(File.separator);
		sb.append(Environment.DIRECTORY_DCIM);
		sb.append(File.separator);
		sb.append(EXPORT_FOLDER_NAME);
		return sb.toString();
	}

	public static boolean mkdirs(final File fileFolder) {
		return fileFolder.exists() ? fileFolder.mkdirs() : fileFolder.isDirectory();
	}

	public static String getDcimSaveFilePath() {
		final String timeString = DATE_FORMATTER.format(System.currentTimeMillis());
		final StringBuilder sb = new StringBuilder();
		sb.append(getDcimFolderPath());
		sb.append(File.separator);
		sb.append(timeString);
		sb.append(IMAGE_EXTENSION);
		return sb.toString();
	}

	public static Bitmap rotate(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, false);
	}

	public static void saveBitmapToFile(File pictureFile, Bitmap bitmap){
		if (null == pictureFile) {
			Log.d(TAG, "Error creating media file, check storage permissions: ");
		} else if (null == bitmap) {
			Log.d(TAG, "Error creating media file, check storage permissions: ");
		}

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}

		if (pictureFile.exists())
			Log.d(TAG, "Saving file success" );
		else
			Log.d(TAG, "Saving file fail");
		onSaveComplete(pictureFile);
	}

	public static void onSaveComplete(File filePath) {
		if(filePath == null){
			Log.w(TAG, "onSaveComplete: file dir error");
			return;
		}

		Log.d(TAG, "onSaveComplete: save image in " + filePath);
		MediaScannerConnection.scanFile(
				Globals.getInstance(),
				new String[]{filePath.getAbsolutePath()},
				null,
				new MediaScannerConnection.MediaScannerConnectionClient() {
					@Override
					public void onMediaScannerConnected() {
						Log.d(TAG, "onMediaScannerConnected");
					}

					@Override
					public void onScanCompleted(String path, Uri uri) {
						Log.d(TAG, "onScanCompleted");
					}
				}
		);

	}
}
