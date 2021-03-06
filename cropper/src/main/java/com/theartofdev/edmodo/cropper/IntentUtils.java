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
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_EXTRA_RESULT;
import static com.theartofdev.edmodo.cropper.Constants.PICK_IMAGE_CHOOSER_REQUEST_CODE;

/**
 * Helper to simplify crop image work like starting pick-image activity and handling camera/gallery
 * intents.<br>
 * The goal of the helper is to simplify the starting and most-common usage of image cropping and
 * not all purpose all possible scenario one-to-rule-them-all code base. So feel free to use it as
 * is and as a wiki to make your own.<br>
 * Added value you get out-of-the-box is some edge case handling that you may miss otherwise, like
 * the stupid-ass Android camera result URI that may differ from version to version and from device
 * to device.
 */
@SuppressWarnings("WeakerAccess, unused")
public final class IntentUtils {

	private IntentUtils() {
	}

	/**
	 * Start an activity to get image for cropping using chooser intent that will have all the
	 * available applications for the device like camera (MyCamera), galkery (Photos), store apps
	 * (DropBox), etc.<br>
	 * Use "pick_image_intent_chooser_title" string resource to override pick chooser title.
	 *
	 * @param activity the activity to be used to start activity from
	 */
	public static void startPickImageActivity(@NonNull Activity activity) {
		activity.startActivityForResult(
				getPickImageChooserIntent(activity), PICK_IMAGE_CHOOSER_REQUEST_CODE);
	}

	/**
	 * Same as {@link #startPickImageActivity(Activity) startPickImageActivity} method but instead of
	 * being called and returning to an Activity, this method can be called and return to a Fragment.
	 *
	 * @param context  The Fragments context. Use getContext()
	 * @param fragment The calling Fragment to start and return the image to
	 */
	public static void startPickImageActivity(@NonNull Context context, @NonNull Fragment fragment) {
		fragment.startActivityForResult(
				getPickImageChooserIntent(context), PICK_IMAGE_CHOOSER_REQUEST_CODE);
	}

	/**
	 * Create a chooser intent to select the source to get image from.<br>
	 * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br>
	 * All possible sources are added to the intent chooser.<br>
	 * Use "pick_image_intent_chooser_title" string resource to override chooser title.
	 *
	 * @param context used to access Android APIs, like content resolve, it is your
	 *                activity/fragment/widget.
	 */
	public static Intent getPickImageChooserIntent(@NonNull Context context) {
		return getPickImageChooserIntent(
				context, context.getString(R.string.pick_image_intent_chooser_title), false, true);
	}

