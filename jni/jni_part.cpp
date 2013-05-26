#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/calib3d/calib3d.hpp>
#include <opencv2/stitching/stitcher.hpp>


#include <vector>
#include <iostream>
#include <stdio.h>
#include <list>
#include <sstream>
#include <string>

using namespace std;
using namespace cv;

extern "C" {

JNIEXPORT void JNICALL Java_com_shu_Pano_helpers_CombinePhotoCv_StitchIt(
		JNIEnv*, jobject, jlong namedir,jint count,jlong im3) {

	vector<Mat> imgs;
	bool try_use_gpu = false;
	Mat& pano = *((Mat*) im3);
	// New testing

	string name;
    ostringstream convert1;
    convert1 << namedir;
    name = convert1.str();
	for (int k = 0; k <= count; k++) {
		string id;

		ostringstream convert;
		convert << k;
		id = convert.str();
		Mat img = imread("/sdcard/Pano/" + name +"/" + id + ".jpg");
		imgs.push_back(img);
	}

	Stitcher stitcher = Stitcher::createDefault(try_use_gpu);
	Stitcher::Status status = stitcher.stitch(imgs, pano);



}

}

