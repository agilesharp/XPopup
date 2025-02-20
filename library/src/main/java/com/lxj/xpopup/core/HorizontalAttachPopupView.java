package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 水平方向的依附于某个View或者某个点的弹窗，可以轻松实现微信朋友圈点赞的弹窗效果。
 * Create by lxj, at 2019/3/13
 */
public class HorizontalAttachPopupView extends AttachPopupView {
    public HorizontalAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        defaultOffsetX = 6;
        defaultOffsetY = 0;
    }

    /**
     * 执行附着逻辑
     */
    protected void doAttach() {
        float translationX = 0, translationY = 0;
        int w = getPopupContentView().getMeasuredWidth();
        int h = getPopupContentView().getMeasuredHeight();
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            // 依附于指定点
            isShowLeft = popupInfo.touchPoint.x > XPopupUtils.getWindowWidth(getContext()) / 2;

            // translationX: 在左边就和点左边对齐，在右边就和其右边对齐
            translationX = isShowLeft ? (popupInfo.touchPoint.x - w - defaultOffsetX) : (popupInfo.touchPoint.x + defaultOffsetX);
            translationY = popupInfo.touchPoint.y - h * .5f + defaultOffsetY;
        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            popupInfo.getAtView().getLocationOnScreen(locations);
            Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                    locations[1] + popupInfo.getAtView().getMeasuredHeight());

            int centerX = (rect.left + rect.right) / 2;

            isShowLeft = centerX > XPopupUtils.getWindowWidth(getContext()) / 2;

            translationX = isShowLeft ? (rect.left - w - defaultOffsetX) : (rect.right + defaultOffsetX);
            translationY = rect.top + (rect.height()-h)/2 + defaultOffsetY;
        }
        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        PopupAnimator animator;
        if (isShowLeft) {
            animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromRight);
        } else {
            animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromLeft);
        }
        return animator;
    }
}
