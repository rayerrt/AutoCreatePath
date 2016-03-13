# Copyright 2007-2008 The Android Open Source Project

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_JAVA_LIBRARIES := vivo-framework
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := AutoCreateFilePath

LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled


include $(BUILD_PACKAGE)
