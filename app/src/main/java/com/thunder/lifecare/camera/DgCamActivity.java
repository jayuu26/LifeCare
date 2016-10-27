package com.thunder.lifecare.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.thunder.lifecare.R;
import com.thunder.lifecare.constant.AppConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DgCamActivity extends AppCompatActivity implements SurfaceHolder.Callback,
		View.OnClickListener {

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private ImageView flipCamera;
	private ImageView flashCameraButton;
	private ImageView captureImage;
	private int cameraId;
	private boolean flashmode = false;
	private int rotation;
	private String fileName;
	OrientationEventListener myOrientationEventListener;
	boolean PORTRAIT_MODE = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_new_layout);

		Intent i = getIntent();
//		from_activity = i.getStringExtra("from_activity");
		fileName = i.getStringExtra("destination");
		Log.d("camera", fileName);

		checkOrientation();

		// camera surface view created
		cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
		flipCamera = (ImageView) findViewById(R.id.flipCamera);
		flashCameraButton = (ImageView) findViewById(R.id.flash);
		captureImage = (ImageView) findViewById(R.id.captureImage);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		flipCamera.setOnClickListener(this);
		captureImage.setOnClickListener(this);
		flashCameraButton.setOnClickListener(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//		if (Camera.getNumberOfCameras() > 1) {
//			flipCamera.setVisibility(View.VISIBLE);
//		}
		if (!getBaseContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH)) {
			flashCameraButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)) {
			alertCameraDialog();
		}
	}

	private boolean openCamera(int id) {
		boolean result = false;
		cameraId = id;
		releaseCamera();
		try {
			camera = Camera.open(cameraId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (camera != null) {
			try {
				setUpCamera(camera);
				camera.setErrorCallback(new Camera.ErrorCallback() {

					@Override
					public void onError(int error, Camera camera) {

					}
				});
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
				releaseCamera();
			}
		}
		return result;
	}

	private void setUpCamera(Camera c) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degree = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degree = 0;
				break;
			case Surface.ROTATION_90:
				degree = 90;
				break;
			case Surface.ROTATION_180:
				degree = 180;
				break;
			case Surface.ROTATION_270:
				degree = 270;
				break;

			default:
				break;
		}

		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			// frontFacing
			rotation = (info.orientation + degree) % 330;
			rotation = (360 - rotation) % 360;
		} else {
			// Back-facing
			rotation = (info.orientation - degree + 360) % 360;
		}
		Log.d("camera", "Rotation is: " +rotation+"");
		c.setDisplayOrientation(rotation);
		Camera.Parameters params = c.getParameters();

		if(android.os.Build.BRAND.equalsIgnoreCase("qcom") && android.os.Build.DEVICE.equalsIgnoreCase("msm8226")){
			params.setPictureSize(1600, 1200);
		}
		params.setPictureFormat(PixelFormat.JPEG);
		params.setJpegQuality(100);
//		showFlashButton(params);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if ( params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) ) {
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
		}

		List<String> focusModes = params.getSupportedFlashModes();
		if (focusModes != null) {
			if (focusModes
					.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
		}

		params.setRotation(rotation);

		c.setParameters(params);
	}

	private void showFlashButton(Camera.Parameters params) {
		boolean showFlash = (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
				&& params.getSupportedFlashModes() != null
				&& params.getSupportedFocusModes().size() > 1;

		flashCameraButton.setVisibility(showFlash ? View.VISIBLE
				: View.INVISIBLE);
	}

	private void releaseCamera() {
		try {
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.setErrorCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			camera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.flash:
				flashOnButton();
				break;
			case R.id.flipCamera:
				flipCamera();
				break;
			case R.id.captureImage:
				takeImage();
				break;
			default:
				break;
		}
	}

	private void takeImage() {
		if (camera != null) {
		camera.takePicture(null, null, new Camera.PictureCallback() {

			private File imageFile;

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				try {
					// convert byte array into bitmap
					Bitmap loadedImage = null;
//					Bitmap rotatedBitmap = null;
					if(!PORTRAIT_MODE) {
						loadedImage = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						// rotate Image
						Matrix rotateMatrix = new Matrix();
						rotateMatrix.postRotate(270);
						loadedImage = Bitmap.createBitmap(loadedImage, 0, 0,
								loadedImage.getWidth(), loadedImage.getHeight(),
								rotateMatrix, false);
					} else {
						loadedImage = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					}

					File pictureFile = new File(fileName);
					ByteArrayOutputStream ostream = new ByteArrayOutputStream();

					// save image into gallery
					loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

					FileOutputStream fout = new FileOutputStream(pictureFile);
					fout.write(ostream.toByteArray());
					fout.close();

				} catch (FileNotFoundException e) {
					Log.d("DG_DEBUG", "File not found: " + e.getMessage());
				} catch (IOException e) {
					Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Insufficient Storage to store data", Toast.LENGTH_SHORT).show();
					// Restart the camera preview.
					if (camera != null)
						camera.startPreview();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (myOrientationEventListener != null){
					myOrientationEventListener.disable();
				}

				releaseCamera();

				Intent intent = new Intent();
				setResult(RESULT_OK, intent);

				finish();
			}
		});
		}
	}

	private void flipCamera() {
		int id = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT
				: Camera.CameraInfo.CAMERA_FACING_BACK);
		if (!openCamera(id)) {
			alertCameraDialog();
		}
	}

	private void alertCameraDialog() {
		AlertDialog.Builder dialog = createAlert(DgCamActivity.this,
				"Camera info", "error to open camera");
		dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		dialog.show();
	}

	private AlertDialog.Builder createAlert(Context context, String title, String message) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Light_Dialog));
		dialog.setIcon(R.drawable.camera);
		if (title != null)
			dialog.setTitle(title);
		else
			dialog.setTitle("Information");
		dialog.setMessage(message);
		dialog.setCancelable(false);
		return dialog;

	}

	private void flashOnButton() {
		if (camera != null) {
			try {
				Camera.Parameters param = camera.getParameters();
				param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH
						: Camera.Parameters.FLASH_MODE_OFF);
				camera.setParameters(param);
				flashmode = !flashmode;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		releaseCamera();
		finish();
	}

	public boolean checkOrientation() {

		myOrientationEventListener = new OrientationEventListener(getApplicationContext(), SensorManager.SENSOR_DELAY_NORMAL)
		{
			@Override
			public void onOrientationChanged(int orientation)
			{
				PORTRAIT_MODE = ((orientation < 100) || (orientation > 280));
				Log.d("Camera", "current Portrait mode: " + PORTRAIT_MODE);
			}
		};
//		Log.w("Listener", " can detect orientation: " + myOrientationEventListener.canDetectOrientation() + " ");
		myOrientationEventListener.enable();
		return PORTRAIT_MODE;
	}

}
