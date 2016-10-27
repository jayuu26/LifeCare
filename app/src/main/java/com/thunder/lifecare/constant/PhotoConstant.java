package com.thunder.lifecare.constant;

public interface PhotoConstant {

	public static final String IMAGES_DEFAULT_PATH = "/mnt/sdcard";
	public static final String IMAGE_DIR = ".LifeCare";
	public String IMAGE_PATH = IMAGES_DEFAULT_PATH+"/"+IMAGE_DIR;

	//public static final String IMAGE_NAME_FORMAT = "yyyy-MM-dd_hh:mm:ss";
	public static final String IMAGE_NAME_FORMAT = "dd-MM-yyyy_hh:mm:ss";
	public static final String IMAGE_FORMAT = ".jpg";
	public static String IMAGE_NA = "NA";

	public static final int PHOTO_UPLOAD_SUCCESS = 1;
	public static final int PHOTO_UPLOAD_FAILURE = 0;

	public static final String KEY_NAME = "Name";
	public static final String KEY_PATH = "Path";
	

}
