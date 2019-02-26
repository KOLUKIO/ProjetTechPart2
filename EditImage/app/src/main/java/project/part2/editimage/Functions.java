package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class Functions {

    public static void toNegative(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        int red, green, blue;
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                red = 255 - Color.red(pixels[i]);
                green = 255 - Color.green(pixels[i]);
                blue = 255 - Color.blue(pixels[i]);
                pixels[i] = Color.rgb(red, green, blue);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    public static void toNegativeRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_negative negative = new ScriptC_negative(rs);

        negative.forEach_negative(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        negative.destroy(); rs.destroy();
    }

    public static int RgbToGrey(int pixel){
        int grey = (int) (0.3*Color.red(pixel) + 0.59*Color.green(pixel) + 0.11*Color.blue(pixel));
        return Color.rgb(grey, grey, grey);
    }

    // filter
    // TP1 Question 10
    /**
     * Transforms color Bitmap to black and white Bitmap using getPixels() and setPixels()
     *
     */
    public static void toGrey(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        int grey;
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = RgbToGrey(pixels[i]);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    public static void toGreyRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_toGrey togrey = new ScriptC_toGrey(rs);

        togrey.forEach_toGrey(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        togrey.destroy(); rs.destroy();
    }

    /**
     * Keeps only the red color of pixels
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    public static void toRed(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = Color.rgb(Color.red(pixels[i]), 0, 0);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    // TP2 Question 3.1 Colorize picture
    /**
     *
     * @param p , a double
     * @param q , a double
     * @param t , a double corresponding to the hue
     * @return a double corresponding to the new color of one chanel RGB
     */
    private static double hueToRgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 0.166666666667) return (p + (q - p) * 6 * t);
        if (t < 0.5)            return q;
        if (t < 0.666666666667) return (p + (q - p) * (0.666666666667 - t) * 6);
        return p;
    }

    /**
     * Transforms rgb color in rgb color with new hue
     *
     * @param color , color to change hue
     * @param hue , hue to apply to the color
     * @return color with new hue
     */
    private static int newColor(int color, double hue) {
        // rgbToHsl
        double red = Color.red(color) / 255.0;
        double green = Color.green(color) / 255.0;
        double blue = Color.blue(color) / 255.0;

        double max = Math.max(red, Math.max(green, blue));
        double min = Math.min(red, Math.min(green, blue));

        double saturation, lightness = (max + min) / 2;
        if (max == min) {
            saturation = 0; // achromatic
        }else {
            if(lightness > 0.5) saturation = (max - min) / (2 - max - min);
            else                saturation = (max - min) / (max + min);
        }
        // HslToRgb
        if (saturation == 0) {
            red = green = blue = lightness; // achromatic
        }else {
            double q;
            if(lightness < 0.5) q = lightness * (1 + saturation);
            else                q = (lightness + saturation) - (saturation * lightness);
            double p = 2 * lightness - q;
            red   = hueToRgb(p, q, hue + 0.333333333333);
            green = hueToRgb(p, q, hue);
            blue  = hueToRgb(p, q, hue - 0.333333333333);
        }
        return Color.rgb((int) (red*255), (int) (green*255),  (int) (blue*255));
    }

    /**
     * Changes the hue of a Bitmap (the hue is chosen randomly) using getPixels() and setPixels()
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param hue , a double
     */
    public static void colorize(Bitmap bmp, double hue){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
       // double hue = Math.random();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = newColor(pixels[i], hue);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    public static void colorizeRS(Bitmap bmp, double hue){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_colorize colorize = new ScriptC_colorize(rs);
        colorize.set_H((float)hue);
        colorize.forEach_colorize(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        colorize.destroy(); rs.destroy();
    }

    // TP2 Question 3.2 Keep one color
    /**
     * Keeps the red color of the image
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    public static void keepRed(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                if(Color.red(pixels[i]) - Color.green(pixels[i]) < 95
                        || Color.red(pixels[i]) - Color.blue(pixels[i]) < 95){
                    pixels[i] = RgbToGrey(pixels[i]);
                }
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    public static void keepRedRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepRed keepRed = new ScriptC_keepRed(rs);

        keepRed.forEach_keepColor(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        keepRed.destroy(); rs.destroy();
    }

    // contrast
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

    // convolution

    double[][] matrixGaussian5 = {{1/98.0, 2/98.0, 3/98.0,  2/98.0, 1/98.0},
            {2/98.0, 6/98.0, 8/98.0,  6/98.0, 2/98.0},
            {3/98.0, 8/98.0, 10/98.0, 8/98.0, 3/98.0},
            {2/98.0, 6/98.0, 8/98.0,  6/98.0, 2/98.0},
            {1/98.0, 2/98.0, 3/98.0,  2/98.0, 1/98.0}};

    /**
     * Calculate the averaging filter
     *
     * @param size , a odd integer corresponding to the size of the matrix
     * @return a matrix
     */
    public static double[][] getMatrixAveraging(int size){
        double[][] matrix = new double[size][size];
        double nbPixel = size*size;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                matrix[i][j] = 1/nbPixel;
            }
        }
        return matrix;
    }

    // a revoir
    /**
     * Calculate the Gaussian filter
     *
     * @param size , a odd integer corresponding to the size of the matrix
     * @param sigma , a double
     * @return a matrix
     */
    public static double[][] getMatrixGaussian(int size, double sigma){
        double[][] matrix = new double[size][size];
        for(int x=0; x<size; x++){
            for(int y=0; y<size; y++){
                matrix[x][y] = Math.exp(-((Math.pow(x, 2)+Math.pow(y, 2))/
                        (2*Math.pow(sigma, 2))))*98;
                Log.e("GAUSS", " "+matrix[x][y]);
            }
        }
        return matrix;
    }

    /**
     * Modify the Bitmap according to the matrix
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param matrix , a matrix corresponding to the filter to apply to the Bitmap
     */
    public static void convolution(Bitmap bmp, double[][] matrix){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int size = matrix.length;
        int border = size-1;
        int pixel;
        double pixelRed, pixelGreen, pixelBlue;
        for(int x=0; x<width-border; x++){
            for(int y=0; y<height-border; y++){
                // get pixel matrix and sum of red, green and blue
                pixelRed = pixelGreen = pixelBlue = 0;
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){
                        pixel = bmp.getPixel(x+i, y+j);
                        pixelRed += Color.red(pixel)*matrix[i][j];
                        pixelGreen += Color.green(pixel)*matrix[i][j];
                        pixelBlue += Color.blue(pixel)*matrix[i][j];
                    }
                }
                bmp.setPixel(x+(border/2), y+(border/2), Color.rgb((int) pixelRed, (int) pixelGreen, (int) pixelBlue));
            }
        }
    }

    private static int[] convolution1(int[] pix,int i, int j, int w, int[] copy, int[][] h){
        int len = h.length;
        int n = len/2;
        int s = 0;
        for(int x = -n; x <= n; x++)
        {
            for (int y = -n; y <= n; y++)
            {
                int p = pix[(i + y) + (j + x) * w];
                int r = Color.red(p);
                s = s + h[y + n][x + n] * r;
            }
        }
        copy[i + j * w] = s;
        return copy;
    }

    public static void sobelFilter(Bitmap bmp){
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int size = w * h;

        toGrey(bmp);

        int[][] h1 = {{-1,0,1},
                        {-2,0,2},
                        {-1,0,1}} ; // Opérateur de sobel h1
        int[][] h2 = {{-1,-2,-1},
                        {0,0,0},
                        {1,2,1}} ; // Opérateur de sobel h2

        int len = h1.length;
        int[] p = new int[size];

        bmp.getPixels(p,0, w,0,0, w, h);

        int[] copy1 = new int[w*h];
        int[] copy2 = new int[w*h];
        int[] res = new int[w*h];

        bmp.getPixels(copy1,0,w,0,0, w, h);
        bmp.getPixels(copy2,0,w,0,0, w, h);
        bmp.getPixels(res,0,w,0,0, w, h);

        int n = len/2;

        for(int i = n; i < w - n; i++) // On applique les masques sur les deux copies
        {
            for(int j = n; j < h - n; j++)
            {
                copy1 = convolution1(p, i, j, w, copy1, h1);
                copy2 = convolution1(p, i, j, w, copy2, h2);
            }
        }

        for(int j = 0; j < w*h; j++) //
        {
            int c1 = copy1[j];
            int c2 = copy2[j];

            double g = Math.sqrt((c1 * c1) + (c2 * c2));
            if (g > 255)
            {
                g = 255;
            }

            int ng = (int) g;
            res[j] = Color.rgb(ng, ng, ng);
        }
        bmp.setPixels(res, 0, w,0,0, w, h);
    }
}
