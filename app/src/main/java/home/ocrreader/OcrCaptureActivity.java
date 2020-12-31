/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package home.ocrreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import home.loto.BaseActivity;
import home.loto.Lotto;
import home.loto.Lotto_List;
import home.loto.RW;
import home.loto.SetLotto;
import home.loto.R;
import home.ocrreader.ui.camera.CameraSource;
import home.ocrreader.ui.camera.CameraSourcePreview;
import home.ocrreader.ui.camera.GraphicOverlay;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

/**
 * Activity for the multi-tracker app.  It detects text with the
 * rear facing camera. During detection overlay graphics are drawn to mark the scanned text.
 */
public final class OcrCaptureActivity extends BaseActivity implements Observer {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String TextBlockObject = "String";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    Button f_btn;
    // Helper objects for detecting taps and pinches.
    //private ScaleGestureDetector scaleGestureDetector;
    //private GestureDetector gestureDetector;
    CheckBox cExtra;
    OcrDetectorProcessor p=null;
    private TextView progressTextView;
    private ProgressBar progressBar;
    public static final String KEY_PREF_AMOUNT =  "amount_preference";
    public static final String KEY_PREF_EXTRA =  "extra_preference";
    boolean FlashToggle = false;
    SharedPreferences prefs;
    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.ocr_capture);
        
        mPreview =  findViewById(R.id.preview);
        mGraphicOverlay =  findViewById(R.id.graphicOverlay);
        cExtra=findViewById(R.id.Extra);
        f_btn= findViewById(R.id.btnFlash);
        SeekBar seekbar =  findViewById(R.id.seekAmount);
        progressTextView = findViewById(R.id.txtAmount);
        progressBar=findViewById(R.id.pScan);
        progressBar.setProgress(0);
        prefs = PreferenceManager.getDefaultSharedPreferences(Lotto.getInstance());

        if(prefs.getBoolean(KEY_PREF_EXTRA,false)) progressBar.setMax(3);
        else progressBar.setMax(2);

        seekbar.setProgress(prefs.getInt(KEY_PREF_AMOUNT,0));
        progressTextView.setText(getString(R.string.txt_ocr_lines,String.valueOf(changeProgress(prefs.getInt(KEY_PREF_AMOUNT,0)))));

        cExtra.setChecked(prefs.getBoolean(KEY_PREF_EXTRA,false));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue=changeProgress(progress);
                progressTextView.setText(getString(R.string.txt_ocr_lines,String.valueOf(progressChangedValue)) );

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(KEY_PREF_AMOUNT, progress);
                editor.apply();
               if(p!=null) p.setAmount(progressChangedValue);
            }


            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        boolean autoFocus = true;
        boolean useFlash = false;

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        cExtra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                boolean on =  buttonView.isChecked();

                if(on) {
                    if(p!=null) p.setExtra(true);
                    progressBar.setMax(3);
                }
                else {
                    if(p!=null) p.setExtra(false);
                    progressBar.setMax(2);
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_PREF_EXTRA,on);
                editor.apply();
            }
        });

    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private int changeProgress(int progress) {
        int res;
        switch(progress) {
            case 0 :
                res=2;
                break; // optional

            case 1 :
                res=4;
                break; // optional
            case 2 :
                res=6;
                break; // optional

            case 3 :
                res=8;
                break; // optional
            case 4 :
                res=10;
                break; // optional
            case 5 :
                res=12;
                break; // optional
            case 6 :
                res=14;
                break; // optional

            default :
                res=0;
        }
    return res;
    }
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated processor instance
        // is set to receive the text recognition results and display graphics for each text block
        // on screen.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        p=new OcrDetectorProcessor(mGraphicOverlay,this);
        p.setAmount(changeProgress(prefs.getInt(KEY_PREF_AMOUNT,0)));
        p.setExtra(prefs.getBoolean(KEY_PREF_EXTRA,false));
        textRecognizer.setProcessor(p);

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        mCameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024 )
                .setRequestedFps(2.0f)
                .setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                .setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE)
                .build();

    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
            p.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // Check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
    /*React when all the necessary data is read*/
    @Override
    public void update(Observable o, Object arg) {
        if(arg=="gotData") {
            progressBar.setProgress(progressBar.getProgress()+1);
        }

        if (arg instanceof SetLotto) {
            RW rw =new RW(this);
            ArrayList<SetLotto> pSets = rw.LoadPlaySets();
            SetLotto pSet = (SetLotto) arg;
            pSets.add(pSet);
            rw.Save(pSets);

            Intent intent = new Intent(this, Lotto_List.class);

            startActivity(intent);

            finish();
        }
    }
    /*Provide camera flash function*/
    public void Flash(View view) {

        if(FlashToggle) {
            if (mCameraSource!=null) mCameraSource.setFlashMode(FLASH_MODE_TORCH);
            f_btn.setBackground(getResources().getDrawable(R.drawable.flash_on));
            FlashToggle=false;
        }
        else {
            if (mCameraSource!=null) mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            f_btn.setBackground(getResources().getDrawable(R.drawable.flash_off));
            FlashToggle=true;
        }
    }
}
