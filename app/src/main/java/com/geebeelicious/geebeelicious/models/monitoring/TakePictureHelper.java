package com.geebeelicious.geebeelicious.models.monitoring;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TakePictureHelper class contains the functionality to take a picture
 *
 * @author Mary Grace Malana
 * @since 21/05/2016.
 */
public class TakePictureHelper {

    /**
     * Where the picture will be shown.
     */
    private ImageView imageViewPlaceholder;

    /**
     * Path where the picture is saved.
     */
    private String mCurrentPhotoPath;

    /**
     * Constructor.
     *
     * @param imageViewPlaceholder {@link #imageViewPlaceholder}
     */
    public TakePictureHelper(ImageView imageViewPlaceholder) {
        this.imageViewPlaceholder = imageViewPlaceholder;
    }

    /**
     * Gets {@link #mCurrentPhotoPath}.
     *
     * @return {@link #mCurrentPhotoPath}
     */
    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    /**
     * Sets {@link #mCurrentPhotoPath}.
     *
     * @param mCurrentPhotoPath new value
     */
    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    /**
     * Convert bitmap to byte[].
     *
     * @param bitmap to be converted
     * @return byte[] value of the {@code bitmap}.
     */
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    /**
     * Creates the image.
     *
     * @return image file
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        //this is android code
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Show in {@link #imageViewPlaceholder} the picture taken.
     */
    public void setPic() {
        // Get the dimensions of the View
        imageViewPlaceholder.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        int targetW = imageViewPlaceholder.getMeasuredWidth();
        int targetH = imageViewPlaceholder.getMeasuredHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageViewPlaceholder.setImageBitmap(bitmap);
    }

    /**
     * Get the picture.
     *
     * @return picture.
     */
    public byte[] getPicture() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap vaccination = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        return getBytesFromBitmap(vaccination);
    }
}
