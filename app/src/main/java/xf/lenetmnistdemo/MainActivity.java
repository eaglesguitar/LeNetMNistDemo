package xf.lenetmnistdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

            for (int i = 0; i < 10; i++) {
                n = detect(i + ".png");
                Log.d(TAG, "digitsDetector.detect " + i + " ==> " + n);
                if (n != i) {
                    Log.e(TAG, "digitsDetector.detect error: " + i + ", " + n);
                }
            }

            Bitmap b = Bitmap.createBitmap(28, 28, Bitmap.Config.ARGB_8888);
            n = digitsDetector.detect(b);
            Log.d(TAG, "digitsDetector.detect empty bitmap ==> " + n);

            Canvas c = new Canvas(b);
            c.drawColor(Color.RED);
            n = digitsDetector.detect(b);
            Log.d(TAG, "digitsDetector.detect red bitmap ==> " + n);

            c.drawColor(Color.GREEN);
            n = digitsDetector.detect(b);
            Log.d(TAG, "digitsDetector.detect green bitmap ==> " + n);

            c.drawColor(Color.BLUE);
            n = digitsDetector.detect(b);
            Log.d(TAG, "digitsDetector.detect blue bitmap ==> " + n);

            c.drawColor(Color.BLACK);
            n = digitsDetector.detect(b);
            Log.d(TAG, "digitsDetector.detect black bitmap ==> " + n);

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
