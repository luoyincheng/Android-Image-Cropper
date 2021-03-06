// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth;
// inexhaustible as the great rivers.
// When they come to an end;
// they begin again;
// like the days and months;
// they die and are reborn;
// like the four seasons."
//
// - Sun Tsu;
// "The Art of War"

package com.theartofdev.edmodo.cropper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_BUNDLE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_OPTIONS;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_SOURCE;

/**
 * All the possible options that can be set to customize crop image.<br>
 * Initialized with default values.
 */
public class ImageCropOptions implements Parcelable {

	public static final Creator<ImageCropOptions> CREATOR =
			new Creator<ImageCropOptions>() {
				@Override
				public ImageCropOptions createFromParcel(Parcel in) {
					return new ImageCropOptions(in);
				}

				@Override
				public ImageCropOptions[] newArray(int size) {
					return new ImageCropOptions[size];
				}
			};

	/**
	 * The shape of the cropping window.
	 */
	public ImageCropView.CropShape cropShape;

	/**
	 * An edge of the crop window will snap to the corresponding edge of a specified bounding box when
	 * the crop window edge is less than or equal to this distance (in pixels) away from the bounding
	 * box edge. (in pixels)
	 */
	public float snapRadius;

	/**
	 * The radius of the touchable area around the handle. (in pixels)<br>
	 * We are basing this value off of the recommended 48dp Rhythm.<br>
	 * See: http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
	 */
	public float touchRadius;

	/**
	 * whether the guidelines should be on, off, or only showing when resizing.
	 */
	public ImageCropView.Guidelines guidelines;

	/**
	 * The initial scale type of the image in the crop image view
	 */
	public ImageCropView.ScaleType scaleType;

	/**
	 * if to show crop overlay UI what contains the crop window UI surrounded by background over the
	 * cropping image.<br>
	 * default: true, may disable for animation or frame transition.
	 */
	public boolean showCropOverlay;

	/**
	 * if to show progress bar when image async loading/cropping is in progress.<br>
	 * default: true, disable to provide custom progress bar UI.
	 */
	public boolean showProgressBar;

	/**
	 * if auto-zoom functionality is enabled.<br>
	 * default: true.
	 */
	public boolean autoZoomEnabled;

	/**
	 * if multi-touch should be enabled on the crop box default: false
	 */
	public boolean multiTouchEnabled;

	/**
	 * The max zoom allowed during cropping.
	 */
	public int maxZoom;

	/**
	 * The initial crop window padding from image borders in percentage of the cropping image
	 * dimensions.
	 */
	public float initialCropWindowPaddingRatio;

	/**
	 * whether the width to height aspect ratio should be maintained or free to change.
	 */
	public boolean fixAspectRatio;

	/**
	 * the X value of the aspect ratio.
	 */
	public int aspectRatioX;

	/**
	 * the Y value of the aspect ratio.
	 */
	public int aspectRatioY;

	/**
	 * the thickness of the guidelines lines in pixels. (in pixels)
	 */
	public float borderLineThickness;

	/**
	 * the color of the guidelines lines
	 */
	public int borderLineColor;

	/**
	 * thickness of the corner line. (in pixels)
	 */
	public float borderCornerThickness;

	/**
	 * the offset of corner line from crop window border. (in pixels)
	 */
	public float borderCornerOffset;

	/**
	 * the length of the corner line away from the corner. (in pixels)
	 */
	public float borderCornerLength;

	/**
	 * the color of the corner line
	 */
	public int borderCornerColor;

	/**
	 * the thickness of the guidelines lines. (in pixels)
	 */
	public float guidelinesThickness;

	/**
	 * the color of the guidelines lines
	 */
	public int guidelinesColor;

	/**
	 * the color of the overlay background around the crop window cover the image parts not in the
	 * crop window.
	 */
	public int backgroundColor;

	/**
	 * the min width the crop window is allowed to be. (in pixels)
	 */
	public int minCropWindowWidth;

	/**
	 * the min height the crop window is allowed to be. (in pixels)
	 */
	public int minCropWindowHeight;

