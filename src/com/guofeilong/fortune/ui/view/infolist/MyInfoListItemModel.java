package com.guofeilong.fortune.ui.view.infolist;

import java.util.List;

/**
 * Created by jsion on 15/8/24.
 */
public class MyInfoListItemModel {
    /**
     * 被选中的标记位
     */
    private boolean isHasSelected;
    /**
     * item的模式
     */
    private int modelStyle;
    /**
     * 是否显示itemicon
     */
    private boolean isShowItemIcon;
    /**
     * 是否显示选中时的图标
     */
    private boolean isShowSelectedIcon;

    /**
     * 是否展示item删除的图标
     */
    private boolean isShowItemDeleteIcon;
    /**
     * 文本域是否可以编辑
     */
    private boolean isEditable;
    /**
     * 显示的itemicon 是什么状态，选中还是默认展示状态
     */
    private int itemIconSRC;
    /**
     * 选中状态的src
     */
    private int itemSelecedIconSRC;
    /**
     * 文本域的背景
     */
    private int itemEditTextBackgroud;
    /**
     * 删除的itemicon
     */
    private int itemDeleteIconSRC;
    /**
     * 一组文本域每行展示的内容集合
     */
    private List<String> mEditTexts;

    public boolean isShowItemIcon() {
        return isShowItemIcon;
    }

    public void setIsShowItemIcon(boolean isShowItemIcon) {
        this.isShowItemIcon = isShowItemIcon;
    }

    public boolean isShowItemDeleteIcon() {
        return isShowItemDeleteIcon;
    }

    public void setIsShowItemDeleteIcon(boolean isShowItemDeleteIcon) {
        this.isShowItemDeleteIcon = isShowItemDeleteIcon;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public int getItemIconSRC() {
        return itemIconSRC;
    }

    public void setItemIconSRC(int itemIconSRC) {
        this.itemIconSRC = itemIconSRC;
    }

    public int getItemEditTextBackgroud() {
        return itemEditTextBackgroud;
    }

    public void setItemEditTextBackgroud(int itemEditTextBackgroud) {
        this.itemEditTextBackgroud = itemEditTextBackgroud;
    }

    public List<String> getmEditTexts() {
        return mEditTexts;
    }

    public void setmEditTexts(List<String> mEditTexts) {
        this.mEditTexts = mEditTexts;
    }

    public boolean isShowSelectedIcon() {
        return isShowSelectedIcon;
    }

    public void setIsShowSelectedIcon(boolean isShowSelectedIcon) {
        this.isShowSelectedIcon = isShowSelectedIcon;
    }

    public int getItemSelecedIconSRC() {
        return itemSelecedIconSRC;
    }

    public void setItemSelecedIconSRC(int itemSelecedIconSRC) {
        this.itemSelecedIconSRC = itemSelecedIconSRC;
    }

    public int getItemDeleteIconSRC() {
        return itemDeleteIconSRC;
    }

    public void setItemDeleteIconSRC(int itemDeleteIconSRC) {
        this.itemDeleteIconSRC = itemDeleteIconSRC;
    }

    public int getModelStyle() {
        return modelStyle;
    }

    public void setModelStyle(int modelStyle) {
        this.modelStyle = modelStyle;
    }

    public boolean isHasSelected() {
        return isHasSelected;
    }

    public void setIsHasSelected(boolean isHasSelected) {
        this.isHasSelected = isHasSelected;
    }
}
