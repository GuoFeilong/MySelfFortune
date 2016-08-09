package com.guofeilong.fortune.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;



/**
 * Created by admin on 14-3-19.
 */
public class ImgListView extends ListView {

    public ImgListView(Context context) {
        super(context);
    }
    public ImgListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void addHeaderView(View v) {
        super.addHeaderView(v);
    }

    /**
     * ���»�����ͼƬ���
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // ��ָѹ����Ļ
            case MotionEvent.ACTION_DOWN:
                ImagList.mode = ImagList.MODE_DRAG;
                // ��¼ImageView��ǰ���ƶ�λ��
                ImagList.currentMatrix.set(ImagList.imageView.getImageMatrix());
                ImagList.startPoint.set(event.getX(), event.getY());
                break;
            // ��ָ����Ļ���ƶ������¼��ᱻ���ϴ���
            case MotionEvent.ACTION_MOVE:
                // ����ͼƬ
                if (ImagList.mode == ImagList.MODE_DRAG) {
                    float dx = event.getX() - ImagList.startPoint.x; // �õ�x����ƶ�����
                    float dy = event.getY() - ImagList.startPoint.y; // �õ�x����ƶ�����
                    // ��û���ƶ�֮ǰ��λ���Ͻ����ƶ�
                    ImagList.matrix.set(ImagList.currentMatrix);
                    float scale = (dy+ImagList.imgHeight) / ImagList.imgHeight;// �õ����ű���
                    if(dy>0){
                        LayoutParams relativeLayout=new LayoutParams((int)(scale*ImagList.imgWidth),(int)(scale*ImagList.imgHeight));
                        ImagList.imageView.setLayoutParams(relativeLayout);
                        ImagList.matrix.postScale(scale, scale,ImagList.imgWidth/2,0);
                    }
                }
                break;
            // ��ָ�뿪��Ļ
            case MotionEvent.ACTION_UP:
                // �������뿪��Ļ��ͼƬ��ԭ
                LayoutParams relativeLayout=new LayoutParams((int)ImagList.imgWidth,(int)ImagList.imgHeight);
                ImagList.imageView.setLayoutParams(relativeLayout);
                ImagList.matrix.set(ImagList.defaultMatrix);
            case MotionEvent.ACTION_POINTER_UP:
                ImagList.mode = 0;
                break;
        }
        ImagList.imageView.setImageMatrix(ImagList.matrix);

        return super.onTouchEvent(event);
    }
}
