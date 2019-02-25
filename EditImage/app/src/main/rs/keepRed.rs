#pragma version(1)
#pragma rs java_package_name(project.part2.editimage)

static const float4 weight = {0.299f, 0.587f, 0.114f, 0.0f};

uchar4 RS_KERNEL keepColor(uchar4 in) {
    const float4 pixelf = rsUnpackColor8888(in);

    float r = pixelf.r;
    float g = pixelf.g;
    float b = pixelf.b;

    if ((r-g)*255 < 95 || (r-b)*255 < 95) {
        float grey = 0.299*r + 0.587*g + 0.114*b;
        r = grey;
        b = grey;
        g = grey;
        return rsPackColorTo8888(r, g, b, pixelf.a);
    }
    else {
        return  rsPackColorTo8888(r, g, b, pixelf.a);
    }
}