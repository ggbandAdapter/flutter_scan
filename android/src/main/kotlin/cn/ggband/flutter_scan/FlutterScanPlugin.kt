package cn.ggband.flutter_scan

import androidx.annotation.NonNull
import android.app.Activity
import android.content.Context

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import android.util.Log

/** FlutterScanPlugin */
class FlutterScanPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null

    private var mFlutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    private var mActivityPluginBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mFlutterPluginBinding = flutterPluginBinding
        doBind()
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
        mFlutterPluginBinding = null
    }


    override fun onDetachedFromActivity() {
        Log.e("onDetachedFromActivity", "onDetachedFromActivity")
        mActivityPluginBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(@NonNull binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.e("onAttachedToActivity", "onAttachedToActivity")
        mActivityPluginBinding = binding
        doBind()
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }


    private fun doBind() {
        if (mActivityPluginBinding == null || mFlutterPluginBinding == null) return
        channel = MethodChannel(mFlutterPluginBinding?.binaryMessenger, "flutter_scan")
        channel?.setMethodCallHandler(this)
        mFlutterPluginBinding?.platformViewRegistry?.registerViewFactory("cn.ggband/scanView", ScanViewFactory(mActivityPluginBinding!!, mFlutterPluginBinding!!.binaryMessenger, null))
    }
}
