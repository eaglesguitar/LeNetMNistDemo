package xf.lenetmnistdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private HandWrittenDigitsDetector digitsDetector = new HandWrittenDigitsDetector();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            initSqueezeNcnn();
            int n = detect("number_4.jpg");
            Log.e(TAG, "digitsDetector.detect number_4.jpg: " + n);

            n = detect("1.png");
            Log.e(TAG, "digitsDetector.detect 1 ==> " + n);

            n = detect("0.png");
            Log.e(TAG, "digitsDetector.detect 0 ==> " + n);

            n = detect("2.png");
            Log.e(TAG, "digitsDetector.detect 2 ==> " + n);

            n = detect("4.png");
            Log.e(TAG, "digitsDetector.detect 4 ==> " + n);

            n = detect("3.png");
            Log.e(TAG, "digitsDetector.detect 3 ==> " + n);

            n = detect("5.png");
            Log.e(TAG, "digitsDetector.detect 5 ==> " + n);

            n = detect("9.png");
            Log.e(TAG, "digitsDetector.detect 9 ==> " + n);

            n = detect("6.png");
            Log.e(TAG, "digitsDetector.detect 6 ==> " + n);

            n = detect("8.png");
            Log.e(TAG, "digitsDetector.detect 8 ==> " + n);

            n = detect("7.png");
            Log.e(TAG, "digitsDetector.detect 7 ==> " + n);
        } catch (IOException e) {
            Log.e("MainActivity", "initSqueezeNcnn error");
        }
    }

    private void initSqueezeNcnn() throws IOException {
        byte[] param_bin;
        byte[] model_bin;
        {
            InputStream assetsInputStream = getAssets().open("lenet.param.bin");
            int available = assetsInputStream.available();
            param_bin = new byte[available];
            assetsInputStream.read(param_bin);
            assetsInputStream.close();
        }
        {
            InputStream assetsInputStream = getAssets().open("lenet.bin");
            int available = assetsInputStream.available();
            model_bin = new byte[available];
            assetsInputStream.read(model_bin);
            assetsInputStream.close();
        }
        int r = digitsDetector.init(model_bin, param_bin);
        if (r != 0) {
            Log.e(TAG, "digitsDetector.init error: " + r);
        }
    }

    private int detect(String file) {
        InputStream in = null;
        try {
            in = getAssets().open(file);
            Bitmap number = BitmapFactory.decodeStream(in);
            if (number.getConfig() != Bitmap.Config.ARGB_8888) {
                number = number.copy(Bitmap.Config.ARGB_8888, false);
            }
            return digitsDetector.detect(number);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}
