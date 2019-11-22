package com.example.hp.blutooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ConnectivityManager connectivityManager;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        listView=findViewById(R.id.list_item);
        connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        imageView=findViewById(R.id.iv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter statefilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(statereciver, statefilter);

        IntentFilter foundfilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(foundReciver,foundfilter);
    }

    BroadcastReceiver statereciver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Toast.makeText(context,"inside changed receiver",Toast.LENGTH_LONG).show();
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
              switch (state){
                  case BluetoothAdapter.STATE_ON:
                      Toast.makeText(MainActivity.this,"Blutooth on",Toast.LENGTH_SHORT).show();
                      break;
                  case BluetoothAdapter.STATE_TURNING_ON:
                      Toast.makeText(MainActivity.this,"Blutooth turning on",Toast.LENGTH_SHORT).show();
                      break;
                  case BluetoothAdapter.STATE_OFF:
                      Toast.makeText(MainActivity.this,"Blutooth off",Toast.LENGTH_SHORT).show();
                      break;
                  case BluetoothAdapter.STATE_TURNING_OFF:
                      Toast.makeText(MainActivity.this,"Blutooth Turnning off",Toast.LENGTH_SHORT).show();
                      break;
              }
            }
        }
    };

    BroadcastReceiver foundReciver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName=device.getName();
                String deviceHardwareAddress=device.getAddress();
                Toast.makeText(MainActivity.this,deviceName,Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void enableBluetooth(View view) {
        if(bluetoothAdapter==null){
            Toast.makeText(this,"Device not supported bluetooth",Toast.LENGTH_LONG).show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i,1);
            }
            if(bluetoothAdapter.isEnabled()){
                Toast.makeText(this,"Blutooth enabled",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Blutooth turned on", Toast.LENGTH_LONG).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth turned on Failed", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==3){
            if(requestCode !=RESULT_CANCELED){
                Toast.makeText(this,"Device Discoverability start",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Device Discoverability distroid",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getPairedDevices(View view) {
        if(bluetoothAdapter!=null) {
            Set<BluetoothDevice> pairedDevice=bluetoothAdapter.getBondedDevices();
            if(pairedDevice.size()>0){
                for(BluetoothDevice device: pairedDevice){
                    String devicename=device.getName();
                    String deviceHardwareAddress=device.getAddress();
                    Toast.makeText(this,"Paired Device are"+devicename,Toast.LENGTH_SHORT).show();
                }
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
            listView.setAdapter(arrayAdapter);
        }
    }

    public void finddevice(View view) {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

       if(bluetoothAdapter!=null){
           bluetoothAdapter.startDiscovery();
           Toast.makeText(this,"Start Discovery"+bluetoothAdapter.startDiscovery(),Toast.LENGTH_SHORT).show();
       }
    }

    public void discoverable(View view) {
        if(bluetoothAdapter.isEnabled()){
            Intent discovertableintent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discovertableintent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            startActivityForResult(discovertableintent,3);
        }
    }

    public void dothis(View view) {

        String imagepath="http://placeimg.com/640/360";
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected()){
            if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this,"Wife",Toast.LENGTH_SHORT).show();

            }
            if(networkInfo.getType()==connectivityManager.TYPE_MOBILE){
                Toast.makeText(this,"Mobile",Toast.LENGTH_SHORT).show();

            }
        }
    }

}
