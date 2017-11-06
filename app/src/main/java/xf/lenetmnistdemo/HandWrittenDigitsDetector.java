package xf.lenetmnistdemo;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Gerald on 06/11/2017.
 */

public class HandWrittenDigitsDetector {
    private static final String TAG = HandWrittenDigitsDetector.class.getSimpleName();

    public HandWrittenDigitsDetector() {
    }

    public int init(byte[] model_bin, byte[] param_bin) {
        return nativeInit(model_bin, param_bin);
    }

    public int detect(Bitmap bitmap) {
        return nativeDetect(bitmap);
    }

    public native int nativeInit(byte[] model_bin, byte[] param_bin);

    public native int nativeDetect(Bitmap bitmap);

    static {
        try {
            System.loadLibrary("hwdd");
            Log.d(TAG, "library loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
