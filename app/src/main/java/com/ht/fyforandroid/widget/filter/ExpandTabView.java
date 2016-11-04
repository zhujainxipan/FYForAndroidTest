package com.ht.fyforandroid.widget.filter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.ht.fyforandroid.R;

import java.util.ArrayList;

/**
 * 筛选器，自处理选中效果切换，抛出子view点击事件处理
 * Created by ZRP on 2015/9/19.
 */
public class ExpandTabView extends LinearLayout implements FilterTabView.OnClickListener {
    // 当前选择的tab
    private FilterTabView selectedView;
    // tab的容器
    private LinearLayout tab_container;
    // 弹窗的容器
    private LinearLayout container;
    // 存储动态创建的tab
    private ArrayList<FilterTabView> tabViews = new ArrayList<FilterTabView>();
    // 当前点击的tab为的position
    private int position = -1;
    // 不支持左右滑动的viewpager
    private NoHorizontalScrollViewPager mViewPager;

    public ExpandTabView(Context context) {
        super(context);
        init();
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.custom_expand_tabview, this);
        tab_container = (LinearLayout) inflate.findViewById(R.id.tab_container);
        container = (LinearLayout) inflate.findViewById(R.id.container);
        mViewPager = (NoHorizontalScrollViewPager) inflate.findViewById(R.id.viewpager);
        // 点击灰色区域后，清除选中状态，关闭弹窗
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeExpand();// 清空所有选中效果，并重置各状态值
            }
        });
    }


    /**
     * 初始化tabs的显示效果
     * @param nameList
     */
    public void setNameList(ArrayList<String> nameList) {
        if (nameList == null) return;
        LinearLayout.LayoutParams tabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams lineParams = new LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < nameList.size(); i++) {
            // 创建tab
            FilterTabView tabView = new FilterTabView(getContext());
            tabView.setText(nameList.get(i));
            tabView.setOnClickListener(this);
            tabViews.add(tabView);
            tabView.setLayoutParams(tabParams);
            tab_container.addView(tabView);
            // 创建line
            if (i < nameList.size() - 1) {
                View line = new View(getContext());
                line.setBackgroundColor(getResources().getColor(R.color.divider_color));
                line.setLayoutParams(lineParams);
                tab_container.addView(line);
            }
        }
    }


    /**
     * 添加expand展现布局
     *
     */
    public void setFilterAdapter(PagerAdapter adapter) {
        if (mViewPager == null) return;
        mViewPager.setAdapter(adapter);
    }

    /**
     * 隐藏弹窗
     */
    private void clearExpandView() {
        container.setVisibility(View.GONE);
    }

    /**
     * 外部调用，清除展现的布局，并取消所有tab的选中效果
     */
    public void closeExpand() {
        position = -1;
        selectedView = null;
        clearExpandView();
        for (int i = 0; i < tabViews.size(); i++) {
            tabViews.get(i).setChecked(false);
        }
    }

    @Override
    public void onClick(FilterTabView tabView) {
        for (int i = 0; i < tabViews.size(); i++) {
            if (tabView == tabViews.get(i)) {
                position = i;
            }
        }

        if (selectedView == null) {
            tabView.setChecked(true);
            selectedView = tabView;

            container.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(position, false);
        } else {
            if (selectedView == tabView) {
                selectedView.setChecked(false);
                clearExpandView();
                selectedView = null;
            } else {
                for (int i = 0; i < tabViews.size(); i++) {
                    FilterTabView filterTabView = tabViews.get(i);
                    filterTabView.setChecked(filterTabView == tabView);
                }
                clearExpandView();
                selectedView = tabView;
                container.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    public void setTabTxt(String text) {
        selectedView.setText(text);
        closeExpand();
    }

}