	/**
	 * the min width the resulting cropping image is allowed to be, affects the cropping window
	 * limits. (in pixels)
	 */
	public int minCropResultWidth;

	/**
	 * the min height the resulting cropping image is allowed to be, affects the cropping window
	 * limits. (in pixels)
	 */
	public int minCropResultHeight;

	/**
	 * the max width the resulting cropping image is allowed to be, affects the cropping window
	 * limits. (in pixels)
	 */
	public int maxCropResultWidth;

	/**
	 * the max height the resulting cropping image is allowed to be, affects the cropping window
	 * limits. (in pixels)
	 */
	public int maxCropResultHeight;

	/**
	 * the title of the {@link ImageCropActivity}
	 */
	public CharSequence activityTitle;

	/**
	 * the color to use for action bar items icons
	 */
	public int activityMenuIconColor;

	/**
	 * the Android Uri to save the cropped image to
	 */
	public Uri outputUri;

	/**
	 * the compression format to use when writing the image
	 */
	public Bitmap.CompressFormat outputCompressFormat;

	/**
	 * the quality (if applicable) to use when writing the image (0 - 100)
	 */
	public int outputCompressQuality;

	/**
	 * the width to resize the cropped image to (see options)
	 */
	public int outputRequestWidth;

	/**
	 * the height to resize the cropped image to (see options)
	 */
	public int outputRequestHeight;

	/**
	 * the resize method to use on the cropped bitmap (see options documentation)
	 */
	public ImageCropView.RequestSizeOptions outputRequestSizeOptions;

	/**
	 * if the result of crop image activity should not save the cropped image bitmap
	 */
	public boolean noOutputImage;

	/**
	 * the initial rectangle to set on the cropping image after loading
	 */
	public Rect initialCropWindowRectangle;

	/**
	 * the initial rotation to set on the cropping image after loading (0-360 degrees clockwise)
	 */
	public int initialRotation;

	/**
	 * if to allow (all) rotation during cropping (activity)
	 */
	public boolean allowRotation;

	/**
	 * if to allow (all) flipping during cropping (activity)
	 */
	public boolean allowFlipping;

	/**
	 * if to allow counter-clockwise rotation during cropping (activity)
	 */
	public boolean allowCounterRotation;

	/**
	 * the amount of degrees to rotate clockwise or counter-clockwise
	 */
	public int rotationDegrees;

	/**
	 * whether the image should be flipped horizontally
	 */
	public boolean flipHorizontally;

	/**
	 * whether the image should be flipped vertically
	 */
	public boolean flipVertically;

	/**
	 * optional, the text of the crop menu crop button
	 */
	public CharSequence cropMenuCropButtonTitle;

	/**
	 * optional image resource to be used for crop menu crop icon instead of text
	 */
	public int cropMenuCropButtonIcon;

