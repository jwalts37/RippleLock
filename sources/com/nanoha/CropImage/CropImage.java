package com.nanoha.CropImage;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.mobclick.android.UmengConstants;
import com.nanoha.CropImage.BitmapManager;
import com.nanoha.MyLockScreen_all.R;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import org.apache.http.conn.ClientConnectionManager;

public class CropImage extends MonitoredActivity {
    public static final int CROP_MSG = 10;
    public static final int CROP_MSG_INTERNAL = 100;
    public static final String DOWNLOAD_BUCKET_NAME = (String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/" + DOWNLOAD_STRING);
    public static final String DOWNLOAD_STRING = "download";
    private static final String TAG = "CropImage";
    /* access modifiers changed from: private */
    public int mAspectX;
    /* access modifiers changed from: private */
    public int mAspectY;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    /* access modifiers changed from: private */
    public boolean mCircleCrop = false;
    private ContentResolver mContentResolver;
    HighlightView mCrop;
    private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
    /* access modifiers changed from: private */
    public boolean mDoFaceDetection = true;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public CropImageView mImageView;
    private MediaItem mItem;
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
    private int mOutputX;
    private int mOutputY;
    Runnable mRunFaceDetection = new Runnable() {
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        Matrix mImageMatrix;
        int mNumFaces;
        float mScale = 1.0f;

        /* access modifiers changed from: private */
        public void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();
            int r = ((int) (f.eyesDistance() * this.mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= this.mScale;
            midPoint.y *= this.mScale;
            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            Rect imageRect = new Rect(0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight());
            RectF faceRect = new RectF((float) midX, (float) midY, (float) midX, (float) midY);
            faceRect.inset((float) (-r), (float) (-r));
            if (faceRect.left < 0.0f) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }
            if (faceRect.top < 0.0f) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }
            if (faceRect.right > ((float) imageRect.right)) {
                faceRect.inset(faceRect.right - ((float) imageRect.right), faceRect.right - ((float) imageRect.right));
            }
            if (faceRect.bottom > ((float) imageRect.bottom)) {
                faceRect.inset(faceRect.bottom - ((float) imageRect.bottom), faceRect.bottom - ((float) imageRect.bottom));
            }
            hv.setup(this.mImageMatrix, imageRect, faceRect, CropImage.this.mCircleCrop, (CropImage.this.mAspectX == 0 || CropImage.this.mAspectY == 0) ? false : true);
            CropImage.this.mImageView.add(hv);
        }

        /* access modifiers changed from: private */
        public void makeDefault() {
            boolean z;
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            int width = CropImage.this.mBitmap.getWidth();
            int height = CropImage.this.mBitmap.getHeight();
            Rect imageRect = new Rect(0, 0, width, height);
            int cropWidth = (Math.min(width, height) * 4) / 5;
            int cropHeight = cropWidth;
            if (!(CropImage.this.mAspectX == 0 || CropImage.this.mAspectY == 0)) {
                if (CropImage.this.mAspectX > CropImage.this.mAspectY) {
                    cropHeight = (CropImage.this.mAspectY * cropWidth) / CropImage.this.mAspectX;
                } else {
                    cropWidth = (CropImage.this.mAspectX * cropHeight) / CropImage.this.mAspectY;
                }
            }
            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;
            RectF cropRect = new RectF((float) x, (float) y, (float) (x + cropWidth), (float) (y + cropHeight));
            Matrix matrix = this.mImageMatrix;
            boolean access$2 = CropImage.this.mCircleCrop;
            if (CropImage.this.mAspectX == 0 || CropImage.this.mAspectY == 0) {
                z = false;
            } else {
                z = true;
            }
            hv.setup(matrix, imageRect, cropRect, access$2, z);
            CropImage.this.mImageView.add(hv);
        }

        private Bitmap prepareBitmap() {
            if (CropImage.this.mBitmap == null) {
                return null;
            }
            if (CropImage.this.mBitmap.getWidth() > 256) {
                this.mScale = 256.0f / ((float) CropImage.this.mBitmap.getWidth());
            }
            Matrix matrix = new Matrix();
            matrix.setScale(this.mScale, this.mScale);
            return Bitmap.createBitmap(CropImage.this.mBitmap, 0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight(), matrix, true);
        }

        public void run() {
            this.mImageMatrix = CropImage.this.mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();
            this.mScale = 1.0f / this.mScale;
            if (faceBitmap != null && CropImage.this.mDoFaceDetection) {
                this.mNumFaces = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), this.mFaces.length).findFaces(faceBitmap, this.mFaces);
            }
            if (!(faceBitmap == null || faceBitmap == CropImage.this.mBitmap)) {
                faceBitmap.recycle();
            }
            CropImage.this.mHandler.post(new Runnable() {
                public void run() {
                    CropImage.this.mWaitingToPick = AnonymousClass1.this.mNumFaces > 1;
                    if (AnonymousClass1.this.mNumFaces > 0) {
                        for (int i = 0; i < AnonymousClass1.this.mNumFaces; i++) {
                            AnonymousClass1.this.handleFace(AnonymousClass1.this.mFaces[i]);
                        }
                    } else {
                        AnonymousClass1.this.makeDefault();
                    }
                    CropImage.this.mImageView.invalidate();
                    if (CropImage.this.mImageView.mHighlightViews.size() == 1) {
                        CropImage.this.mCrop = CropImage.this.mImageView.mHighlightViews.get(0);
                        CropImage.this.mCrop.setFocus(true);
                    }
                    if (AnonymousClass1.this.mNumFaces > 1) {
                        Toast.makeText(CropImage.this, R.string.multiface_crop_help, 0).show();
                    }
                }
            });
        }
    };
    private Uri mSaveUri = null;
    boolean mSaving;
    private boolean mScale;
    private boolean mScaleUp = true;
    boolean mWaitingToPick;

    public void onCreate(Bundle icicle) {
        boolean z;
        String outputFormatString;
        super.onCreate(icicle);
        this.mContentResolver = getContentResolver();
        requestWindowFeature(1);
        setContentView(R.layout.cropimage);
        this.mImageView = (CropImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getString("circleCrop") != null) {
                this.mCircleCrop = true;
                this.mAspectX = 1;
                this.mAspectY = 1;
            }
            this.mSaveUri = (Uri) extras.getParcelable("output");
            if (!(this.mSaveUri == null || (outputFormatString = extras.getString("outputFormat")) == null)) {
                this.mOutputFormat = Bitmap.CompressFormat.valueOf(outputFormatString);
            }
            this.mBitmap = (Bitmap) extras.getParcelable("data");
            this.mAspectX = extras.getInt("aspectX");
            this.mAspectY = extras.getInt("aspectY");
            this.mOutputX = extras.getInt("outputX");
            this.mOutputY = extras.getInt("outputY");
            this.mScale = extras.getBoolean("scale", true);
            this.mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);
            if (extras.containsKey("noFaceDetection")) {
                z = !extras.getBoolean("noFaceDetection");
            } else {
                z = true;
            }
            this.mDoFaceDetection = z;
        }
        if (this.mBitmap == null) {
            Uri target = intent.getData();
            String targetScheme = target.getScheme();
            int rotation = 0;
            if (targetScheme.equals(UmengConstants.AtomKey_Content)) {
                this.mItem = CropImageUtil.createMediaItemFromUri(this, target, 0);
            }
            try {
                if (this.mItem != null) {
                    this.mBitmap = CropImageUtil.createFromUri(this, this.mItem.mContentUri, 1024, 1024, 0, (ClientConnectionManager) null);
                    rotation = (int) this.mItem.mRotation;
                } else {
                    this.mBitmap = CropImageUtil.createFromUri(this, target.toString(), 1024, 1024, 0, (ClientConnectionManager) null);
                    if (targetScheme.equals("file")) {
                        rotation = (int) CropImageUtil.exifOrientationToDegrees(new ExifInterface(target.getPath()).getAttributeInt("Orientation", 1));
                    }
                }
            } catch (IOException | URISyntaxException e) {
            }
            if (!(this.mBitmap == null || ((float) rotation) == 0.0f)) {
                this.mBitmap = CropImageUtil.rotate(this.mBitmap, rotation);
            }
        }
        if (this.mBitmap == null) {
            Log.e(TAG, "Cannot load bitmap, exiting.");
            finish();
            return;
        }
        getWindow().addFlags(1024);
        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropImage.this.setResult(0);
                CropImage.this.finish();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CropImage.this.onSaveClicked();
            }
        });
        startFaceDetection();
    }

    /* access modifiers changed from: private */
    public void onSaveClicked() {
        if (!this.mSaving && this.mCrop != null) {
            this.mSaving = true;
            Rect r = this.mCrop.getCropRect();
            int width = r.width();
            int height = r.height();
            Bitmap croppedImage = Bitmap.createBitmap(width, height, this.mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            new Canvas(croppedImage).drawBitmap(this.mBitmap, r, new Rect(0, 0, width, height), (Paint) null);
            if (this.mCircleCrop) {
                Canvas c = new Canvas(croppedImage);
                Path p = new Path();
                p.addCircle(((float) width) / 2.0f, ((float) height) / 2.0f, ((float) width) / 2.0f, Path.Direction.CW);
                c.clipPath(p, Region.Op.DIFFERENCE);
                c.drawColor(0, PorterDuff.Mode.CLEAR);
            }
            if (!(this.mOutputX == 0 || this.mOutputY == 0)) {
                if (this.mScale) {
                    Bitmap old = croppedImage;
                    croppedImage = CropImageUtil.transform(new Matrix(), croppedImage, this.mOutputX, this.mOutputY, this.mScaleUp);
                    if (old != croppedImage) {
                        old.recycle();
                    }
                } else {
                    Bitmap b = Bitmap.createBitmap(this.mOutputX, this.mOutputY, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(b);
                    Rect srcRect = this.mCrop.getCropRect();
                    Rect rect = new Rect(0, 0, this.mOutputX, this.mOutputY);
                    int dx = (srcRect.width() - rect.width()) / 2;
                    int dy = (srcRect.height() - rect.height()) / 2;
                    srcRect.inset(Math.max(0, dx), Math.max(0, dy));
                    rect.inset(Math.max(0, -dx), Math.max(0, -dy));
                    canvas.drawBitmap(this.mBitmap, srcRect, rect, (Paint) null);
                    croppedImage.recycle();
                    croppedImage = b;
                }
            }
            Bundle myExtras = getIntent().getExtras();
            if (myExtras == null || (myExtras.getParcelable("data") == null && !myExtras.getBoolean("return-data"))) {
                final Bitmap bitmap = croppedImage;
                CropImageUtil.startBackgroundJob(this, (String) null, getResources().getString(R.string.saving_image), new Runnable() {
                    public void run() {
                        CropImage.this.saveOutput(bitmap);
                    }
                }, this.mHandler);
                return;
            }
            Bundle extras = new Bundle();
            extras.putParcelable("data", croppedImage);
            setResult(-1, new Intent().setAction("inline-data").putExtras(extras));
            finish();
        }
    }

    private void startFaceDetection() {
        if (!isFinishing()) {
            this.mImageView.setImageBitmapResetBase(this.mBitmap, true);
            CropImageUtil.startBackgroundJob(this, (String) null, getResources().getString(R.string.running_face_detection), new Runnable() {
                public void run() {
                    final CountDownLatch latch = new CountDownLatch(1);
                    final Bitmap b = CropImage.this.mBitmap;
                    CropImage.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (!(b == CropImage.this.mBitmap || b == null)) {
                                CropImage.this.mImageView.setImageBitmapResetBase(b, true);
                                CropImage.this.mBitmap.recycle();
                                CropImage.this.mBitmap = b;
                            }
                            if (CropImage.this.mImageView.getScale() == 1.0f) {
                                CropImage.this.mImageView.center(true, true);
                            }
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                        CropImage.this.mRunFaceDetection.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, this.mHandler);
        }
    }

    /* access modifiers changed from: private */
    public void saveOutput(Bitmap croppedImage) {
        if (this.mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = this.mContentResolver.openOutputStream(this.mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(this.mOutputFormat, 75, outputStream);
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot open file: " + this.mSaveUri, e);
            } finally {
                CropImageUtil.closeSilently(outputStream);
            }
            setResult(-1, new Intent(this.mSaveUri.toString()).putExtras(new Bundle()));
        } else {
            new Bundle().putString("rect", this.mCrop.getCropRect().toString());
            if (this.mItem != null) {
                File file = new File(this.mItem.mFilePath);
                File file2 = new File(file.getParent());
                int x = 0;
                String fileName = file.getName();
                String fileName2 = fileName.substring(0, fileName.lastIndexOf("."));
                do {
                    x++;
                } while (new File(String.valueOf(file2.toString()) + "/" + fileName2 + "-" + x + ".jpg").exists());
                MediaItem item = this.mItem;
                String title = String.valueOf(fileName2) + "-" + x;
                String finalFileName = String.valueOf(title) + ".jpg";
                int[] degree = new int[1];
                Double latitude = null;
                Double longitude = null;
                if (item.isLatLongValid()) {
                    latitude = new Double(item.mLatitude);
                    longitude = new Double(item.mLongitude);
                }
                Uri newUri = CropImageUtil.addImage(this.mContentResolver, title, item.mDateAddedInSec, item.mDateTakenInMs, latitude, longitude, file2.toString(), finalFileName, croppedImage, (byte[]) null, degree);
                if (newUri != null) {
                    setResult(-1, new Intent().setData(newUri));
                } else {
                    setResult(-1, new Intent().setAction((String) null));
                }
            }
        }
        croppedImage.recycle();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(this.mDecodingThreads);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
