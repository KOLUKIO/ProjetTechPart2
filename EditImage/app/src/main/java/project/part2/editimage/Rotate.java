package project.part2.editimage;

import android.graphics.Bitmap;

public class Rotate {

    public static Bitmap rotateLeft(Bitmap bmp){
        int height = bmp.getHeight();
        int width = bmp.getWidth();

        Bitmap rotateBmp = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                rotateBmp.setPixel(y, width-1-i, pixels[i]);

            }
        }
        return rotateBmp;
    }

    public static Bitmap rotateRight(Bitmap bmp){
        int height = bmp.getHeight();
        int width = bmp.getWidth();

        Bitmap rotateBmp = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                rotateBmp.setPixel(height-1-y, i, pixels[i]);

            }
        }
        return rotateBmp;
    }
}
