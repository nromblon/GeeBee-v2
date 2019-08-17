package com.geebeelicious.capture;


import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicky on 11/19/2017.
 */

public class IPUtils {

    ImageView imageView1;
    public static Bitmap basicTresholdedForView(Mat ImageMat) {
        Mat newMat = new Mat();
        newMat = ImageMat.clone();
        //CONVERT to gray to be saved.
        int thresh_type = Imgproc.THRESH_BINARY;
        Imgproc.cvtColor(newMat, newMat, Imgproc.COLOR_BGR2GRAY);
        Mat display = new Mat();
        display = newMat;
        Imgproc.threshold(display, display, 100, 155, thresh_type);
        Bitmap bm = Bitmap.createBitmap(display.cols(), display.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(display, bm);


        return bm;

    }
    public static Mat basicThresholdAlgo(Mat ImageMat) {
        Mat newMat = new Mat();
        newMat = ImageMat;
//        Bitmap bm = basicTresholdedForView(newMat);
        Mat imageToThreshold = ImageMat.clone();
//        Utils.bitmapToMat(bm, imageToThreshold);

        //blur slghtly, reapply threshold.
        int thresh_type = Imgproc.THRESH_BINARY;
        org.opencv.core.Size s = new Size(7, 7);
        Imgproc.cvtColor(imageToThreshold, imageToThreshold, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(imageToThreshold, imageToThreshold, 100, 155, thresh_type);
        Imgproc.GaussianBlur(imageToThreshold, imageToThreshold, s, 0);
        Imgproc.threshold(imageToThreshold, imageToThreshold, 20, 155, thresh_type);

//        //perform edge detection, then perform a dilation + erosion to# close gaps in between object edges
        Imgproc.Canny(imageToThreshold, imageToThreshold, 50, 100);
        Imgproc.dilate(imageToThreshold, imageToThreshold, Imgproc.getStructuringElement(Imgproc.CHAIN_APPROX_NONE, new Size(2, 2)));
        Imgproc.erode(imageToThreshold, imageToThreshold, Imgproc.getStructuringElement(Imgproc.CHAIN_APPROX_NONE, new Size(2, 2)));

        return imageToThreshold;
    }


    public static Rect findReferenceObjectRect(Mat imageMat){
        Mat mat_startImage = imageMat.clone();
        int twentyPercent = (int) (imageMat.cols() * .20);
        int fifteenPercent = (int) (imageMat.rows() * .15);

        mat_startImage = cropImageForReference(mat_startImage,twentyPercent,fifteenPercent);

        mat_startImage = basicThresholdAlgo(mat_startImage);
        Rect referenceObject = findReferenceObject(mat_startImage);

        return  referenceObject;
    }

    private static Mat cropImageForReference(Mat mat_currentImage,int twentyPercent,int tenPercent) {
        int rightSet = (int) (mat_currentImage.cols() - twentyPercent);

        Mat roi = mat_currentImage.submat(0, mat_currentImage.rows(), twentyPercent/2, rightSet);
        mat_currentImage = roi;

        double uped = mat_currentImage.cols();
        //mat_currentImage.submat(mat_currentImage.rows() - tenPercent, mat_currentImage.rows(), 0, (int) uped).setTo(new Scalar(255, 255, 255));
        mat_currentImage.submat(0, tenPercent, 0, (int) uped).setTo(new Scalar(255, 255, 255));

        double fivePercent = mat_currentImage .rows()*0.05;
        roi = mat_currentImage.submat(0,mat_currentImage.rows(),0,mat_currentImage.cols());

        mat_currentImage = roi;

        return mat_currentImage;
    }


    private static Rect findReferenceObject(Mat imageMat){

        List<MatOfPoint> contours= new ArrayList<>();
        Mat sample = new Mat();
        Mat hierarchy = new Mat();
        //find left most bounding box.
        Imgproc.findContours(imageMat.clone(), contours , hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for( int i = 0; i< contours.size(); i++ )
        {
            Imgproc.drawContours(imageMat, contours, i, new Scalar(255,255,255), -1);
        }
        Rect referenceObject = Imgproc.boundingRect(contours.get(0));



        for (int j = 0; j < contours.size(); j++) {
//            if(Imgproc.contourArea(contours.get(j))>modifier){//if area is not large enough, ignore it.
            Rect r = Imgproc.boundingRect(contours.get(j));
            if(r.area()>1000) {
                if(referenceObject.area()<1000)
                {
                    referenceObject = r;
                }
                //gets the reference object contour.
                if (referenceObject.tl().x > r.tl().x) {
                    referenceObject = r;
                }
            }
        }

        System.out.println("ref got: "+referenceObject);
        return  referenceObject;

    }

    public static void display(ImageView imageView,Bitmap imageToDisplay){
        imageView.setImageBitmap(imageToDisplay);
    }
}