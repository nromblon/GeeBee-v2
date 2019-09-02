package com.geebeelicious.geebeelicious.activities.capture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geebeelicious.geebeelicious.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CaptureActivityTablet extends AppCompatActivity {
    private static final String TAG = "GBCapture::Main";
    private static final int CAMERA_REQUEST1 = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private final int SELECT_PHOTO1 = 1;
    private final int SELECT_PHOTO2 = 2;
    private ImageView imageView1, imageView2;
    private Mat mat_targetOrigImage;
    private Bitmap bmp_targetImage;

    int numberOfTimesCalled;
    public int n = 0;
    Mat mat_adaptedTresholdedImage;
    Button btn_take_picture1, btn_load_image1, btn_take_picture2, btn_load_image2, btn_compute, btn_go_back, btn_cancel,btn_calibrate;
    TextView tv_height, tv_weight, tv_BMI;
    int currentImage = 0; // 0 for front, 1 for side
    Double final_height=0.0;
    Double final_weight = 0.0;
    Double final_BMI;
    private String path1, path2;
    private Bitmap bmp_silhouette, siFront, siSide;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablet_layout);

        instantiateVariables();

        btn_take_picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST1);
            }
        });

        btn_take_picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST2);
            }
        });

        btn_load_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO1);
            }
        });

        btn_load_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO2);
            }
        });

        btn_compute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int currentPhoto = 0;

                Mat mat_startImageSide = new Mat();
                getMatPhoto(currentPhoto).copyTo(mat_startImageSide);
                Mat mat_cloneForBiggestXSide = new Mat();
                getMatPhoto(currentPhoto).copyTo(mat_cloneForBiggestXSide);

                //System.out.println("**********SIDE**********");
                Double sideLengthS, sideLengthF;

                int twentyPercent = (int) (mat_startImageSide.cols() * .20);
                Rect Sref = IPUtils.findReferenceObjectRect(mat_startImageSide);

                Mat  Smat_topMostY;
                Smat_topMostY = cropImageForTopY(mat_startImageSide,Sref,twentyPercent);
                Smat_topMostY = IPUtils.basicThresholdAlgo(Smat_topMostY);
                Rect StopMostY = findTopMostY(Smat_topMostY);

                Rect biggestXSide=null;
                mat_cloneForBiggestXSide = cropImageForTopY(mat_cloneForBiggestXSide,Sref,twentyPercent);
                mat_cloneForBiggestXSide = IPUtils.basicThresholdAlgo(mat_cloneForBiggestXSide);
                biggestXSide = findBiggestX(mat_cloneForBiggestXSide);

                double pixelsPerMetric = (Sref.height/30.48)*1.144125879;
                sideLengthS = biggestXSide.width/pixelsPerMetric;

                System.out.println("Pixels Per Metric: "+pixelsPerMetric);
                double sideHeight = ((Sref.tl().y-StopMostY.y)/pixelsPerMetric)+60.96;
                System.out.println("Height: "+sideHeight);

                double Sslope = (Sref.tl().y-StopMostY.y)/(StopMostY.br().x-Sref.tl().x);
                System.out.println("Side Slope: "+Sslope);


                //System.out.println("*********FRONT*********");
                currentPhoto = 1;
                Mat mat_startImage = new Mat();
                getMatPhoto(currentPhoto).copyTo(mat_startImage);
                Mat mat_cloneForBiggestX = new Mat();
                getMatPhoto(currentPhoto).copyTo(mat_cloneForBiggestX);

                twentyPercent = (int) (mat_startImage.cols() * .20);
                Rect Fref = null;
                Fref = IPUtils.findReferenceObjectRect(mat_startImage);

                Mat Fmat_topMostY = null;
                Fmat_topMostY = cropImageForTopY(mat_startImage,Fref,twentyPercent);
                Fmat_topMostY = IPUtils.basicThresholdAlgo(Fmat_topMostY);
                Rect FtopMostY = null;
                FtopMostY = findTopMostY(Fmat_topMostY);

                Mat proxy = new Mat();
                Rect biggestX = null;
                mat_cloneForBiggestX = cropImageForTopY(mat_startImage,Fref,twentyPercent);
                mat_cloneForBiggestX.copyTo(proxy);

                mat_cloneForBiggestX = IPUtils.basicThresholdAlgo(mat_cloneForBiggestX);

                biggestX = findBiggestX(mat_cloneForBiggestX);



                pixelsPerMetric = (Fref.height/30.48)*1.1444125879;
                sideLengthF = biggestX.width/pixelsPerMetric;

                //System.out.println("%Warp: "+30.48/(sideLengthF/2));

                System.out.println("Pixels Per Metric: "+pixelsPerMetric);

                double Fslope = (Fref.tl().y-FtopMostY.y)/(FtopMostY.br().x-Fref.tl().x);
                System.out.println("Front Slope: "+Fslope);


                double frontHeight = ((Fref.tl().y-FtopMostY.y)/pixelsPerMetric)+60.96;
                System.out.println("Height: "+frontHeight+"\n");

                double tsHeight = sideHeight - sideHeight*((Sslope-Fslope)/Sslope*.1);
                final_height = (frontHeight+tsHeight)/2;
                final_weight = (sideLengthS*0.112056783)+(-0.084950304*sideLengthF)+(0.412067434*final_height)-27.12699289;

                final_BMI = final_weight / (final_height * final_height) * 10000;
                tv_height.setText(String.format("Height: %.2f", final_height) + "cm");
                tv_weight.setText(String.format("Weight: %.2f", final_weight) + "kg");
                tv_BMI.setText(String.format("BMI: %.2f", final_BMI));
            }

        });


        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (final_height == 0 || final_weight == 0) { //if no height or weight was entered
                    Toast.makeText(getBaseContext(), "No height and weight detected", Toast.LENGTH_LONG).show();
                } else {
                    // convert the silhouette from bitmap to byte[]
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bmp_silhouette.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] silhouette = stream.toByteArray();

                    Intent backIntent = getIntent();
                    backIntent.putExtra("height", final_height);
                    backIntent.putExtra("weight", final_weight);
