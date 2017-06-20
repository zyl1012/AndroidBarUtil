package cn.zyl1012.barlib;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

/**
 * Created by zyl on 2017/5/26.
 */
public class BarUtil {
    private static final String TAG = "window_status_view_for_bar_util";

    private static FrameLayout findBarColorView(FrameLayout decorView){
        for (int i = 0; i < decorView.getChildCount(); i++){
            View view = decorView.getChildAt(i);
            if(view instanceof FrameLayout && view.getTag() != null && view.getTag().equals(TAG)){
                return (FrameLayout) view;
            }
        }
        return null;
    }

    private static void addBarColorView(FrameLayout decorView, int statusBarColor, int navigationBarColor){
        View statusBarColorView = new View(decorView.getContext());
        statusBarColorView.setBackgroundColor(statusBarColor);
        statusBarColorView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, BarUtil.getStatusBarHeight()));

        View navigationBarColorView = new View(decorView.getContext());
        navigationBarColorView.setBackgroundColor(navigationBarColor);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, BarUtil.getNavigationBarHeight());
        lp.gravity = Gravity.BOTTOM;
        navigationBarColorView.setLayoutParams(lp);


        FrameLayout barColorView = new FrameLayout(decorView.getContext());
        barColorView.setTag(TAG);
        barColorView.addView(statusBarColorView);
        barColorView.addView(navigationBarColorView);
        decorView.addView(barColorView);
    }

    private static int blendARGB(int color1, int color2, float ratio) {
        final float inverseRatio = 1 - ratio;
        float a = Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio;
        float r = Color.red(color1) * inverseRatio + Color.red(color2) * ratio;
        float g = Color.green(color1) * inverseRatio + Color.green(color2) * ratio;
        float b = Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio;
        return Color.argb((int) Math.rint(a), (int) Math.rint(r), (int) Math.rint(g), (int) Math.rint(b));
    }

    public static int getStatusBarHeight() {
        int result = 0;
        Resources r = Resources.getSystem();
        int resourceId = r.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = r.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(){
        int result = 0;
        Resources r = Resources.getSystem();
        int resourceId = r.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = r.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasNavigationBar() {
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            IBinder serviceBinder = (IBinder)serviceManager.getMethod("getService", String.class).invoke(serviceManager, "window");
            Class<?> stub = Class.forName("android.view.IWindowManager$Stub");
            Object windowManagerService = stub.getMethod("asInterface", IBinder.class).invoke(stub, serviceBinder);
            Method hasNavigationBar = windowManagerService.getClass().getMethod("hasNavigationBar");
            return (boolean)hasNavigationBar.invoke(windowManagerService);
        } catch (Exception e) {
            return false;
        }
    }

    public static void setStatusBar(Activity activity, int color){
        setStatusBar(activity, color, false, false);
    }

    public static void setStatusBar(Activity activity, int color, boolean isTranslucent){
        setStatusBar(activity, color, isTranslucent, false);
    }

    public static void setStatusBar(Activity activity, int color, boolean isTranslucent, boolean layoutToStatusBar){
        setStatusAndNavigationBar(activity, color, isTranslucent, layoutToStatusBar, Color.BLACK, false, false);
    }

    public static void setNavigationBar(Activity activity, int color){
        setNavigationBar(activity, color, false, false);
    }

    public static void setNavigationBar(Activity activity, int color, boolean isTranslucent){
        setNavigationBar(activity, color, isTranslucent, false);
    }

    public static void setNavigationBar(Activity activity, int color, boolean isTranslucent, boolean layoutToNavigationBar){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusAndNavigationBar(activity, activity.getWindow().getStatusBarColor(), false, false, color, isTranslucent, layoutToNavigationBar);
        }else {
            setStatusAndNavigationBar(activity, Color.BLACK, false, false, color, isTranslucent, layoutToNavigationBar);
        }
    }

    public static void setStatusAndNavigationBar(final Activity activity,
                                                 int statusBarColor,
                                                 boolean isTranslucentStatusBar,
                                                 final boolean layoutToStatusBar,
                                                 int navigationBarColor,
                                                 boolean isTranslucentNavigationBar,
                                                 final boolean layoutToNavigationBar){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }

        Window window = activity.getWindow();
        if((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0){
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        final FrameLayout decorView = (FrameLayout) window.getDecorView();
        final boolean hasNavBar = hasNavigationBar();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout colorBarView = findBarColorView(decorView);
            if((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if(colorBarView != null){
                colorBarView.setVisibility(View.VISIBLE);
                colorBarView.getChildAt(0).setBackgroundColor(statusBarColor);
                colorBarView.getChildAt(1).setBackgroundColor(navigationBarColor);
                if(hasNavBar){
                    colorBarView.getChildAt(1).setVisibility(View.VISIBLE);
                }else {
                    colorBarView.getChildAt(1).setVisibility(View.GONE);
                }
            } else {
                addBarColorView(decorView, statusBarColor, navigationBarColor);
            }
        } else {
            if(isTranslucentStatusBar){
                statusBarColor = blendARGB(Color.BLACK, statusBarColor, 0.60f);
            }
            if(isTranslucentNavigationBar){
                navigationBarColor = blendARGB(Color.BLACK, navigationBarColor, 0.60f);
            }
            window.setStatusBarColor(statusBarColor);
            window.setNavigationBarColor(navigationBarColor);
        }
        decorView.findViewById(android.R.id.content).setPadding(0,layoutToStatusBar?0:getStatusBarHeight(),0,layoutToNavigationBar || !hasNavBar?0:getNavigationBarHeight());
        decorView.post(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                } else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        });
    }

    public enum ShowType{
        NEVER,//(Never Show Again) Cannot hide navigation bar
        CLICK,// Show again while touch the screen. Invalid before kitat if only hide the status bar
        DRAG,//Show again while drag from the top or the bottom of screen. Only work after kitat
        DRAG_TRANSLUCENT//Show again while drag from the top or the bottom of screen. Only work after kitat(It will hide again while release the drag)
    }

    public static void setFullScreen(Activity activity){
        setFullScreen(activity, false, ShowType.NEVER);
    }

    public static void setFullScreen(Activity activity, boolean hideNavigationBar){
        setFullScreen(activity, hideNavigationBar, ShowType.CLICK);
    }

    public static void setFullScreen(Activity activity, boolean hideNavigationBar, ShowType showType){
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        decorView.findViewById(android.R.id.content).setPadding(0,0,0,0);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            if(showType.equals(ShowType.NEVER)){
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }else {
                int visibility = decorView.getSystemUiVisibility();
                visibility = hideNavigationBar?visibility|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION:visibility|View.SYSTEM_UI_FLAG_FULLSCREEN;
                activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
            }
        }else {
            if(showType.equals(ShowType.NEVER)){
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }else {
                int visibility = decorView.getSystemUiVisibility();
                visibility = hideNavigationBar?visibility|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION:visibility|View.SYSTEM_UI_FLAG_FULLSCREEN;
                if(showType.equals(ShowType.DRAG)){
                    visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                }else if(showType.equals(ShowType.DRAG_TRANSLUCENT)){
                    visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
            }
        }
    }
}
