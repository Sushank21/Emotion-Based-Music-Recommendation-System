package com.example.myspotify.ui

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.liveface.databinding.ActivityMainBinding
import com.example.myspotify.databinding.ActivityCameraBinding
import com.example.myspotify.databinding.ActivityEmotionModelBinding
import org.opencv.android.*
import org.opencv.android.CameraActivity
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import java.io.IOException


class CameraActivity : Activity(), CameraBridgeViewBase.CvCameraViewListener2 {
    companion object {
        private const val TAG = "EmotionModelActivity"
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 0
    }
    private lateinit var binding: ActivityCameraBinding
    private lateinit var mRgba: Mat
    private lateinit var mGray: Mat
    private var mOpenCvCameraView: CameraBridgeViewBase? = null
    private lateinit var facialExpressionRecognition: FacialExpressionRecognition
    private var mCameraId:Int =0

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCv Is loaded")

                    activateOpenCVCameraView()
                    try {
                        val inputSize = 48
                        Log.i(TAG, "called facial recognition OKKKKKKKKKK")
                        facialExpressionRecognition = FacialExpressionRecognition(assets, this@MainActivity, "model300.tflite", inputSize)
                    } catch (e: IOException) {
                        Log.i(TAG,"THIS IS ERROR")
                        e.printStackTrace()
                    }
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        }
        swapCamera()
        activateOpenCVCameraView()
        binding.clickCam.setOnClickListener{
            val emotion = facialExpressionRecognition.emo
            val intent = Intent(this, RecommendedPlaylist::class.java)
            intent.putExtra("CameraActivity", emotion)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Opencv initialization is done")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        } else {
            Log.d(TAG, "Opencv is not loaded. try again")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback)
        }
    }
    private fun activateOpenCVCameraView() {
        mOpenCvCameraView = binding.frameSurface
        Log.i(TAG, "Activity camera called")
        if(mOpenCvCameraView != null){
            Log.d(TAG, "Camera view found!")
        }else{
            Log.d(TAG, "Camera view not found!")
        }
        mOpenCvCameraView?.setCameraPermissionGranted()
        mOpenCvCameraView?.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_ANY)
        mOpenCvCameraView?.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView?.setCvCameraViewListener(this)
        mOpenCvCameraView?.enableView()
    }

    override fun onPause() {
        super.onPause()
        mOpenCvCameraView?.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mOpenCvCameraView?.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.i(TAG, "OonCameraViewStarted called")
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mGray = Mat(height, width, CvType.CV_8UC1)
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat? {
        mRgba = inputFrame.rgba() ?: mRgba
        mGray = inputFrame.gray() ?: mGray
        Core.rotate(mRgba, mRgba, Core.ROTATE_90_CLOCKWISE)
        if(mCameraId == 1){
            Core.flip(mRgba,mRgba,-1)
            Core.flip(mGray,mGray,-1)
        }
        mRgba = facialExpressionRecognition.recognizeImage(mRgba)
        return mRgba
    }

    private fun swapCamera(){
        mCameraId = mCameraId xor 1
        mOpenCvCameraView?.disableView()
        mOpenCvCameraView?.setCameraIndex(mCameraId)
        mOpenCvCameraView?.enableView()
    }
}