//                    backIntent.putExtra("silhouette", silhouette);
                    setResult(RESULT_OK, backIntent);
                    finish();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = getIntent();
                setResult(RESULT_CANCELED, backIntent);
                finish();
            }
        });
    }

    private void instantiateVariables() {
//        mContext = getBaseContext();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        numberOfTimesCalled = 0;
        mat_targetOrigImage = new Mat();
        mat_adaptedTresholdedImage = new Mat();
//        btn_calibrate = (Button) this.findViewById(R.id.btn_calibrate);
        imageView1 = (ImageView) this.findViewById(R.id.imageView1);
        imageView2 = (ImageView) this.findViewById(R.id.imageView2);
        btn_take_picture1 = (Button) this.findViewById(R.id.btn_take_picture1);
        btn_load_image1 = (Button) this.findViewById(R.id.btn_load_image1);
        btn_take_picture2 = (Button) this.findViewById(R.id.btn_take_picture2);
        btn_load_image2 = (Button) findViewById(R.id.btn_load_image2);
        btn_compute = (Button) findViewById(R.id.btn_compute);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_BMI = (TextView) findViewById(R.id.tv_BMI);

        btn_go_back = (Button) findViewById(R.id.btn_go_back);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK ||
                requestCode == CAMERA_REQUEST2 && resultCode == Activity.RESULT_OK ||
                requestCode == SELECT_PHOTO1 && resultCode == Activity.RESULT_OK ||
                requestCode == SELECT_PHOTO2 && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                bmp_targetImage = selectedImage;

                Utils.bitmapToMat(bmp_targetImage, mat_targetOrigImage);
                path1="";
                path2="";
                if(requestCode == CAMERA_REQUEST1 || requestCode == SELECT_PHOTO1){
                    //currentImage = 0;
                    imageView1.setImageBitmap(selectedImage);
                    siFront = selectedImage;
                    path1 = getRealPathFromURI(imageUri);
                }
                if(requestCode == CAMERA_REQUEST2 || requestCode == SELECT_PHOTO2){
                    //currentImage = 1;
                    imageView2.setImageBitmap(selectedImage);
                    siSide = selectedImage;
                    path2 = getRealPathFromURI(imageUri);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }

    private static Mat cropImageForTopY(Mat mat_currentImage,Rect reference,int twentyCropped){

        int twentyPercent = (int) (mat_currentImage.cols() * .20);

        int rightSet = (int) (mat_currentImage.cols() - twentyPercent);
//        Mat roi = new Mat();
        Mat roi;
        roi = mat_currentImage.submat(0,mat_currentImage.rows(), (int) reference.br().x+twentyCropped,rightSet);
        mat_currentImage = roi;
        //roi.copyTo(mat_currentImage);

        //FOR ROW
        double uped = mat_currentImage.cols();
        int tenPercent = (int) (mat_currentImage.rows() * .10);

        mat_currentImage.submat(mat_currentImage.rows() - tenPercent, mat_currentImage.rows(), 0, (int) uped).setTo(new Scalar(255, 255, 255));
        return  mat_currentImage;
    }

    private static Rect findTopMostY(Mat mat_currentImage){
        Rect lowestY = null;
        List<MatOfPoint> frontContours = new ArrayList<>();
        Mat mat_topMostY = mat_currentImage.clone();
        Imgproc.findContours(mat_topMostY, frontContours , new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);
        Double fivePercent = mat_currentImage.rows()*.1;
        try {
            lowestY = Imgproc.boundingRect(frontContours.get(0));
        }catch (IndexOutOfBoundsException error){
            System.out.println("no bounding boxes found");
        }
        if(frontContours.size()>0) {
            for (int j = 0; j < frontContours.size(); j++) {

                Rect r = Imgproc.boundingRect(frontContours.get(j));
                if (r.area() > 300) {
                    //takes the heighest y coordinate, thus the top of the head of the person.
                    if(r.height<fivePercent){
                        //do nothing.
                    }
                    else if (lowestY.tl().y > r.tl().y) {
                        lowestY = r;
                    }
                }
            }
        }
        if(lowestY==null){
        }
        System.out.println("lowest Y got: "+lowestY);
        return lowestY;

    }

    private static Rect findBiggestX(Mat mat_currentImage){
        Rect biggestX = null;
        List<MatOfPoint> frontContours = new ArrayList<>();
        Mat mat_biggestX = mat_currentImage.clone();
        Imgproc.findContours(mat_biggestX, frontContours , new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);
        biggestX = Imgproc.boundingRect(frontContours.get(0));
        for (int j = 0; j < frontContours.size(); j++) {

//            if(Imgproc.contourArea(contours.get(j))>modifier){//if area is not large enough, ignore it.
            Rect r = Imgproc.boundingRect(frontContours.get(j));
            if(r.area()>300 && r.br().y > mat_currentImage.height()-mat_currentImage.height()*0.15) {
                //takes the heighest y coordinate, thus the top of the head of the person.

                //gets the reference object contour.
                if (biggestX.width < r.width) {
                    biggestX = r;
                }
            }
        }
        return biggestX;
    }

    private Mat getMatPhoto(int currentPhoto){
        Bitmap image =null;
        if(currentPhoto ==1 ){
            //image =((BitmapDrawable)imageView1.getDrawable()).getBitmap();
            image = siFront;
        }else{
            //image = ((BitmapDrawable)imageView2.getDrawable()).getBitmap();
            image = siSide;
        }



        Mat mat = new Mat();
        Utils.bitmapToMat(image,mat);
        return mat;
    }
}