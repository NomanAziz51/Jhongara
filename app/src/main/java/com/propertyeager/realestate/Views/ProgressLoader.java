package com.propertyeager.realestate.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.propertyeager.realestate.R;


/**
 * Created by ahsan on 28-Nov-16.
 */

public class ProgressLoader extends RelativeLayout {

    public ProgressLoader(Context context) {
        super(context);
        init();
        hide();
    }

    public ProgressLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        hide();
    }

    private void init() {
        inflate(getContext(), R.layout.view_loader, this);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

}
