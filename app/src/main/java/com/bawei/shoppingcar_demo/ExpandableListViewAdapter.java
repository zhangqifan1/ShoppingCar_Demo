package com.bawei.shoppingcar_demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 张祺钒
 * on2017/10/19.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<List<Bean>> dataList;
    private List<HashMap<Integer, Boolean>> childList;
    private HashMap<Integer, Boolean> groupHashMap;
    private String[][] child;
    private String[] group;


    public ExpandableListViewAdapter(Context context) {
        this.context = context;
        initData();
    }

    /**
     * 准备组数据 状态存取数组
     * 此处的5都代表有5条租数据  而child的5则是要和组数据同步
     * 保存组状态的Map
     * 保存子组状态的Map
     * 装Bean的List集合  List<List<Bean>>
     */
    private void initData() {
        group = new String[5];
        child = new String[5][];
        groupHashMap = new HashMap<>();
        childList = new ArrayList<>();
        dataList = new ArrayList<>();

        //开始装数据  因为定义5组  ~
        for (int i = 0; i < 5; i++) {
            group[i] = "商家" + i;
            //为每一个组数据 赋予一个初始状态 false(用于全选 反选)
            groupHashMap.put(i, false);

            //准备子组装的具体数据  这里是3条  即每个商家里商品的数量  这个用来保存子组里边商家0商品X
            String[] strings = new String[3];

            //定义Map集合 用以存放每条子数据的状态
            HashMap<Integer, Boolean> map = new HashMap<>();

            //定义子数据Bean   List<List<Bean>> dataList  定义他的数据
            List<Bean> been = new ArrayList<>();

            //开始添加数据
            for (int j = 0; j < strings.length; j++) {
                strings[j] = "商家" + i + "商品" + j;
                //赋予初始状态
                map.put(j, false);

                Bean bean = new Bean("100", "1");
                been.add(bean);
            }
            child[i] = strings;
            childList.add(map);
            dataList.add(been);
        }
    }


    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder=new GroupViewHolder();
            convertView = View.inflate(context, R.layout.group_item, null);
            holder.cb = convertView.findViewById(R.id.group_cb);
            holder.tv = convertView.findViewById(R.id.group_tv);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tv.setText(group[groupPosition]);
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //赋  相反的值
                groupHashMap.put(groupPosition, !groupHashMap.get(groupPosition));

                //设置二级列表的选中状态,根据以及列表的状态改变
                setChildCheckAll();

                //计算选中的价格和数量
                String shopPrice = getShopPrice();

                //判断商品是否全部选中
                boolean b = selectAll();
                adapterData.Data(v, shopPrice, b);
            }


        });
        holder.cb.setChecked(groupHashMap.get(groupPosition));
        return convertView;
    }

    /**
     * 判断商品是否全部选中
     */
    boolean selectAll() {
        boolean isChecked = true;
        for (int i = 0; i < childList.size(); i++) {
            HashMap<Integer, Boolean> map = childList.get(i);
            Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
            for (Map.Entry<Integer, Boolean> entry : entries) {
                if (!entry.getValue()) {//只要其中有一个没有选中  就置为false
                    isChecked = false;
                    break;//不在判断
                }
            }
        }
        return isChecked;
    }

    /**
     * 计算选中的价格和数量
     */
    String getShopPrice() {
        int price = 0;
        int number = 0;
        for (int i = 0; i < childList.size(); i++) {
            HashMap<Integer, Boolean> map = childList.get(i);
            Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
            for (Map.Entry<Integer, Boolean> entry : entries) {
                if (entry.getValue()) {//如果标识为true  说明选中了
                    //找到对应的Bean
                    Bean bean = dataList.get(i).get(entry.getKey());
                    price += Integer.parseInt(bean.getPrice()) * Integer.parseInt(bean.getNumber());
                    number += Integer.parseInt(bean.getNumber());
                }
            }
        }
        return price + "," + number;
    }

    /**
     * 设置二级列表的选中状态,根据以及列表的状态改变
     */
    private void setChildCheckAll() {
        for (int i = 0; i < childList.size(); i++) {
            HashMap<Integer, Boolean> map = childList.get(i);
            Set<Map.Entry<Integer, Boolean>> entrySet = map.entrySet();
            for (Map.Entry<Integer, Boolean> entry : entrySet) {
                entry.setValue(groupHashMap.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.child_item, null);
            holder.cb = convertView.findViewById(R.id.child_cb);
            holder.tv = convertView.findViewById(R.id.child_tv);
            holder.jianshao = convertView.findViewById(R.id.jianshao);
            holder.zengjia = convertView.findViewById(R.id.zengjia);
            holder.number = convertView.findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.tv.setText(child[groupPosition][childPosition]);
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer, Boolean> map = childList.get(groupPosition);
                map.put(childPosition, !map.get(childPosition));

                //判断二级列表是否全部选中
                ChildIsChecked(groupPosition);

                //计算选中的价格和数量
                String shopPrice = getShopPrice();

                //判断商品是否全部选中
                boolean b = selectAll();

                adapterData.Data(v, shopPrice, b);
            }
        });
        final ChildViewHolder finalHolder = holder;
        holder.zengjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bean> been = dataList.get(groupPosition);
                String s = finalHolder.number.getText().toString();
                int i = Integer.parseInt(s);
                ++i;
                been.get(childPosition).setNumber(i + "");

                //计算选中的价格和数量
                String shopPrice = getShopPrice();
                //判断是否全部选中
                boolean b = selectAll();

                adapterData.Data(v, shopPrice, b);
                notifyDataSetChanged();
            }
        });
        holder.zengjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bean> been = dataList.get(groupPosition);
                String s = finalHolder.number.getText().toString();
                int i = Integer.parseInt(s);
                ++i;
                been.get(childPosition).setNumber(i + "");

                //计算选中的价格和数量
                String shopPrice = getShopPrice();
                //判断是否全部选中
                boolean b = selectAll();

                adapterData.Data(v, shopPrice, b);
                notifyDataSetChanged();
            }
        });
        holder.jianshao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bean> been = dataList.get(groupPosition);
                String s = finalHolder.number.getText().toString();
                int i = Integer.parseInt(s);
                if (i > 1) {
                    --i;
                }
                been.get(childPosition).setNumber(i + "");

                //计算选中的价格和数量
                String shopPrice = getShopPrice();
                //判断是否全部选中
                boolean b = selectAll();

                adapterData.Data(v, shopPrice, b);
                notifyDataSetChanged();
            }
        });
        holder.number.setText(dataList.get(groupPosition).get(childPosition).getNumber().toString());

        holder.cb.setChecked(childList.get(groupPosition).get(childPosition));
        return convertView;
    }

    private void ChildIsChecked(int groupPosition) {
        boolean isChecked = true;
        HashMap<Integer, Boolean> map = childList.get(groupPosition);
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            if (!entry.getValue()) {
                isChecked = false;
                break;
            }
        }
        groupHashMap.put(groupPosition, isChecked);
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }

    static class GroupViewHolder {
        TextView tv;
        CheckBox cb;
    }

    static class ChildViewHolder {
        TextView tv;
        CheckBox cb;
        TextView jianshao;
        TextView zengjia;
        TextView number;
    }
    //全选
    public void checkAllShop(boolean checked) {
        Set<Map.Entry<Integer, Boolean>> entries = groupHashMap.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(checked);
        }
        //调用让二级列表全选的方法
        setChildCheckAll();
        notifyDataSetChanged();
    }

    /**
     * 自定义一个
     */
    private AdapterData adapterData;

    public interface AdapterData {
        void Data(View v, String str, boolean b);
    }

    public void getAdapterData(AdapterData adapterData) {
        this.adapterData = adapterData;
    }
}
