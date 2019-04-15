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

import android.util.Pair;

import com.theartofdev.edmodo.cropper.ImageCropView;

/** The crop image view options that can be changed live. */
final class CropImageViewOptions {

  public ImageCropView.ScaleType scaleType = ImageCropView.ScaleType.CENTER_INSIDE;

  public ImageCropView.CropShape cropShape = ImageCropView.CropShape.RECTANGLE;

  public ImageCropView.Guidelines guidelines = ImageCropView.Guidelines.ON_TOUCH;

  public Pair<Integer, Integer> aspectRatio = new Pair<>(1, 1);

  public boolean autoZoomEnabled;

  public int maxZoomLevel;

  public boolean fixAspectRatio;

  public boolean multitouch;

  public boolean showCropOverlay;

  public boolean showProgressBar;

  public boolean flipHorizontally;

  public boolean flipVertically;
}
