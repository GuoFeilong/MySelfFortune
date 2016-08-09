package com.guofeilong.fortune.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.guofeilong.fortune.R;

import java.util.ArrayList;

/**
 * Created by jsion on 15/11/5.
 */
public class TreeView extends View {
    /**
     * 数据源
     */
    private ArrayList<TreeEntity> treeEntities;
    /**
     * 间距
     */
    private int commonPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, getContext().getResources().getDisplayMetrics());
    /**
     * 线段间距
     */
    private int lineDistance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65.0f, getContext().getResources().getDisplayMetrics());
    /**
     * 文字大小
     */
    private int textSize;
    /**
     * 文字颜色
     */
    private int textColor;
    /**
     * 线条的颜色
     */
    private int lineColor;

    /**
     * view宽
     */
    private int with;
    /**
     * view高度
     */
    private int height;
    /**
     * 线条的画笔
     */
    private Paint linePaint;
    /**
     * 文字的画笔
     */
    private Paint textPaint;
    /**
     * 方块背景颜色
     */
    private Paint bgPaint;

    /**
     * 控件测量完毕在构造之后 ondraw之前赋值
     */
    public interface OnTreeViewSizeChangeListener {
        void setTreeViewData(ArrayList<TreeEntity> treeEntities);
    }

    private OnTreeViewSizeChangeListener treeViewSizeChangeListener;

    public void setTreeViewSizeChangeListener(OnTreeViewSizeChangeListener treeViewSizeChangeListener) {
        this.treeViewSizeChangeListener = treeViewSizeChangeListener;
    }

    public TreeView(Context context) {
        this(context, null);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TreeView, 0, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TreeView_tree_view_text_size:
                    textSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10.0f, getContext().getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TreeView_tree_view_text_color:
                    textColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.TreeView_tree_view_line_color:
                    lineColor = a.getColor(attr, Color.GRAY);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        treeEntities = new ArrayList<>();
        TreeEntity entity = new TreeEntity("日本AV链", Color.BLACK, getResources().getColor(R.color.demo_title1));
        treeEntities.add(entity);
        entity = new TreeEntity("苍井空,硬盘女", Color.BLACK, getResources().getColor(R.color.demo_title2));
        treeEntities.add(entity);
        entity = new TreeEntity("武藤兰,骨灰级AV开发", Color.BLACK, getResources().getColor(R.color.demo_title3));
        treeEntities.add(entity);
        entity = new TreeEntity("小仓优子,新时代AV女星,~", Color.BLACK, getResources().getColor(R.color.demo_title4));
        treeEntities.add(entity);
        entity = new TreeEntity("吉泽明步,制服女王,!", Color.BLACK, getResources().getColor(R.color.demo_title5));
        treeEntities.add(entity);
        entity = new TreeEntity("神奈川,AV梦工厂,~", Color.BLACK, getResources().getColor(R.color.demo_title6));
        treeEntities.add(entity);
        entity = new TreeEntity("骑兵,步兵,~", Color.BLACK, getResources().getColor(R.color.demo_title7));
        treeEntities.add(entity);


        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);


        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        with = w;
        height = h;

        if (treeViewSizeChangeListener != null) {
            treeViewSizeChangeListener.setTreeViewData(treeEntities);
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String treeHead = treeEntities.get(0).getDesc();
        int treeHeadBgColor = treeEntities.get(0).getBgColor();
        int treeHeadTextColor = treeEntities.get(0).getTcolor();

        float treeHeadHeight = getTextHeight(textPaint, treeHead);
        float treeHeadWith = getTextWith(textPaint, treeHead);

        /**
         * 确定矩形范围
         */
        RectF rectF = new RectF();
        rectF.top = 0;
        rectF.left = 0;
        rectF.right = treeHeadWith * 1.5f;
        rectF.bottom = treeHeadHeight * 2;

        /**
         * 设置线条宽度
         */
        linePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1.0f, getContext().getResources().getDisplayMetrics()));
        canvas.drawLine(rectF.width() / 2, 0, rectF.width() / 2, (treeEntities.size() - 1) * lineDistance, linePaint);


        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(treeHeadBgColor);
        canvas.drawRect(rectF, bgPaint);
        canvas.drawText(treeHead, (rectF.right - treeHeadWith) * 0.5f, treeHeadHeight * 1.5f, textPaint);

        // 把头部数据移除,循环绘制树叶
        treeEntities.remove(0);
        for (int i = 0; i < treeEntities.size(); i++) {
            TreeEntity currentEntity = treeEntities.get(i);
            String currentHead = currentEntity.getDesc();
            int currentBGColor = currentEntity.getBgColor();
            int currentTextColor = currentEntity.getTcolor();


            float currentNoteHeight = getTextHeight(textPaint, currentHead);
            float currentNoteWith = getTextWith(textPaint, currentHead);


            /**
             * 确定树叶矩形范围
             */
            RectF currentRectF = new RectF();
            currentRectF.top = lineDistance * (1 + i) - currentNoteHeight;
            currentRectF.left = lineDistance + rectF.width() / 2;
            currentRectF.right = currentNoteWith * 1.5f + currentRectF.left;
            currentRectF.bottom = currentNoteHeight * 2 + currentRectF.top;

            /**
             * 设置线条宽度
             */
            linePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1.0f, getContext().getResources().getDisplayMetrics()));
            canvas.drawLine(rectF.width() / 2, lineDistance * (1 + i), lineDistance + rectF.width() / 2, lineDistance * (1 + i), linePaint);


            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(currentBGColor);
            canvas.drawRect(currentRectF, bgPaint);
            canvas.drawText(currentHead, currentRectF.left + (currentRectF.width() / 2 - currentNoteWith / 2), currentRectF.top + (currentRectF.height() - currentNoteHeight)*1.5f, textPaint);

        }

    }

    /**
     * 设置数据的方法
     *
     * @param treeEntities
     */
    public void setTreeEntities(ArrayList<TreeEntity> treeEntities) {
        this.treeEntities = treeEntities;
    }

    class TreeEntity {
        /**
         * 字体内容
         */
        String desc;
        /**
         * 字体颜色
         */
        int tcolor;
        /**
         * 方块的背景色
         */
        int bgColor;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getTcolor() {
            return tcolor;
        }

        public void setTcolor(int tcolor) {
            this.tcolor = tcolor;
        }

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public TreeEntity(String desc, int tcolor, int bgColor) {
            this.desc = desc;
            this.tcolor = tcolor;
            this.bgColor = bgColor;
        }


    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    private float getTextWith(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    private float getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


}
