package com.example.chiachen.camerapractice;

/**
 * @author Jose Davis Nidhin
 */

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "Preview";

    private SurfaceHolder mHolder;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private Camera mCamera;
	private Context mContext;
	private Camera.PictureCallback mJpegCallback;
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {

		}
	};

    CameraPreview(Context context, Camera camera) {
        super(context);

	    mContext = context;
	    mCamera = camera;
	    mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

	public void openCamera(){
		if (mCamera != null) {
			return;
		}
		mCamera = Camera.open();
		mCamera.startPreview();
		mCamera.setErrorCallback(new Camera.ErrorCallback() {
			public void onError(int error, Camera camera) {
				mCamera.release();
				mCamera = Camera.open();
				Log.d("Camera died", "error camera");
			}
		});
		mCamera = setCamera(mCamera);
	}

	private Camera setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera == null)
			return camera;

		mSupportedPreviewSizes = mCamera.getParameters().getSupportedPictureSizes();
		requestLayout();

		// get Camera parameters
		Camera.Parameters params = mCamera.getParameters();

		List<String> focusModes = params.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			// set the focus mode
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			// set Camera parameters
			mCamera.setParameters(params);
		}
		return camera;
	}

	public void setCameraDisplayOrientation(int rotation , int cameraId) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
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
		if (mCamera!=null)
			mCamera.setDisplayOrientation(result);
	}

	//touch event start
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();

			Rect touchRect = new Rect(
					(int)(x - 200),//left
					(int)(y - 200),//top
					(int)(x + 200),//right
					(int)(y + 200));//bottom

			final Rect targetFocusRect = new Rect(
					touchRect.left * 2000/this.getWidth() - 1000,
					touchRect.top * 2000/this.getHeight() - 1000,
					touchRect.right * 2000/this.getWidth() - 1000,
					touchRect.bottom * 2000/this.getHeight() - 1000);

			doTouchFocus(targetFocusRect);
		}
		return super.onTouchEvent(event);
	}

	public void doTouchFocus(final Rect tfocusRect) {
		try {
			List<Camera.Area> focusList = new ArrayList<Camera.Area>();
			Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
			focusList.add(focusArea);

			Camera.Parameters param = mCamera.getParameters();
			param.setFocusAreas(focusList);
			param.setMeteringAreas(focusList);
			mCamera.setParameters(param);

			mCamera.autoFocus(mAutoFocusCallback);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Unable to autofocus");
		}
	}

	Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success){
				mCamera.cancelAutoFocus();
			}
		}
	};
	//Touch event end

	//Focus and take picture start
	public void tryFocusAndTakePicture(){
		if (mCamera == null)
			return;
		mCamera.autoFocus(mAutoFocusCallbackAndTakePicture);
	}

	Camera.AutoFocusCallback mAutoFocusCallbackAndTakePicture = new Camera.AutoFocusCallback(){
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (!success || null == mJpegCallback || null == mShutterCallback)
				return;
			camera.takePicture(mShutterCallback, null, mJpegCallback);
		}
	};

	public void setShutterCallback(Camera.ShutterCallback shutterCallback) {
		this.mShutterCallback = shutterCallback;
	}

	public void setJpegCallback(Camera.PictureCallback jpegCallback) {
		this.mJpegCallback = jpegCallback;
	}
	// Focus and take picture end

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
	        mPreviewSize = maxSize();
        }
    }

    public Size maxSize(){
//    	heightmax =0;
//    	widthmax =0;
	    Size sizeMax = mSupportedPreviewSizes.get(0);
		//long totalsize = heightmax*widthmax;
    	//long maxsize=mSupportedPreviewSizes.get(0).height*mSupportedPreviewSizes.get(0).width;

	    for (Size size : mSupportedPreviewSizes) {
		    if (size.height * size.width > sizeMax.width * sizeMax.height) {
			    sizeMax = size;
		    }
	    }
    	
    	return sizeMax;
    }

	// ====SurfaceHolder.Callback====
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if(mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();

			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}
	// ====SurfaceHolder.Callback====

}
