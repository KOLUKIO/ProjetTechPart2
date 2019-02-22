#pragma version(1)
#pragma rs java_package_name(project.part2.editimage)

float H;

uchar4 RS_KERNEL colorize(uchar4 in) {

    const float4 pixelf = rsUnpackColor8888(in);

    float r1 = pixelf.r/255;
    float g1 = pixelf.g/255;
    float b1 = pixelf.b/255;

    float Cmax = max(r1, g1);
    Cmax = max(Cmax, b1);
    float Cmin = min(r1, g1);
    Cmin = min(Cmin, b1);

    float delta = Cmax - Cmin;



        float S;

        if (Cmax == 0){
            S = 0;
        }
        else{
            S = (delta/Cmax);
        }

        float V = Cmax;

        //H *= 6;
        float C = V * S;

        // fmod -> return float mod int
        // fabs -> return absolute value of a float
        float X = C * (1 - fabs(fmod(H/60, 2) -1));
        float m = V - C;

        float r2;
        float g2;
        float b2;

        if (H >= 0 && H < 60){
            r2 = C;
            g2 = X;
            b2 = 0;
        }
        if (H >= 60 && H < 120){
            r2 = X;
            g2 = C;
            b2 = 0;
        }
        if (H >= 120 && H < 180){
            r2 = 0;
            g2 = C;
            b2 = X;
        }
        if (H >= 180 && H < 240){
            r2 = 0;
            g2 = X;
            b2 = C;
        }
        if (H >= 240 && H < 300){
            r2 = X;
            g2 = 0;
            b2 = C;
        }
        if (H >= 300 && H < 360){
            r2 = C;
            g2 = 0;
            b2 = X;
        }

        float r3;
        float g3;
        float b3;

        r3 = (r2 + m) * 255;
        g3 = (g2 + m) * 255;
        b3 = (b2 + m) * 255;

    return rsPackColorTo8888(r3, g3, b3, pixelf.a);

}