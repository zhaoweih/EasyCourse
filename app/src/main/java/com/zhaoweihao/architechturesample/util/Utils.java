package com.zhaoweihao.architechturesample.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;


public class Utils {
    public static void log(Class c,String msg) {
        Log.d(c.getSimpleName(),msg);
    }
    public static ProgressDialog createDialog(Context context) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("请稍等...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        return progress;

    }

    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }
}
