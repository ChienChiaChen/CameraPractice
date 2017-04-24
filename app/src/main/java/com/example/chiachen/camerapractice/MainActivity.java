package com.example.chiachen.camerapractice;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import static android.hardware.Camera.getCameraInfo;
import static android.hardware.Camera.getNumberOfCameras;

public class MainActivity extends Activity {
	private Camera mCamera = null;
	private CameraView mCameraView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			Log.e("camera", "getMessage  : " + e.getMessage());
		}

		if (null == mCamera) {
			Log.e("camera", "mCamera is null");
			return;
		}

		// mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
		// FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
		// camera_view.addView(mCameraView);//add the SurfaceView to the layout

		mCameraView = new CameraView(this, mCamera);
		FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
		camera_view.addView(mCameraView);

		findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// System.exit(0);
				mCamera.takePicture(null, mPictureCallback, null, mPictureCallback1);
			}
		});

	}

	Camera.PictureCallback mPictureCallback=new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] bytes, Camera camera) {
			Log.e("tag", "raw");
		}
	};

	Camera.PictureCallback mPictureCallback1=new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] bytes, Camera camera) {
			Log.e("tag", "jpeg");
			mCamera.startPreview();
		}
	};

	public static Camera open() {
		int numberOfCameras = getNumberOfCameras();
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

				Log.e("camera","i: "+i);
			}
		}
		return null;
	}
}
