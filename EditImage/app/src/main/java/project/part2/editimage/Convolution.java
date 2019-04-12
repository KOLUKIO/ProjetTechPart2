package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.Type;
import android.util.Log;

import static project.part2.editimage.Filters.*;

public class Convolution {

    static double[][] matrixGaussian5 = {
            {1/98.0, 2/98.0, 3/98.0,  2/98.0, 1/98.0},
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
                Log.e("MATRIX","matrix = "+matrix[i][j]);
            }
        }
        return matrix;
    }

    // not good
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
        int len = matrix.length;
        int border = (len-1)/2;
        int[] pixels = new int[width-border*2];  // allows you to browse all the pixels of the image
        int[] pixelsMatrix = new int[len];  // allows to browse the neighbors of pixel
        double pixelRed, pixelGreen, pixelBlue;

        for(int y=border; y<height-border*2; y++){
            bmp.getPixels(pixels, 0, width-border*2, border, y, width-border*2, 1);
            for(int x=0; x<width-border*2; x++) {
                pixelRed = pixelGreen = pixelBlue = 0;

                for (int j = -border; j <= border; j++) {
                    bmp.getPixels(pixelsMatrix, 0, len, x, y + j, len, 1);
                    for (int i=0; i<len; i++) {
                        pixelRed += Color.red(pixelsMatrix[i]) * matrix[i][j + border];
                        pixelGreen += Color.green(pixelsMatrix[i]) * matrix[i][j + border];
                        pixelBlue += Color.blue(pixelsMatrix[i]) * matrix[i][j + border];
                    }
                }
                pixels[x] =  Color.rgb((int) pixelRed, (int) pixelGreen, (int) pixelBlue);
            }
            bmp.setPixels(pixels, 0, width-border*2, border, y, width-border*2, 1);
        }
    }


    private static int[] convolutionArr(int[] pixels, int i, int j, int w, int[] copy, int[][] h){
        int len = h.length;
        int n = len/2;
        int s = 0;
        for(int x = -n; x <= n; x++)
        {
            for (int y = -n; y <= n; y++)
            {
                int p = pixels[(i + y) + (j + x) * w];
                int r = Color.red(p);
                s = s + h[y + n][x + n] * r;
            }
        }
        copy[i + j * w] = s;
        return copy;
    }

    public static void sobelFilter(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int size = width * height;

        toGreyRS(bmp);

        int[][] h1 = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}} ; // operator of sobel h1

        int[][] h2 = {
                {-1,-2,-1},
                {0, 0, 0},
                {1, 2, 1}} ; // operator of sobel h2

        int len = h1.length;
        int[] p = new int[size];

        bmp.getPixels(p,0, width,0,0, width, height);

        int[] copy1 = new int[width*height];
        int[] copy2 = new int[width*height];
        int[] res = new int[width*height];

        bmp.getPixels(copy1,0,width,0,0, width, height);
        bmp.getPixels(copy2,0,width,0,0, width, height);
        bmp.getPixels(res,0,width,0,0, width, height);

        int n = len/2;

        for(int i = n; i < width - n; i++) // apply the masks on both copies
        {
            for(int j = n; j < height - n; j++)
            {
                copy1 = convolutionArr(p, i, j, width, copy1, h1);
                copy2 = convolutionArr(p, i, j, width, copy2, h2);
            }
        }

        for(int j = 0; j < width*height; j++)
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
        bmp.setPixels(res, 0, width,0,0, width, height);
    }

    public static void BlurRS(Bitmap bmp, int radius)
    {
        //Create renderscript
        RenderScript rs = RenderScript.create(MainActivity.getContext());

        //Create allocation from Bitmap
        Allocation allocation = Allocation.createFromBitmap(rs, bmp);

        Type t = allocation.getType();

        //Create allocation with the same type
        Allocation blurredAllocation = Allocation.createTyped(rs, t);

        //Create script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Set blur radius (maximum 25.0)
        blurScript.setRadius(radius);
        //Set input for script
        blurScript.setInput(allocation);
        //Call script for output allocation
        blurScript.forEach(blurredAllocation);

        //Copy script result into bitmap
        blurredAllocation.copyTo(bmp);

        //Destroy everything to free memory
        allocation.destroy();
        blurredAllocation.copyTo(bmp);
        blurredAllocation.destroy();
        blurScript.destroy();
        t.destroy();
        rs.destroy();

    }
}
