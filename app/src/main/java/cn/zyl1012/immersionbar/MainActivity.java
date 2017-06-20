package cn.zyl1012.immersionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import cn.zyl1012.barlib.BarUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Toolbar mToolbar;
    private boolean mIsTranslucent;
    private boolean mLayoutToBar;
    private @ColorInt int mColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ToggleButton translucent = (ToggleButton) findViewById(R.id.translucent);
        ToggleButton layout = (ToggleButton) findViewById(R.id.layout);
        translucent.setOnCheckedChangeListener(this);
        layout.setOnCheckedChangeListener(this);

        findViewById(R.id.btn_set_status_bar).setOnClickListener(this);
        findViewById(R.id.btn_set_navigation_bar).setOnClickListener(this);
        findViewById(R.id.btn_set_status_and_navigation_bar).setOnClickListener(this);

        findViewById(R.id.btn_fullscreen).setOnClickListener(this);
        findViewById(R.id.btn_fullscreen_click).setOnClickListener(this);
        findViewById(R.id.btn_fullscreen_drag).setOnClickListener(this);
        findViewById(R.id.btn_fullscreen_drag_translucent).setOnClickListener(this);

        ((AppCompatSpinner)findViewById(R.id.acs_colors)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mColor = Color.BLACK;
                        break;
                    case 1:
                        mColor = Color.RED;
                        break;
                    case 2:
                        mColor = Color.YELLOW;
                        break;
                    case 3:
                        mColor = Color.BLUE;
                        break;
                    case 4:
                        mColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                        break;
                    case 5:
                        mColor = Color.TRANSPARENT;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set_status_bar:
                BarUtil.setStatusBar(this, mColor, mIsTranslucent, mLayoutToBar);
                break;
            case R.id.btn_set_navigation_bar:
                BarUtil.setNavigationBar(this, mColor, mIsTranslucent, mLayoutToBar);
                break;
            case R.id.btn_set_status_and_navigation_bar:
                BarUtil.setStatusAndNavigationBar(this, mColor, mIsTranslucent, mLayoutToBar, mColor, mIsTranslucent, mLayoutToBar);
                break;
            case R.id.btn_fullscreen:
                BarUtil.setFullScreen(this);
                break;
            case R.id.btn_fullscreen_click:
                BarUtil.setFullScreen(this, true, BarUtil.ShowType.CLICK);
                break;
            case R.id.btn_fullscreen_drag:
                BarUtil.setFullScreen(this, true, BarUtil.ShowType.DRAG);
                break;
            case R.id.btn_fullscreen_drag_translucent:
                BarUtil.setFullScreen(this, true, BarUtil.ShowType.DRAG_TRANSLUCENT);
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.layout:
                mLayoutToBar = isChecked;
                break;
            case R.id.translucent:
                mIsTranslucent = isChecked;
                break;
        }
    }
}
