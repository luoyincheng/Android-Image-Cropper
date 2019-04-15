package com.theartofdev.edmodo.cropper.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.R;
import com.theartofdev.edmodo.cropper.ImageCrop;
import com.theartofdev.edmodo.cropper.ImageCropOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

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
		new ImageCropOptions.Builder(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// handle result of ImageCropActivity
		if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			ImageCrop.ActivityResult result = ImageCrop.getActivityResult(data);
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
