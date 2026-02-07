package com.example.kostkav3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;

import androidx.camera.core.Camera;



public class CameraXActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "extra_image_path";

    private PreviewView previewView;
    private Button btnCapture;

    private ImageCapture imageCapture;

    private Camera camera;

    public static final String EXTRA_LEFT_COLOR  = "extra_left_color";
    public static final String EXTRA_RIGHT_COLOR = "extra_right_color";
    public static final String EXTRA_DOWN_COLOR  = "extra_down_color";
    public static final String EXTRA_UP_COLOR    = "extra_up_color";
    public static final String EXTRA_C5_COLOR = "extra_c5_color";


    private Button btnLeft, btnRight, btnDown, btnUp, btnC5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_xactivity);

        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);

        startCamera();

        btnCapture.setOnClickListener(v -> takePhoto());

        btnLeft  = findViewById(R.id.cam_left);
        btnRight = findViewById(R.id.cam_right);
        btnDown  = findViewById(R.id.cam_down);
        btnUp    = findViewById(R.id.cam_up);
        btnC5 = findViewById(R.id.cam_c5);

// pobierz kolory z intentu (domyślnie białe, gdyby nie przyszły)
        Intent i = getIntent();
        int leftColor  = i.getIntExtra(EXTRA_LEFT_COLOR,  0xFFFFFFFF);
        int rightColor = i.getIntExtra(EXTRA_RIGHT_COLOR, 0xFFFFFFFF);
        int downColor  = i.getIntExtra(EXTRA_DOWN_COLOR,  0xFFFFFFFF);
        int upColor    = i.getIntExtra(EXTRA_UP_COLOR,    0xFFFFFFFF);
        int c5Color = i.getIntExtra(EXTRA_C5_COLOR, 0xFFFFFFFF);

// ustaw tła
        btnLeft.setBackgroundColor(leftColor);
        btnRight.setBackgroundColor(rightColor);
        btnDown.setBackgroundColor(downColor);
        btnUp.setBackgroundColor(upColor);
        btnC5.setBackgroundColor(c5Color);
        btnC5.setAlpha(0.5f);

    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        Executor executor = ContextCompat.getMainExecutor(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                // domyślny zoom (np. 1.6x). Zmieniaj wg potrzeby.
                camera.getCameraControl().setZoomRatio(1.6f);


            } catch (Exception e) {
                Toast.makeText(this, "Nie udało się uruchomić kamery: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }, executor);
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        // zapis do cache aplikacji
        File photoFile = new File(getCacheDir(), "cube_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        Executor executor = ContextCompat.getMainExecutor(this);

        imageCapture.takePicture(options, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    if (bmp == null) {
                        Toast.makeText(CameraXActivity.this, "Nie udało się wczytać zdjęcia",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    bmp = rotateUsingExif(photoFile.getAbsolutePath(), bmp);
                    bmp = cropCenterSquare(bmp);

                    //Skala do sensownego rozmiaru (dla 3x3 i kolorów wystarczy)
                    bmp = Bitmap.createScaledBitmap(bmp, 1200, 1200, true);


                    try (FileOutputStream fos = new FileOutputStream(photoFile)) {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    }

                    Intent result = new Intent();
                    result.putExtra(EXTRA_IMAGE_PATH, photoFile.getAbsolutePath());
                    setResult(RESULT_OK, result);
                    finish();

                } catch (Exception e) {
                    Toast.makeText(CameraXActivity.this,
                            "Błąd przetwarzania zdjęcia: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraXActivity.this,
                        "Błąd zdjęcia: " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private Bitmap cropCenterSquare(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        int size = Math.min(w, h);
        int x = (w - size) / 2;
        int y = (h - size) / 2;
        return Bitmap.createBitmap(src, x, y, size, size);
    }

    /**
     * Obrót bitmapy na podstawie EXIF z pliku JPG.
     * Dzięki temu zdjęcie będzie poprawnie obrócone na różnych urządzeniach.
     */
    private Bitmap rotateUsingExif(String path, Bitmap bitmap) {
        try {
            androidx.exifinterface.media.ExifInterface exif =
                    new androidx.exifinterface.media.ExifInterface(path);

            int orientation = exif.getAttributeInt(
                    androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
            );

            Matrix matrix = new Matrix();
            switch (orientation) {
                case androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                default:
                    return bitmap;
            }

            return Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (Exception e) {
            return bitmap; // jak EXIF nie wyjdzie, zwróć bez obrotu
        }
    }
}
