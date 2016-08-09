package com.guofeilong.fortune.ui;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.R;

/**
 * recycler view 适配器 由于recycleview没有提供点击事件和长点击事件 需要手动提供
 * 
 * @author guofl
 * 
 */
class MyReAdapter extends RecyclerView.Adapter<MyReAdapter.MyHolder> {
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	private ArrayList<String> listData;
	private Context context;
	private int[] mColors;

	public synchronized void setContext(Context context) {
		this.context = context;
	}

	public synchronized void setmColors(int[] mColors) {
		this.mColors = mColors;
	}

	public synchronized void setListData(ArrayList<String> listData) {
		this.listData = listData;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	/**
	 * 自己重写holder
	 * 
	 * @author guofl
	 * 
	 */
	class MyHolder extends ViewHolder {
		// 书写holder中的控件用来复用
		// 构造的时候传入要复用的item view
		TextView tv;
		RelativeLayout rlContainer;

		public MyHolder(View itemView) {
			super(itemView);
			tv = (TextView) itemView.findViewById(R.id.tv_recycle);
			rlContainer = (RelativeLayout) itemView.findViewById(R.id.rl_container);
		}

	}

	@Override
	public void onBindViewHolder(final MyHolder holder, int arg1) {
		// TODO Auto-generated method stub
		holder.tv.setText(listData.get(arg1));
		holder.rlContainer.setBackgroundColor(mColors[arg1 % 8]);

		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = holder.getPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
				}
			});

			holder.itemView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					int pos = holder.getPosition();
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
					return false;
				}
			});
		}
	}

	@Override
	public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		MyHolder holder = new MyHolder(View.inflate(context, R.layout.item_recycle_view, null));
		return holder;
	}

}
