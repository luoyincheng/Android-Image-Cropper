// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.theartofdev.edmodo.cropper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.theartofdev.edmodo.cropper.Constants.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_BUNDLE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_OPTIONS;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_RESULT;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_SOURCE;
import static com.theartofdev.edmodo.cropper.Constants.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.Constants.PICK_IMAGE_PERMISSIONS_REQUEST_CODE;

/**
 * Built-in activity for image cropping.<br>
 * Use {@link ImageCrop#activity(Uri)} to create a builder to start this activity.
 */
public class ImageCropActivity extends AppCompatActivity
		implements ImageCropView.OnSetImageUriCompleteListener,
		ImageCropView.OnCropImageCompleteListener {

	/**
	 * The crop image view library widget used in the activity
	 */
	private ImageCropView mImageCropView;

	/**
	 * Persist URI image to crop URI if specific permissions are required
	 */
	private Uri mCropImageUri;

	/**
	 * the options that were set for the crop image
	 */
	private ImageCropOptions mOptions;

	@Override
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_image_activity);

		mImageCropView = findViewById(R.id.cropImageView);

		Bundle bundle = getIntent().getBundleExtra(CROP_IMAGE_EXTRA_BUNDLE);
		mCropImageUri = bundle.getParcelable(CROP_IMAGE_EXTRA_SOURCE);
		mOptions = bundle.getParcelable(CROP_IMAGE_EXTRA_OPTIONS);

		if (savedInstanceState == null) {
			if (mCropImageUri == null || mCropImageUri.equals(Uri.EMPTY)) {
				if (ImageCrop.isExplicitCameraPermissionRequired(this)) {
					// request permissions and handle the result in onRequestPermissionsResult()
					requestPermissions(
							new String[]{Manifest.permission.CAMERA},
							CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
				} else {
					ImageCrop.startPickImageActivity(this);
				}
			} else if (ImageCrop.isReadExternalStoragePermissionsRequired(this, mCropImageUri)) {
				// request permissions and handle the result in onRequestPermissionsResult()
				requestPermissions(
						new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
			} else {
				// no permissions required or already grunted, can start crop image activity
				mImageCropView.setImageUriAsync(mCropImageUri);
			}
		}

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			CharSequence title = mOptions != null &&
					mOptions.activityTitle != null && mOptions.activityTitle.length() > 0
					? mOptions.activityTitle
					: getResources().getString(R.string.crop_image_activity_title);
			actionBar.setTitle(title);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mImageCropView.setOnSetImageUriCompleteListener(this);
		mImageCropView.setOnCropImageCompleteListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mImageCropView.setOnSetImageUriCompleteListener(null);
		mImageCropView.setOnCropImageCompleteListener(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.crop_image_menu, menu);

		if (!mOptions.allowRotation) {
			menu.removeItem(R.id.crop_image_menu_rotate_left);
			menu.removeItem(R.id.crop_image_menu_rotate_right);
		} else if (mOptions.allowCounterRotation) {
			menu.findItem(R.id.crop_image_menu_rotate_left).setVisible(true);
		}

		if (!mOptions.allowFlipping) {
			menu.removeItem(R.id.crop_image_menu_flip);
		}

		if (mOptions.cropMenuCropButtonTitle != null) {
			menu.findItem(R.id.crop_image_menu_crop).setTitle(mOptions.cropMenuCropButtonTitle);
		}

		Drawable cropIcon = null;
		try {
			if (mOptions.cropMenuCropButtonIcon != 0) {
				cropIcon = ContextCompat.getDrawable(this, mOptions.cropMenuCropButtonIcon);
				menu.findItem(R.id.crop_image_menu_crop).setIcon(cropIcon);
			}
		} catch (Exception e) {
			Log.w("AIC", "Failed to read menu crop drawable", e);
		}

		if (mOptions.activityMenuIconColor != 0) {
			updateMenuItemIconColor(
					menu, R.id.crop_image_menu_rotate_left, mOptions.activityMenuIconColor);
			updateMenuItemIconColor(
					menu, R.id.crop_image_menu_rotate_right, mOptions.activityMenuIconColor);
			updateMenuItemIconColor(menu, R.id.crop_image_menu_flip, mOptions.activityMenuIconColor);
			if (cropIcon != null) {
				updateMenuItemIconColor(menu, R.id.crop_image_menu_crop, mOptions.activityMenuIconColor);
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.crop_image_menu_crop) {
			cropImage();
			return true;
		}
		if (item.getItemId() == R.id.crop_image_menu_rotate_left) {
			rotateImage(-mOptions.rotationDegrees);
			return true;
		}
		if (item.getItemId() == R.id.crop_image_menu_rotate_right) {
			rotateImage(mOptions.rotationDegrees);
			return true;
		}
		if (item.getItemId() == R.id.crop_image_menu_flip_horizontally) {
			mImageCropView.flipImageHorizontally();
			return true;
		}
		if (item.getItemId() == R.id.crop_image_menu_flip_vertically) {
			mImageCropView.flipImageVertically();
			return true;
		}
		if (item.getItemId() == android.R.id.home) {
			setResultCancel();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResultCancel();
	}

	@Override
	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// handle result of pick image chooser
		if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the picker. We don't have anything to crop
				setResultCancel();
			}

			if (resultCode == Activity.RESULT_OK) {
				mCropImageUri = ImageCrop.getPickImageResultUri(this, data);

				// For API >= 23 we need to check specifically that we have permissions to read external
				// storage.
				if (ImageCrop.isReadExternalStoragePermissionsRequired(this, mCropImageUri)) {
					// request permissions and handle the result in onRequestPermissionsResult()
					requestPermissions(
							new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
							PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
				} else {
					// no permissions required or already grunted, can start crop image activity
					mImageCropView.setImageUriAsync(mCropImageUri);
				}
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		if (requestCode == PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
			if (mCropImageUri != null
					&& grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// required permissions granted, start crop image activity
				mImageCropView.setImageUriAsync(mCropImageUri);
			} else {
				Toast.makeText(this, R.string.crop_image_activity_no_permissions, Toast.LENGTH_LONG).show();
				setResultCancel();
			}
		}

		if (requestCode == CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
			// Irrespective of whether camera permission was given or not, we show the picker
			// The picker will not add the camera intent if permission is not available
			ImageCrop.startPickImageActivity(this);
		}
	}

	@Override
	public void onSetImageUriComplete(ImageCropView view, Uri uri, Exception error) {
		if (error == null) {
			if (mOptions.initialCropWindowRectangle != null) {
				mImageCropView.setCropRect(mOptions.initialCropWindowRectangle);
			}
			if (mOptions.initialRotation > -1) {
				mImageCropView.setRotatedDegrees(mOptions.initialRotation);
			}
		} else {
			setResult(null, error, 1);
		}
	}

	@Override
	public void onCropImageComplete(ImageCropView view, ImageCropView.CropResult result) {
		setResult(result.getUri(), result.getError(), result.getSampleSize());
	}

	// region: Private methods

	/**
	 * Execute crop image and save the result tou output uri.
	 */
	protected void cropImage() {
		if (mOptions.noOutputImage) {
			setResult(null, null, 1);
		} else {
			Uri outputUri = getOutputUri();
			mImageCropView.saveCroppedImageAsync(
					outputUri,
					mOptions.outputCompressFormat,
					mOptions.outputCompressQuality,
					mOptions.outputRequestWidth,
					mOptions.outputRequestHeight,
					mOptions.outputRequestSizeOptions);
		}
	}

	/**
	 * Rotate the image in the crop image view.
	 */
	protected void rotateImage(int degrees) {
		mImageCropView.rotateImage(degrees);
	}

	/**
	 * Get Android uri to save the cropped image into.<br>
	 * Use the given in options or create a temp file.
	 */
	protected Uri getOutputUri() {
		Uri outputUri = mOptions.outputUri;
		if (outputUri == null || outputUri.equals(Uri.EMPTY)) {
			try {
				String ext =
						mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG
								? ".jpg"
								: mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
				outputUri = Uri.fromFile(File.createTempFile("cropped", ext, getCacheDir()));
			} catch (IOException e) {
				throw new RuntimeException("Failed to create temp file for output image", e);
			}
		}
		return outputUri;
	}

	/**
	 * Result with cropped image data or error if failed.
	 */
	protected void setResult(Uri uri, Exception error, int sampleSize) {
		int resultCode = error == null ? RESULT_OK : CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
		setResult(resultCode, getResultIntent(uri, error, sampleSize));
		finish();
	}

	/**
	 * Cancel of cropping activity.
	 */
	protected void setResultCancel() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Get intent instance to be used for the result of this activity.
	 */
	protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
		ImageCrop.ActivityResult result =
				new ImageCrop.ActivityResult(
						mImageCropView.getImageUri(),
						uri,
						error,
						mImageCropView.getCropPoints(),
						mImageCropView.getCropRect(),
						mImageCropView.getRotatedDegrees(),
						mImageCropView.getWholeImageRect(),
						sampleSize);
		Intent intent = new Intent();
		intent.putExtras(getIntent());
		intent.putExtra(CROP_IMAGE_EXTRA_RESULT, result);
		return intent;
	}

	/**
	 * Update the color of a specific menu item to the given color.
	 */
	private void updateMenuItemIconColor(Menu menu, int itemId, int color) {
		MenuItem menuItem = menu.findItem(itemId);
		if (menuItem != null) {
			Drawable menuItemIcon = menuItem.getIcon();
			if (menuItemIcon != null) {
				try {
					menuItemIcon.mutate();
					menuItemIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
					menuItem.setIcon(menuItemIcon);
				} catch (Exception e) {
					Log.w("AIC", "Failed to update menu item color", e);
				}
			}
		}
	}
	// endregion
}
