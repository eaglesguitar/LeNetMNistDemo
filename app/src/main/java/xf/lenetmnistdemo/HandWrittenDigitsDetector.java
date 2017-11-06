package xf.lenetmnistdemo;

import android.graphics.Bitmap;

/**
 * Created by Gerald on 06/11/2017.
 */

public class HandWrittenDigitsDetector {
    public HandWrittenDigitsDetector() {
    }

    public int init(byte[] net_bin, byte[] param_bin) {
        return native_init(net_bin, param_bin);
    }

    public int detect(Bitmap bitmap) {
        return native_detect(bitmap);
    }

    private native int native_init(byte[] net_bin, byte[] param_bin);

    private native int native_detect(Bitmap bitmap);
}
