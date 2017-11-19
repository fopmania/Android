package korea2canada.com.ninjapeanut2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import korea2canada.com.ninjapeanut2.utils.PixelHelper;


public class Peanut extends AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    public static float GRAVITY = 4000;
    public static float StartSpeedY = -3000;
    private float speedY;
    private float spin = 0;
    private long currentTime = 0;
    private float cur_spin = 0;

    private ValueAnimator mAnimator;
    private PeanutListener mListener;
    private boolean mPopped;  //디폴트는 true

    public Peanut(Context context) {
        super(context);
    }

    public Peanut(Context context, int color, int rawHeight) {
        super(context);


        speedY = StartSpeedY + ((float)Math.random())*800.0f;
        spin = ((float)Math.random())*100.0f - 50.0f;
        mListener = (PeanutListener) context;

        this.setImageResource(R.drawable.peanut1);
//        this.setColorFilter(color);

        int rawWidth = rawHeight / 2;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public void releasePeanut(int screenHeight, int duration) {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!mPopped) {
            mListener.popPeanut(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        long c = animation.getCurrentPlayTime();
        float t = (c - currentTime)*0.001f;

        currentTime = c;
        float distance = speedY * t + GRAVITY * t * t;
        float newSpeed = speedY + GRAVITY * t;
        speedY = newSpeed;
        setY(getY() + distance);

        Matrix matrix = new Matrix();
        setScaleType(ImageView.ScaleType.MATRIX);   //required
        cur_spin += spin*t*30.0f;
        matrix.postRotate((float) cur_spin , getDrawable().getBounds().width()/2,
                getDrawable().getBounds().height()/2);
        setImageMatrix(matrix);
    }

    //11


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mPopped && event.getAction() == MotionEvent.ACTION_DOWN) {
            mListener.popPeanut(this, true);
            mPopped = true;
            mAnimator.cancel();
        }
        return super.onTouchEvent(event);
    }
//true일때 애니메이션 중
    public void setPopped(boolean popped) {
        mPopped = popped;
        if (popped) {
            mAnimator.cancel();
        }
    }


    //10
    public interface PeanutListener {
        void popPeanut(Peanut peanut, boolean userTouch);
    }
}
