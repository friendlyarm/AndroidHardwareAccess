LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := LCD1602 

LOCAL_CERTIFICATE := platform
LOCAL_MODULE_TAGS := eng

include $(BUILD_PACKAGE)
