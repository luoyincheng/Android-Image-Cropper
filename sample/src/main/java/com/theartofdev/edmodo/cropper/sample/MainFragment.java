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

package com.theartofdev.edmodo.cropper.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.croppersample.R;
import com.theartofdev.edmodo.cropper.ActivityResult;
import com.theartofdev.edmodo.cropper.ImageCropView;

import static com.theartofdev.edmodo.cropper.ActivityResult.getActivityResult;
import static com.theartofdev.edmodo.cropper.BitmapUtils.toOvalBitmap;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

/**
 * The fragment that will show the Image Cropping UI by requested preset.
 */
public final class MainFragment extends Fragment
		implements ImageCropView.OnSetImageUriCompleteListener,
		ImageCropView.OnCropImageCompleteListener {

	// region: Fields and Consts

	private CropDemoPreset mDemoPreset;

	private ImageCropView mImageCropView;
	// endregion

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFragment newInstance(CropDemoPreset demoPreset) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putString("DEMO_PRESET", demoPreset.name());
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Set the image to show for cropping.
	 */
	public void setImageUri(Uri imageUri) {
		mImageCropView.setImageUriAsync(imageUri);
		//        IntentUtils.activity(imageUri)
		//                .start(getContext(), this);
	}

	/**
	 * Set the options of the crop image view to the given values.
	 */
	public void setCropImageViewOptions(CropImageViewOptions options) {
		mImageCropView.setScaleType(options.scaleType);
		mImageCropView.setCropShape(options.cropShape);
		mImageCropView.setGuidelines(options.guidelines);
		mImageCropView.setAspectRatio(options.aspectRatio.first, options.aspectRatio.second);
		mImageCropView.setFixedAspectRatio(options.fixAspectRatio);
		mImageCropView.setMultiTouchEnabled(options.multitouch);
		mImageCropView.setShowCropOverlay(options.showCropOverlay);
		mImageCropView.setShowProgressBar(options.showProgressBar);
		mImageCropView.setAutoZoomEnabled(options.autoZoomEnabled);
		mImageCropView.setMaxZoom(options.maxZoomLevel);
		mImageCropView.setFlippedHorizontally(options.flipHorizontally);
		mImageCropView.setFlippedVertically(options.flipVertically);
	}

	/**
	 * Set the initial rectangle to use.
	 */
	public void setInitialCropRect() {
		mImageCropView.setCropRect(new Rect(100, 300, 500, 1200));
	}

	/**
	 * Reset crop window to initial rectangle.
	 */
	public void resetCropRect() {
		mImageCropView.resetCropRect();
	}

	public void updateCurrentCropViewOptions() {
		CropImageViewOptions options = new CropImageViewOptions();
		options.scaleType = mImageCropView.getScaleType();
		options.cropShape = mImageCropView.getCropShape();
		options.guidelines = mImageCropView.getGuidelines();
		options.aspectRatio = mImageCropView.getAspectRatio();
		options.fixAspectRatio = mImageCropView.isFixAspectRatio();
		options.showCropOverlay = mImageCropView.isShowCropOverlay();
		options.showProgressBar = mImageCropView.isShowProgressBar();
		options.autoZoomEnabled = mImageCropView.isAutoZoomEnabled();
		options.maxZoomLevel = mImageCropView.getMaxZoom();
		options.flipHorizontally = mImageCropView.isFlippedHorizontally();
		options.flipVertically = mImageCropView.isFlippedVertically();
		((MainActivity) getActivity()).setCurrentOptions(options);
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView;
		switch (mDemoPreset) {
			case RECT:
				rootView = inflater.inflate(R.layout.fragment_main_rect, container, false);
				break;
			case CIRCULAR:
				rootView = inflater.inflate(R.layout.fragment_main_oval, container, false);
				break;
			case CUSTOMIZED_OVERLAY:
				rootView = inflater.inflate(R.layout.fragment_main_customized, container, false);
				break;
			case MIN_MAX_OVERRIDE:
				rootView = inflater.inflate(R.layout.fragment_main_min_max, container, false);
				break;
			case SCALE_CENTER_INSIDE:
				rootView = inflater.inflate(R.layout.fragment_main_scale_center, container, false);
				break;
			case CUSTOM:
				rootView = inflater.inflate(R.layout.fragment_main_rect, container, false);
				break;
			default:
				throw new IllegalStateException("Unknown preset: " + mDemoPreset);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mImageCropView = view.findViewById(R.id.cropImageView);
		mImageCropView.setOnSetImageUriCompleteListener(this);
		mImageCropView.setOnCropImageCompleteListener(this);

		updateCurrentCropViewOptions();

		if (savedInstanceState == null) {
			if (mDemoPreset == CropDemoPreset.SCALE_CENTER_INSIDE) {
				mImageCropView.setImageResource(R.drawable.cat_small);
			} else {
				mImageCropView.setImageResource(R.drawable.cat);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.main_action_crop) {
			mImageCropView.getCroppedImageAsync();
			return true;
		} else if (item.getItemId() == R.id.main_action_rotate) {
			mImageCropView.rotateImage(90);
			return true;
		} else if (item.getItemId() == R.id.main_action_flip_horizontally) {
			mImageCropView.flipImageHorizontally();
			return true;
		} else if (item.getItemId() == R.id.main_action_flip_vertically) {
			mImageCropView.flipImageVertically();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mDemoPreset = CropDemoPreset.valueOf(getArguments().getString("DEMO_PRESET"));
		((MainActivity) activity).setCurrentFragment(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mImageCropView != null) {
			mImageCropView.setOnSetImageUriCompleteListener(null);
			mImageCropView.setOnCropImageCompleteListener(null);
		}
	}

	@Override
	public void onSetImageUriComplete(ImageCropView view, Uri uri, Exception error) {
		if (error == null) {
			Toast.makeText(getActivity(), "Image load successful", Toast.LENGTH_SHORT).show();
		} else {
			Log.e("AIC", "Failed to load image by URI", error);
			Toast.makeText(getActivity(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onCropImageComplete(ImageCropView view, ImageCropView.CropResult result) {
		handleCropResult(result);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			ActivityResult result = getActivityResult(data);
			handleCropResult(result);
		}
	}

	private void handleCropResult(ImageCropView.CropResult result) {
		if (result.getError() == null) {
			Intent intent = new Intent(getActivity(), CropResultActivity.class);
			intent.putExtra("SAMPLE_SIZE", result.getSampleSize());
			if (result.getUri() != null) {
				intent.putExtra("URI", result.getUri());
			} else {
				CropResultActivity.mImage =
						mImageCropView.getCropShape() == ImageCropView.CropShape.OVAL
								? toOvalBitmap(result.getBitmap())
								: result.getBitmap();
			}
			startActivity(intent);
		} else {
			Log.e("AIC", "Failed to crop image", result.getError());
			Toast.makeText(
					getActivity(),
					"Image crop failed: " + result.getError().getMessage(),
					Toast.LENGTH_LONG)
					.show();
		}
	}
}
