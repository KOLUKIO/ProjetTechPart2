#pragma version(1)
#pragma rs java_package_name(project.part2.editimage)

uchar4 RS_KERNEL negative(uchar4 in) {
    uchar4 out = in ;
    out.r = 255 - in.r ;
    out.g = 255 - in.g ;
    out.b = 255 - in.b ;
    return out ;

}