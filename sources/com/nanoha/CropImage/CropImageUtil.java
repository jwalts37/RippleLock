package com.nanoha.CropImage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import com.mobclick.android.UmengConstants;
import com.nanoha.CropImage.MonitoredActivity;
import com.nanoha.MyLockScreen_all.SelectWidgetActivity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import org.apache.http.conn.ClientConnectionManager;

public class CropImageUtil {
    private static long[] CRCTable = new long[SelectWidgetActivity.APPWIDGET_HOST_ID];
    private static final long INITIALCRC = -1;
    public static final int MEDIA_BUCKET_ID_INDEX = 10;
    public static final int MEDIA_CAPTION_INDEX = 1;
    public static final int MEDIA_DATA_INDEX = 8;
    public static final int MEDIA_DATE_ADDED_INDEX = 6;
    public static final int MEDIA_DATE_MODIFIED_INDEX = 7;
    public static final int MEDIA_DATE_TAKEN_INDEX = 5;
    public static final int MEDIA_ID_INDEX = 0;
    public static final int MEDIA_LATITUDE_INDEX = 3;
    public static final int MEDIA_LONGITUDE_INDEX = 4;
    public static final int MEDIA_MIME_TYPE_INDEX = 2;
    public static final int MEDIA_ORIENTATION_OR_DURATION_INDEX = 9;
    private static final long POLY64REV = -7661587058870466123L;
    public static final String[] PROJECTION_IMAGES = {"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "orientation", "bucket_id"};
    public static final String[] PROJECTION_VIDEOS = {"_id", "title", "mime_type", "latitude", "longitude", "datetaken", "date_added", "date_modified", "_data", "duration", "bucket_id"};
    private static final Uri STORAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String TAG = "CropImageUtil";
    private static final int UNCONSTRAINED = -1;
    public static final String URI_CACHE = getCachePath("hires-image-cache");
    private static boolean init = false;

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len <= 0) {
                in.close();
                out.close();
                return;
            }
            out.write(buf, 0, len);
        }
    }

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
            }
        }
    }

    public static final long Crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        if (!init) {
            for (int i = 0; i < 256; i++) {
                long part = (long) i;
                for (int j = 0; j < 8; j++) {
                    if ((((int) part) & 1) != 0) {
                        part = (part >> 1) ^ POLY64REV;
                    } else {
                        part >>= 1;
                    }
                }
                CRCTable[i] = part;
            }
            init = true;
        }
        int length = in.length();
        int k = 0;
        long crc = -1;
        while (k < length) {
            k++;
            crc = (crc >> 8) ^ CRCTable[(in.charAt(k) ^ ((int) crc)) & 255];
        }
        return crc;
    }

    public static final String getCachePath(String subFolderName) {
        return Environment.getExternalStorageDirectory() + "/Android/data/com.cooliris.media/cache/" + subFolderName;
    }

    public static final String createFilePathFromCrc64(long crc64, int maxResolution) {
        return String.valueOf(URI_CACHE) + crc64 + "_" + maxResolution + ".cache";
    }

    public static Bitmap createFromCache(long crc64, int maxResolution) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            if (crc64 != 0) {
                bitmap = BitmapFactory.decodeFile(createFilePathFromCrc64(crc64, maxResolution), options);
            }
            return bitmap;
        } catch (Exception e) {
            Exception exc = e;
            return null;
        }
    }

    private static final BufferedInputStream createInputStreamFromRemoteUrl(String uri, ClientConnectionManager connectionManager) {
        InputStream contentInput = null;
        if (connectionManager == null) {
            try {
                URLConnection conn = new URI(uri).toURL().openConnection();
                conn.connect();
                contentInput = conn.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (contentInput != null) {
            return new BufferedInputStream(contentInput, 4096);
        }
        return null;
    }

    private static int computeSampleSize(InputStream stream, int maxResolutionX, int maxResolutionY) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, (Rect) null, options);
        return computeSampleSize(options, Math.min(maxResolutionX, maxResolutionY) / 2, maxResolutionX * maxResolutionY);
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int lowerBound;
        int upperBound;
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        if (maxNumOfPixels == -1) {
            lowerBound = 1;
        } else {
            lowerBound = (int) Math.ceil(Math.sqrt((w * h) / ((double) maxNumOfPixels)));
        }
        if (minSideLength == -1) {
            upperBound = 128;
        } else {
            upperBound = (int) Math.min(Math.floor(w / ((double) minSideLength)), Math.floor(h / ((double) minSideLength)));
        }
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (minSideLength == -1) {
            return lowerBound;
        }
        return upperBound;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize > 8) {
            return ((initialSize + 7) / 8) * 8;
        }
        int roundedSize = 1;
        while (roundedSize < initialSize) {
            roundedSize <<= 1;
        }
        return roundedSize;
    }

    public static final Bitmap createFromUri(Context context, String uri, int maxResolutionX, int maxResolutionY, long cacheId, ClientConnectionManager connectionManager) throws IOException, URISyntaxException, OutOfMemoryError {
        long crc64;
        boolean local;
        BufferedInputStream bufferedInput;
        BufferedInputStream bufferedInput2;
        Bitmap bitmap;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        if (uri.startsWith(UmengConstants.AtomKey_Content)) {
            crc64 = cacheId;
        } else {
            crc64 = Crc64Long(uri);
        }
        Bitmap bitmap2 = createFromCache(crc64, maxResolutionX);
        if (bitmap2 != null) {
            Bitmap bitmap3 = bitmap2;
            return bitmap2;
        }
        if (uri.startsWith(UmengConstants.AtomKey_Content) || uri.startsWith("file://")) {
            local = true;
        } else {
            local = false;
        }
        if (uri.startsWith(UmengConstants.AtomKey_Content) || uri.startsWith("file")) {
            bufferedInput = new BufferedInputStream(context.getContentResolver().openInputStream(Uri.parse(uri)), 16384);
        } else {
            bufferedInput = createInputStreamFromRemoteUrl(uri, connectionManager);
        }
        if (bufferedInput != null) {
            options.inSampleSize = computeSampleSize((InputStream) bufferedInput, maxResolutionX, maxResolutionY);
            if (uri.startsWith(UmengConstants.AtomKey_Content) || uri.startsWith("file")) {
                bufferedInput2 = new BufferedInputStream(context.getContentResolver().openInputStream(Uri.parse(uri)), 16384);
            } else {
                bufferedInput2 = createInputStreamFromRemoteUrl(uri, connectionManager);
            }
            if (bufferedInput2 != null) {
                options.inDither = false;
                options.inJustDecodeBounds = false;
                new Thread("BitmapTimeoutThread") {
                    public void run() {
                        try {
                            Thread.sleep(6000);
                            options.requestCancelDecode();
                        } catch (InterruptedException e) {
                        }
                    }
                }.start();
                bitmap = BitmapFactory.decodeStream(bufferedInput2, (Rect) null, options);
            } else {
                bitmap = bitmap2;
            }
            if ((options.inSampleSize > 1 || !local) && bitmap != null) {
                writeToCache(crc64, bitmap, maxResolutionX / options.inSampleSize);
            }
            return bitmap;
        }
        Bitmap bitmap4 = bitmap2;
        return null;
    }

    public static void writeToCache(long crc64, Bitmap bitmap, int maxResolution) {
        String file = createFilePathFromCrc64(crc64, maxResolution);
        if (bitmap != null && file != null && crc64 != 0) {
            try {
                File fileC = new File(file);
                fileC.createNewFile();
                FileOutputStream fos = new FileOutputStream(fileC);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 16384);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public static MediaItem createMediaItemFromUri(Context context, Uri target, int mediaType) {
        Uri uri;
        String[] projection;
        MediaItem item = null;
        long id = ContentUris.parseId(target);
        ContentResolver cr = context.getContentResolver();
        String whereClause = "_id=" + Long.toString(id);
        if (mediaType == 0) {
            try {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } catch (Exception e) {
            }
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        if (mediaType == 0) {
            projection = PROJECTION_IMAGES;
        } else {
            projection = PROJECTION_VIDEOS;
        }
        Cursor cursor = cr.query(uri, projection, whereClause, (String[]) null, (String) null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                MediaItem item2 = new MediaItem();
                try {
                    populateMediaItemFromCursor(item2, cr, cursor, String.valueOf(uri.toString()) + "/");
                    item = item2;
                } catch (Exception e2) {
                    item = item2;
                }
            }
            cursor.close();
        }
        item.mId = id;
        return item;
    }

    public static final void populateMediaItemFromCursor(MediaItem item, ContentResolver cr, Cursor cursor, String baseUri) {
        item.mId = cursor.getLong(0);
        item.mCaption = cursor.getString(1);
        item.mMimeType = cursor.getString(2);
        item.mLatitude = cursor.getDouble(3);
        item.mLongitude = cursor.getDouble(4);
        item.mDateTakenInMs = cursor.getLong(5);
        item.mDateAddedInSec = cursor.getLong(6);
        item.mDateModifiedInSec = cursor.getLong(7);
        if (item.mDateTakenInMs == item.mDateModifiedInSec) {
            item.mDateTakenInMs = item.mDateModifiedInSec * 1000;
        }
        item.mFilePath = cursor.getString(8);
        if (baseUri != null) {
            item.mContentUri = String.valueOf(baseUri) + item.mId;
        }
        int itemMediaType = item.getMediaType();
        int orientationDurationValue = cursor.getInt(9);
        if (itemMediaType == 0) {
            item.mRotation = (float) orientationDurationValue;
        } else {
            item.mDurationInSec = orientationDurationValue;
        }
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0 || b == null) {
            return b;
        }
        Matrix m = new Matrix();
        m.setRotate((float) degrees, ((float) b.getWidth()) / 2.0f, ((float) b.getHeight()) / 2.0f);
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (b == b2) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError e) {
            return b;
        }
    }

    public static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == 6) {
            return 90.0f;
        }
        if (exifOrientation == 3) {
            return 180.0f;
        }
        if (exifOrientation == 8) {
            return 270.0f;
        }
        return 0.0f;
    }

    private static class BackgroundJob extends MonitoredActivity.LifeCycleAdapter implements Runnable {
        /* access modifiers changed from: private */
        public final MonitoredActivity mActivity;
        private final Runnable mCleanupRunner = new Runnable() {
            public void run() {
                BackgroundJob.this.mActivity.removeLifeCycleListener(BackgroundJob.this);
                if (BackgroundJob.this.mDialog.getWindow() != null) {
                    BackgroundJob.this.mDialog.dismiss();
                }
            }
        };
        /* access modifiers changed from: private */
        public final ProgressDialog mDialog;
        private final Handler mHandler;
        private final Runnable mJob;

        public BackgroundJob(MonitoredActivity activity, Runnable job, ProgressDialog dialog, Handler handler) {
            this.mActivity = activity;
            this.mDialog = dialog;
            this.mJob = job;
            this.mActivity.addLifeCycleListener(this);
            this.mHandler = handler;
        }

        public void run() {
            try {
                this.mJob.run();
            } finally {
                this.mHandler.post(this.mCleanupRunner);
            }
        }

        public void onActivityDestroyed(MonitoredActivity activity) {
            this.mCleanupRunner.run();
            this.mHandler.removeCallbacks(this.mCleanupRunner);
        }

        public void onActivityStopped(MonitoredActivity activity) {
            this.mDialog.hide();
        }

        public void onActivityStarted(MonitoredActivity activity) {
            this.mDialog.show();
        }
    }

    public static void startBackgroundJob(MonitoredActivity activity, String title, String message, Runnable job, Handler handler) {
        new Thread(new BackgroundJob(activity, job, ProgressDialog.show(activity, title, message, true, false), handler)).start();
    }

    public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp) {
        Bitmap b1;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (scaleUp || (deltaX >= 0 && deltaY >= 0)) {
            float bitmapWidthF = (float) source.getWidth();
            float bitmapHeightF = (float) source.getHeight();
            if (bitmapWidthF / bitmapHeightF > ((float) targetWidth) / ((float) targetHeight)) {
                float scale = ((float) targetHeight) / bitmapHeightF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            } else {
                float scale2 = ((float) targetWidth) / bitmapWidthF;
                if (scale2 < 0.9f || scale2 > 1.0f) {
                    scaler.setScale(scale2, scale2);
                } else {
                    scaler = null;
                }
            }
            if (scaler != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
            } else {
                b1 = source;
            }
            Bitmap b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - targetWidth) / 2, Math.max(0, b1.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
            if (b1 != source) {
                b1.recycle();
            }
            return b2;
        }
        Bitmap b22 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b22);
        int deltaXHalf = Math.max(0, deltaX / 2);
        int deltaY2 = Math.max(0, deltaY / 2);
        Rect src = new Rect(deltaXHalf, deltaY2, Math.min(targetWidth, source.getWidth()) + deltaXHalf, Math.min(targetHeight, source.getHeight()) + deltaY2);
        int dstX = (targetWidth - src.width()) / 2;
        int dstY = (targetHeight - src.height()) / 2;
        c.drawBitmap(source, src, new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY), (Paint) null);
        return b22;
    }

    public static int getExifOrientation(String filepath) {
        int orientation;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            Log.e(TAG, "cannot read exif", e);
        }
        if (exif == null || (orientation = exif.getAttributeInt("Orientation", -1)) == -1) {
            return 0;
        }
        switch (orientation) {
            case 3:
                return 180;
            case MEDIA_DATE_ADDED_INDEX /*6*/:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public static Uri addImage(ContentResolver cr, String title, long dateAdded, long dateTaken, Double latitude, Double longitude, String directory, String filename, Bitmap source, byte[] jpegData, int[] degree) {
        OutputStream outputStream;
        OutputStream outputStream2;
        OutputStream outputStream3;
        Throwable th;
        String filePath = String.valueOf(directory) + "/" + filename;
        try {
            File file = new File(directory);
            if (!file.exists()) {
                file.mkdirs();
            }
            OutputStream outputStream4 = new FileOutputStream(new File(directory, filename));
            if (source != null) {
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 75, outputStream4);
                    degree[0] = 0;
                } catch (FileNotFoundException e) {
                    ex = e;
                    outputStream = outputStream4;
                } catch (IOException e2) {
                    ex = e2;
                    outputStream2 = outputStream4;
                    Log.w(TAG, ex);
                    closeSilently(outputStream2);
                    OutputStream outputStream5 = outputStream2;
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    outputStream3 = outputStream4;
                    closeSilently(outputStream3);
                    throw th;
                }
            } else {
                outputStream4.write(jpegData);
                degree[0] = getExifOrientation(filePath);
            }
            closeSilently(outputStream4);
            long size = new File(directory, filename).length();
            ContentValues contentValues = new ContentValues(11);
            contentValues.put("title", title);
            contentValues.put("_display_name", filename);
            contentValues.put("datetaken", Long.valueOf(dateTaken));
            contentValues.put("date_modified", Long.valueOf(dateTaken));
            contentValues.put("date_added", Long.valueOf(dateAdded));
            contentValues.put("mime_type", "image/jpeg");
            contentValues.put("orientation", Integer.valueOf(degree[0]));
            contentValues.put("_data", filePath);
            contentValues.put("_size", Long.valueOf(size));
            if (!(latitude == null || longitude == null)) {
                contentValues.put("latitude", Float.valueOf(latitude.floatValue()));
                contentValues.put("longitude", Float.valueOf(longitude.floatValue()));
            }
            Uri insert = cr.insert(STORAGE_URI, contentValues);
            OutputStream outputStream6 = outputStream4;
            return insert;
        } catch (FileNotFoundException e3) {
            ex = e3;
            outputStream = null;
            try {
                Log.w(TAG, ex);
                closeSilently(outputStream2);
                FileOutputStream fileOutputStream = outputStream2;
                return null;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                outputStream3 = outputStream2;
                th = th4;
                closeSilently(outputStream3);
                throw th;
            }
        } catch (IOException e4) {
            ex = e4;
            outputStream2 = null;
            Log.w(TAG, ex);
            closeSilently(outputStream2);
            OutputStream outputStream52 = outputStream2;
            return null;
        } catch (Throwable th5) {
            th = th5;
            outputStream3 = null;
            closeSilently(outputStream3);
            throw th;
        }
    }
}
