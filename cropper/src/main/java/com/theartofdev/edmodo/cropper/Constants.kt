package com.theartofdev.edmodo.cropper

class Constants {
    companion object {
        /**
         * The key used to pass crop image source URI to {@link ImageCropActivity}.
         */
        const val CROP_IMAGE_EXTRA_SOURCE = "CROP_IMAGE_EXTRA_SOURCE"

        /**
         * The key used to pass crop image options to {@link ImageCropActivity}.
         */
        const val CROP_IMAGE_EXTRA_OPTIONS = "CROP_IMAGE_EXTRA_OPTIONS"

        /**
         * The key used to pass crop image bundle data to {@link ImageCropActivity}.
         */
        const val CROP_IMAGE_EXTRA_BUNDLE = "CROP_IMAGE_EXTRA_BUNDLE"

        /**
         * The key used to pass crop image result data back from {@link ImageCropActivity}.
         */
        const val CROP_IMAGE_EXTRA_RESULT = "CROP_IMAGE_EXTRA_RESULT"

        /**
         * The request code used to start pick image activity to be used on result to identify the this
         * specific request.
         */
        const val PICK_IMAGE_CHOOSER_REQUEST_CODE = 200

        /**
         * The request code used to request permission to pick image from external storage.
         */
        const val PICK_IMAGE_PERMISSIONS_REQUEST_CODE = 201

        /**
         * The request code used to request permission to capture image from camera.
         */
        const val CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE = 2011

        /**
         * The request code used to start {@link ImageCropActivity} to be used on result to identify the
         * this specific request.
         */
        const val CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203

        /**
         * The result code used to return error from {@link ImageCropActivity}.
         */
        const val CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE = 204
    }
}