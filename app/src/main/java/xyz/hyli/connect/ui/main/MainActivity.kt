package xyz.hyli.connect.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import xyz.hyli.connect.R
import xyz.hyli.connect.client.HttpClient
import xyz.hyli.connect.service.HttpServerService
import xyz.hyli.connect.utils.PackageUtils
import java.util.concurrent.CompletableFuture


class MainActivity : ComponentActivity() {
    private val SHIZUKU_CODE = 0xCA07A
    private var shizukuPermissionFuture = CompletableFuture<Boolean>()
    private var appList: Deferred<List<String>>? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var UUID: Deferred<String>? = null
    var IP_ADDRESS: Deferred<String>? = null
    var NICKNAME: Deferred<String>? = null
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        startService(Intent(this, HttpServerService::class.java))
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        Log.i("MainActivity", "$sharedPreferences")

        val applist_button = findViewById<Button>(R.id.applist_button)
        val textView = findViewById<TextView>(R.id.info_textview)
        val editText = findViewById<TextView>(R.id.edittext)
        val connectButton = findViewById<Button>(R.id.connect_button)
        val linearLayout = findViewById<LinearLayout>(R.id.linearlayout_main)
        connectButton.setOnClickListener{
            val ip = editText.text.toString()
            var response: String
            GlobalScope.launch(Dispatchers.IO) {
                response = HttpClient.get_info(ip)
                runOnUiThread{ linearLayout.addView(TextView(this@MainActivity).apply { text = response }) }
                response = HttpClient.connect(ip)
                runOnUiThread{ linearLayout.addView(TextView(this@MainActivity).apply { text = response }) }
                response = HttpClient.get_clients(ip)
                runOnUiThread{ linearLayout.addView(TextView(this@MainActivity).apply { text = response }) }
//                response = HttpClient.get_apps(ip)
//                runOnUiThread{ linearLayout.addView(TextView(this@MainActivity).apply { text = response }) }
                response = HttpClient.disconnect(ip)
                runOnUiThread{ linearLayout.addView(TextView(this@MainActivity).apply { text = response }) }
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            appList = async { PackageUtils.GetAppList(packageManager) }
            UUID = async { ConfigHelper().getUUID(sharedPreferences, editor) }
            IP_ADDRESS = async { ConfigHelper().getIPAddress(this@MainActivity) }
            NICKNAME = async { ConfigHelper().getNickname(sharedPreferences, editor) }
        }
        GlobalScope.launch(Dispatchers.Main) {
            textView.text = "UUID: ${UUID?.await()}\nIP_ADDRESS: ${IP_ADDRESS?.await()}\nNICKNAME: ${NICKNAME?.await()}"
        }
        applist_button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val intent = Intent(this@MainActivity, AppListActivity::class.java)
                intent.putExtra("appList", appList?.await()?.toTypedArray())
                startActivity(intent)
            }
        }

        Shizuku.addRequestPermissionResultListener { requestCode, grantResult ->
            if (requestCode == SHIZUKU_CODE) {
                val granted = grantResult == PackageManager.PERMISSION_GRANTED
                shizukuPermissionFuture.complete(granted)
            }
        }
    }

    private fun checkShizukuPermission(): Boolean {
        val b = if (!Shizuku.pingBinder()) {
            Toast.makeText(this, "Shizuku is not available", Toast.LENGTH_LONG).show()
            false
        } else if (Shizuku.isPreV11()) {
            Toast.makeText(this, "Shizuku < 11 is not supported!", Toast.LENGTH_LONG).show()
            false
        } else if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            true
        } else if (Shizuku.shouldShowRequestPermissionRationale()) {
            Toast.makeText(
                this,
                "You denied the permission for Shizuku. Please enable it in app.",
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            Shizuku.requestPermission(SHIZUKU_CODE)

            val result = shizukuPermissionFuture.get()
            shizukuPermissionFuture = CompletableFuture<Boolean>()

            result
        }

        return b
    }
}
