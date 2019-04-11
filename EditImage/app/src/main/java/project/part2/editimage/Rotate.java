package project.part2.editimage;

import android.graphics.Bitmap;

class Rotate {

    static Bitmap rotateLeft(Bitmap bmp){
        int newWidth = bmp.getHeight();
        int newHeight = bmp.getWidth();

        Bitmap rotateBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[newHeight];
        for(int y=0; y<newWidth; y++){
            bmp.getPixels(pixels, 0, newHeight, 0, y, newHeight, 1);
            for(int i=0; i<newHeight; i++){
                rotateBmp.setPixel(y, newHeight-1-i, pixels[i]);
            }
        }
        return rotateBmp;
    }

    static Bitmap rotateRight(Bitmap bmp){
        int newWidth = bmp.getHeight();
        int newHeight = bmp.getWidth();

        Bitmap rotateBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[newHeight];
        for(int y=0; y<newWidth; y++){
            bmp.getPixels(pixels, 0, newHeight, 0, y, newHeight, 1);
            for(int i=0; i<newHeight; i++){
                rotateBmp.setPixel(newWidth-1-y, i, pixels[i]);
            }
        }
        return rotateBmp;
    }
}
