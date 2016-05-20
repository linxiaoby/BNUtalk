package com.bnutalk.util;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.AdapterView;

 abstract class BaseFlingAdapterView extends AdapterView {
	 
	   private int heightMeasureSpec;
	    private int widthMeasureSpec;
	   
		public BaseFlingAdapterView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public BaseFlingAdapterView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public BaseFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
		}

	

		  @Override
		    public void setSelection(int i) {
		        throw new UnsupportedOperationException("Not supported");
		    }

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			this.widthMeasureSpec = widthMeasureSpec;
	        this.heightMeasureSpec = heightMeasureSpec;
		}

	    public int getWidthMeasureSpec() {
	        return widthMeasureSpec;
	    }

	    public int getHeightMeasureSpec() {
	        return heightMeasureSpec;
	    }
		  
}
