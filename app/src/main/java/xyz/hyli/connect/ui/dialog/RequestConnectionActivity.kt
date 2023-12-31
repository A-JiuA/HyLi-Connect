package xyz.hyli.connect.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import com.alibaba.fastjson2.JSONObject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import xyz.hyli.connect.R
import xyz.hyli.connect.socket.utils.SocketUtils
import xyz.hyli.connect.ui.theme.HyliConnectTheme

class RequestConnectionActivity : ComponentActivity() {
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        val intent = intent
        val ip = intent.getStringExtra("ip")
        val nickname = intent.getStringExtra("nickname")
        val uuid = intent.getStringExtra("uuid")
        val api_version = intent.getIntExtra("api_version", 0)
        val app_version = intent.getIntExtra("app_version", 0)
        val app_version_name = intent.getStringExtra("app_version_name")
        val platform = intent.getStringExtra("platform")

        setContent {
            HyliConnectTheme {
                if ((ip.isNullOrEmpty().not() && nickname.isNullOrEmpty().not() && uuid.isNullOrEmpty().not() && api_version != 0 && app_version != 0 && app_version_name.isNullOrEmpty().not() && platform.isNullOrEmpty().not())
                    && Settings.canDrawOverlays(this)) {
                    val IP_Address = ip!!.substring(1, ip.length)
                    val data = JSONObject(
                        mapOf(
                            "api_version" to api_version,
                            "app_version" to app_version,
                            "app_version_name" to app_version_name,
                            "platform" to platform,
                            "nickname" to nickname,
                            "uuid" to uuid
                        )
                    )
                    dialog = MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.dialog_connect_request_title))
                        .setMessage("${getString(R.string.dialog_connect_request_message, nickname, IP_Address)}\n\nUUID: $uuid")
                        .setPositiveButton(getString(R.string.dialog_connect_request_accept)) { dialog, which ->
                            SocketUtils.acceptConnection(ip, data)
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.dialog_connect_request_reject_countdown, 30000 / 1000)) { dialog, which ->
                            SocketUtils.rejectConnection(ip)
                            dialog.dismiss()
                        }
                        .setCancelable(true)
                        .create()
                    val timer = object : CountDownTimer(30000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).text = getString(R.string.dialog_connect_request_reject_countdown, millisUntilFinished / 1000)
                        }
                        override fun onFinish() {
                            dialog.dismiss()
                        }
                    }
                    dialog.setOnDismissListener {
                        timer.cancel()
                    }
                    dialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                    dialog.window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
                    dialog.window?.attributes?.width = WindowManager.LayoutParams.WRAP_CONTENT
                    dialog.window?.attributes?.gravity = Gravity.CENTER
                    dialog.show()
                    timer.start()
                }
                finish()
            }
        }
    }
}