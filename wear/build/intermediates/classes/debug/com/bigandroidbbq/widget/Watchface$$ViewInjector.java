// Generated code from Butter Knife. Do not modify!
package com.bigandroidbbq.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class Watchface$$ViewInjector {
  public static void inject(Finder finder, final com.bigandroidbbq.widget.Watchface target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034141, "field 'face'");
    target.face = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034142, "field 'shadowOverlay'");
    target.shadowOverlay = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034143, "field 'handHour'");
    target.handHour = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034144, "field 'handMinute'");
    target.handMinute = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034145, "field 'handSecond'");
    target.handSecond = (android.widget.ImageView) view;
  }

  public static void reset(com.bigandroidbbq.widget.Watchface target) {
    target.face = null;
    target.shadowOverlay = null;
    target.handHour = null;
    target.handMinute = null;
    target.handSecond = null;
  }
}
