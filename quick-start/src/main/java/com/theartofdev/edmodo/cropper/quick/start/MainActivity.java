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

package com.theartofdev.edmodo.cropper.quick.start;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.ActivityResult;
import com.theartofdev.edmodo.cropper.IntentUtils;
import com.theartofdev.edmodo.cropper.ImageCropOptions;
import com.theartofdev.edmodo.cropper.ImageCropView;

import static com.theartofdev.edmodo.cropper.ActivityResult.getActivityResult;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.Constants.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Start pick image activity with chooser.
	 */
	public void onSelectImageClick(View view) {
		new ImageCropOptions
				.Builder(null)
				.setGuidelines(ImageCropView.Guidelines.ON)
				.setActivityTitle("My Crop")
				.setCropShape(ImageCropView.CropShape.RECTANGLE)
				.setCropMenuCropButtonTitle("Done")
				.setRequestedSize(400, 400)
				.setCropMenuCropButtonIcon(R.drawable.ic_launcher)
				.setMultiTouchEnabled(true)
				.setMinCropWindowSize(100,200)
				.start(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// handle result of ImageCropActivity
		if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			ActivityResult result = getActivityResult(data);
			if (resultCode == RESULT_OK) {
				((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
				Toast.makeText(
						this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
						.show();
			} else if (resultCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
			}
		}
	}
}
