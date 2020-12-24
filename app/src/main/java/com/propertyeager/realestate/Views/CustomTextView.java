package com.propertyeager.realestate.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomTextView(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomTextView(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf");
	    setTypeface(tf);
	}
}