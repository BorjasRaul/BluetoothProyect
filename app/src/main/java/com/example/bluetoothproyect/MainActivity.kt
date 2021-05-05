package com.example.bluetoothproyect

import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bluetoothproyect.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

val MY_UUID:UUID= UUID.fromString("3e6994d2-91b8-11eb-a8b3-0242ac130003")
class MainActivity : AppCompatActivity() {
    lateinit var firstnameDevice: String
    private val REQUEST_CODE_ENABLE_BT: Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2
    lateinit var bthAdapter: BluetoothAdapter
    lateinit var pairedDevices: Set<BluetoothDevice>
    lateinit var mbinding: ActivityMainBinding
    private val list: ArrayList<BluetoothDevice> = ArrayList()


    var bluetoothHeadset: BluetoothHeadset? = null

    companion object {
        val EXTRA_NAME: String = "Device_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        bthAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bthAdapter == null) {
            mbinding.bthStatus.text = "Bluetooth not available"
        } else {
            mbinding.bthStatus.text = "Bluetooth is available"
        }
        if (bthAdapter.isEnabled) {
            mbinding.bthIV.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            mbinding.bthIV.setImageResource(R.drawable.ic_bluetooth_off)
        }
        firstnameDevice = bthAdapter!!.name
        val nameDev = "BAZ_" + firstnameDevice
        bthAdapter!!.setName(nameDev)
        Log.i("NAMEDEVICE BAZ" , bthAdapter.name)
        mbinding.bthON.setOnClickListener {
            if (bthAdapter.isEnabled) {
                Toast.makeText(this, "already on", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }

        }
        mbinding.bthOFF.setOnClickListener {
            if (!bthAdapter.isEnabled) {
                Toast.makeText(this, " already off", Toast.LENGTH_LONG).show()
            } else {
                bthAdapter.disable()
                mbinding.bthIV.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, " bleutooth turn off", Toast.LENGTH_LONG).show()
            }
        }
        mbinding.bthDiscov.setOnClickListener {

            if (bthAdapter.isDiscovering) {
                Toast.makeText(this, "Makin your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)

            }
            //pairedList()

        }
        mbinding.pairedDev.setOnClickListener {
            /*if (bthAdapter.isEnabled){
                mbinding.pairedList.text= "Paired Device"
                val devices = bthAdapter.bondedDevices
                for (device in devices){
                    val deviceName= device.name
                    val deviceAddress= devices
                    mbinding.pairedList.append("\n Device $deviceName , $device ")
                }
            }else{
                Toast.makeText(this,"Turn on First",Toast.LENGTH_LONG).show()
            }*/
            pairedList()
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)



    }

    private fun pairedList() {
        pairedDevices = bthAdapter!!.bondedDevices

            if (!pairedDevices.isEmpty()) {
                for (device: BluetoothDevice in pairedDevices) {
                    val name = device.name
                     list.add(device)
                    Log.i("Device", "\n" + name + " :: " + device)
                }
            } else {
                Toast.makeText(this, "No paired device found", Toast.LENGTH_LONG).show()
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            mbinding.listDevice.adapter = adapter
            mbinding.listDevice.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list[position]
                val address: String = device.name
                Log.i("NAME", address)

              //  val intent = Intent(this, BluetoothServerController::class.java)
                intent.putExtra(EXTRA_NAME, address)
                startActivity(intent)
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    mbinding.bthIV.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is ON", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "could not available", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    list.add(device!!)
                    Log.i("NEW","//////////////////////")
                    Log.i("Dispositivos",deviceName+"mac: "+deviceHardwareAddress)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bthAdapter.setName(firstnameDevice)
        Log.i("NAME DEVICE set", bthAdapter.name)
        Log.i("NAME DEVICE var",firstnameDevice)
    }
}


