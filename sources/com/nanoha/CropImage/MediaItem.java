package com.nanoha.CropImage;

import java.util.Date;

public final class MediaItem {
    public static final int CLUSTERED = 1;
    public static final String ID = new String("id");
    public static final long MAX_VALID_DATE_IN_MS = 2049840000000L;
    public static final long MAX_VALID_DATE_IN_SEC = 2049840000;
    public static final int MEDIA_TYPE_IMAGE = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;
    public static final long MIN_VALID_DATE_IN_MS = 157680000000L;
    public static final long MIN_VALID_DATE_IN_SEC = 157680000;
    public static final int NOT_CLUSTERED = 0;
    public static final int NOT_PRIMED = 0;
    public static final int PRIMED = 2;
    public static final int STARTED_PRIMING = 1;
    private static final String VIDEO = "video/";
    public String mCaption = "";
    public int mClusteringState = 0;
    public String mContentUri;
    public long mDateAddedInSec = 0;
    public long mDateModifiedInSec = 0;
    public long mDateTakenInMs = 0;
    public String mDescription;
    private String mDisplayMimeType;
    public int mDurationInSec;
    public String mEditUri;
    public String mFilePath;
    public boolean mFlagForDelete;
    public String mGuid;
    public long mId;
    public double mLatitude;
    public Date mLocaltime;
    public double mLongitude;
    private int mMediaType = -1;
    public String mMicroThumbnailUri;
    public String mMimeType;
    public int mPrimingState = 0;
    public String mReverseGeocodedLocation;
    public String mRole;
    public float mRotation;
    public String mScreennailUri;
    public int mThumbnailFocusX;
    public int mThumbnailFocusY;
    public long mThumbnailId;
    public String mThumbnailUri;
    public boolean mTriedRetrievingExifDateTaken = false;
    public String mWeblink;

    public boolean isWellFormed() {
        return true;
    }

    public String toString() {
        return this.mCaption;
    }

    public boolean isLatLongValid() {
        return (this.mLatitude == 0.0d && this.mLongitude == 0.0d) ? false : true;
    }

    public boolean isDateTakenValid() {
        return this.mDateTakenInMs > MIN_VALID_DATE_IN_MS && this.mDateTakenInMs < MAX_VALID_DATE_IN_MS;
    }

    public boolean isDateModifiedValid() {
        return this.mDateModifiedInSec > MIN_VALID_DATE_IN_SEC && this.mDateModifiedInSec < MAX_VALID_DATE_IN_SEC;
    }

    public boolean isDateAddedValid() {
        return this.mDateAddedInSec > MIN_VALID_DATE_IN_SEC && this.mDateAddedInSec < MAX_VALID_DATE_IN_SEC;
    }

    public int getMediaType() {
        int i;
        if (this.mMediaType == -1) {
            if (this.mMimeType == null || !this.mMimeType.startsWith(VIDEO)) {
                i = 0;
            } else {
                i = 1;
            }
            this.mMediaType = i;
        }
        return this.mMediaType;
    }

    public void setMediaType(int mediaType) {
        this.mMediaType = mediaType;
    }

    public String getDisplayMimeType() {
        if (this.mDisplayMimeType == null && this.mMimeType != null) {
            int slashPos = this.mMimeType.indexOf(47);
            if (slashPos == -1 || slashPos + 1 >= this.mMimeType.length()) {
                this.mDisplayMimeType = this.mMimeType.toUpperCase();
            } else {
                this.mDisplayMimeType = this.mMimeType.substring(slashPos + 1).toUpperCase();
            }
        }
        return this.mDisplayMimeType == null ? "" : this.mDisplayMimeType;
    }

    public void setDisplayMimeType(String displayMimeType) {
        this.mDisplayMimeType = displayMimeType;
    }
}
