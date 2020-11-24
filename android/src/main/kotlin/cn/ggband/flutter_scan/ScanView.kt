package cn.ggband.flutter_scan

import android.content.Context
import android.os.Build
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import android.widget.TextView;

/** ScanView */
class ScanView(private val context: Context, private val id: Int, private val params: Map<String, Any>, val messenger: BinaryMessenger? = null, private val containerView: View?) : PlatformView, MethodChannel.MethodCallHandler {
    private val channel: MethodChannel = MethodChannel(messenger, "cn.ggband/scanView_$id")

    private lateinit var scanView: TextView

    init {
        scanView = TextView(context)
        scanView.text = params["text"].toString()
        channel.setMethodCallHandler(this)
    }


    override fun getView(): View {
        scanView.postDelayed({
            channel.invokeMethod("onRecognizeQR", "f8y478hefbdufbergerg")
        },5000)
        return scanView
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "setText" -> {
                val arg = call.arguments as Map<String, Any>
                val text = arg["text"].toString()
                scanView.text = text
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun dispose() {
        channel.setMethodCallHandler(null)
    }
}