	/**
	 * Init options with defaults.
	 */
	public ImageCropOptions() {

		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();

		cropShape = ImageCropView.CropShape.RECTANGLE;
		snapRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm);
		touchRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, dm);
		guidelines = ImageCropView.Guidelines.ON_TOUCH;
		scaleType = ImageCropView.ScaleType.FIT_CENTER;
		showCropOverlay = true;
		showProgressBar = true;
		autoZoomEnabled = true;
		multiTouchEnabled = false;
		maxZoom = 4;
		initialCropWindowPaddingRatio = 0.1f;

		fixAspectRatio = false;
		aspectRatioX = 1;
		aspectRatioY = 1;

		borderLineThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm);
		borderLineColor = Color.argb(170, 255, 255, 255);
		borderCornerThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);
		borderCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
		borderCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, dm);
		borderCornerColor = Color.WHITE;

		guidelinesThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);
		guidelinesColor = Color.argb(170, 255, 255, 255);
		backgroundColor = Color.argb(119, 0, 0, 0);

		minCropWindowWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, dm);
		minCropWindowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, dm);
		minCropResultWidth = 40;
		minCropResultHeight = 40;
		maxCropResultWidth = 99999;
		maxCropResultHeight = 99999;

		activityTitle = "";
		activityMenuIconColor = 0;

		outputUri = Uri.EMPTY;
		outputCompressFormat = Bitmap.CompressFormat.JPEG;
		outputCompressQuality = 90;
		outputRequestWidth = 0;
		outputRequestHeight = 0;
		outputRequestSizeOptions = ImageCropView.RequestSizeOptions.NONE;
		noOutputImage = false;

		initialCropWindowRectangle = null;
		initialRotation = -1;
		allowRotation = true;
		allowFlipping = true;
		allowCounterRotation = false;
		rotationDegrees = 90;
		flipHorizontally = false;
		flipVertically = false;
		cropMenuCropButtonTitle = null;

		cropMenuCropButtonIcon = 0;
	}

	/**
	 * Builder used for creating Image Crop Activity by user request.
	 */
	public static final class Builder {

		/**
		 * The image to crop source Android uri.
		 */
		@Nullable
		private final Uri mSource;

		/**
		 * Options for image crop UX
		 */
		private final ImageCropOptions mOptions;

		public Builder(@Nullable Uri source) {
			mSource = source;
			mOptions = new ImageCropOptions();
		}

		/**
		 * Get {@link ImageCropActivity} intent to start the activity.
		 */
		public Intent getIntent(@NonNull Context context) {
			return getIntent(context, ImageCropActivity.class);
		}

		/**
		 * Get {@link ImageCropActivity} intent to start the activity.
		 */
		public Intent getIntent(@NonNull Context context, @Nullable Class<?> cls) {
			mOptions.validate();

			Intent intent = new Intent();
			intent.setClass(context, cls);
			Bundle bundle = new Bundle();
			bundle.putParcelable(CROP_IMAGE_EXTRA_SOURCE, mSource);
			bundle.putParcelable(CROP_IMAGE_EXTRA_OPTIONS, mOptions);
			intent.putExtra(CROP_IMAGE_EXTRA_BUNDLE, bundle);
			return intent;
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param activity activity to receive result
		 */
		public void start(@NonNull Activity activity) {
			mOptions.validate();
			activity.startActivityForResult(getIntent(activity), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param activity activity to receive result
		 */
		public void start(@NonNull Activity activity, @Nullable Class<?> cls) {
			mOptions.validate();
			activity.startActivityForResult(getIntent(activity, cls), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param fragment fragment to receive result
		 */
		public void start(@NonNull Context context, @NonNull Fragment fragment) {
			fragment.startActivityForResult(getIntent(context), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param fragment fragment to receive result
		 */
		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		public void start(@NonNull Context context, @NonNull android.app.Fragment fragment) {
			fragment.startActivityForResult(getIntent(context), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param fragment fragment to receive result
		 */
		public void start(
				@NonNull Context context, @NonNull Fragment fragment, @Nullable Class<?> cls) {
			fragment.startActivityForResult(getIntent(context, cls), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * Start {@link ImageCropActivity}.
		 *
		 * @param fragment fragment to receive result
		 */
		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		public void start(
				@NonNull Context context, @NonNull android.app.Fragment fragment, @Nullable Class<?> cls) {
			fragment.startActivityForResult(getIntent(context, cls), CROP_IMAGE_ACTIVITY_REQUEST_CODE);
		}

		/**
		 * The shape of the cropping window.<br>
		 * To set square/circle crop shape set aspect ratio to 1:1.<br>
		 * <i>Default: RECTANGLE</i>
		 */
		public Builder setCropShape(@NonNull ImageCropView.CropShape cropShape) {
			mOptions.cropShape = cropShape;
			return this;
		}

		/**
		 * An edge of the crop window will snap to the corresponding edge of a specified bounding box
		 * when the crop window edge is less than or equal to this distance (in pixels) away from the
		 * bounding box edge (in pixels).<br>
		 * <i>Default: 3dp</i>
		 */
		public Builder setSnapRadius(float snapRadius) {
			mOptions.snapRadius = snapRadius;
			return this;
		}

		/**
		 * The radius of the touchable area around the handle (in pixels).<br>
		 * We are basing this value off of the recommended 48dp Rhythm.<br>
		 * See: http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm<br>
		 * <i>Default: 48dp</i>
		 */
		public Builder setTouchRadius(float touchRadius) {
			mOptions.touchRadius = touchRadius;
			return this;
		}

		/**
		 * whether the guidelines should be on, off, or only showing when resizing.<br>
		 * <i>Default: ON_TOUCH</i>
		 */
		public Builder setGuidelines(@NonNull ImageCropView.Guidelines guidelines) {
			mOptions.guidelines = guidelines;
			return this;
		}

		/**
		 * The initial scale type of the image in the crop image view<br>
		 * <i>Default: FIT_CENTER</i>
		 */
		public Builder setScaleType(@NonNull ImageCropView.ScaleType scaleType) {
			mOptions.scaleType = scaleType;
			return this;
		}

		/**
		 * if to show crop overlay UI what contains the crop window UI surrounded by background over the
		 * cropping image.<br>
		 * <i>default: true, may disable for animation or frame transition.</i>
		 */
		public Builder setShowCropOverlay(boolean showCropOverlay) {
			mOptions.showCropOverlay = showCropOverlay;
			return this;
		}

		/**
		 * if auto-zoom functionality is enabled.<br>
		 * default: true.
		 */
		public Builder setAutoZoomEnabled(boolean autoZoomEnabled) {
			mOptions.autoZoomEnabled = autoZoomEnabled;
			return this;
		}

		/**
		 * if multi touch functionality is enabled.<br>
		 * default: true.
		 */
		public Builder setMultiTouchEnabled(boolean multiTouchEnabled) {
			mOptions.multiTouchEnabled = multiTouchEnabled;
			return this;
		}

		/**
		 * The max zoom allowed during cropping.<br>
		 * <i>Default: 4</i>
		 */
		public Builder setMaxZoom(int maxZoom) {
			mOptions.maxZoom = maxZoom;
			return this;
		}

		/**
		 * The initial crop window padding from image borders in percentage of the cropping image
		 * dimensions.<br>
		 * <i>Default: 0.1</i>
		 */
		public Builder setInitialCropWindowPaddingRatio(float initialCropWindowPaddingRatio) {
			mOptions.initialCropWindowPaddingRatio = initialCropWindowPaddingRatio;
			return this;
		}

		/**
		 * whether the width to height aspect ratio should be maintained or free to change.<br>
		 * <i>Default: false</i>
		 */
		public Builder setFixAspectRatio(boolean fixAspectRatio) {
			mOptions.fixAspectRatio = fixAspectRatio;
			return this;
		}

		/**
		 * the X,Y value of the aspect ratio.<br>
		 * Also sets fixes aspect ratio to TRUE.<br>
		 * <i>Default: 1/1</i>
		 *
		 * @param aspectRatioX the width
		 * @param aspectRatioY the height
		 */
		public Builder setAspectRatio(int aspectRatioX, int aspectRatioY) {
			mOptions.aspectRatioX = aspectRatioX;
			mOptions.aspectRatioY = aspectRatioY;
			mOptions.fixAspectRatio = true;
			return this;
		}

		/**
		 * the thickness of the guidelines lines (in pixels).<br>
		 * <i>Default: 3dp</i>
		 */
		public Builder setBorderLineThickness(float borderLineThickness) {
			mOptions.borderLineThickness = borderLineThickness;
			return this;
		}

		/**
		 * the color of the guidelines lines.<br>
		 * <i>Default: Color.argb(170, 255, 255, 255)</i>
		 */
		public Builder setBorderLineColor(int borderLineColor) {
			mOptions.borderLineColor = borderLineColor;
			return this;
		}

		/**
		 * thickness of the corner line (in pixels).<br>
		 * <i>Default: 2dp</i>
		 */
		public Builder setBorderCornerThickness(float borderCornerThickness) {
			mOptions.borderCornerThickness = borderCornerThickness;
			return this;
		}

		/**
		 * the offset of corner line from crop window border (in pixels).<br>
		 * <i>Default: 5dp</i>
		 */
		public Builder setBorderCornerOffset(float borderCornerOffset) {
			mOptions.borderCornerOffset = borderCornerOffset;
			return this;
		}

		/**
		 * the length of the corner line away from the corner (in pixels).<br>
		 * <i>Default: 14dp</i>
		 */
		public Builder setBorderCornerLength(float borderCornerLength) {
			mOptions.borderCornerLength = borderCornerLength;
			return this;
		}

		/**
		 * the color of the corner line.<br>
		 * <i>Default: WHITE</i>
		 */
		public Builder setBorderCornerColor(int borderCornerColor) {
			mOptions.borderCornerColor = borderCornerColor;
			return this;
		}

		/**
		 * the thickness of the guidelines lines (in pixels).<br>
		 * <i>Default: 1dp</i>
		 */
		public Builder setGuidelinesThickness(float guidelinesThickness) {
			mOptions.guidelinesThickness = guidelinesThickness;
			return this;
		}

		/**
		 * the color of the guidelines lines.<br>
		 * <i>Default: Color.argb(170, 255, 255, 255)</i>
		 */
		public Builder setGuidelinesColor(int guidelinesColor) {
			mOptions.guidelinesColor = guidelinesColor;
			return this;
		}

		/**
		 * the color of the overlay background around the crop window cover the image parts not in the
		 * crop window.<br>
		 * <i>Default: Color.argb(119, 0, 0, 0)</i>
		 */
		public Builder setBackgroundColor(int backgroundColor) {
			mOptions.backgroundColor = backgroundColor;
			return this;
		}

		/**
		 * the min size the crop window is allowed to be (in pixels).<br>
		 * <i>Default: 42dp, 42dp</i>
		 */
		public Builder setMinCropWindowSize(int minCropWindowWidth, int minCropWindowHeight) {
			mOptions.minCropWindowWidth = minCropWindowWidth;
			mOptions.minCropWindowHeight = minCropWindowHeight;
			return this;
		}

		/**
		 * the min size the resulting cropping image is allowed to be, affects the cropping window
		 * limits (in pixels).<br>
		 * <i>Default: 40px, 40px</i>
		 */
		public Builder setMinCropResultSize(int minCropResultWidth, int minCropResultHeight) {
			mOptions.minCropResultWidth = minCropResultWidth;
			mOptions.minCropResultHeight = minCropResultHeight;
			return this;
		}

		/**
		 * the max size the resulting cropping image is allowed to be, affects the cropping window
		 * limits (in pixels).<br>
		 * <i>Default: 99999, 99999</i>
		 */
		public Builder setMaxCropResultSize(int maxCropResultWidth, int maxCropResultHeight) {
			mOptions.maxCropResultWidth = maxCropResultWidth;
			mOptions.maxCropResultHeight = maxCropResultHeight;
			return this;
		}

		/**
		 * the title of the {@link ImageCropActivity}.<br>
		 * <i>Default: ""</i>
		 */
		public Builder setActivityTitle(CharSequence activityTitle) {
			mOptions.activityTitle = activityTitle;
			return this;
		}

		/**
		 * the color to use for action bar items icons.<br>
		 * <i>Default: NONE</i>
		 */
		public Builder setActivityMenuIconColor(int activityMenuIconColor) {
			mOptions.activityMenuIconColor = activityMenuIconColor;
			return this;
		}

		/**
		 * the Android Uri to save the cropped image to.<br>
		 * <i>Default: NONE, will create a temp file</i>
		 */
		public Builder setOutputUri(Uri outputUri) {
			mOptions.outputUri = outputUri;
			return this;
		}

		/**
		 * the compression format to use when writting the image.<br>
		 * <i>Default: JPEG</i>
		 */
		public Builder setOutputCompressFormat(Bitmap.CompressFormat outputCompressFormat) {
			mOptions.outputCompressFormat = outputCompressFormat;
			return this;
		}

		/**
		 * the quility (if applicable) to use when writting the image (0 - 100).<br>
		 * <i>Default: 90</i>
		 */
		public Builder setOutputCompressQuality(int outputCompressQuality) {
			mOptions.outputCompressQuality = outputCompressQuality;
			return this;
		}

		/**
		 * the size to resize the cropped image to.<br>
		 * Uses {@link ImageCropView.RequestSizeOptions#RESIZE_INSIDE} option.<br>
		 * <i>Default: 0, 0 - not set, will not resize</i>
		 */
		public Builder setRequestedSize(int reqWidth, int reqHeight) {
			return setRequestedSize(reqWidth, reqHeight, ImageCropView.RequestSizeOptions.RESIZE_INSIDE);
		}

		/**
		 * the size to resize the cropped image to.<br>
		 * <i>Default: 0, 0 - not set, will not resize</i>
		 */
		public Builder setRequestedSize(
				int reqWidth, int reqHeight, ImageCropView.RequestSizeOptions options) {
			mOptions.outputRequestWidth = reqWidth;
			mOptions.outputRequestHeight = reqHeight;
			mOptions.outputRequestSizeOptions = options;
			return this;
		}

		/**
		 * if the result of crop image activity should not save the cropped image bitmap.<br>
		 * Used if you want to crop the image manually and need only the crop rectangle and rotation
		 * data.<br>
		 * <i>Default: false</i>
		 */
		public Builder setNoOutputImage(boolean noOutputImage) {
			mOptions.noOutputImage = noOutputImage;
			return this;
		}

		/**
		 * the initial rectangle to set on the cropping image after loading.<br>
		 * <i>Default: NONE - will initialize using initial crop window padding ratio</i>
		 */
		public Builder setInitialCropWindowRectangle(Rect initialCropWindowRectangle) {
			mOptions.initialCropWindowRectangle = initialCropWindowRectangle;
			return this;
		}

		/**
		 * the initial rotation to set on the cropping image after loading (0-360 degrees clockwise).
		 * <br>
		 * <i>Default: NONE - will read image exif data</i>
		 */
		public Builder setInitialRotation(int initialRotation) {
			mOptions.initialRotation = (initialRotation + 360) % 360;
			return this;
		}

		/**
		 * if to allow rotation during cropping.<br>
		 * <i>Default: true</i>
		 */
		public Builder setAllowRotation(boolean allowRotation) {
			mOptions.allowRotation = allowRotation;
			return this;
		}

		/**
		 * if to allow flipping during cropping.<br>
		 * <i>Default: true</i>
		 */
		public Builder setAllowFlipping(boolean allowFlipping) {
			mOptions.allowFlipping = allowFlipping;
			return this;
		}

		/**
		 * if to allow counter-clockwise rotation during cropping.<br>
		 * Note: if rotation is disabled this option has no effect.<br>
		 * <i>Default: false</i>
		 */
		public Builder setAllowCounterRotation(boolean allowCounterRotation) {
			mOptions.allowCounterRotation = allowCounterRotation;
			return this;
		}

		/**
		 * The amount of degreees to rotate clockwise or counter-clockwise (0-360).<br>
		 * <i>Default: 90</i>
		 */
		public Builder setRotationDegrees(int rotationDegrees) {
			mOptions.rotationDegrees = (rotationDegrees + 360) % 360;
			return this;
		}

		/**
		 * whether the image should be flipped horizontally.<br>
		 * <i>Default: false</i>
		 */
		public Builder setFlipHorizontally(boolean flipHorizontally) {
			mOptions.flipHorizontally = flipHorizontally;
			return this;
		}

		/**
		 * whether the image should be flipped vertically.<br>
		 * <i>Default: false</i>
		 */
		public Builder setFlipVertically(boolean flipVertically) {
			mOptions.flipVertically = flipVertically;
			return this;
		}

		/**
		 * optional, set crop menu crop button title.<br>
		 * <i>Default: null, will use resource string: crop_image_menu_crop</i>
		 */
		public Builder setCropMenuCropButtonTitle(CharSequence title) {
			mOptions.cropMenuCropButtonTitle = title;
			return this;
		}

		/**
		 * Image resource id to use for crop icon instead of text.<br>
		 * <i>Default: 0</i>
		 */
		public Builder setCropMenuCropButtonIcon(@DrawableRes int drawableResource) {
			mOptions.cropMenuCropButtonIcon = drawableResource;
			return this;
		}
	}

	/**
	 * Create object from parcel.
	 */
	protected ImageCropOptions(Parcel in) {
		cropShape = ImageCropView.CropShape.values()[in.readInt()];
		snapRadius = in.readFloat();
		touchRadius = in.readFloat();
		guidelines = ImageCropView.Guidelines.values()[in.readInt()];
		scaleType = ImageCropView.ScaleType.values()[in.readInt()];
		showCropOverlay = in.readByte() != 0;
		showProgressBar = in.readByte() != 0;
		autoZoomEnabled = in.readByte() != 0;
		multiTouchEnabled = in.readByte() != 0;
		maxZoom = in.readInt();
		initialCropWindowPaddingRatio = in.readFloat();
		fixAspectRatio = in.readByte() != 0;
		aspectRatioX = in.readInt();
		aspectRatioY = in.readInt();
		borderLineThickness = in.readFloat();
		borderLineColor = in.readInt();
		borderCornerThickness = in.readFloat();
		borderCornerOffset = in.readFloat();
		borderCornerLength = in.readFloat();
		borderCornerColor = in.readInt();
		guidelinesThickness = in.readFloat();
		guidelinesColor = in.readInt();
		backgroundColor = in.readInt();
		minCropWindowWidth = in.readInt();
		minCropWindowHeight = in.readInt();
		minCropResultWidth = in.readInt();
		minCropResultHeight = in.readInt();
		maxCropResultWidth = in.readInt();
		maxCropResultHeight = in.readInt();
		activityTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
		activityMenuIconColor = in.readInt();
		outputUri = in.readParcelable(Uri.class.getClassLoader());
		outputCompressFormat = Bitmap.CompressFormat.valueOf(in.readString());
		outputCompressQuality = in.readInt();
		outputRequestWidth = in.readInt();
		outputRequestHeight = in.readInt();
		outputRequestSizeOptions = ImageCropView.RequestSizeOptions.values()[in.readInt()];
		noOutputImage = in.readByte() != 0;
		initialCropWindowRectangle = in.readParcelable(Rect.class.getClassLoader());
		initialRotation = in.readInt();
		allowRotation = in.readByte() != 0;
		allowFlipping = in.readByte() != 0;
		allowCounterRotation = in.readByte() != 0;
		rotationDegrees = in.readInt();
		flipHorizontally = in.readByte() != 0;
		flipVertically = in.readByte() != 0;
		cropMenuCropButtonTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
		cropMenuCropButtonIcon = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(cropShape.ordinal());
		dest.writeFloat(snapRadius);
		dest.writeFloat(touchRadius);
		dest.writeInt(guidelines.ordinal());
		dest.writeInt(scaleType.ordinal());
		dest.writeByte((byte) (showCropOverlay ? 1 : 0));
		dest.writeByte((byte) (showProgressBar ? 1 : 0));
		dest.writeByte((byte) (autoZoomEnabled ? 1 : 0));
		dest.writeByte((byte) (multiTouchEnabled ? 1 : 0));
		dest.writeInt(maxZoom);
		dest.writeFloat(initialCropWindowPaddingRatio);
		dest.writeByte((byte) (fixAspectRatio ? 1 : 0));
		dest.writeInt(aspectRatioX);
		dest.writeInt(aspectRatioY);
		dest.writeFloat(borderLineThickness);
		dest.writeInt(borderLineColor);
		dest.writeFloat(borderCornerThickness);
		dest.writeFloat(borderCornerOffset);
		dest.writeFloat(borderCornerLength);
		dest.writeInt(borderCornerColor);
		dest.writeFloat(guidelinesThickness);
		dest.writeInt(guidelinesColor);
		dest.writeInt(backgroundColor);
		dest.writeInt(minCropWindowWidth);
		dest.writeInt(minCropWindowHeight);
		dest.writeInt(minCropResultWidth);
		dest.writeInt(minCropResultHeight);
		dest.writeInt(maxCropResultWidth);
		dest.writeInt(maxCropResultHeight);
		TextUtils.writeToParcel(activityTitle, dest, flags);
		dest.writeInt(activityMenuIconColor);
		dest.writeParcelable(outputUri, flags);
		dest.writeString(outputCompressFormat.name());
		dest.writeInt(outputCompressQuality);
		dest.writeInt(outputRequestWidth);
		dest.writeInt(outputRequestHeight);
		dest.writeInt(outputRequestSizeOptions.ordinal());
		dest.writeInt(noOutputImage ? 1 : 0);
		dest.writeParcelable(initialCropWindowRectangle, flags);
		dest.writeInt(initialRotation);
		dest.writeByte((byte) (allowRotation ? 1 : 0));
		dest.writeByte((byte) (allowFlipping ? 1 : 0));
		dest.writeByte((byte) (allowCounterRotation ? 1 : 0));
		dest.writeInt(rotationDegrees);
		dest.writeByte((byte) (flipHorizontally ? 1 : 0));
		dest.writeByte((byte) (flipVertically ? 1 : 0));
		TextUtils.writeToParcel(cropMenuCropButtonTitle, dest, flags);
		dest.writeInt(cropMenuCropButtonIcon);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Validate all the options are withing valid range.
	 *
	 * @throws IllegalArgumentException if any of the options is not valid
	 */
	public void validate() {
		if (maxZoom < 0) {
			throw new IllegalArgumentException("Cannot set max zoom to a number < 1");
		}
		if (touchRadius < 0) {
			throw new IllegalArgumentException("Cannot set touch radius value to a number <= 0 ");
		}
		if (initialCropWindowPaddingRatio < 0 || initialCropWindowPaddingRatio >= 0.5) {
			throw new IllegalArgumentException(
					"Cannot set initial crop window padding value to a number < 0 or >= 0.5");
		}
		if (aspectRatioX <= 0) {
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		}
		if (aspectRatioY <= 0) {
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		}
		if (borderLineThickness < 0) {
			throw new IllegalArgumentException(
					"Cannot set line thickness value to a number less than 0.");
		}
		if (borderCornerThickness < 0) {
			throw new IllegalArgumentException(
					"Cannot set corner thickness value to a number less than 0.");
		}
		if (guidelinesThickness < 0) {
			throw new IllegalArgumentException(
					"Cannot set guidelines thickness value to a number less than 0.");
		}
		if (minCropWindowHeight < 0) {
			throw new IllegalArgumentException(
					"Cannot set min crop window height value to a number < 0 ");
		}
		if (minCropResultWidth < 0) {
			throw new IllegalArgumentException("Cannot set min crop result width value to a number < 0 ");
		}
		if (minCropResultHeight < 0) {
			throw new IllegalArgumentException(
					"Cannot set min crop result height value to a number < 0 ");
		}
		if (maxCropResultWidth < minCropResultWidth) {
			throw new IllegalArgumentException(
					"Cannot set max crop result width to smaller value than min crop result width");
		}
		if (maxCropResultHeight < minCropResultHeight) {
			throw new IllegalArgumentException(
					"Cannot set max crop result height to smaller value than min crop result height");
		}
		if (outputRequestWidth < 0) {
			throw new IllegalArgumentException("Cannot set request width value to a number < 0 ");
		}
		if (outputRequestHeight < 0) {
			throw new IllegalArgumentException("Cannot set request height value to a number < 0 ");
		}
		if (rotationDegrees < 0 || rotationDegrees > 360) {
			throw new IllegalArgumentException(
					"Cannot set rotation degrees value to a number < 0 or > 360");
		}
	}
}
