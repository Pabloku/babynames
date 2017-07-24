package com.kamisoft.babynames.presentation.custom_view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

@CoordinatorLayout.DefaultBehavior(MoveUpwardBehavior.class)
public class MovableFrameLayout extends FrameLayout {
  public MovableFrameLayout(Context context) {
    super(context);
  }

  public MovableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MovableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}