	/**
	 * Create a chooser intent to select the source to get image from.<br>
	 * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br>
	 * All possible sources are added to the intent chooser.
	 *
	 * @param context          used to access Android APIs, like content resolve, it is your
	 *                         activity/fragment/widget.
	 * @param title            the title to use for the chooser UI
	 * @param includeDocuments if to include KitKat documents activity containing all sources
	 * @param includeCamera    if to include camera intents
	 */
	public static Intent getPickImageChooserIntent(
			@NonNull Context context,
			CharSequence title,
			boolean includeDocuments,
			boolean includeCamera) {

		List<Intent> allIntents = new ArrayList<>();
		PackageManager packageManager = context.getPackageManager();

		// collect all camera intents if Camera permission is available
		if (!isExplicitCameraPermissionRequired(context) && includeCamera) {
			allIntents.addAll(getCameraIntents(context, packageManager));
		}

		List<Intent> galleryIntents =
				getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
		if (galleryIntents.size() == 0) {
			// if no intents found for get-content try pick intent action (Huawei P9).
			galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
		}
		allIntents.addAll(galleryIntents);

		Intent target;
		if (allIntents.isEmpty()) {
			target = new Intent();
		} else {
			target = allIntents.get(allIntents.size() - 1);
			allIntents.remove(allIntents.size() - 1);
		}

		// Create a chooser from the main  intent
		Intent chooserIntent = Intent.createChooser(target, title);

		// Add all other intents
		chooserIntent.putExtra(
				Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

		return chooserIntent;
	}

	/**
	 * Get the main Camera intent for capturing image using device camera app. If the outputFileUri is
	 * null, a default Uri will be created with {@link #getCaptureImageOutputUri(Context)}, so then
	 * you will be able to get the pictureUri using {@link #getPickImageResultUri(Context, Intent)}.
	 * Otherwise, it is just you use the Uri passed to this method.
	 *
	 * @param context       used to access Android APIs, like content resolve, it is your
	 *                      activity/fragment/widget.
	 * @param outputFileUri the Uri where the picture will be placed.
	 */
	public static Intent getCameraIntent(@NonNull Context context, Uri outputFileUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (outputFileUri == null) {
			outputFileUri = getCaptureImageOutputUri(context);
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		return intent;
	}

	/**
	 * Get all Camera intents for capturing image using device camera apps.
	 */
	public static List<Intent> getCameraIntents(
			@NonNull Context context, @NonNull PackageManager packageManager) {

		List<Intent> allIntents = new ArrayList<>();

		// Determine Uri of camera image to  save.
		Uri outputFileUri = getCaptureImageOutputUri(context);

		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for (ResolveInfo res : listCam) {
			Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(res.activityInfo.packageName);
			if (outputFileUri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			}
			allIntents.add(intent);
		}

		return allIntents;
	}

	/**
	 * Get all Gallery intents for getting image from one of the apps of the device that handle
	 * images.
	 */
	public static List<Intent> getGalleryIntents(
			@NonNull PackageManager packageManager, String action, boolean includeDocuments) {
		List<Intent> intents = new ArrayList<>();
		Intent galleryIntent =
				action == Intent.ACTION_GET_CONTENT
						? new Intent(action)
						: new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		galleryIntent.setType("image/*");
		List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
		for (ResolveInfo res : listGallery) {
			Intent intent = new Intent(galleryIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(res.activityInfo.packageName);
			intents.add(intent);
		}

		// remove documents intent
		if (!includeDocuments) {
			for (Intent intent : intents) {
				if (intent
						.getComponent()
						.getClassName()
						.equals("com.android.documentsui.DocumentsActivity")) {
					intents.remove(intent);
					break;
				}
			}
		}
		return intents;
	}

	/**
	 * Check if explicitly requesting camera permission is required.<br>
	 * It is required in Android Marshmallow and above if "CAMERA" permission is requested in the
	 * manifest.<br>
	 * See <a
	 * href="http://stackoverflow.com/questions/32789027/android-m-camera-intent-permission-bug">StackOverflow
	 * question</a>.
	 */
	public static boolean isExplicitCameraPermissionRequired(@NonNull Context context) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& hasPermissionInManifest(context, "android.permission.CAMERA")
				&& context.checkSelfPermission(Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Check if the app requests a specific permission in the manifest.
	 *
	 * @param permissionName the permission to check
	 * @return true - the permission in requested in manifest, false - not.
	 */
	public static boolean hasPermissionInManifest(
			@NonNull Context context, @NonNull String permissionName) {
		String packageName = context.getPackageName();
		try {
			PackageInfo packageInfo =
					context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
			final String[] declaredPermissions = packageInfo.requestedPermissions;
			if (declaredPermissions != null && declaredPermissions.length > 0) {
				for (String p : declaredPermissions) {
					if (p.equalsIgnoreCase(permissionName)) {
						return true;
					}
				}
			}
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		return false;
	}

	/**
	 * Get URI to image received from capture by camera.
	 *
	 * @param context used to access Android APIs, like content resolve, it is your
	 *                activity/fragment/widget.
	 */
	public static Uri getCaptureImageOutputUri(@NonNull Context context) {
		Uri outputFileUri = null;
		File getImage = context.getExternalCacheDir();
		if (getImage != null) {
			outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
		}
		return outputFileUri;
	}

	/**
	 * Get the URI of the selected image from {@link #getPickImageChooserIntent(Context)}.<br>
	 * Will return the correct URI for camera and gallery image.
	 *
	 * @param context used to access Android APIs, like content resolve, it is your
	 *                activity/fragment/widget.
	 * @param data    the returned data of the activity result
	 */
	public static Uri getPickImageResultUri(@NonNull Context context, @Nullable Intent data) {
		boolean isCamera = true;
		if (data != null && data.getData() != null) {
			String action = data.getAction();
			isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
		}
		return isCamera || data.getData() == null ? getCaptureImageOutputUri(context) : data.getData();
	}

	/**
	 * Check if the given picked image URI requires READ_EXTERNAL_STORAGE permissions.<br>
	 * Only relevant for API version 23 and above and not required for all URI's depends on the
	 * implementation of the app that was used for picking the image. So we just test if we can open
	 * the stream or do we get an exception when we try, Android is awesome.
	 *
	 * @param context used to access Android APIs, like content resolve, it is your
	 *                activity/fragment/widget.
	 * @param uri     the result URI of image pick.
	 * @return true - required permission are not granted, false - either no need for permissions or
	 * they are granted
	 */
	public static boolean isReadExternalStoragePermissionsRequired(
			@NonNull Context context, @NonNull Uri uri) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED
				&& isUriRequiresPermissions(context, uri);
	}

	/**
	 * Test if we can open the given Android URI to test if permission required error is thrown.<br>
	 * Only relevant for API version 23 and above.
	 *
	 * @param context used to access Android APIs, like content resolve, it is your
	 *                activity/fragment/widget.
	 * @param uri     the result URI of image pick.
	 */
	public static boolean isUriRequiresPermissions(@NonNull Context context, @NonNull Uri uri) {
		try {
			ContentResolver resolver = context.getContentResolver();
			InputStream stream = resolver.openInputStream(uri);
			if (stream != null) {
				stream.close();
			}
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
