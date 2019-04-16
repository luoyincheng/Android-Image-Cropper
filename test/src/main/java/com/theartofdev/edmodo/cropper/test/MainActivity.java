package com.theartofdev.edmodo.cropper.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.R;
import com.theartofdev.edmodo.cropper.ActivityResult;
import com.theartofdev.edmodo.cropper.BitmapUtils;
import com.theartofdev.edmodo.cropper.IntentUtils;
import com.theartofdev.edmodo.cropper.ImageCropOptions;
import com.theartofdev.edmodo.cropper.ImageCropView;

import java.io.FileNotFoundException;

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
		new ImageCropOptions.Builder(null).setGuidelines(ImageCropView.Guidelines.ON).start(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// handle result of ImageCropActivity
		if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			ActivityResult result = getActivityResult(data);
			if (resultCode == RESULT_OK) {
//				ContentResolver contentResolver = this.getContentResolver();
//				try {
//					Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(result.getUri()));
//					Log.i("onactivityersult", bitmap.getWidth() + ":" + bitmap.getHeight());
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
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
