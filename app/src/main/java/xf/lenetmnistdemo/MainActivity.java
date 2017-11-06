package xf.lenetmnistdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileNotFoundException;
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
        } catch (IOException e) {
            Log.e("MainActivity", "initSqueezeNcnn error");
        }
    }

    private void initSqueezeNcnn() throws IOException {
        byte[] param_bin;
        byte[] net_bin;
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
            net_bin = new byte[available];
            assetsInputStream.read(net_bin);
            assetsInputStream.close();
        }
        int r = digitsDetector.init(net_bin, param_bin);
        if (r != 0) {
            Log.e(TAG, "digitsDetector.init error: " + r);
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

}
