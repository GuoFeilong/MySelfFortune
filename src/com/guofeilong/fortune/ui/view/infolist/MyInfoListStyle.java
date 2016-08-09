package com.guofeilong.fortune.ui.view.infolist;

/**
 * Created by jsion on 15/8/19.
 */
public class MyInfoListStyle {
    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 文本域个数
     */
    private int mFieldCount;
    /**
     * 是否可编辑，控制铅笔是否现实
     */
    private boolean mCanEdit;

    public boolean ismCanEdit() {
        return mCanEdit;
    }

    public void setmCanEdit(boolean mCanEdit) {
        this.mCanEdit = mCanEdit;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmFieldCount() {
        return mFieldCount;
    }

    public void setmFieldCount(int mFieldCount) {
        this.mFieldCount = mFieldCount;
    }
}
