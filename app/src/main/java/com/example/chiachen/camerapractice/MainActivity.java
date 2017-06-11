package com.example.chiachen.camerapractice;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private View mTopBar = null;
	private View mTopbarClose = null;
	private CameraPreview mCameraPreview = null;
	private FrameLayout mCameraFrame = null;
	private Camera mCamera = null;

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
		mTopbarClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(view.getContext(), "show toast",Toast.LENGTH_SHORT).show();
			}
		});

		mCameraPreview =new CameraPreview(this,mCamera);
		mCameraPreview.setKeepScreenOn(true);
		FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_preview);
		camera_view.addView(mCameraPreview);
	}

	private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}
}
