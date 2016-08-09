package com.guofeilong.fortune.ui.view.infolist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.guofeilong.fortune.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jsion on 15/8/19.
 */
public class MUiInfoListView implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 当前view的状态
     */
    private int mCurrentInfoListState;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 布局填充器
     */
    private LayoutInflater mLayoutInflater;
    /**
     * 填充的view
     */
    private View mUiInfoListView;


    //**************InfoList属性start*******************
    /**
     * infolist背景色
     */
    private int mBackGroundColor;
    /**
     * inforlist 标题
     */
    private String mTitle;
    /**
     * inforlist 标题大小
     */
    private int mTitleSize;
    /**
     * inforlist 标题颜色
     */
    private int mTitleColor;
    /**
     * inforlist 标题后面要展开的图标
     */
    private int mExpandIcon = R.drawable.bg_muilist_title_icon_up;
    /**
     * inforlist 标题后面要伸缩的图标
     */
    private int mCollapseIcon = R.drawable.bg_muilist_title_icon_down;
    /**
     * inforlist 效果图中铅笔的图标✏️
     */
    private int mEditorIcon = R.drawable.bg_muilist_title_icon_edit;
    /**
     * inforlist 效果图中铅笔的另外一个图标✏️
     */
    private int mCommitIcon = R.drawable.bg_muilist_title_icon_commit;
    /**
     * inforlist 文本区域前面图标
     */
    private int mItemIcon = R.drawable.bg_muilist_text_icon_item;
    /**
     * inforlist 文本区域选中的☑️的图标，默认展示第一个
     */
    private int mSelectedItemIcon = R.drawable.bg_muilist_text_icon_selected;
    /**
     * inforlist 底部在编辑状态下添加一组的图标
     */
    private int mAddICon = R.drawable.bg_muilist_icon_add_text;
    /**
     * inforlist 文本区域后面的删除图标，默认展示第一个
     */
    private int mDeleteIcon = R.drawable.bg_muilist_text_icon_delete;

    //**************InfoList属性End*********************

    /**
     * 数据
     */
    private MyInfoListStyle mInfoListStyle;
    /**
     * 紧缩状态下展示的数据
     */
    private MyInfoListItemModel dataForShow;
    private List<MyInfoListItemModel> mDataForList;
    private MyUinfoListAdapter myUinfoListAdapter;

    private List<EditText> mShowEditTexts;

    public int getmCurrentInfoListState() {
        return mCurrentInfoListState;
    }

    public void setmCurrentInfoListState(int mCurrentInfoListState) {
        this.mCurrentInfoListState = mCurrentInfoListState;
    }

    /**
     * 构造方式传入上下文
     *
     * @param context
     */
    public MUiInfoListView(Context context, MyInfoListStyle infolistStyle) {
        this.mContext = context;
        this.mInfoListStyle = infolistStyle;

        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInfoListController = new MInfoListController();

        infolateMInFoListLayout();
        initDataForList();
        initInFolistViews();
        initInFolistEvent();
    }

    /**
     * 初始化数据
     */
    private void initDataForList() {
//        mAllEditTexts = new HashMap<Integer, List<EditText>>();

        //1，初始化收缩状态下的展示数据
        dataForShow = new MyInfoListItemModel();

        dataForShow.setIsEditable(false);
        dataForShow.setIsShowItemDeleteIcon(false);
        dataForShow.setIsShowItemIcon(true);
        dataForShow.setIsShowSelectedIcon(false);
        dataForShow.setItemEditTextBackgroud(R.color.transparent);


        List<String> tempStrings = new ArrayList<String>();
        for (int j = 0; j < mInfoListStyle.getmFieldCount(); j++) {
            tempStrings.add("");
        }
        dataForShow.setmEditTexts(tempStrings);
        dataForShow.setItemDeleteIconSRC(mDeleteIcon);
        dataForShow.setItemIconSRC(mItemIcon);


        //2，初始化list后选数据,默认是展开状态下的数据
        mDataForList = new ArrayList<MyInfoListItemModel>();
    }


    /**
     * infolist 背景色
     */
    private LinearLayout mUiInfolistBackgroud;
    /**
     * 动态的添加文本域个数的容器
     */
    private LinearLayout mUiInfoListTextContainer;
    /**
     * 添加一组文本域的按钮底部，在编辑状态下可见
     */
    private ImageView mUiInfoListAddTextComponents;
    /**
     * 标题名和图标的容器，有点击事件进行状态切换 收缩和展开
     */
    private RelativeLayout mUiInfoListTitleNameIconContainer;
    /**
     * 标题名
     */
    private TextView mUiInfoListTitleName;
    /**
     * 标题icon 展开一个背景  收缩一个背景
     */
    private ImageView mUiInfoListTitleIcon;
    /**
     * 编辑的icon 编辑状态一个背景 展开状态一个背景
     */
    private RelativeLayout mUiInfoListEditIcon;

    /**
     * 编辑icon的控件
     */
    private ImageView mUiInfoListeditPen;


    /**
     * 标题底部的线条 根据展开现实 收缩隐藏
     */
    private ImageView mUiInfolistLine;
    /**
     * 编辑状态下显示出来都删除组按钮，
     */
    private ImageView mUiInfoListDeleteIcon;


    /**
     * 控件默认收缩状态下的展示的view
     */
    private LinearLayout mUinfoListForDefaultShow;
    /**
     * 展开状态或者编辑状态下展示的listview
     */
    private ListView mUinfoListForCompons;

    /**
     * 实例华infolist views
     */
    private void initInFolistViews() {
        mUiInfolistBackgroud = (LinearLayout) mUiInfoListView.findViewById(R.id.ll_muilist_backgroud_color);
        mUiInfoListTextContainer = (LinearLayout) mUiInfoListView.findViewById(R.id.ll_muilist_text_container);
        mUiInfoListAddTextComponents = (ImageView) mUiInfoListView.findViewById(R.id.iv_muilist_add_text_component);
        mUiInfoListTitleNameIconContainer = (RelativeLayout) mUiInfoListView.findViewById(R.id.rl_muilist_title_name_icon_container);
        mUiInfoListTitleName = (TextView) mUiInfoListView.findViewById(R.id.tv_muilist_title_name);
        mUiInfoListTitleIcon = (ImageView) mUiInfoListView.findViewById(R.id.iv_muilist_title_icon);
        mUiInfoListEditIcon = (RelativeLayout) mUiInfoListView.findViewById(R.id.iv_muilist_edit_icon);
        mUiInfolistLine = (ImageView) mUiInfoListView.findViewById(R.id.iv_muilist_title_line);
        mUiInfoListeditPen = (ImageView) mUiInfoListView.findViewById(R.id.iv_muilist_edit_pen);


        //用listview 改造相关
        mUinfoListForDefaultShow = (LinearLayout) mUiInfoListView.findViewById(R.id.include_layout_minfolist_text_component);
        mUinfoListForCompons = (ListView) mUiInfoListView.findViewById(R.id.lv_my_minfolist_component);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       if(mUinfoListForDefaultShow.getChildCount()<=0){
           mUinfoListForDefaultShow.addView(creatTextCompon(-1, mShowEditTexts, dataForShow, mInfoListStyle.getmFieldCount(), false));
       }



        //1,根据position修改当前 item的选中状态图标
        for (int i = 0; i < mDataForList.size(); i++) {
            if (i == position) {
                mDataForList.get(i).setItemSelecedIconSRC(mSelectedItemIcon);
                mDataForList.get(i).setIsHasSelected(true);
            } else {
                mDataForList.get(i).setItemSelecedIconSRC(mItemIcon);
                mDataForList.get(i).setIsHasSelected(false);

            }
        }
        myUinfoListAdapter.notifyDataSetChanged();
        //2,拿到当前改变的数据
        dataForShow = mDataForList.get(position);
    }

    /**
     * 默认候选地址的适配器
     */
    class MyUinfoListAdapter extends BaseAdapter {
        private List<MyInfoListItemModel> listData;
        private Context context;
        private LayoutInflater inflater;

        public void setListData(List<MyInfoListItemModel> listData) {
            this.listData = listData;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setInflater(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = creatTextCompon(position, null, listData.get(position), mInfoListStyle.getmFieldCount(), true);

            return convertView;
        }
    }

    /**
     * 所有的文本域的组集合
     */
    private int tempPosition;

    /**
     * 创建一组文本域
     */
    private LinearLayout creatTextCompon(final int position, List<EditText> editTexts, final MyInfoListItemModel itemModel, int filedCount, boolean isShowBottomLine) {

        LinearLayout tempLL = (LinearLayout) mLayoutInflater.inflate(R.layout.infolist_layout_minfolist_unit_view, null);


        for (int i = 0; i < filedCount; i++) {

            tempPosition = i;

            View tempTextComponent = mLayoutInflater.inflate(R.layout.infolist_layout_minfolist_text_component, null);
            ImageView tempSelectIcon = (ImageView) tempTextComponent.findViewById(R.id.iv_muilist_text_selected_icon);
            ImageView tempDeleteIcon = (ImageView) tempTextComponent.findViewById(R.id.iv_muilist_text_delete_icon);
            EditText tempTextDesc = (EditText) tempTextComponent.findViewById(R.id.et_muilist_text_content);
            ImageView tempBottomLine = (ImageView) tempTextComponent.findViewById(R.id.iv_muilist_text_line);


            tempTextDesc.addTextChangedListener(new InfoListTextWatcher(tempTextDesc, itemModel, tempPosition));


            if (editTexts != null) {
                editTexts.add(tempTextDesc);
            }

            if (itemModel.getModelStyle() == MyConstants.INFOLIST_EDIT_STATE) {
                // 编辑模式
                if (itemModel.isHasSelected() && 0 == i) {
                    // 当前是选中项，选中图标可见
                    tempSelectIcon.setVisibility(View.VISIBLE);
                    if (itemModel != null) {

                        tempSelectIcon.setImageResource(itemModel.getItemSelecedIconSRC());
                    }
                } else {
                    tempSelectIcon.setVisibility(View.INVISIBLE);
                }

                // 删除按钮可见
                if (0 == i && (!itemModel.isHasSelected())) {
                    // 编辑模式下只有非选中的第一个才会出现删除图标，选中的item不能删除

                    tempDeleteIcon.setVisibility(View.VISIBLE);
                    tempDeleteIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDataForList.remove(itemModel);
                            myUinfoListAdapter.notifyDataSetChanged();
                        }
                    });
                }

                // edittext 从新获得焦点可编辑
                if (itemModel != null) {

                    tempTextDesc.setBackgroundResource(itemModel.getItemEditTextBackgroud());
                }
                tempTextDesc.setFocusableInTouchMode(true);
                tempTextDesc.setFocusable(true);

            } else {
                // 非编辑模式


                if (0 == i) {

                    tempSelectIcon.setImageResource(itemModel.getItemSelecedIconSRC());
                } else {
                    tempSelectIcon.setVisibility(View.INVISIBLE);
                }

                tempDeleteIcon.setImageResource(itemModel.getItemDeleteIconSRC());

                tempTextDesc.setFocusable(false);
                tempTextDesc.setFocusableInTouchMode(false);
                tempTextDesc.setBackgroundColor(mContext.getResources().getColor(itemModel.getItemEditTextBackgroud()));


            }
            if (isShowBottomLine && i == filedCount - 1) {
                tempBottomLine.setVisibility(View.VISIBLE);
            } else {
                tempBottomLine.setVisibility(View.INVISIBLE);
            }
            if (itemModel != null) {

                tempTextDesc.setText(itemModel.getmEditTexts().get(i));
            }

            tempLL.addView(tempTextComponent);
        }

        return tempLL;
    }


    /**
     * 初始化事件
     */
    private void initInFolistEvent() {
        // 默认是第一种状态，收缩
        mCurrentInfoListState = MyConstants.INFOLIST_COMMON_STATE;
        // title线条不可见
        mUiInfolistLine.setVisibility(View.GONE);

        // 根据外界传入的是否可编辑来显示铅笔状态
        if (mInfoListStyle.ismCanEdit()) {
            mUiInfoListEditIcon.setVisibility(View.VISIBLE);
            //给编辑的铅笔设置点击事件，根据不同的状态进入编辑模式或者收缩模式
            mUiInfoListEditIcon.setOnClickListener(this);
        } else {
            mUiInfoListEditIcon.setVisibility(View.GONE);
        }

        //title 区域的点击事件用来改变收缩或者展开状态

        mUiInfoListTitleNameIconContainer.setOnClickListener(this);
        mUiInfoListAddTextComponents.setOnClickListener(this);

        //1，获取到FIELDCOUNT个数
        int fildCount = mInfoListStyle.getmFieldCount();

        //----用listview改造步骤
        //1,给默认头展示数据赋值
        mShowEditTexts = new ArrayList<EditText>();


        if(mDataForList!=null&&mDataForList.size()>0){
            mUinfoListForDefaultShow.addView(creatTextCompon(-1, mShowEditTexts, dataForShow, fildCount, false));
        }
        //2,给listview赋值，默认不可见
        myUinfoListAdapter = new MyUinfoListAdapter();
        myUinfoListAdapter.setContext(mContext);
        myUinfoListAdapter.setInflater(mLayoutInflater);
        myUinfoListAdapter.setListData(mDataForList);
        mUinfoListForCompons.setAdapter(myUinfoListAdapter);

        //3,给listview设置item点击事件
        mUinfoListForCompons.setOnItemClickListener(this);


    }


    /**
     * 填充infolist 整体布局
     */
    private void infolateMInFoListLayout() {
        mUiInfoListView = mLayoutInflater.inflate(R.layout.infolist_layout_minfolist_container, null);
    }

    /**
     * 开始编辑接口
     */
    private void beginEditing() {

    }

    /**
     * 结束编辑接口
     */
    private void endEditing() {

    }

    /**
     * 开始☑️选择接口
     */
    private void beginSelect() {

    }

    /**
     * 结束选择接口
     */
    private void endSelect() {

    }


    public void setmBackGroundColor(int mBackGroundColor) {
        this.mBackGroundColor = mBackGroundColor;
        mUiInfolistBackgroud.setBackgroundColor(mBackGroundColor);
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
        mUiInfoListTitleName.setText(mTitle);
    }

    public void setmTitleSize(int mTitleSize) {
        this.mTitleSize = mTitleSize;
        mUiInfoListTitleName.setTextSize(mTitleSize);
    }

    public void setmTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
        mUiInfoListTitleName.setTextColor(mTitleColor);
    }

    public void setmExpandIcon(int mExpandIcon) {
        this.mExpandIcon = mExpandIcon;
        //TODO 绑定点击事件，根据当前列表打状态更新src如果客户没设置用默认的
    }

    public void setmCollapseIcon(int mCollapseIcon) {
        this.mCollapseIcon = mCollapseIcon;
        //TODO 绑定点击事件，根据当前列表打状态更新src如果客户没设置用默认的

    }

    public void setmEditorIcon(int mEditorIcon) {
        this.mEditorIcon = mEditorIcon;
    }

    public void setmCommitIcon(int mCommitIcon) {
        this.mCommitIcon = mCommitIcon;
    }

    public void setmItemIcon(int mItemIcon) {
        this.mItemIcon = mItemIcon;
    }

    public void setmSelectedItemIcon(int mSelectedItemIcon) {
        this.mSelectedItemIcon = mSelectedItemIcon;
    }

    public void setmAddICon(int mAddICon) {
        this.mAddICon = mAddICon;
        mUiInfoListAddTextComponents.setImageResource(mAddICon);
    }

    public void setmDeleteIcon(int mDeleteIcon) {
        this.mDeleteIcon = mDeleteIcon;
    }

    public View getmUiInfoListView() {
        return mUiInfoListView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_muilist_title_name_icon_container:


                if (mUiInfoListAddTextComponents.getVisibility() == View.VISIBLE) {
                    mUiInfoListAddTextComponents.setVisibility(View.INVISIBLE);
                }

                mInfoListController.onFoldOrUnFold();
                Log.e("unfold_flag", "我是展开布局。。。");


                if (mCurrentInfoListState == MyConstants.INFOLIST_EDIT_STATE) {
                    mUiInfoListeditPen.setImageResource(mCommitIcon);
                } else {
                    mUiInfoListeditPen.setImageResource(mEditorIcon);
                }


                break;
            case R.id.iv_muilist_edit_icon:

                if (mCurrentInfoListState == MyConstants.INFOLIST_EDIT_STATE) {
                    mUiInfoListeditPen.setImageResource(mEditorIcon);
                } else {
                    mUiInfoListeditPen.setImageResource(mCommitIcon);
                }


                mInfoListController.onEditorTapped();
                Log.e("edit_flag", "我是编辑按钮。。。");
                break;
            case R.id.iv_muilist_add_text_component:
                // TODO: 15/8/20 增加一组文本域
                Log.e("add_flag", "我是添加按钮");
                addButtonClick();
                break;
        }
    }

    /**
     * 点击添加按钮，增加一组文本
     */
    private void addButtonClick() {
        //1，初始化收缩状态下的展示数据
        MyInfoListItemModel dataForShow = new MyInfoListItemModel();
        dataForShow.setIsEditable(true);
        dataForShow.setIsShowItemDeleteIcon(true);
        dataForShow.setIsShowItemIcon(false);
        dataForShow.setIsShowSelectedIcon(false);

        List<String> tempStrings = new ArrayList<String>();
        for (int j = 0; j < mInfoListStyle.getmFieldCount(); j++) {
            tempStrings.add("");
        }
        dataForShow.setmEditTexts(tempStrings);
        dataForShow.setItemDeleteIconSRC(mDeleteIcon);
        dataForShow.setItemIconSRC(mItemIcon);
        dataForShow.setItemSelecedIconSRC(mItemIcon);

        dataForShow.setItemEditTextBackgroud(R.drawable.bg_edit_shape_my);
        dataForShow.setModelStyle(MyConstants.INFOLIST_EDIT_STATE);
        mDataForList.add(dataForShow);

        myUinfoListAdapter.notifyDataSetChanged();
    }

    private MInfoListController mInfoListController;

    class MInfoListController {
        private int mUiInfoListState;

        /**
         * 点击编辑✏图标调用
         */
        private void onEditorTapped() {
            // TODO: 15/8/24 点击编辑按钮后的触发事件

            switch (mCurrentInfoListState) {
                case MyConstants.INFOLIST_COMMON_STATE:


                    // 先展开，在编辑
                    onFoldOrUnFold();
                    // 编辑状态参与的对象，文本域前面状态ionc，后面删除ionc 中间编辑文本，改变编辑背景，底部add按钮组按钮出现
                    mCurrentInfoListState = MyConstants.INFOLIST_EDIT_STATE;
                    editStateForTextComp();

                    // 进入编辑模式后显示add 文本域按钮
                    mUiInfoListAddTextComponents.setVisibility(View.VISIBLE);
                    break;
                case MyConstants.INFOLIST_UNFOLD_STATE:


                    // 直接进入编辑模式
                    mCurrentInfoListState = MyConstants.INFOLIST_EDIT_STATE;

                    editStateForTextComp();

                    // 进入编辑模式后显示add 文本域按钮

                    mUiInfoListAddTextComponents.setVisibility(View.VISIBLE);


                    break;
                case MyConstants.INFOLIST_EDIT_STATE:


                    // 退出编辑模式
                    mCurrentInfoListState = MyConstants.INFOLIST_COMMON_STATE;

                    editStateForTextComp();

                    // 退出编辑模式，按钮不可见
                    mUiInfoListAddTextComponents.setVisibility(View.INVISIBLE);

                    break;
            }
        }

        /**
         * 在展开模式下点击一组地址时候调用的事件
         */
        private void onSelectorTapped() {

        }

        /**
         * 展开后者收缩
         */
        private void onFoldOrUnFold() {
            switch (mCurrentInfoListState) {
                case MyConstants.INFOLIST_COMMON_STATE:
                    // 当前是收缩状态展开，线条显示，箭头朝上，改变状态为展开


                    mCurrentInfoListState = MyConstants.INFOLIST_UNFOLD_STATE;
                    mUiInfoListTitleIcon.setImageResource(mExpandIcon);
                    mUiInfolistLine.setVisibility(View.VISIBLE);

                    setMuiInfoListTextFoldOrUnfoldListView();

                    break;
                case MyConstants.INFOLIST_UNFOLD_STATE:
                    //当前是展开状态收缩，线条消失，箭头朝下，改变状态为收缩
                    mCurrentInfoListState = MyConstants.INFOLIST_COMMON_STATE;
                    mUiInfoListTitleIcon.setImageResource(mCollapseIcon);
                    mUiInfolistLine.setVisibility(View.INVISIBLE);

                    setMuiInfoListTextFoldOrUnfoldListView();

                    break;
                case MyConstants.INFOLIST_EDIT_STATE:
                    // 当前是编辑状态，点击后，返回紧缩状态，所有的文本域控件，不可编辑，背景透明，deleteicon消失，
                    // item icon 暂时选中第一个

                    mCurrentInfoListState = MyConstants.INFOLIST_COMMON_STATE;
                    mUiInfoListTitleIcon.setImageResource(mCollapseIcon);
                    mUiInfolistLine.setVisibility(View.INVISIBLE);

                    setMuiInfoListTextFoldOrUnfoldListView();

                    break;
            }

        }
    }

    /**
     * 点击标题美容区域显示或者隐藏listview
     */
    private void setMuiInfoListTextFoldOrUnfoldListView() {
        switch (mCurrentInfoListState) {
            case MyConstants.INFOLIST_COMMON_STATE:
                mUinfoListForDefaultShow.setVisibility(View.VISIBLE);
                mUinfoListForCompons.setVisibility(View.GONE);

                for (int i = 0; i < mShowEditTexts.size(); i++) {
                    mShowEditTexts.get(i).setText(dataForShow.getmEditTexts().get(i));
                }

                for (int i = 0; i < mDataForList.size(); i++) {
                    mDataForList.get(i).setModelStyle(MyConstants.INFOLIST_COMMON_STATE);
                    mDataForList.get(i).setItemEditTextBackgroud(R.color.transparent);
                }

                break;
            case MyConstants.INFOLIST_UNFOLD_STATE:
                mUinfoListForDefaultShow.setVisibility(View.GONE);
                mUinfoListForCompons.setVisibility(View.VISIBLE);


                for (int i = 0; i < mDataForList.size(); i++) {
                    mDataForList.get(i).setModelStyle(MyConstants.INFOLIST_COMMON_STATE);
                    mDataForList.get(i).setItemEditTextBackgroud(R.color.transparent);
                }
                break;
            case MyConstants.INFOLIST_EDIT_STATE:
                mUinfoListForDefaultShow.setVisibility(View.VISIBLE);
                mUinfoListForCompons.setVisibility(View.GONE);

                for (int i = 0; i < mShowEditTexts.size(); i++) {
                    mShowEditTexts.get(i).setText(dataForShow.getmEditTexts().get(i));
                }

                break;
        }

        myUinfoListAdapter.notifyDataSetChanged();
    }

    /**
     * 点击编辑按钮
     */
    private void editStateForTextComp() {
        switch (mCurrentInfoListState) {
            case MyConstants.INFOLIST_COMMON_STATE:
                // 进入展开模式
                mCurrentInfoListState = MyConstants.INFOLIST_COMMON_STATE;
                mInfoListController.onFoldOrUnFold();

                for (int i = 0; i < mDataForList.size(); i++) {
                    mDataForList.get(i).setItemEditTextBackgroud(R.color.transparent);
                    mDataForList.get(i).setModelStyle(MyConstants.INFOLIST_UNFOLD_STATE);
                }


                break;
            case MyConstants.INFOLIST_EDIT_STATE:
                // 进入编辑模式
                mCurrentInfoListState = MyConstants.INFOLIST_EDIT_STATE;


                for (int i = 0; i < mDataForList.size(); i++) {
                    mDataForList.get(i).setItemEditTextBackgroud(R.drawable.bg_edit_shape_my);
                    mDataForList.get(i).setModelStyle(MyConstants.INFOLIST_EDIT_STATE);

                }


                break;
            case MyConstants.INFOLIST_UNFOLD_STATE:
                // 进入展开模式
                mCurrentInfoListState = MyConstants.INFOLIST_UNFOLD_STATE;
                mInfoListController.onFoldOrUnFold();

                for (int i = 0; i < mDataForList.size(); i++) {
                    mDataForList.get(i).setItemEditTextBackgroud(R.color.transparent);
                    mDataForList.get(i).setModelStyle(MyConstants.INFOLIST_UNFOLD_STATE);

                }
                break;

        }
        myUinfoListAdapter.notifyDataSetChanged();


    }


    /**
     * 自己文本监听
     */

    class InfoListTextWatcher implements TextWatcher {
        private EditText editText;
        private int tempPosition;
        private MyInfoListItemModel itemModel;

        public InfoListTextWatcher(EditText editText, MyInfoListItemModel itemModel, int tempPosition) {
            this.editText = editText;
            this.tempPosition = tempPosition;
            this.itemModel = itemModel;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            itemModel.getmEditTexts().remove(tempPosition);
            itemModel.getmEditTexts().add(tempPosition, editText.getText().toString());
        }
    }
}
