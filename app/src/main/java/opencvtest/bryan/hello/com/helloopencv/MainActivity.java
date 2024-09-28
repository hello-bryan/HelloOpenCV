package opencvtest.bryan.hello.com.helloopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getClass().getSimpleName();
    private boolean isOpenCvLoaded = false;
    private boolean isTest = false; // git 웹에서 수정함.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = findViewById(R.id.imageView);
        final ImageView imageView2 = findViewById(R.id.imageView2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !isOpenCvLoaded )
                    return;

                try {
                    InputStream is = getAssets().open("redshoes.jpg");
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);

                    Mat gray = new Mat();
                    Utils.bitmapToMat(bitmap, gray);

                    Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY);

                    Bitmap grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(gray, grayBitmap);

                    imageView2.setImageBitmap(grayBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            isOpenCvLoaded = true;
        }
    }
}
