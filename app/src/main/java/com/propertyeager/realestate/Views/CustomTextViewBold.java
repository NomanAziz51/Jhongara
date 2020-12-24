package com.propertyeager.realestate.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewBold extends TextView {

	public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomTextViewBold(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomTextViewBold(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/HelveticaNeueLTStd-LtCn.otf" /*"fonts/Blaimim.ttf"*/);
	    setTypeface(tf);
	}
}