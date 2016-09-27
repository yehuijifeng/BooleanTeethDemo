package com.booleanteeth.demo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.booleanteeth.demo.contants.BltContant;
import com.booleanteeth.demo.manager.BltManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button searth_switch, searth_my_switch;
    private Switch blue_switch;
    private ListView blue_list;
    private List<BluetoothDevice> bltList;
    private MyAdapter myAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BltManager.getInstance().initBltManager(this);
        initView();
        initData();
    }

    private void initView() {
        searth_switch = (Button) findViewById(R.id.searth_switch);
        searth_my_switch = (Button) findViewById(R.id.searth_my_switch);
        blue_switch = (Switch) findViewById(R.id.blue_switch);
        blue_list = (ListView) findViewById(R.id.blue_list);
        searth_switch.setOnClickListener(this);
        searth_my_switch.setOnClickListener(this);

    }

    private void initData() {
        bltList = new ArrayList<>();
        myAdapter = new MyAdapter();
        blue_list.setOnItemClickListener(this);
        blue_list.setAdapter(myAdapter);
        //检查蓝牙是否开启
        BltManager.getInstance().checkBleDevice(this);
        //注册蓝牙扫描广播
        blueToothRegister();
        //更新蓝牙开关状态
        checkBlueTooth();
        //第一次进来搜索设备
        BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
    }

    /**
     * 注册蓝牙回调广播
     */
    private void blueToothRegister() {
        BltManager.getInstance().registerBltReceiver(this, new BltManager.OnRegisterBltReceiver() {

            /**搜索到新设备
             * @param device
             */
            @Override
            public void onBluetoothDevice(BluetoothDevice device) {
                if (bltList != null && !bltList.contains(device)) {
                    bltList.add(device);
                }
                if (myAdapter != null)
                    myAdapter.notifyDataSetChanged();
            }

            /**连接中
             * @param device
             */
            @Override
            public void onBltIng(BluetoothDevice device) {
                progressDialog.setMessage("连接" + device.getName() + "中……");
                progressDialog.dismiss();
            }

            /**连接完成
             * @param device
             */
            @Override
            public void onBltEnd(BluetoothDevice device) {
                progressDialog.setMessage("连接" + device.getName() + "完成");
                progressDialog.dismiss();
            }

            /**取消链接
             * @param device
             */
            @Override
            public void onBltNone(BluetoothDevice device) {
                progressDialog.setMessage("取消了连接" + device.getName());
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 检查蓝牙的开关状态
     */
    private void checkBlueTooth() {
        if (BltManager.getInstance().getmBluetoothAdapter() == null || !BltManager.getInstance().getmBluetoothAdapter().isEnabled()) {
            blue_switch.setChecked(false);
        } else
            blue_switch.setChecked(true);
        blue_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    //启用蓝牙
                    BltManager.getInstance().clickBlt(MainActivity.this, BltContant.BLUE_TOOTH_OPEN);
                else
                    //禁用蓝牙
                    BltManager.getInstance().clickBlt(MainActivity.this, BltContant.BLUE_TOOTH_CLOSE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searth_switch://搜索设备
                BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
                break;
            case R.id.searth_my_switch://检查蓝牙是否可用，并打开
                //让本机设备能够被其他人搜索到
                BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_CLEAR);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setInverseBackgroundForced(false);
        BluetoothDevice bluetoothDevice = bltList.get(position);
        progressDialog.setMessage("正在连接" + bluetoothDevice.getName());
        progressDialog.show();
        BltManager.getInstance().createBond(bluetoothDevice);
        //progressDialog.dismiss();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bltList.size();
        }

        @Override
        public Object getItem(int position) {
            return bltList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;
            BluetoothDevice device = bltList.get(position);// 从集合中获取当前行的数据
            if (convertView == null) {
                // 说明当前这一行不是重用的
                // 加载行布局文件，产生具体的一行
                v = getLayoutInflater().inflate(R.layout.item_blt, null);
                // 创建存储一行控件的对象
                vh = new ViewHolder();
                // 将该行的控件全部存储到vh中
                vh.blt_name = (TextView) v.findViewById(R.id.blt_name);
                vh.blt_address = (TextView) v.findViewById(R.id.blt_address);
                vh.blt_type = (TextView) v.findViewById(R.id.blt_type);
                vh.blt_bond_state = (TextView) v.findViewById(R.id.blt_bond_state);
                v.setTag(vh);// 将vh存储到行的Tag中
            } else {
                v = convertView;
                // 取出隐藏在行中的Tag--取出隐藏在这一行中的vh控件缓存对象
                vh = (ViewHolder) convertView.getTag();
            }

            // 从ViewHolder缓存的控件中改变控件的值
            // 这里主要是避免多次强制转化目标对象而造成的资源浪费
            vh.blt_name.setText("蓝牙名称：" + device.getName());
            vh.blt_address.setText("蓝牙地址:" + device.getAddress());
            vh.blt_type.setText("蓝牙类型:" + device.getType());
            vh.blt_bond_state.setText("蓝牙状态:" + BltManager.getInstance().bltStatus(device.getBondState()));
            return v;
        }

        private class ViewHolder {
            TextView blt_name, blt_address, blt_type, blt_bond_state;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BltManager.getInstance().unregisterReceiver(this);
    }
}
