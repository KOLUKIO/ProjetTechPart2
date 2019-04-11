package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Contrast {

    /**
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @return the histogram of the Bitmap
     */
    private static int[] histogram(Bitmap bmp){
        int[] hist = new int[256];
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                hist[Color.red(pixels[i])] += 1;
                hist[Color.green(pixels[i])] += 1;
                hist[Color.blue(pixels[i])] += 1;
            }
        }
        return hist;
    }

    static int[][] histogramRgb(Bitmap bmp){
        int[][] hist = new int[3][256];
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                hist[0][Color.red(pixels[i])] += 1;
                hist[1][Color.green(pixels[i])] += 1;
                hist[2][Color.blue(pixels[i])] += 1;
            }
        }
        return hist;
    }

    /**
     *
     * @param hist , the histogram of the Bitmap
     * @return a float corresponding to the minimum of the histogram
     */
    private static float getMinHistogram(int[] hist){
        for(int i=0; i<255; i++){
            if(hist[i]!=0) return i;
        }
        return 255;
    }

    /**
     *
     * @param hist , the histogram of the Bitmap
     * @return a float corresponding to the maximum of the histogram
     */
    private static float getMaxHistogram(int[] hist){
        for(int i=255; i>0; i--){
            if(hist[i]!=0) return i;
        }
        return 0;
    }


    /**
     * Allows to stretch the dynamic between 0 and 255
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @return the lut (Look Up Table) of the Bitmap
     */
    public static int[] lutContrastAuto(Bitmap bmp){
        int[] lut = new int[256];
        int[] hist = histogram(bmp);
        float minRgb = getMinHistogram(hist);
        float maxRgb = getMaxHistogram(hist);
        float result;
        for(int j=0; j<256; j++) {
            result = ((j - minRgb) / (maxRgb - minRgb)) * 255;
            lut[j] = (int) result;
        }
        return lut;
    }

    /**
     * Allows to reduce the contrast by tightening the histogram
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @return the lut (Look Up Table) of the Bitmap
     */
    public static int[] lutContrastLess(Bitmap bmp){
        int[] lut = new int[256];
        int[] hist = histogram(bmp);
        float minRgb = getMinHistogram(hist);
        float maxRgb = getMaxHistogram(hist);
        float center = (minRgb + maxRgb) / 2;
        for(int j=0; j<256; j++) {
            double result = (center + (j - center) * 0.70);
            lut[j] = (int) result;
        }
        return lut;
    }


    /**
     * Equalizes the histogram of the Bitmap by taking the average of the three RGB channels
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @return the new histogram
     */
    public static int[] histogramEqualization(Bitmap bmp){
        int[] histogram = histogram(bmp);
        float[] cHistogram = new float[256];  // cumulative histogram
        int[] lut = new int[256];             // equalized histogram
        float N = bmp.getWidth() * bmp.getHeight();
        cHistogram[0] = histogram[0]/3;
        for(int j=1; j<256; j++){
            cHistogram[j] = cHistogram[j-1] + histogram[j]/3;
            float result = (cHistogram[j] * 255) / N;
            lut[j] = (int) result;
        }
        return lut;
    }

    /**
     * Changes the contrast of the Bitmap according to the Look Up Table
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param lut , the Look Up Table corresponding to new pixel values
     */
    public static void contrast(Bitmap bmp, int[] lut){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = Color.rgb(lut[Color.red(pixels[i])], lut[Color.green(pixels[i])], lut[Color.blue(pixels[i])]);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }
}
