package com.example.chiachen.camerapractice;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private View mTopBar;
	private View mTopbarClose;
	private View mBottomBar;
	private View mShutterBtn;

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
		mBottomBar = findViewById(R.id.bottomBar);
		mShutterBtn = mBottomBar.findViewById(R.id.shutter_button);
		setBtnListener(onClickListener, mShutterBtn, mTopbarClose);

		mCameraPreview = new CameraPreview(this, mCamera);
		mCameraPreview.setKeepScreenOn(true);
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
