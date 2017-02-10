package com.tianfb.text.carouselfigure.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by TianFb on 2017/2/10,0010.
 */

public class CarouselViewPager extends ViewPager {

    //图片切换的间隔时间
    private static final int INTERVAL = 2000;
    //为实现无限循环增加的count值
    private static final int COUNT = 500000;
    //动画切换的时间
    private static final int SWITCHTIME = 1000;
    //用于发送延时消息实现自动轮播
    private Handler mViewPagerHandler = null;
    //handler消息的what
    private int mAutoSeithImage = 0;

    private class ViewPagerHandler extends Handler {
        private final WeakReference<CarouselViewPager> viewPager;

        public ViewPagerHandler(CarouselViewPager pager) {
            viewPager = new WeakReference<>(pager);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (viewPager.get() == null) {
                return;
            }
            swithImage();
        }
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置动画切换的时间
        changeViewPageScrollerTime(SWITCHTIME);
    }

    /**
     * 图片切换的方法
     */
    private void swithImage() {
        if (getAdapter() == null) return;
        int currentItem = getCurrentItem();
        if (currentItem == getAdapter().getCount() - 1) {
            currentItem = 0;
        } else {
            currentItem++;
        }
        setCurrentItem(currentItem);
        if (mViewPagerHandler != null)//mViewPagerHandler为null时，说明动画已关闭
            mViewPagerHandler.sendEmptyMessageDelayed(mAutoSeithImage, INTERVAL);
    }

    /**
     * 界面销毁时调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //调用关闭轮播图的方法
        stopAutoSwithImage();
    }

    /**
     * 界面显示在前台时调用   设置适配器方法优先
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //调用开启轮播图的方法
        startAutoSwithImage();
    }

    /**
     * 开启自动轮播
     */
    private void startAutoSwithImage() {
        if (getAdapter() == null) return;
        int count = getAdapter().getCount();
        if (COUNT < count) {//多张图片时才开启自动轮播
            if (mViewPagerHandler == null) {
                mViewPagerHandler = new ViewPagerHandler(this);
            }
            mViewPagerHandler.removeMessages(mAutoSeithImage);//移除已发送的消息避免重复发送消息
            mViewPagerHandler.sendEmptyMessageDelayed(mAutoSeithImage, INTERVAL);
        }
    }

    /**
     * 关闭自动轮播
     */
    private void stopAutoSwithImage() {
        if (mViewPagerHandler != null) {
            mViewPagerHandler.removeMessages(mAutoSeithImage);
            mViewPagerHandler = null;//避免handler泄漏
        }
    }

    //viewpager的切换监听 用于处理自动轮播和触摸切换时的冲突
    private class OnCarouselPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {//空闲状态时  恢复自动播放
                if (mViewPagerHandler == null) {
                    mViewPagerHandler = new ViewPagerHandler(CarouselViewPager.this);
                }
                mViewPagerHandler.removeMessages(mAutoSeithImage);
                mViewPagerHandler.sendEmptyMessageDelayed(mAutoSeithImage, INTERVAL);
            } else {//非空闲状态  取消自动播放
                stopAutoSwithImage();
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }


    /**
     * 设置切换时间
     */
    private int mDuration;

    private void changeViewPageScrollerTime(int time) {
        try {
            mDuration = time;
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller scroller;
            scroller = new FixedSpeedScroller(this.getContext(), new AccelerateDecelerateInterpolator());
            mField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class FixedSpeedScroller extends Scroller {

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置轮播图的数据
     *
     * @param list    轮播图的数据
     * @param context 上下文
     */
    public void setAdapter(Context context, ArrayList<Integer> list) {
        if (list.size() == 1) {//一张图片时不需要轮播
            setAdapter(new ViewPageAdapter(list, false, context));
        } else {//多张图片时进行轮播
            //设置轮播动画
            setPageTransformer(true, new DepthPageTransformer());
            //设置viewpager的数据
            setAdapter(new ViewPageAdapter(list, true, context));
            //设置图片的起始位置在数据的中间 避免设置在某一端造成一段划到头的现象
            setCurrentItem(list.size() * COUNT / 2);
            //处理轮播图被触摸时自动轮播是的冲突
            setOnPageChangeListener(new OnCarouselPageChangeListener());
            //开启动画
            startAutoSwithImage();
        }
    }


    /**
     * viewpager 使用的适配器
     */
    private class ViewPageAdapter extends PagerAdapter {
        private ArrayList<Integer> pagers = new ArrayList<>();
        private boolean mHasMore;
        private Context context;

        public ViewPageAdapter(ArrayList<Integer> pagers, boolean hasMore, Context context) {
            this.pagers = pagers;
            mHasMore = hasMore;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (mHasMore) {
                return pagers.size() * COUNT * 2;
            } else {
                return pagers.size();
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            container.removeView(imageView);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mHasMore) {
                position = position % pagers.size();
            }
            ImageView im = new ImageView(context);
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
            im.setImageResource(pagers.get(position));
            container.addView(im);
            im.setTag(pagers.get(position));
            //处理图片的点击
            im.setOnTouchListener(new ImageView.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            stopAutoSwithImage();
                            break;
                        case MotionEvent.ACTION_UP:
                            startAutoSwithImage();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            startAutoSwithImage();
                            break;
                    }
                    return true;
                }

            });

            return im;
        }
    }
}

