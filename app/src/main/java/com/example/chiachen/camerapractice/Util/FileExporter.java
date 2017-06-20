package com.example.chiachen.camerapractice.Util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by chiachen on 2017/6/19.
 */

public class FileExporter {
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

	public static File saveBitmapToFile(){
		// Save Bitmap here
		return null;
	}

}
