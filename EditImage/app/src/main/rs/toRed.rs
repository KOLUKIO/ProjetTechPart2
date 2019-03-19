#pragma version(1)
#pragma rs java_package_name(project.part2.editimage)

uchar4 RS_KERNEL toRed(uchar4 in) {
    const float4 pixelf = rsUnpackColor8888(in);

    pixelf.g = 0;
    pixelf.b = 0;
    return rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}