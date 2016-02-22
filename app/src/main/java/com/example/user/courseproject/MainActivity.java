package com.example.user.courseproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener {

    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    View rl;

    Button myButton;
    SurfaceHolder surfaceHolder;
    boolean recording;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recording = false; // по умолчанию запись отключена

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //что б не спал

        myCamera = getCameraInstance(); //получаем доступ к камере.Сам метод описан ниже
        if(myCamera == null){
            Toast.makeText(MainActivity.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);//присваиваем переменной нашу камеру
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        rl = myCameraPreview;
        rl.setOnTouchListener(this);
        myCameraPreview.addView(myCameraSurfaceView);//говорим что FramLayout отображает наш вид с камеры

        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                startContinuousAutoFocus();
        }
        return true;
    }

    Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object

                //Exit after saved
                finish();
            }else{

                //Release Camera before MediaRecorder start
                releaseCamera();

                if(!prepareMediaRecorder()){
                    Toast.makeText(MainActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

                mediaRecorder.start();
                recording = true;
                myButton.setText("STOP");
            }
        }};

    private Camera getCameraInstance(){
// TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); //пытаемся получить экземпляр
        }
        catch (Exception e){
            // исключения если с камерой что-то не так или её нет
        }
        return c; // returns null if camera is unavailable
    }

    private boolean prepareMediaRecorder() //здесь задаём все параметры для нашей записи
    {
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_HIGH));
        mediaRecorder.setVideoFrameRate(15); //fps
        mediaRecorder.setCaptureRate(5.0); //сохранение кадра каждые #.# секунд
        mediaRecorder.setOutputFile("/sdcard/tmp/" + System.nanoTime() + "_video.mp4");


        mediaRecorder.setMaxDuration(600000); // Set max duration 600 sec.
        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    boolean startContinuousAutoFocus() {

        Camera.Parameters params = myCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();

        String CAF_VIDEO = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
                supportedMode = focusModes
                        .contains(CAF_VIDEO) ? CAF_VIDEO : "";

        if (!supportedMode.equals("")) {

            params.setFocusMode(supportedMode);
            myCamera.setParameters(params);
            return true;
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }


    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }


}


