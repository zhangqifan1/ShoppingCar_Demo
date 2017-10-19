package com.bawei.shoppingcar_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * 购物车Demo
 * 涉及在知识点:二级列表 ExpandableListView
 */
public class MainActivity extends AppCompatActivity {

    private ExpandableListView mElv;
    /**
     * 全选
     */
    private CheckBox mCheckboxAll;
    /**
     * (0)
     */
    private TextView mCheckedNum;
    /**
     * 合计:￥0.00
     */
    private TextView mHeji;
    /**
     * 结算
     */
    private TextView mJieSuan;
    private ExpandableListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        adapter = new ExpandableListViewAdapter(this);
        mElv.setAdapter(adapter);
        mCheckboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置商品全部选中
                adapter.checkAllShop(mCheckboxAll.isChecked());
                //计算选中的价格和数量
                String shopPrice = adapter.getShopPrice();
                //判断商品是否全部选中
                boolean b = adapter.selectAll();

                String[] split = shopPrice.split(",");
                mHeji.setText(split[0]);
                mCheckedNum.setText(split[1]);
                mCheckboxAll.setChecked(b);
            }
        });
        adapter.getAdapterData(new ExpandableListViewAdapter.AdapterData() {
            @Override
            public void Data(View v, String str, boolean b) {
                String[] split = str.split(",");
                mHeji.setText(split[0]);
                mCheckedNum.setText(split[1]);
                mCheckboxAll.setChecked(b);
            }
        });

        mCheckboxAll.setChecked(adapter.selectAll());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mElv = (ExpandableListView) findViewById(R.id.elv);
        mCheckboxAll = (CheckBox) findViewById(R.id.checkboxAll);
        mCheckedNum = (TextView) findViewById(R.id.checkedNum);
        mHeji = (TextView) findViewById(R.id.heji);
        mJieSuan = (TextView) findViewById(R.id.jieSuan);
    }
}
