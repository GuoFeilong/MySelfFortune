package com.guofeilong.fortune.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guofeilong.fortune.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 14-3-18.
 * ͼƬͷlistView
 */
public class ImagList extends Activity {
    public static ImageView imageView;
    public static int displayWidth;
    private Bitmap bmp;
    /** ���ڼ�¼����ͼƬ�ƶ�������λ�� */
    public static  Matrix matrix = new Matrix();
    /** ���ڼ�¼ͼƬҪ��������ʱ�������λ�� */
    public static  Matrix currentMatrix = new Matrix();
    public static  Matrix defaultMatrix = new Matrix();
    public static  float imgHeight,imgWidth;
    /** ��¼��������Ƭģʽ���ǷŴ���С��Ƭģʽ */
    public static int mode = 0;// ��ʼ״̬
    /** ������Ƭģʽ */
    public static final int MODE_DRAG = 1;

    /** ���ڼ�¼��ʼʱ�������λ�� */
    public static PointF startPoint = new PointF();
    private ImgListView sListView;
    private List<News> sNewsList;
    private NewsAdapter sNewsAdapter;
    private Context sContext;
    private View headerView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        /* ȡ����Ļ�ֱ��ʴ�С */
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth=dm.widthPixels;
        sContext = ImagList.this;
        sNewsList = new ArrayList<News>();
        geneItems();
        sListView = (ImgListView) findViewById(R.id.xListView);
//		sListView.setPullLoadEnable(true);

        bmp= BitmapFactory.decodeResource(getResources(), R.drawable.top_img);

        headerView=getLayoutInflater().inflate(R.layout.top_img, null);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        initView();
        sListView.addHeaderView(headerView);

        sNewsAdapter = new NewsAdapter();
        sListView.setAdapter(sNewsAdapter);
//        sListView.setOnTouchListener(new TouchListener());
    }

    /**
     * ��ʼ��ͼƬ
     */
    private void initView(){
        float scale = (float)displayWidth/(float)bmp.getWidth();//1080/1800
        matrix.postScale(scale, scale,0,0);
        imageView.setImageMatrix(matrix);
        defaultMatrix.set(imageView.getImageMatrix());
        imgHeight=scale*bmp.getHeight();
        imgWidth=scale*bmp.getWidth();
        ListView.LayoutParams relativeLayout=new ListView.LayoutParams((int)imgWidth,(int)imgHeight);
        imageView.setLayoutParams(relativeLayout);
    }

    protected void onDestroy() {
        super.onDestroy();
        bmp.recycle();
        matrix=new Matrix();
    }


    private class NewsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public NewsAdapter() {
            mInflater = LayoutInflater.from(sContext);
        }

        @Override
        public int getCount() {
            return sNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return sNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder h = null;
            if (convertView == null) {
                h = new Holder();
                convertView = mInflater.inflate(R.layout.list_item,
                        null);
                h.tv = (TextView) convertView.findViewById(R.id.tv_time);
                h.iv = (TextView) convertView.findViewById(R.id.iv_icon);
                h.content = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(h);
            } else {
                h = (Holder) convertView.getTag();
            }
            News news = sNewsList.get(position);
            int time = news.getTime();
            int icon = news.getIcon();
            int img = news.getPic();

            h.tv.setText(sContext.getString(time));
//			h.iv.setText(position+"");
//			h.content.setBackgroundResource(img);

            return convertView;
        }

        private class Holder {
            public TextView tv;
            public TextView iv;
            public TextView content;
        }
    }
    private void geneItems() {
        for (int i = 0; i != 5; ++i) {
            //items.add("refresh cnt " + (++start));
//            ++start;
            News news = new News();
            news.setTime((int) System.currentTimeMillis());
            news.setIcon(R.drawable.psd);
//			news.setPic(R.drawable.hn_sms_in_bg);
            sNewsList.add(news);
        }
    }
}
