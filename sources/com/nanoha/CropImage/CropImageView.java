package com.nanoha.CropImage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.nanoha.CropImage.HighlightView;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: CropImage */
class CropImageView extends ImageViewTouchBase {
    ArrayList<HighlightView> mHighlightViews = new ArrayList<>();
    float mLastX;
    float mLastY;
    int mMotionEdge;
    HighlightView mMotionHighlightView = null;

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mBitmapDisplayed.getBitmap() != null) {
            Iterator<HighlightView> it = this.mHighlightViews.iterator();
            while (it.hasNext()) {
                HighlightView hv = it.next();
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void zoomTo(float scale, float centerX, float centerY) {
        super.zoomTo(scale, centerX, centerY);
        Iterator<HighlightView> it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void zoomIn() {
        super.zoomIn();
        Iterator<HighlightView> it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void zoomOut() {
        super.zoomOut();
        Iterator<HighlightView> it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < this.mHighlightViews.size(); i++) {
            HighlightView hv = this.mHighlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    private void recomputeFocus(MotionEvent event) {
        for (int i = 0; i < this.mHighlightViews.size(); i++) {
            HighlightView hv = this.mHighlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.mHighlightViews.size()) {
                break;
            }
            HighlightView hv2 = this.mHighlightViews.get(i2);
            if (hv2.getHit(event.getX(), event.getY()) == 1) {
                i2++;
            } else if (!hv2.hasFocus()) {
                hv2.setFocus(true);
                hv2.invalidate();
            }
        }
        invalidate();
    }

    /* Debug info: failed to restart local var, previous not found, register: 11 */
    public boolean onTouchEvent(MotionEvent event) {
        HighlightView.ModifyMode modifyMode;
        CropImage cropImage = (CropImage) getContext();
        if (cropImage.mSaving) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                if (cropImage.mWaitingToPick) {
                    recomputeFocus(event);
                    break;
                } else {
                    int i = 0;
                    while (true) {
                        if (i >= this.mHighlightViews.size()) {
                            break;
                        } else {
                            HighlightView hv = this.mHighlightViews.get(i);
                            int edge = hv.getHit(event.getX(), event.getY());
                            if (edge != 1) {
                                this.mMotionEdge = edge;
                                this.mMotionHighlightView = hv;
                                this.mLastX = event.getX();
                                this.mLastY = event.getY();
                                HighlightView highlightView = this.mMotionHighlightView;
                                if (edge == 32) {
                                    modifyMode = HighlightView.ModifyMode.Move;
                                } else {
                                    modifyMode = HighlightView.ModifyMode.Grow;
                                }
                                highlightView.setMode(modifyMode);
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                }
            case 1:
                if (cropImage.mWaitingToPick) {
                    for (int i2 = 0; i2 < this.mHighlightViews.size(); i2++) {
                        HighlightView hv2 = this.mHighlightViews.get(i2);
                        if (hv2.hasFocus()) {
                            cropImage.mCrop = hv2;
                            for (int j = 0; j < this.mHighlightViews.size(); j++) {
                                if (j != i2) {
                                    this.mHighlightViews.get(j).setHidden(true);
                                }
                            }
                            centerBasedOnHighlightView(hv2);
                            ((CropImage) getContext()).mWaitingToPick = false;
                            return true;
                        }
                    }
                } else if (this.mMotionHighlightView != null) {
                    centerBasedOnHighlightView(this.mMotionHighlightView);
                    this.mMotionHighlightView.setMode(HighlightView.ModifyMode.None);
                }
                this.mMotionHighlightView = null;
                break;
            case 2:
                if (!cropImage.mWaitingToPick) {
                    if (this.mMotionHighlightView != null) {
                        this.mMotionHighlightView.handleMotion(this.mMotionEdge, event.getX() - this.mLastX, event.getY() - this.mLastY);
                        this.mLastX = event.getX();
                        this.mLastY = event.getY();
                        ensureVisible(this.mMotionHighlightView);
                        break;
                    }
                } else {
                    recomputeFocus(event);
                    break;
                }
                break;
        }
        switch (event.getAction()) {
            case 1:
                center(true, true);
                break;
            case 2:
                if (getScale() == 1.0f) {
                    center(true, true);
                    break;
                }
                break;
        }
        return true;
    }

    private void ensureVisible(HighlightView hv) {
        int panDeltaX;
        int panDeltaY;
        Rect r = hv.mDrawRect;
        int panDeltaX1 = Math.max(0, getLeft() - r.left);
        int panDeltaX2 = Math.min(0, getRight() - r.right);
        int panDeltaY1 = Math.max(0, getTop() - r.top);
        int panDeltaY2 = Math.min(0, getBottom() - r.bottom);
        if (panDeltaX1 != 0) {
            panDeltaX = panDeltaX1;
        } else {
            panDeltaX = panDeltaX2;
        }
        if (panDeltaY1 != 0) {
            panDeltaY = panDeltaY1;
        } else {
            panDeltaY = panDeltaY2;
        }
        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy((float) panDeltaX, (float) panDeltaY);
        }
    }

    private void centerBasedOnHighlightView(HighlightView hv) {
        Rect drawRect = hv.mDrawRect;
        float width = (float) drawRect.width();
        float height = (float) drawRect.height();
        float zoom = Math.max(1.0f, Math.min((((float) getWidth()) / width) * 0.6f, (((float) getHeight()) / height) * 0.6f) * getScale());
        if (((double) (Math.abs(zoom - getScale()) / zoom)) > 0.1d) {
            float[] coordinates = {hv.mCropRect.centerX(), hv.mCropRect.centerY()};
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300.0f);
        }
        ensureVisible(hv);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mHighlightViews.size(); i++) {
            this.mHighlightViews.get(i).draw(canvas);
        }
    }

    public void add(HighlightView hv) {
        this.mHighlightViews.add(hv);
        invalidate();
    }
}
