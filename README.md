# 效果图
![image](https://github.com/tiantutu/CarouselFigure/blob/master/GIF.gif)
#### 由于ViwePager的适配器直接封装在了自定义的控件里，所以使用时直接使用public void setAdapter(Context context, ArrayList<Integer> list) 设置数据即可

#### 可在CarouselViewPager 设置切换时间 和 切换时间
    //图片切换的间隔时间
    private static final int INTERVAL = 2000;
    //动画切换的时间
    private static final int SWITCHTIME = 1000;
    
    
    
    
    
####在DepthPageTransformer类中修改切换动画. 第一页的position变化就是( 0, -1]，第二页的position变化就是[ 1 , 0 ].
    public class DepthPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) {//第一页的变化
                view.setAlpha(1);
                view.setAlpha(1 + position);
                view.setTranslationX(pageWidth * -position);
            } else if (position <= 1) {//第二页的变化
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
            } else {
                view.setAlpha(0);
            }
        }
    }
