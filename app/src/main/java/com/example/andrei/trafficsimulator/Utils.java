package com.example.andrei.trafficsimulator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by corina on 5/13/17.
 */
public class Utils {

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
