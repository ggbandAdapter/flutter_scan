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
class FlutterScanPlugin : FlutterPlugin, ActivityAware {


    private var mFlutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    private var mActivityPluginBinding: ActivityPluginBinding? = null


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mFlutterPluginBinding = flutterPluginBinding
        doBind()
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        mFlutterPluginBinding = null
    }


    override fun onDetachedFromActivity() {
        mActivityPluginBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(@NonNull binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivityPluginBinding = binding
        doBind()
    }

    override fun onDetachedFromActivityForConfigChanges() {

    }

    private fun doBind() {
        if (mActivityPluginBinding == null || mFlutterPluginBinding == null) return
        mFlutterPluginBinding?.platformViewRegistry?.registerViewFactory("cn.ggband.ruilong/scanView", ScanViewFactory(mActivityPluginBinding!!, mFlutterPluginBinding!!.binaryMessenger))
    }
}
