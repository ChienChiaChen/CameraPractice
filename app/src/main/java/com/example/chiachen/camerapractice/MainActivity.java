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

		mCameraPreview = new CameraPreview(this, mCamera);
		mCameraPreview.setKeepScreenOn(true);
		FrameLayout cameraView = (FrameLayout) findViewById(R.id.camera_preview);
		cameraView.addView(mCameraPreview);
	}
}
