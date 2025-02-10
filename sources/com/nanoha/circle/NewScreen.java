package com.nanoha.circle;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nanoha.CropImage.CropImageUtil;
import com.nanoha.MyLockScreen_all.CircleLockScreen;
import com.nanoha.MyLockScreen_all.CustomImageView;
import com.nanoha.MyLockScreen_all.PasswordActivity;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.ManageKeyguard;
import com.nanoha.util.Util;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NewScreen extends RelativeLayout {
    static final int ALPHA_ANIM_DURATION = 500;
    private static final int END_RENDERING = 7;
    private static final int END_RENDERING_RENDER_COUNT = 6;
    private static final int IDEAL_CALL = 202;
    private static final int INSIDE_CIRCLE_ANIM_DURATION = 1500;
    private static final int INSIDE_CIRCLE_ANIM_OFFSET = 800;
    private static final int INSIDE_CIRCLE_TWO_VIEW_ID = 258;
    private static final int INSIDE_CIRCLE_VIEW_ID = 257;
    static final float LOCK_CIRCLE_VIEW_OFFSET = 0.55f;
    static final float LOCK_RATIO = 0.3f;
    private static final int NO_OUTSIDE = 1;
    private static final int OUTSIDE_CAN_MOVE = 3;
    static final float OUTSIDE_CIRCLE_OFFSET = 0.9f;
    private static final int OUTSIDE_NO_MOVE = 2;
    private static final int PRE_END_RENDERING = 6;
    private static final int QUICKVIEW_ALPHA_ANIM_DURATION = 200;
    private static final int QUICKVIEW_TRANSLATE_ANIM_DURATION = 100;
    private static final int RINGINT_CALL = 201;
    private static final int START_RENDERING = 5;
    static final int STATUSBAR_HEIGHT = 25;
    static final String TAG = "NewScreen";
    private static final int TRANSLATE_SCALE_ANIM_FIRST = 1;
    private static final int TRANSLATE_SCALE_ANIM_SECOND = 2;
    static final long VIBRATE_LONG = 40;
    boolean all_end = true;
    boolean already_start = false;
    AudioManager am;
    CustomImageView answerCall;
    private List<Bitmap> bitmapCache = new ArrayList();
    CustomImageView bottomQuickView;
    RelativeLayout circleLayout;
    AnimationSet circleViewAnim;
    Context context;
    boolean displayLockIcon = true;
    boolean displayQuickView = true;
    boolean display_mute_view;
    CustomImageView endCall;
    int end_render_count = 6;
    /* access modifiers changed from: private */
    public int fixDoubleLockInt = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!NewScreen.this.isInOutsideCircle) {
                        if (NewScreen.this.circleViewAnim == null) {
                            NewScreen.this.initCircleViewAnim();
                        }
                        NewScreen.this.inside_circle_view.startAnimation(NewScreen.this.circleViewAnim);
                        return;
                    }
                    return;
                case 2:
                    if (!NewScreen.this.isInOutsideCircle) {
                        if (NewScreen.this.twoCircleViewAnim == null) {
                            NewScreen.this.initCircleTwoViewAnim();
                        }
                        NewScreen.this.inside_circle_two_view.startAnimation(NewScreen.this.twoCircleViewAnim);
                        return;
                    }
                    return;
                case 5:
                    if (NewScreen.this.display_mute_view) {
                        NewScreen.this.muteView.setVisibility(0);
                    }
                    NewScreen.this.scaleAnimation(NewScreen.this.outside_circle_view, NewScreen.LOCK_RATIO, NewScreen.LOCK_RATIO, 1.0f, 1.0f, (float) (NewScreen.this.outside_circle_view_width / 2), (float) (NewScreen.this.outside_circle_view_width / 2), NewScreen.INSIDE_CIRCLE_ANIM_DURATION, false);
                    NewScreen.this.outside_circle_view.setVisibility(0);
                    return;
                case CropImageUtil.MEDIA_DATE_ADDED_INDEX:
                    NewScreen.this.lock_circle_view_preX = NewScreen.this.touch_x - ((float) (NewScreen.this.lock_circle_view_length / 2));
                    NewScreen.this.lock_circle_view_preY = NewScreen.this.touch_y - ((float) (NewScreen.this.lock_circle_view_length / 2));
                    NewScreen.this.rendering = false;
                    NewScreen.this.preEnd = true;
                    NewScreen.this.positionChange_one_end = false;
                    NewScreen.this.positionChange_two_end = false;
                    NewScreen.this.positionChange = true;
                    return;
                case 7:
                    NewScreen.this.lockAllTouch = true;
                    NewScreen.this.lock_circle_view.layout(NewScreen.this.lock_circle_view_x, NewScreen.this.lock_circle_view_y, NewScreen.this.lock_circle_view_x + NewScreen.this.lock_circle_view.getMeasuredWidth(), NewScreen.this.lock_circle_view_y + NewScreen.this.lock_circle_view.getMeasuredHeight());
                    NewScreen.this.inside_circle_view.clearAnimation();
                    NewScreen.this.inside_circle_two_view.clearAnimation();
                    NewScreen.this.lock_circle_view.setVisibility(4);
                    NewScreen.this.inside_circle_view.setVisibility(4);
                    NewScreen.this.inside_circle_two_view.setVisibility(4);
                    NewScreen.this.positionChange = false;
                    if (NewScreen.this.display_mute_view) {
                        NewScreen.this.muteView.setVisibility(4);
                    }
                    NewScreen.this.scaleAnimation(NewScreen.this.outside_circle_view, NewScreen.LOCK_RATIO, NewScreen.LOCK_RATIO, 1.0f, 1.0f, (float) (NewScreen.this.outside_circle_view_width / 2), (float) (NewScreen.this.outside_circle_view_width / 2), NewScreen.INSIDE_CIRCLE_ANIM_DURATION, true);
                    NewScreen.this.last_render_inside_circle(NewScreen.this.inside_circle_view, ((float) NewScreen.this.outside_circle_view_width) * 0.35f, ((float) NewScreen.this.outside_circle_view_width) * 0.35f, 0.0f, 0.0f, NewScreen.LOCK_RATIO, NewScreen.LOCK_RATIO, 1.0f, 1.0f, NewScreen.INSIDE_CIRCLE_ANIM_DURATION);
                    NewScreen.this.outside_circle_view.setVisibility(4);
                    return;
                case NewScreen.RINGINT_CALL /*201*/:
                    if (NewScreen.this.quickViewLayout != null) {
                        NewScreen.this.quickViewLayout.setVisibility(4);
                    }
                    NewScreen.this.endCall.setVisibility(0);
                    NewScreen.this.answerCall.setVisibility(0);
                    NewScreen.this.ringingName.setVisibility(0);
                    NewScreen.this.ringingNum.setVisibility(0);
                    String number = (String) msg.obj;
                    NewScreen.this.ringingName.setText(NewScreen.this.getContactNameFromPhoneNum(NewScreen.this.context, number));
                    NewScreen.this.ringingNum.setText(number);
                    return;
                case NewScreen.IDEAL_CALL /*202*/:
                    if (NewScreen.this.quickViewLayout != null) {
                        NewScreen.this.quickViewLayout.setVisibility(0);
                    }
                    NewScreen.this.endCall.setVisibility(4);
                    NewScreen.this.answerCall.setVisibility(4);
                    NewScreen.this.ringingName.setVisibility(4);
                    NewScreen.this.ringingNum.setVisibility(4);
                    return;
                default:
                    return;
            }
        }
    };
    boolean hasFinish = false;
    boolean hasPassword;
    ImageView inside_circle_two_view;
    ImageView inside_circle_view;
    int inside_circle_view_width;
    boolean isInMuteArea = false;
    boolean isInOutsideCircle = false;
    CustomImageView leftQuickView;
    boolean lockAllTouch = false;
    Rect lockCircle_rect = new Rect();
    ImageView lock_circle_view;
    int lock_circle_view_length;
    float lock_circle_view_preX;
    float lock_circle_view_preY;
    int lock_circle_view_x;
    int lock_circle_view_y;
    ImageView lock_view;
    int lock_view_x;
    int lock_view_y;
    private Vibrator mVibrator;
    float minusCircleX;
    float minusCircleY;
    ImageView muteView;
    int muteView_width;
    int muteView_x;
    int muteView_y;
    ImageView outside_circle_view;
    int outside_circle_view_width;
    int outside_circle_view_x;
    int outside_circle_view_y;
    boolean positionChange = false;
    boolean positionChange_one_end = true;
    boolean positionChange_two_end = true;
    boolean preEnd = false;
    RelativeLayout quickViewLayout;
    float quickViewOriX;
    float quickViewOriY;
    float quickViewPreX;
    float quickViewPreY;
    private View.OnTouchListener quickViewTouchListener = new View.OnTouchListener() {
        /* Debug info: failed to restart local var, previous not found, register: 11 */
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            float x = event.getX() - ((float) (NewScreen.this.quickView_width / 2));
            float y = event.getY() - ((float) (NewScreen.this.quickView_width / 2));
            switch (action) {
                case 0:
                    NewScreen.this.quickViewPreX = x;
                    NewScreen.this.quickViewPreY = y;
                    NewScreen.this.quickViewOriX = x;
                    NewScreen.this.quickViewOriY = y;
                    NewScreen.this.touchedQuickView = (CustomImageView) v;
                    if (NewScreen.this.touchedQuickView.sort == 1) {
                        NewScreen.this.quickViewLayout.bringChildToFront(NewScreen.this.touchedQuickView);
                    }
                    NewScreen.this.alphaAnimation(NewScreen.this.lock_view, 1.0f, 0.0f, NewScreen.QUICKVIEW_ALPHA_ANIM_DURATION, true, (Animation.AnimationListener) null);
                    return true;
                case 1:
                    if (!NewScreen.this.lockCircle_rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        NewScreen.this.quickViewReturn();
                        return true;
                    } else if (NewScreen.this.touchedQuickView.sort == 1) {
                        if (NewScreen.this.hasPassword) {
                            NewScreen.this.showPasswordView(NewScreen.this.touchedQuickView.intent.toURI());
                            return true;
                        }
                        if (NewScreen.this.touchedQuickView.intent != null) {
                            if (NewScreen.this.touchedQuickView.isBroadCast) {
                                NewScreen.this.context.sendBroadcast(NewScreen.this.touchedQuickView.intent);
                            } else {
                                try {
                                    NewScreen.this.context.startActivity(NewScreen.this.touchedQuickView.intent);
                                } catch (ActivityNotFoundException e) {
                                }
                            }
                        }
                        if (NewScreen.this.fixDoubleLockInt == 0) {
                            ManageKeyguard.exitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
                        }
                        NewScreen.this.util.saveIsLocked(false);
                        ((CircleLockScreen) NewScreen.this.context).finish();
                        return true;
                    } else if (NewScreen.this.touchedQuickView.sort == 2) {
                        NewScreen.this.util.saveIsLocked(false);
                        ((CircleLockScreen) NewScreen.this.context).finish();
                        return true;
                    } else if (NewScreen.this.touchedQuickView.sort != 3) {
                        return true;
                    } else {
                        NewScreen.this.util.saveIsLocked(false);
                        ((CircleLockScreen) NewScreen.this.context).finish();
                        return true;
                    }
                case 2:
                    NewScreen.this.translateAnimation(NewScreen.this.touchedQuickView, NewScreen.this.quickViewPreX, NewScreen.this.quickViewPreY, x, y, true, 100);
                    NewScreen.this.quickViewPreX = x;
                    NewScreen.this.quickViewPreY = y;
                    return true;
                default:
                    return true;
            }
        }
    };
    int quickView_width;
    boolean rendering = false;
    CustomImageView rightQuickView;
    TextView ringingName;
    TextView ringingNum;
    int ringingTextHeight;
    int ringingTextWidth;
    CustomImageView topQuickView;
    float touch_x;
    float touch_y;
    CustomImageView touchedQuickView;
    AnimationSet twoCircleViewAnim;
    Util util;

    public NewScreen(Context context2) {
        super(context2);
        this.context = context2;
        this.util = Util.getInstance(context2);
        initView();
        initListener();
    }

    public NewScreen(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
        this.util = Util.getInstance(context2);
        this.fixDoubleLockInt = this.util.getFixDoubleLockIssue();
        if (this.util.isUseSecurityLock()) {
            this.fixDoubleLockInt = -1;
        }
        this.am = (AudioManager) context2.getSystemService("audio");
        initView();
        initListener();
        playSounds(true);
    }

    /* access modifiers changed from: private */
    public synchronized void vibrate(long duration) {
        if (this.mVibrator == null) {
            this.mVibrator = (Vibrator) this.context.getSystemService("vibrator");
        }
        this.mVibrator.vibrate(duration);
    }

    /* access modifiers changed from: private */
    public void playSounds(boolean locked) {
        if (this.fixDoubleLockInt != 0 && this.fixDoubleLockInt != -1) {
            ContentResolver cr = this.context.getContentResolver();
            if (this.util.isPlayLockSound()) {
                String whichSound = locked ? Util.KEY_LOCK_SOUND : "unlock_sound";
                String soundPath = Settings.System.getString(cr, whichSound);
                if (soundPath != null) {
                    Uri soundUri = Uri.parse("file://" + soundPath);
                    if (soundUri != null) {
                        Ringtone sfx = RingtoneManager.getRingtone(this.context, soundUri);
                        if (sfx != null) {
                            sfx.setStreamType(1);
                            sfx.play();
                            return;
                        }
                        Log.d(TAG, "playSounds: failed to load ringtone from uri: " + soundUri);
                        return;
                    }
                    Log.d(TAG, "playSounds: could not parse Uri: " + soundPath);
                    return;
                }
                Log.d(TAG, "playSounds: whichSound = " + whichSound + "; soundPath was null");
            }
        }
    }

    /* access modifiers changed from: private */
    public void initCircleTwoViewAnim() {
        ScaleAnimation scaleAnim = new ScaleAnimation(LOCK_RATIO, 1.0f, LOCK_RATIO, 1.0f);
        scaleAnim.setDuration(1500);
        TranslateAnimation tranAnimTwo = new TranslateAnimation(this.touch_x - this.minusCircleX, 0.0f, this.touch_y - this.minusCircleY, 0.0f);
        tranAnimTwo.setDuration(1500);
        this.twoCircleViewAnim = new AnimationSet(true);
        this.twoCircleViewAnim.addAnimation(scaleAnim);
        this.twoCircleViewAnim.addAnimation(tranAnimTwo);
        this.twoCircleViewAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (!NewScreen.this.isInOutsideCircle) {
                    List<Animation> circleViewAnimList = NewScreen.this.twoCircleViewAnim.getAnimations();
                    TranslateAnimation tmpTranAnim = new TranslateAnimation(NewScreen.this.touch_x - NewScreen.this.minusCircleX, 0.0f, NewScreen.this.touch_y - NewScreen.this.minusCircleY, 0.0f);
                    tmpTranAnim.setDuration(1500);
                    circleViewAnimList.remove(1);
                    circleViewAnimList.add(tmpTranAnim);
                    if (NewScreen.this.rendering) {
                        NewScreen.this.inside_circle_two_view.startAnimation(NewScreen.this.twoCircleViewAnim);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void initCircleViewAnim() {
        ScaleAnimation scaleAnim = new ScaleAnimation(LOCK_RATIO, 1.0f, LOCK_RATIO, 1.0f);
        scaleAnim.setDuration(1500);
        TranslateAnimation tranAnim = new TranslateAnimation(this.touch_x - this.minusCircleX, 0.0f, this.touch_y - this.minusCircleY, 0.0f);
        tranAnim.setDuration(1500);
        this.circleViewAnim = new AnimationSet(true);
        this.circleViewAnim.addAnimation(scaleAnim);
        this.circleViewAnim.addAnimation(tranAnim);
        this.circleViewAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (!NewScreen.this.isInOutsideCircle) {
                    List<Animation> circleViewAnimList = NewScreen.this.circleViewAnim.getAnimations();
                    TranslateAnimation tmpTranAnim = new TranslateAnimation(NewScreen.this.touch_x - NewScreen.this.minusCircleX, 0.0f, NewScreen.this.touch_y - NewScreen.this.minusCircleY, 0.0f);
                    tmpTranAnim.setDuration(1500);
                    circleViewAnimList.remove(1);
                    circleViewAnimList.add(tmpTranAnim);
                    if (NewScreen.this.rendering) {
                        NewScreen.this.inside_circle_view.startAnimation(NewScreen.this.circleViewAnim);
                    } else if (!NewScreen.this.rendering && !NewScreen.this.lockAllTouch) {
                        if (NewScreen.this.end_render_count <= 0) {
                            NewScreen.this.end_render_count = 6;
                            NewScreen.this.handler.sendEmptyMessage(7);
                            return;
                        }
                        NewScreen.this.end_render_count--;
                        NewScreen.this.inside_circle_view.startAnimation(NewScreen.this.circleViewAnim);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void last_render_inside_circle(final View view, float fromX, float fromY, float toX, float toY, float scaleFromX, float scaleFromY, float scaleToX, float scaleToY, int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(scaleFromX, scaleToX, scaleFromY, scaleToY);
        scaleAnimation.setDuration((long) duration);
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration((long) duration);
        AlphaAnimation up_alphaAnimation = new AlphaAnimation(LOCK_RATIO, 1.0f);
        up_alphaAnimation.setDuration((long) (duration - QUICKVIEW_ALPHA_ANIM_DURATION));
        AlphaAnimation down_alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        down_alphaAnimation.setStartOffset((long) (duration - QUICKVIEW_ALPHA_ANIM_DURATION));
        down_alphaAnimation.setDuration(200);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(up_alphaAnimation);
        animationSet.addAnimation(down_alphaAnimation);
        animationSet.setFillAfter(true);
        animationSet.setStartOffset(700);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (view.getId() == NewScreen.this.inside_circle_view.getId()) {
                    view.clearAnimation();
                    view.setVisibility(4);
                    NewScreen.this.all_end = true;
                    NewScreen.this.lockAllTouch = false;
                    NewScreen.this.already_start = false;
                }
            }
        });
        view.setVisibility(0);
        view.startAnimation(animationSet);
    }

    public void scaleAnimation(View view, float fromX, float fromY, float toX, float toY, float pivotX, float pivotY, int duration, boolean isEnd) {
        ScaleAnimation sa = new ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY);
        sa.setDuration((long) duration);
        final boolean z = isEnd;
        sa.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (!z) {
                    NewScreen.this.handler.sendEmptyMessageDelayed(1, 200);
                    NewScreen.this.handler.sendEmptyMessageDelayed(2, 1000);
                    return;
                }
                if (NewScreen.this.displayQuickView) {
                    NewScreen.this.alphaAnimation(NewScreen.this.quickViewLayout, 0.0f, 1.0f, NewScreen.QUICKVIEW_ALPHA_ANIM_DURATION, true, (Animation.AnimationListener) null);
                }
                NewScreen.this.lock_circle_view.setVisibility(0);
                if (!NewScreen.this.displayLockIcon) {
                    NewScreen.this.lock_view.setVisibility(4);
                } else {
                    NewScreen.this.lock_view.setVisibility(0);
                }
            }
        });
        view.startAnimation(sa);
    }

    public String getContactNameFromPhoneNum(Context context2, String phoneNum) {
        String contactName = context2.getString(R.string.unknown);
        Cursor cursor1 = null;
        try {
            Cursor cursor12 = context2.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNum)), new String[]{"display_name"}, (String) null, (String[]) null, (String) null);
            if (cursor12.moveToFirst()) {
                contactName = cursor12.getString(cursor12.getColumnIndex("display_name"));
            }
            if (cursor12 != null) {
                cursor12.close();
            }
        } catch (Exception e) {
            Log.e("nanoha", "getContactNameFromPhoneNum error=" + e.toString());
            if (cursor1 != null) {
                cursor1.close();
            }
        } catch (Throwable th) {
            if (cursor1 != null) {
                cursor1.close();
            }
            throw th;
        }
        return contactName;
    }

    /* access modifiers changed from: private */
    public void outSideControl(float rawX, float rawY) {
        float rawY2 = rawY - 25.0f;
        switch (whetherOutside(rawX, rawY2)) {
            case 1:
                if (this.isInOutsideCircle) {
                    this.isInOutsideCircle = false;
                    this.handler.removeMessages(1);
                    this.handler.sendEmptyMessageDelayed(1, 200);
                    this.handler.removeMessages(2);
                    this.handler.sendEmptyMessageDelayed(2, 1000);
                }
                this.lock_circle_view.layout((int) (rawX - ((float) (this.lock_circle_view_length / 2))), (int) (rawY2 - ((float) (this.lock_circle_view_length / 2))), (int) (((float) (this.lock_circle_view_length / 2)) + rawX), (int) (((float) (this.lock_circle_view_length / 2)) + rawY2));
                this.lock_circle_view_preX = this.touch_x - ((float) (this.lock_circle_view_length / 2));
                this.lock_circle_view_preY = this.touch_y - ((float) (this.lock_circle_view_length / 2));
                this.positionChange_one_end = false;
                this.positionChange_two_end = false;
                this.positionChange = true;
                return;
            case 2:
                this.isInOutsideCircle = true;
                outsideTranslate(rawX, rawY2);
                return;
            case 3:
                this.isInOutsideCircle = true;
                this.lock_circle_view.layout((int) (rawX - ((float) (this.lock_circle_view_length / 2))), (int) (rawY2 - ((float) (this.lock_circle_view_length / 2))), (int) (((float) (this.lock_circle_view_length / 2)) + rawX), (int) (((float) (this.lock_circle_view_length / 2)) + rawY2));
                this.lock_circle_view_preX = this.touch_x - ((float) (this.lock_circle_view_length / 2));
                this.lock_circle_view_preY = this.touch_y - ((float) (this.lock_circle_view_length / 2));
                this.positionChange_one_end = false;
                this.positionChange_two_end = false;
                this.positionChange = true;
                return;
            default:
                return;
        }
    }

    private void outsideTranslate(float rawX, float rawY) {
        float tempY;
        float outsideCircleX = (float) (this.outside_circle_view_x + (this.outside_circle_view_width / 2));
        float outsideCircleY = (float) (this.outside_circle_view_y + (this.outside_circle_view_width / 2));
        double length = Math.sqrt((double) (((rawX - outsideCircleX) * (rawX - outsideCircleX)) + ((rawY - outsideCircleY) * (rawY - outsideCircleY))));
        if (rawY < outsideCircleY) {
            tempY = (float) (((((double) Math.abs(rawY - outsideCircleY)) * (length - ((double) (this.outside_circle_view_width / 2)))) / length) + ((double) rawY));
        } else {
            tempY = (float) ((((double) (Math.abs(rawY - outsideCircleY) * ((float) (this.outside_circle_view_width / 2)))) / length) + ((double) outsideCircleY));
        }
        float tempY2 = tempY - ((float) this.lock_circle_view_y);
        float tempX = ((float) Math.abs((((double) ((Math.abs(rawX - outsideCircleX) * ((float) this.outside_circle_view_width)) / 2.0f)) / length) + ((double) (rawX > outsideCircleX ? outsideCircleX : -outsideCircleX)))) - ((float) this.lock_circle_view_x);
        this.lock_circle_view.layout((int) ((tempX - ((float) (this.lock_circle_view_length / 2))) + ((float) this.lock_circle_view_x)), (int) ((tempY2 - ((float) (this.lock_circle_view_length / 2))) + ((float) this.lock_circle_view_y)), (int) (((float) (this.lock_circle_view_length / 2)) + tempX + ((float) this.lock_circle_view_x)), (int) (((float) (this.lock_circle_view_length / 2)) + tempY2 + ((float) this.lock_circle_view_y)));
        this.lock_circle_view_preX = tempX - ((float) (this.lock_circle_view_length / 2));
        this.lock_circle_view_preY = tempY2 - ((float) (this.lock_circle_view_length / 2));
        this.positionChange_one_end = false;
        this.positionChange_two_end = false;
        this.positionChange = true;
    }

    private int whetherOutside(float rawX, float rawY) {
        float outsideCircleX = (float) (this.outside_circle_view_x + (this.outside_circle_view_width / 2));
        float outsideCircleY = (float) (this.outside_circle_view_y + (this.outside_circle_view_width / 2));
        double length = Math.sqrt((double) (((rawX - outsideCircleX) * (rawX - outsideCircleX)) + ((rawY - outsideCircleY) * (rawY - outsideCircleY))));
        if (length > ((double) ((((float) this.outside_circle_view_width) * OUTSIDE_CIRCLE_OFFSET) / 2.0f))) {
            return 2;
        }
        if (length > ((double) (((((float) this.outside_circle_view_width) * OUTSIDE_CIRCLE_OFFSET) / 2.0f) - ((((float) this.lock_circle_view_length) * LOCK_CIRCLE_VIEW_OFFSET) / 2.0f)))) {
            return 3;
        }
        return 1;
    }

    /* access modifiers changed from: private */
    public void translateAnimation(View view, float fromX, float fromY, float destX, float destY, boolean fillAfter, int duration) {
        TranslateAnimation tranAnim = new TranslateAnimation(fromX, destX, fromY, destY);
        tranAnim.setFillAfter(fillAfter);
        tranAnim.setDuration((long) duration);
        view.startAnimation(tranAnim);
    }

    /* access modifiers changed from: private */
    public void alphaAnimation(View view, float fromAlpha, float toAlpha, int duration, boolean fillAfter, Animation.AnimationListener listener) {
        AlphaAnimation alphaAnim = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnim.setDuration((long) duration);
        alphaAnim.setFillAfter(fillAfter);
        if (listener != null) {
            alphaAnim.setAnimationListener(listener);
        }
        view.startAnimation(alphaAnim);
    }

    private void setShortcutIntent(CustomImageView view, String key) {
        int defaultDrawable = R.drawable.phone;
        Intent defaultIntent = null;
        PackageManager pm = this.context.getPackageManager();
        if (key.equals(Util.KEY_LEFT_SHORTCUT_SETTING)) {
            defaultDrawable = R.drawable.phone;
            defaultIntent = new Intent("android.intent.action.DIAL");
        } else if (key.equals(Util.KEY_TOP_SHORTCUT_SETTING)) {
            defaultDrawable = R.drawable.mail;
            defaultIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:"));
        } else if (key.equals(Util.KEY_RIGHT_SHORTCUT_SETTING)) {
            defaultDrawable = R.drawable.browser;
            defaultIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
        } else if (key.equals(Util.KEY_BOTTOM_SHORTCUT_SETTING)) {
            defaultDrawable = R.drawable.camera;
            defaultIntent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
        }
        String savedIntentString = this.util.getShortcutSetting(key);
        if ("-1".equals(savedIntentString)) {
            view.setBackgroundResource(defaultDrawable);
            view.setIntent(defaultIntent);
        } else if ("0".equals(savedIntentString)) {
            view.setVisibility(8);
        } else {
            try {
                Intent intent = Intent.getIntent(savedIntentString);
                intent.setAction("android.intent.action.MAIN");
                String iconUriString = this.util.getShortcutIcon(String.valueOf(key) + Util.KEY_SHORTCU_ICON);
                if ("-1".equals(iconUriString)) {
                    view.setBackgroundDrawable(pm.getActivityInfo(intent.getComponent(), 128).loadIcon(pm));
                } else {
                    try {
                        Bitmap bitmap = readBitMap(this.context.getContentResolver(), iconUriString);
                        this.bitmapCache.add(bitmap);
                        view.setImageBitmap(bitmap);
                    } catch (OutOfMemoryError e) {
                        OutOfMemoryError outOfMemoryError = e;
                        view.setBackgroundDrawable(pm.getActivityInfo(intent.getComponent(), 128).loadIcon(pm));
                    } catch (Exception e2) {
                        Exception exc = e2;
                        view.setBackgroundDrawable(pm.getActivityInfo(intent.getComponent(), 128).loadIcon(pm));
                    }
                }
                view.setIntent(intent);
            } catch (Exception e3) {
                e3.printStackTrace();
                view.setBackgroundResource(defaultDrawable);
                view.setIntent(defaultIntent);
            }
        }
    }

    public Bitmap readBitMap(ContentResolver cr, String uri) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(uri)), (Rect) null, opt);
    }

    public void initView() {
        boolean z;
        this.displayQuickView = this.util.isDisplayQuickView();
        this.display_mute_view = this.util.isDisplayMuteIcon();
        Resources res = this.context.getResources();
        this.circleLayout = new RelativeLayout(this.context);
        this.lock_circle_view = new ImageView(this.context);
        this.lock_view = new ImageView(this.context);
        this.lock_view.setBackgroundDrawable(res.getDrawable(R.drawable.lock));
        if (!this.util.isDisplayLockIcon()) {
            this.displayLockIcon = false;
            this.lock_view.setVisibility(4);
        }
        this.inside_circle_view = new ImageView(this.context);
        this.inside_circle_view.setId(INSIDE_CIRCLE_VIEW_ID);
        this.inside_circle_two_view = new ImageView(this.context);
        this.inside_circle_two_view.setId(INSIDE_CIRCLE_TWO_VIEW_ID);
        this.outside_circle_view = new ImageView(this.context);
        if (this.util.getValue(Util.KEY_THEME, 0) == 0) {
            this.lock_circle_view.setBackgroundResource(R.drawable.lock_cricle);
            this.inside_circle_view.setBackgroundResource(R.drawable.inside);
            this.inside_circle_two_view.setBackgroundResource(R.drawable.inside);
            this.outside_circle_view.setBackgroundResource(R.drawable.outside);
        } else {
            this.lock_circle_view.setBackgroundResource(R.drawable.lock_circle_two);
            this.inside_circle_view.setBackgroundResource(R.drawable.inside_two);
            this.inside_circle_two_view.setBackgroundResource(R.drawable.inside_two);
            this.outside_circle_view.setBackgroundResource(R.drawable.outside_two);
        }
        this.circleLayout.addView(this.lock_circle_view);
        this.circleLayout.addView(this.lock_view);
        this.circleLayout.addView(this.inside_circle_view);
        this.circleLayout.addView(this.inside_circle_two_view);
        this.circleLayout.addView(this.outside_circle_view);
        if (this.display_mute_view) {
            this.muteView = new ImageView(this.context);
            if (this.am.getRingerMode() == 2) {
                this.muteView.setBackgroundResource(R.drawable.unmute);
            } else {
                this.muteView.setBackgroundResource(R.drawable.mute);
            }
            this.muteView.setBackgroundResource(R.drawable.mute);
            this.circleLayout.addView(this.muteView);
            this.muteView.setVisibility(4);
        }
        if (this.displayQuickView) {
            this.quickViewLayout = new RelativeLayout(this.context);
            this.leftQuickView = new CustomImageView(this.context);
            this.rightQuickView = new CustomImageView(this.context);
            this.topQuickView = new CustomImageView(this.context);
            this.bottomQuickView = new CustomImageView(this.context);
            setShortcutIntent(this.leftQuickView, Util.KEY_LEFT_SHORTCUT_SETTING);
            setShortcutIntent(this.rightQuickView, Util.KEY_RIGHT_SHORTCUT_SETTING);
            setShortcutIntent(this.topQuickView, Util.KEY_TOP_SHORTCUT_SETTING);
            setShortcutIntent(this.bottomQuickView, Util.KEY_BOTTOM_SHORTCUT_SETTING);
            this.quickViewLayout.addView(this.leftQuickView);
            this.quickViewLayout.addView(this.topQuickView);
            this.quickViewLayout.addView(this.rightQuickView);
            this.quickViewLayout.addView(this.bottomQuickView);
            this.circleLayout.addView(this.quickViewLayout, new RelativeLayout.LayoutParams(-1, -1));
            this.leftQuickView.setOnTouchListener(this.quickViewTouchListener);
            this.topQuickView.setOnTouchListener(this.quickViewTouchListener);
            this.rightQuickView.setOnTouchListener(this.quickViewTouchListener);
            this.bottomQuickView.setOnTouchListener(this.quickViewTouchListener);
        }
        addView(this.circleLayout, new RelativeLayout.LayoutParams(-1, -1));
        if (!this.util.isUsePassword() || !this.util.hasPassword()) {
            z = false;
        } else {
            z = true;
        }
        this.hasPassword = z;
        this.inside_circle_view.setVisibility(4);
        this.inside_circle_two_view.setVisibility(4);
        this.outside_circle_view.setVisibility(4);
        this.endCall = new CustomImageView(this.context);
        this.endCall.setBackgroundResource(R.drawable.dial_end);
        this.endCall.sort = 2;
        this.endCall.setOnTouchListener(this.quickViewTouchListener);
        this.answerCall = new CustomImageView(this.context);
        this.answerCall.setBackgroundResource(R.drawable.dial_answer);
        this.answerCall.sort = 3;
        this.answerCall.setOnTouchListener(this.quickViewTouchListener);
        addView(this.endCall);
        addView(this.answerCall);
        this.endCall.setVisibility(4);
        this.answerCall.setVisibility(4);
        this.ringingName = new TextView(this.context);
        this.ringingNum = new TextView(this.context);
        addView(this.ringingName);
        addView(this.ringingNum);
        this.ringingName.setVisibility(4);
        this.ringingNum.setVisibility(4);
        initTextColor();
    }

    private void initTextColor() {
        this.ringingName.setTextColor(-256);
        this.ringingNum.setTextColor(-256);
    }

    public void ringingCall(String number) {
        this.handler.obtainMessage(RINGINT_CALL, number).sendToTarget();
    }

    public void idealCall() {
        this.handler.sendEmptyMessage(IDEAL_CALL);
    }

    private void clearTranslateAnim(final View view, float fromX, float fromY, float destX, float destY, int duration) {
        TranslateAnimation tranAnim = new TranslateAnimation(fromX, destX, fromY, destY);
        tranAnim.setFillAfter(true);
        tranAnim.setDuration((long) duration);
        tranAnim.setInterpolator(new DecelerateInterpolator(1.0f));
        tranAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(tranAnim);
    }

    public void onDestory() {
        if (this.bitmapCache != null && this.bitmapCache.size() > 0) {
            for (int i = 0; i < this.bitmapCache.size(); i++) {
                Bitmap bitmap = this.bitmapCache.get(i);
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
        System.gc();
    }

    /* access modifiers changed from: private */
    public void quickViewReturn() {
        clearTranslateAnim(this.touchedQuickView, this.quickViewPreX, this.quickViewPreY, 0.0f, 0.0f, QUICKVIEW_ALPHA_ANIM_DURATION);
        alphaAnimation(this.lock_view, 0.0f, 1.0f, QUICKVIEW_ALPHA_ANIM_DURATION, true, new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                NewScreen.this.lock_view.clearAnimation();
            }
        });
    }

    public void initListener() {
        this.lock_circle_view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (NewScreen.this.lockAllTouch) {
                    return true;
                }
                switch (event.getAction()) {
                    case 0:
                        NewScreen.this.isInMuteArea = false;
                        if (!NewScreen.this.all_end) {
                            NewScreen.this.end_render_count = 6;
                        }
                        if (!NewScreen.this.already_start) {
                            NewScreen.this.lock_view.setVisibility(4);
                            NewScreen.this.rendering = true;
                            NewScreen.this.touch_x = event.getX();
                            NewScreen.this.touch_y = event.getY();
                            NewScreen.this.lock_circle_view_preX = NewScreen.this.touch_x - ((float) (NewScreen.this.lock_circle_view_length / 2));
                            NewScreen.this.lock_circle_view_preY = NewScreen.this.touch_y - ((float) (NewScreen.this.lock_circle_view_length / 2));
                            NewScreen.this.inside_circle_view.setVisibility(0);
                            NewScreen.this.inside_circle_two_view.setVisibility(0);
                            NewScreen.this.positionChange = false;
                            NewScreen.this.handler.sendEmptyMessage(5);
                            NewScreen.this.all_end = false;
                            NewScreen.this.already_start = true;
                            if (NewScreen.this.displayQuickView) {
                                NewScreen.this.alphaAnimation(NewScreen.this.quickViewLayout, 1.0f, 0.0f, NewScreen.QUICKVIEW_ALPHA_ANIM_DURATION, true, (Animation.AnimationListener) null);
                                break;
                            }
                        } else {
                            NewScreen.this.touch_x = event.getX();
                            NewScreen.this.touch_y = event.getY();
                            NewScreen.this.outSideControl(event.getRawX(), event.getRawY());
                            break;
                        }
                        break;
                    case 1:
                        float rawX = event.getRawX();
                        float rawY = event.getRawY();
                        if (!NewScreen.this.display_mute_view || !NewScreen.this.isInOutsideCircle || rawX < ((float) NewScreen.this.muteView_x) || rawX > ((float) (NewScreen.this.muteView_x + NewScreen.this.muteView_width)) || rawY >= ((float) (NewScreen.this.outside_circle_view_y + (NewScreen.this.outside_circle_view_width / 2)))) {
                            if (!NewScreen.this.isInOutsideCircle) {
                                NewScreen.this.touch_x = event.getRawX() - ((float) NewScreen.this.lock_circle_view_x);
                                NewScreen.this.touch_y = (event.getRawY() - ((float) NewScreen.this.lock_circle_view_y)) - (((float) NewScreen.this.lock_circle_view_length) * 0.1f);
                                NewScreen.this.handler.sendEmptyMessage(6);
                                break;
                            } else {
                                NewScreen.this.playSounds(false);
                                if (NewScreen.this.util.isUnlockVibrate()) {
                                    NewScreen.this.vibrate(NewScreen.VIBRATE_LONG);
                                }
                                if (!NewScreen.this.hasPassword) {
                                    if (NewScreen.this.fixDoubleLockInt == 0) {
                                        ManageKeyguard.exitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
                                    }
                                    NewScreen.this.util.saveIsLocked(false);
                                    ((CircleLockScreen) NewScreen.this.context).finish();
                                    ((CircleLockScreen) NewScreen.this.context).overridePendingTransition(0, NewScreen.this.util.getUnlcokAnim());
                                    break;
                                } else {
                                    NewScreen.this.showPasswordView((String) null);
                                    break;
                                }
                            }
                        } else {
                            NewScreen.this.mutePhone();
                            NewScreen.this.touch_x = event.getX();
                            NewScreen.this.touch_y = event.getY();
                            NewScreen.this.handler.sendEmptyMessage(7);
                            break;
                        }
                        break;
                    case 2:
                        NewScreen.this.touch_x = event.getRawX() - ((float) NewScreen.this.lock_circle_view_x);
                        NewScreen.this.touch_y = (event.getRawY() - ((float) NewScreen.this.lock_circle_view_y)) - (((float) NewScreen.this.lock_circle_view_length) * 0.1f);
                        NewScreen.this.outSideControl(event.getRawX(), event.getRawY());
                        break;
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showPasswordView(String intentStr) {
        this.rendering = false;
        this.handler.sendEmptyMessage(7);
        Intent passwordIntent = new Intent(this.context, PasswordActivity.class);
        if (passwordIntent != null) {
            passwordIntent.putExtra(Util.QUICK_INTENT_STRING, intentStr);
        }
        this.context.startActivity(passwordIntent);
        if (this.fixDoubleLockInt == 0) {
            ManageKeyguard.exitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
        }
        this.util.saveIsLocked(false);
        ((CircleLockScreen) this.context).finish();
        ((CircleLockScreen) this.context).overridePendingTransition(0, this.util.getUnlcokAnim());
    }

    /* access modifiers changed from: private */
    public void mutePhone() {
        if (this.am.getRingerMode() == 2) {
            this.muteView.setBackgroundResource(R.drawable.mute);
            this.am.setRingerMode(1);
            vibrate(VIBRATE_LONG);
            return;
        }
        this.muteView.setBackgroundResource(R.drawable.unmute);
        this.am.setRingerMode(2);
        vibrate(VIBRATE_LONG);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        this.outside_circle_view_width = Math.min(Math.max(widthSpecSize, heightSpecSize) / 2, getResources().getDrawable(R.drawable.outside).getIntrinsicWidth());
        this.inside_circle_view_width = this.outside_circle_view_width;
        this.outside_circle_view.measure(View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824));
        if (this.display_mute_view) {
            this.muteView_width = this.outside_circle_view_width / 8;
            this.muteView.measure(View.MeasureSpec.makeMeasureSpec(this.muteView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.muteView_width, 1073741824));
        }
        this.quickView_width = this.outside_circle_view_width / 6;
        if (this.displayQuickView) {
            this.leftQuickView.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
            this.rightQuickView.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
            this.topQuickView.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
            this.bottomQuickView.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
        }
        this.endCall.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
        this.answerCall.measure(View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.quickView_width, 1073741824));
        this.inside_circle_view.measure(View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824));
        this.inside_circle_two_view.measure(View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width, 1073741824));
        this.lock_view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        this.lock_circle_view.measure(View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width / 2, 1073741824), View.MeasureSpec.makeMeasureSpec(this.outside_circle_view_width / 2, 1073741824));
        this.lock_circle_view_length = this.outside_circle_view_width / 2;
        this.ringingTextWidth = this.outside_circle_view_width;
        this.ringingTextHeight = this.lock_circle_view_length / 4;
        this.ringingName.measure(View.MeasureSpec.makeMeasureSpec(this.ringingTextWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.ringingTextHeight, 1073741824));
        this.ringingNum.measure(View.MeasureSpec.makeMeasureSpec(this.ringingTextWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.ringingTextHeight, 1073741824));
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = r - l;
        int height = b - t;
        if (width > height) {
            this.outside_circle_view_y = (height - this.outside_circle_view_width) / 2;
            this.outside_circle_view_x = width / 2;
        } else {
            this.outside_circle_view_y = height / 2;
            this.outside_circle_view_x = (width - this.outside_circle_view_width) / 2;
        }
        this.outside_circle_view.layout(this.outside_circle_view_x, this.outside_circle_view_y, this.outside_circle_view_x + this.outside_circle_view_width, this.outside_circle_view_y + this.outside_circle_view.getMeasuredHeight());
        if (this.display_mute_view) {
            this.muteView_x = this.outside_circle_view_x + ((this.outside_circle_view_width - this.muteView_width) / 2);
            this.muteView_y = this.outside_circle_view_y;
            this.muteView.layout(this.muteView_x, this.muteView_y, this.muteView_x + this.muteView_width, this.muteView_y + this.muteView_width);
        }
        this.inside_circle_view.layout(this.outside_circle_view_x, this.outside_circle_view_y, this.outside_circle_view_x + this.outside_circle_view_width, this.outside_circle_view_y + this.outside_circle_view.getMeasuredHeight());
        this.inside_circle_two_view.layout(this.outside_circle_view_x, this.outside_circle_view_y, this.outside_circle_view_x + this.outside_circle_view_width, this.outside_circle_view_y + this.outside_circle_view.getMeasuredHeight());
        this.lock_view_x = (this.outside_circle_view_x + (this.outside_circle_view_width / 2)) - (this.lock_view.getMeasuredWidth() / 2);
        this.lock_view_y = (this.outside_circle_view_y + (this.outside_circle_view_width / 2)) - (this.lock_view.getMeasuredHeight() / 2);
        this.lock_view.layout(this.lock_view_x, this.lock_view_y, this.lock_view_x + this.lock_view.getMeasuredWidth(), this.lock_view_y + this.lock_view.getMeasuredHeight());
        this.lock_circle_view_x = (this.outside_circle_view_x + (this.outside_circle_view_width / 2)) - (this.lock_circle_view.getMeasuredWidth() / 2);
        this.lock_circle_view_y = (this.outside_circle_view_y + (this.outside_circle_view_width / 2)) - (this.lock_circle_view.getMeasuredHeight() / 2);
        int lock_circle_view_width = this.lock_circle_view.getMeasuredWidth();
        int lock_circle_view_height = this.lock_circle_view.getMeasuredHeight();
        this.lock_circle_view.layout(this.lock_circle_view_x, this.lock_circle_view_y, this.lock_circle_view_x + this.lock_circle_view.getMeasuredWidth(), this.lock_circle_view_y + this.lock_circle_view.getMeasuredHeight());
        this.lockCircle_rect.left = this.lock_circle_view_x + (lock_circle_view_width / 8);
        this.lockCircle_rect.top = this.lock_circle_view_y + (lock_circle_view_width / 8);
        this.lockCircle_rect.right = (this.lock_circle_view_x + this.lock_circle_view.getMeasuredWidth()) - (lock_circle_view_width / 8);
        this.lockCircle_rect.bottom = (this.lock_circle_view_y + this.lock_circle_view.getMeasuredHeight()) - (lock_circle_view_width / 8);
        int leftQuickViewX = this.lock_circle_view_x - this.quickView_width;
        int leftQuickViewY = this.lock_circle_view_y + ((lock_circle_view_height - this.quickView_width) / 2);
        int rightQuickViewX = this.lock_circle_view_x + lock_circle_view_width;
        int rightQuickViewY = leftQuickViewY;
        this.answerCall.layout(leftQuickViewX, leftQuickViewY, this.quickView_width + leftQuickViewX, this.quickView_width + leftQuickViewY);
        this.endCall.layout(rightQuickViewX, rightQuickViewY, this.quickView_width + rightQuickViewX, this.quickView_width + rightQuickViewY);
        if (this.displayQuickView) {
            int topQuickViewX = this.lock_circle_view_x + ((lock_circle_view_width - this.quickView_width) / 2);
            int topQuickViewY = this.lock_circle_view_y - this.quickView_width;
            int bottomQuickViewX = topQuickViewX;
            int bottomQuickViewY = this.lock_circle_view_y + lock_circle_view_height;
            this.leftQuickView.layout(leftQuickViewX, leftQuickViewY, this.quickView_width + leftQuickViewX, this.quickView_width + leftQuickViewY);
            this.rightQuickView.layout(rightQuickViewX, rightQuickViewY, this.quickView_width + rightQuickViewX, this.quickView_width + rightQuickViewY);
            this.topQuickView.layout(topQuickViewX, topQuickViewY, this.quickView_width + topQuickViewX, this.quickView_width + topQuickViewY);
            this.bottomQuickView.layout(bottomQuickViewX, bottomQuickViewY, this.quickView_width + bottomQuickViewX, this.quickView_width + bottomQuickViewY);
        }
        this.minusCircleX = ((float) ((this.lock_circle_view_length / 2) - (this.lock_circle_view_x - this.outside_circle_view_x))) - ((((float) this.lock_circle_view_length) * 0.27f) - ((((float) this.inside_circle_view_width) * LOCK_RATIO) * 0.09f));
        this.minusCircleY = ((float) ((this.lock_circle_view_length / 2) - (this.lock_circle_view_y - this.outside_circle_view_y))) - ((((float) this.lock_circle_view_length) * 0.27f) - ((((float) this.inside_circle_view_width) * LOCK_RATIO) * 0.09f));
        int ringingNameX = (width - this.ringingTextWidth) / 2;
        int ringingNameY = this.lock_circle_view_y - (this.ringingTextHeight * 2);
        this.ringingName.setGravity(17);
        this.ringingName.layout(ringingNameX, ringingNameY, this.ringingTextWidth + ringingNameX, this.ringingTextHeight + ringingNameY);
        this.ringingNum.setGravity(17);
        this.ringingNum.layout(ringingNameX, this.ringingTextHeight + ringingNameY, this.ringingTextWidth + ringingNameX, (this.ringingTextHeight * 2) + ringingNameY);
        this.ringingName.setTextSize((float) (this.ringingTextHeight / 2));
        this.ringingNum.setTextSize((float) (this.ringingTextHeight / 3));
    }
}
