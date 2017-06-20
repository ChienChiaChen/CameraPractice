package com.example.chiachen.camerapractice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chiachen.camerapractice.Util.FileExporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
	private View mTopBar;
	private View mTopbarClose;
	private View mBottomBar;
	private View mShutterBtn;
	private ImageView result;

	private CameraPreview mCameraPreview = null;
	private Camera mCamera = null;
	private ExecutorService saveTaskExecutorService = Executors.newFixedThreadPool(4);
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(final byte[] data, final Camera camera) {
			new AsyncTask<Void, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(Void... params) {
					if (FileExporter.mkdirs(new File(FileExporter.getDcimFolderPath())))
						return null;
					FileOutputStream outStream = null;
					String filePath = FileExporter.getDcimSaveFilePath();
					try {
						outStream = new FileOutputStream(filePath);
						outStream.write(data);
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Bitmap realImage;
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 5;
					// Tell to gc that whether it needs free memory, the Bitmap can be cleared
					options.inPurgeable=true;
					// Which kind of reference will be used to recover
					// the Bitmap data after being clear, when it will be used in the future
					options.inInputShareable=true;

					realImage = BitmapFactory.decodeByteArray(data, 0, data.length, options);
					try {
						ExifInterface exif = new ExifInterface(filePath);
						Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
						if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("1")) {
							realImage = FileExporter.rotate(realImage, 90);
						} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
							realImage = FileExporter.rotate(realImage, 90);
						} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
							realImage = FileExporter.rotate(realImage, 90);
						} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
							realImage = FileExporter.rotate(realImage, 90);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					FileExporter.saveBitmapToFile(new File(filePath), realImage);
					return realImage;
				}

				@Override
				protected void onPostExecute(Bitmap bitmap) {
					super.onPostExecute(bitmap);
					if (bitmap != null)
						result.setImageBitmap(bitmap);
					camera.startPreview();
				}
			}.executeOnExecutor(saveTaskExecutorService);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCameraPreview.openCamera();
		mCameraPreview.setCameraDisplayOrientation(
				this.getWindowManager().getDefaultDisplay().getRotation(),
				Camera.CameraInfo.CAMERA_FACING_BACK);
	}

	private void initComponent(){
		mTopBar = findViewById(R.id.topBar);
		mTopbarClose = mTopBar.findViewById(R.id.topToolBarCloseBtn);
		mBottomBar = findViewById(R.id.bottomBar);
		mShutterBtn = mBottomBar.findViewById(R.id.shutter_button);
		result =(ImageView) mBottomBar.findViewById(R.id.photo_thumbnail);
		setBtnListener(onClickListener, mShutterBtn, mTopbarClose);

		mCameraPreview = new CameraPreview(this, mCamera);
		mCameraPreview.setKeepScreenOn(true);
		mCameraPreview.setJpegCallback(mJpegCallback);
		FrameLayout cameraView = (FrameLayout) findViewById(R.id.camera_preview);
		cameraView.addView(mCameraPreview);
	}

	private void setBtnListener(View.OnClickListener listener, View... buttons) {
		for (View button : buttons) {
			if (button != null)
				button.setOnClickListener(listener);
		}
	}

	private View.OnClickListener onClickListener =new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.shutter_button:{
					// Toast.makeText(v.getContext(), "show toast",Toast.LENGTH_SHORT).show();
					mCameraPreview.tryFocusAndTakePicture();
					break;
				}
				case R.id.topToolBarCloseBtn:{
					Toast.makeText(v.getContext(), "show toast",Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
	};
}
