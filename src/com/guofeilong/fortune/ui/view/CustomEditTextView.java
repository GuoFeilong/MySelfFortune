package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by jsion on 15/9/28.
 */
public class CustomEditTextView extends EditText {
    public interface OnTextChangeListener {
        void myTextChanged(String content);
    }

    private OnTextChangeListener textChangeListener;

    public void setTextChangeListener(OnTextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    public CustomEditTextView(Context context) {
        super(context);
    }

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isChanged;

    @Override
    protected void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(s, start, lengthBefore, lengthAfter);
        if (textChangeListener != null) {
            textChangeListener.myTextChanged(s.toString());
        }

    }
}

