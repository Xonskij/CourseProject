package com.example.user.courseproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity implements View.OnTouchListener {

    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    public static MediaRecorder mediaRecorder;
    public int current;

    Button myButton;
    Button button5;
    ImageView image;
    SurfaceHolder surfaceHolder;
    boolean recording;
    int[] images = {R.drawable.grid_null, R.drawable.grid};

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
        myCameraPreview.setOnTouchListener(this);
        myCameraPreview.addView(myCameraSurfaceView);//говорим что FramLayout отображает наш вид с камеры

        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);

        addListenerOnButton();
    }

/////////////////////////////////////////////////
    public void selectResolutoin(View v){

        SelectResolution my_dialog = new SelectResolution();
        my_dialog.show(getSupportFragmentManager(), "my_dialog");
    }

    public void selectFrameInterval(View v){

        SelectFrameInterval my_dialog1 = new SelectFrameInterval();
        my_dialog1.show(getSupportFragmentManager(), "my_dialog1");
    }

    public void selectFPS(View v){

        SelectFpsInVideo my_dialod2 = new SelectFpsInVideo();
        my_dialod2.show(getSupportFragmentManager(), "my_dialog2");
    }

    public void selectDuration(View v){

        SelectDuration my_dialog3 = new SelectDuration();
        my_dialog3.show(getSupportFragmentManager(), "my_dialog3");
    }

    private FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }
/////////////////////////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                startContinuousAutoFocus();
        }
        return true;
    }
//////
        public void addListenerOnButton() {

        image = (ImageView) findViewById(R.id.grid1);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            //image.setImageResource(R.drawable.grid);
            current++;
            current = current % images.length;
            image.setImageResource(images[current]);
        }
    });

}
////////
    Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(mediaRecorder != null) {
                mediaRecorder.stop();
                recording = false;
                releaseMediaRecorder();
            }
            else
            {
               releaseCamera();
            if (prepareMediaRecorder()) {
                recording=true;
                mediaRecorder.start();
            }
            else {
                recording = false;
                releaseMediaRecorder();
            }
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

        mediaRecorder.setProfile(CamcorderProfile.get(SelectResolution.size));
        mediaRecorder.setVideoFrameRate(SelectFpsInVideo.fps); //fps
        mediaRecorder.setCaptureRate(SelectFrameInterval.rate);//сохранение кадра каждые #.# секунд
        mediaRecorder.setMaxDuration(SelectDuration.dur);//продолжительность видео


        File wallpaperDirectory = new File("/sdcard/TimeLapseCamera/");
        wallpaperDirectory.mkdirs();
        mediaRecorder.setOutputFile("/sdcard/TimeLapseCamera/" + System.nanoTime() + "_video.mp4");

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
    protected void onResume() {
        super.onResume();
        initSpinners();
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



    void initSpinners() {
        // Цветовые эффекты
        // получаем список цветовых эффектов
        final List<String> colorEffects = myCamera.getParameters()
                .getSupportedColorEffects();
        Spinner spEffect = initSpinner(R.id.spEffect, colorEffects, myCamera
                .getParameters().getColorEffect());
        // обработчик выбора
        spEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Camera.Parameters params = myCamera.getParameters();
                params.setColorEffect(colorEffects.get(arg2));
                myCamera.setParameters(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //Баланс Белого
        final List<String> colorEffectsWB = myCamera.getParameters()
                .getSupportedWhiteBalance();
        Spinner spEffectWB = initSpinner(R.id.spEffectWB, colorEffectsWB, myCamera
                .getParameters().getWhiteBalance());
        // обработчик выбора
        spEffectWB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Camera.Parameters params = myCamera.getParameters();
                params.setWhiteBalance(colorEffectsWB.get(arg2));
                myCamera.setParameters(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //Режим фокуса
        final List<String> focusSelect = myCamera.getParameters()
                .getSupportedFocusModes();
        Spinner focusSel = initSpinner(R.id.focusSel, focusSelect, myCamera
                .getParameters().getFocusMode());
        // обработчик выбора
        focusSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Camera.Parameters params = myCamera.getParameters();
                params.setFocusMode(focusSelect.get(arg2));
                myCamera.setParameters(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //Выбор сцены
        final List<String> sceneMode = myCamera.getParameters()
                .getSupportedSceneModes();
        Spinner sceneMode1 = initSpinner(R.id.scene_mode, sceneMode, myCamera
                .getParameters().getSceneMode());
        // обработчик выбора
        sceneMode1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Camera.Parameters params = myCamera.getParameters();
                params.setSceneMode(sceneMode.get(arg2));
                myCamera.setParameters(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // Режимы вспышки
        // получаем список режимов вспышки
        final List<String> flashModes = myCamera.getParameters()
                .getSupportedFlashModes();
        // настройка спиннера
        Spinner spFlash = initSpinner(R.id.spFlash, flashModes, myCamera
                .getParameters().getFlashMode());
        // обработчик выбора
        spFlash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Camera.Parameters params = myCamera.getParameters();
                params.setFlashMode(flashModes.get(arg2));
                myCamera.setParameters(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    Spinner initSpinner(int spinnerId, List<String> data, String currentValue) {
        // настройка спиннера и адаптера для него
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // определеяем какое значение в списке является текущей настройкой
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            if (item.equals(currentValue)) {
                spinner.setSelection(i);
            }
        }

        return spinner;
    }


}


