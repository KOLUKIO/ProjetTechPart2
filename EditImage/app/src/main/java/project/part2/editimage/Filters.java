package project.part2.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import java.util.Arrays;

import static project.part2.editimage.Convolution.BlurRS;

public class Filters {

    /**
     * Transforms a image to negative
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
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

    /**
     * Transforms a image to negative in renderscript
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    static void toNegativeRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_negative negative = new ScriptC_negative(rs);

        negative.forEach_negative(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        negative.destroy(); rs.destroy();
    }

    /**
     * Transforms rgb color to grey color
     *
     * @param pixel , an integer corresponding to the color pixel to change
     * @return an integer corresponding to the new color
     */
    private static int RgbToGrey(int pixel){
        int grey = (int) (0.3*Color.red(pixel) + 0.59*Color.green(pixel) + 0.11*Color.blue(pixel));
        return Color.rgb(grey, grey, grey);
    }

    /**
     * Transforms color Bitmap to black and white Bitmap using getPixels() and setPixels()
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    public static void toGrey(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = RgbToGrey(pixels[i]);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    /**
     * Transforms color Bitmap to black and white Bitmap in renderscript
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    static void toGreyRS(Bitmap bmp){
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

    /**
     * Keeps only the red color of pixels in renderscript
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    static void toRedRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_toRed toRed = new ScriptC_toRed(rs);

        toRed.forEach_toRed(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        toRed.destroy(); rs.destroy();
    }

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
     * Changes the hue of a Bitmap using getPixels() and setPixels()
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param hue , a double
     */
    public static void colorize(Bitmap bmp, double hue){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        for(int y=0; y<height; y++){
            bmp.getPixels(pixels, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                pixels[i] = newColor(pixels[i], hue);
            }
            bmp.setPixels(pixels, 0, width, 0, y, width, 1);
        }
    }

    /**
     * Changes the hue of a Bitmap in renderscript
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param hue , a double
     */
    static void colorizeRS(Bitmap bmp, double hue){
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

    /**
     * Keeps the red color of the image in renderscript
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    static void keepRedRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(MainActivity.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_keepRed keepRed = new ScriptC_keepRed(rs);

        keepRed.forEach_keepColor(input, output);
        output.copyTo(bmp);
        input.destroy(); output.destroy();
        keepRed.destroy(); rs.destroy();
    }

    /**
     * Allows to add bars
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     * @param nbBar , an integer corresponding to the number of bars wanted
     * @param widthBar , an integer corresponding to the width of the bars
     */
    static void addBarHorizontal(Bitmap bmp, int nbBar, int widthBar){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width];
        int grey;
        int spaceBetweenBars = height/(nbBar+1);

        for(int y=spaceBetweenBars; y<=spaceBetweenBars*nbBar; y+=spaceBetweenBars){
            for(int j=0; j<widthBar; j++){
                grey = j*255/widthBar;
                for(int i=0; i<width; i++){
                    pixels[i] = Color.rgb(grey, grey, grey);
                }
                bmp.setPixels(pixels, 0, width, 0, j+y, width, 1);
            }
        }
    }

    /**
     * Allows to apply pencil sketch effect
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    static void pencilSketch(Bitmap bmp){
        Bitmap bmpGrey, bmpNegative;
        bmpGrey = bmp;
        toGreyRS(bmpGrey);
        bmpNegative = bmpGrey;
        toNegativeRS(bmpNegative);
        BlurRS(bmpNegative, 3);

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels1 = new int[width];
        int[] pixels2 = new int[width];

        for(int y=0; y<height; y++){
            bmpNegative.getPixels(pixels1, 0, width, 0, y, width, 1);
            bmpGrey.getPixels(pixels2, 0, width, 0, y, width, 1);
            for(int i=0; i<width; i++){
                if(Color.red(pixels2[i]) == 255){
                    pixels1[i] = Color.rgb(255, 255, 255);
                }else {
                    int color =  ((Color.red(pixels1[i]) << 8) / (255 - Color.red(pixels2[i])));
                    if(255 > color){
                        pixels1[i] = Color.rgb(255, 255, 255);
                    }else {
                        pixels1[i] = Color.rgb(color, color, color);
                    }
                }
            }
            bmp.setPixels(pixels1, 0, width, 0, y, width, 1);
        }
    }

    /**
     * Allows to reduce noise of picture
     *
     * @param bmp , a Bitmap corresponding to the image to edit
     */
    public static void medianFilter(Bitmap bmp){
        int[] mask = new int[3*3];
        int[] p = new int[bmp.getWidth()*bmp.getHeight()];

        bmp.getPixels(p,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        int[] p2 = p;

        for(int i = 1; i < bmp.getWidth() - 1; i++) {
            for (int j = 1; j < bmp.getHeight() - 1; j++) {
                int index=0;
                for(int c = -1; c <= 1; c++) {
                    for (int l = -1; l <= 1; l++) {
                        mask[index] = p[(i + c)  + (j + l) * bmp.getWidth()];
                        index += 1;
                    }
                }
                Arrays.sort(mask);
                p2[i  + j * bmp.getWidth()] = mask[mask.length/2];
            }
        }
        bmp.setPixels(p2,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
}
