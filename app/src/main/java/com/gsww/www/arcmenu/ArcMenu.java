package com.gsww.www.arcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Author   : luweicheng on 2017/5/3 0003 18:56
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 自定义ViewGroup
 */

public class ArcMenu extends ViewGroup implements View.OnClickListener {
    private Position mPosition = Position.LEFT_BOTTOM;
    private Statu mStatu = Statu.CLOSE;
    private int radius = 100;
    private View mCbutton;

    @Override
    public void onClick(View v) {
        toggleButton();
    }
    private void toggleButton() {
        final int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {

            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            int xflag = 1;
            int yflag = 1;

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM)
                xflag = -1;
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP)
                yflag = -1;

            // child left
            int cl = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
            // child top
            int ct = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));

            AnimationSet animSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = null;
            switch (mStatu) {
                case CLOSE:
                    //to open
                    translateAnimation = new TranslateAnimation(xflag * cl, 0f, yflag * ct, 0f);
                    childView.setClickable(true);
                    childView.setFocusable(true);
                    break;
                case OPEN:
                    //to close
                    translateAnimation= new TranslateAnimation(0f, xflag * cl, 0f, yflag * ct);
                    childView.setClickable(false);
                    childView.setFocusable(false);
                    break;
            }

            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.e("", "onAnimationEnd: ");
                    if (mStatu == Statu.CLOSE) {
                        childView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(300);
            translateAnimation.setStartOffset((i * 100) / (count - 1));
            translateAnimation.setInterpolator(new OvershootInterpolator(2F));
            RotateAnimation rotateAnimation = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(300);

            animSet.addAnimation(rotateAnimation);
            animSet.addAnimation(translateAnimation);
            childView.startAnimation(animSet);
            final int index = i+1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMenuItemClickListener!=null){
                        onMenuItemClickListener.itemClick(index);
                        menuItemAnim(index);
                    }
                }
            });


        }

        changeMenuStaus();

    }

    /**
     * Menu的Item动画监听
     * @param index
     */
    private void menuItemAnim(int index) {
        for (int i = 0; i < getChildCount()-1; i++) {
            View view = getChildAt(i+1);
            if(index == i+1){
               scaleItem(view);
            }else{
                smallScale(view);
            }
            view.setClickable(false);
            view.setFocusable(false);
            mStatu = Statu.CLOSE;
          }


    }

    /**
     * 点击菜单项放大缩小
     * @param childView
     */
    private void scaleItem(View childView) {
        AnimationSet set  = new AnimationSet(true);
        ScaleAnimation scale1= new ScaleAnimation(1.0f,1.5f,1.0f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale1.setDuration(200);
        ScaleAnimation scale2= new ScaleAnimation(1.5f,1.0f,1.5f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale2.setStartOffset(200);
        scale2.setDuration(200);
        scale2.setFillAfter(true);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        set.addAnimation(scale1);
        set.addAnimation(scale2);
        set.addAnimation(alphaAnimation);
        set.setFillAfter(true);
        childView.startAnimation(set);

    }
    private void smallScale(View v){
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        v.startAnimation(alphaAnimation);
    }



    /**
     * 菜单点击监听
     */
    public interface OnMenuItemClickListener{
        void itemClick(int index);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    private OnMenuItemClickListener onMenuItemClickListener;

    /**
     * 切换按钮状态
     */
    private void changeMenuStaus() {
        switch (mStatu) {
            case OPEN:
                rotateView(mCbutton, 0, 45f);
                mStatu = Statu.CLOSE;
                break;
            case CLOSE:
                rotateView(mCbutton, 45f, 0f);
                mStatu = Statu.OPEN;
                break;
        }
    }



    public void rotateView(View v, float from, Float to) {
        /**
         * 创建旋转动画，
         * from 开始的旋转角 to 终止的旋转角
         * piovXType piovYType 旋转的类型 Animation.RELATIVE_TO_DELF 以自身为标准
         *
         * pivotXValue （0 - 1）0 代表以该View 的左上角为中心， 1代表以该View 的右底角为中心旋转
         */
        RotateAnimation anim = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        anim.setFillAfter(true);
        mCbutton.setAnimation(anim);
        anim.start();
    }

    /**
     * 打开和关闭的枚举类
     */
    public enum Statu {
        CLOSE,
        OPEN
    }

    /**
     * 位置的枚举类
     */
    public enum Position {
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        //dp转化成px
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        int count = a.getIndexCount();
        /**
         * 遍历自定义属性赋值
         */
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ArcMenu_position:
                    int index = a.getInt(attr, 0);
                    switch (index) {
                        case 0:
                            mPosition = Position.LEFT_TOP;
                            break;
                        case 1:
                            mPosition = Position.LEFT_BOTTOM;
                            break;
                        case 2:
                            mPosition = Position.RIGHT_TOP;
                            break;
                        case 3:
                            mPosition = Position.RIGHT_BOTTOM;
                            break;
                    }
                    break;
                case R.styleable.ArcMenu_radius:
                    radius = a.getDimensionPixelSize(attr, 100);
                    break;
            }
        }
        a.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 测量子View
         */
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            measureChild(v, MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCenterButton();
            int count = getChildCount() - 2;
            for (int i = 0; i < count + 1; i++) {
                View view = getChildAt(i + 1);
                int w = view.getMeasuredWidth();
                int h = view.getMeasuredHeight();
                int left = 0;
                int top = 0;
                int right = 0;
                int bottom = 0;
                switch (mPosition) {
                    case LEFT_TOP:
                        left = (int) (radius * Math.sin(Math.PI / 2 / count * i));
                        top = (int) (radius * Math.cos(Math.PI / 2 / count * i));
                        right = left + w;
                        bottom = top + h;
                        break;
                    case LEFT_BOTTOM:
                        left = (int) (radius * Math.sin(Math.PI / 2 / count * i));
                        bottom = getMeasuredHeight() - (int) (radius * Math.cos(Math.PI / 2 / count * i));
                        right = left + w;
                        top = bottom - h;
                        break;
                    case RIGHT_BOTTOM:
                        right = getMeasuredWidth() - (int) (radius * Math.sin(Math.PI / 2 / count * i));
                        bottom = getMeasuredHeight() - (int) (radius * Math.cos(Math.PI / 2 / count * i));
                        top = bottom - h;
                        left = right - w;
                        break;
                    case RIGHT_TOP:
                        right = getMeasuredWidth() - (int) (radius * Math.sin(Math.PI / 2 / count * i));
                        top = (int) (radius * Math.cos(Math.PI / 2 / count * i));
                        bottom = top + h;
                        left = right - w;
                        break;

                }
                view.layout(left, top, right, bottom);
                view.setVisibility(GONE);
                mStatu = Statu.CLOSE;
            }


        }
    }

    /**
     * 根据布局中设置的position属性来确定中间按钮的位置
     */
    private void layoutCenterButton() {
        int left = 0;
        int top = 0;
        mCbutton = getChildAt(0);
        mCbutton.setOnClickListener(this);
        int but_width = mCbutton.getMeasuredWidth();
        int but_height = mCbutton.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                mCbutton.layout(left, top, left + but_width, top + but_height);
                break;
            case LEFT_BOTTOM:
                mCbutton.layout(left, getMeasuredHeight() - but_width, left + but_width, getMeasuredHeight());

                break;
            case RIGHT_TOP:
                mCbutton.layout(getMeasuredWidth() - but_width, top, getMeasuredWidth(), top + but_height);
                break;
            case RIGHT_BOTTOM:
                mCbutton.layout(getMeasuredWidth() - but_width, getMeasuredHeight() - but_height, getMeasuredWidth(), getMeasuredHeight());
                break;
        }
    }
}
