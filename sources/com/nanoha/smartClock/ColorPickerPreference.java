package com.nanoha.smartClock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nanoha.MyLockScreen_all.R;

public class ColorPickerPreference extends DialogPreference {
    private ColorPickerView mCPView;
    /* access modifiers changed from: private */
    public int mCurrentColor;
    private int mInitialColor;
    private TextView summary;
    private Util util;

    public interface OnColorChangedListener {
        void colorChanged(int i);
    }

    private static class ColorPickerView extends View {
        private static final int CENTER_RADIUS = 30;
        private static final int CENTER_X = 100;
        private static final int CENTER_Y = 100;
        private static final float PI = 3.1415925f;
        private Paint mCenterPaint;
        private final int[] mColors = {-65536, -65281, -16776961, -16711681, -16711936, -256, -65536};
        private int[] mHSVColors;
        private Paint mHSVPaint;
        private boolean mHighlightCenter;
        private OnColorChangedListener mListener;
        private Paint mPaint;
        private boolean mRedrawHSV;
        private boolean mTrackingCenter;

        ColorPickerView(Context c, OnColorChangedListener l, int color) {
            super(c);
            this.mListener = l;
            Shader s = new SweepGradient(0.0f, 0.0f, this.mColors, (float[]) null);
            this.mPaint = new Paint(1);
            this.mPaint.setShader(s);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(55.0f);
            this.mCenterPaint = new Paint(1);
            this.mCenterPaint.setColor(color);
            this.mCenterPaint.setStrokeWidth(5.0f);
            this.mHSVColors = new int[]{-16777216, color, -1};
            this.mHSVPaint = new Paint(1);
            this.mHSVPaint.setStrokeWidth(10.0f);
            this.mRedrawHSV = true;
        }

        public int getColor() {
            return this.mCenterPaint.getColor();
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            float r = 100.0f - (this.mPaint.getStrokeWidth() * 0.5f);
            canvas.translate(100.0f, 100.0f);
            int c = this.mCenterPaint.getColor();
            if (this.mRedrawHSV) {
                this.mHSVColors[1] = c;
                this.mHSVPaint.setShader(new LinearGradient(-100.0f, 0.0f, 100.0f, 0.0f, this.mHSVColors, (float[]) null, Shader.TileMode.CLAMP));
            }
            canvas.drawOval(new RectF(-r, -r, r, r), this.mPaint);
            canvas.drawCircle(0.0f, 0.0f, 30.0f, this.mCenterPaint);
            canvas.drawRect(new RectF(-100.0f, 130.0f, 100.0f, 110.0f), this.mHSVPaint);
            if (this.mTrackingCenter) {
                this.mCenterPaint.setStyle(Paint.Style.STROKE);
                if (this.mHighlightCenter) {
                    this.mCenterPaint.setAlpha(255);
                } else {
                    this.mCenterPaint.setAlpha(128);
                }
                canvas.drawCircle(0.0f, 0.0f, this.mCenterPaint.getStrokeWidth() + 30.0f, this.mCenterPaint);
                this.mCenterPaint.setStyle(Paint.Style.FILL);
                this.mCenterPaint.setColor(c);
            }
            this.mRedrawHSV = true;
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(200, 250);
        }

        private int ave(int s, int d, float p) {
            return Math.round(((float) (d - s)) * p) + s;
        }

        private int interpColor(int[] colors, float unit) {
            if (unit <= 0.0f) {
                return colors[0];
            }
            if (unit >= 1.0f) {
                return colors[colors.length - 1];
            }
            float p = unit * ((float) (colors.length - 1));
            int i = (int) p;
            float p2 = p - ((float) i);
            int c0 = colors[i];
            int c1 = colors[i + 1];
            return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p2), ave(Color.red(c0), Color.red(c1), p2), ave(Color.green(c0), Color.green(c1), p2), ave(Color.blue(c0), Color.blue(c1), p2));
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean z;
            boolean z2;
            int c0;
            int c1;
            float p;
            float x = event.getX() - 100.0f;
            float y = event.getY() - 100.0f;
            boolean inCenter = Math.sqrt((double) ((x * x) + (y * y))) <= 30.0d;
            switch (event.getAction()) {
                case 0:
                    this.mTrackingCenter = inCenter;
                    if (inCenter) {
                        this.mHighlightCenter = true;
                        invalidate();
                        return true;
                    }
                    break;
                case 1:
                    if (!this.mTrackingCenter) {
                        return true;
                    }
                    if (inCenter) {
                        this.mListener.colorChanged(this.mCenterPaint.getColor());
                    }
                    this.mTrackingCenter = false;
                    invalidate();
                    return true;
                case 2:
                    break;
                default:
                    return true;
            }
            if (!this.mTrackingCenter) {
                if (x >= -100.0f) {
                    z = true;
                } else {
                    z = false;
                }
                if (x <= 100.0f) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if ((!z || !z2) || y > 130.0f || y < 110.0f) {
                    float unit = ((float) Math.atan2((double) y, (double) x)) / 6.283185f;
                    if (unit < 0.0f) {
                        unit += 1.0f;
                    }
                    this.mCenterPaint.setColor(interpColor(this.mColors, unit));
                    invalidate();
                    return true;
                }
                if (x < 0.0f) {
                    c0 = this.mHSVColors[0];
                    c1 = this.mHSVColors[1];
                    p = (100.0f + x) / 100.0f;
                } else {
                    c0 = this.mHSVColors[1];
                    c1 = this.mHSVColors[2];
                    p = x / 100.0f;
                }
                this.mCenterPaint.setColor(Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p)));
                this.mRedrawHSV = false;
                invalidate();
                return true;
            } else if (this.mHighlightCenter == inCenter) {
                return true;
            } else {
                this.mHighlightCenter = inCenter;
                invalidate();
                return true;
            }
        }
    }

    public ColorPickerPreference(Context contex) {
        this(contex, (AttributeSet) null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            this.mCurrentColor = this.mCPView.getColor();
            this.summary.setText(Integer.toHexString(this.mCurrentColor));
            this.summary.setTextColor(this.mCurrentColor);
            Log.d("nanoha", "key=" + getKey() + " color=" + this.mCurrentColor);
            this.util.saveFontColor(getKey(), this.mCurrentColor);
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getKey(), this.mCurrentColor);
            editor.commit();
            callChangeListener(new Integer(this.mCurrentColor));
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.color_picker, (ViewGroup) null);
        this.util = Util.getInstance(getContext());
        ((TextView) view.findViewById(R.id.title)).setText(getTitle());
        this.summary = (TextView) view.findViewById(R.id.summary);
        this.summary.setText(getSummary());
        this.mInitialColor = getPreferenceManager().getSharedPreferences().getInt(getKey(), -1);
        this.summary.setTextColor(this.mInitialColor);
        this.summary.setText("#" + Integer.toHexString(this.mInitialColor));
        return view;
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                ColorPickerPreference.this.mCurrentColor = color;
                ColorPickerPreference.this.onDialogClosed(true);
                ColorPickerPreference.this.getDialog().dismiss();
            }
        };
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(1);
        this.mCPView = new ColorPickerView(getContext(), l, this.mInitialColor);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-2, -2);
        params1.gravity = 17;
        this.mCPView.setLayoutParams(params1);
        layout.addView(this.mCPView);
        layout.setId(16908312);
        builder.setView(layout);
    }
}
