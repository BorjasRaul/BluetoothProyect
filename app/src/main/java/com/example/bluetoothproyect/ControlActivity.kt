package com.example.bluetoothproyect

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.icu.lang.UProperty.NAME
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothproyect.databinding.ControlLayoutBinding
import java.io.IOException
import java.util.*

class ControlActivity: AppCompatActivity() {
    lateinit var mBinding: ControlLayoutBinding
    lateinit var bthAdapter: BluetoothAdapter

    companion object {
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bthSocket: BluetoothSocket? = null
        lateinit var bthProgress: ProgressDialog
        //lateinit var bthAdapter: BluetoothAdapter
        var ssConected: Boolean = false
        lateinit var name: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ControlLayoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        bthAdapter= BluetoothAdapter.getDefaultAdapter()
        name = intent.getStringExtra(MainActivity.EXTRA_NAME).toString()




    }
}


