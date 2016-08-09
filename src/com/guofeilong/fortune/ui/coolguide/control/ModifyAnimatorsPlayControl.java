package com.guofeilong.fortune.ui.coolguide.control;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.transition.Transition;

import com.guofeilong.fortune.ui.coolguide.AnimatorUtils;

import java.util.ArrayList;

public abstract class ModifyAnimatorsPlayControl implements PlayControl{

    public void onPreRunAnimator(Transition transition, ArrayList<ValueAnimator> animators) {
        ArrayList<ValueAnimator> newAnimators = new ArrayList<>();
        for(int i=0, size=animators.size(); i<size; i++){
            Animator animator = onModifyAnimator(transition, animators.get(i));
            newAnimators.addAll(AnimatorUtils.collectValueAnimators(animator));
        }
        animators.clear();
        animators.addAll(newAnimators);
        newAnimators.clear();
    }
    protected abstract Animator onModifyAnimator(Transition transition, ValueAnimator animator);
}
