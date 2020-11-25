package cn.ggband.flutter_scan

import android.os.Build
import android.view.View
import android.app.Activity
import android.content.Context
import android.app.Application
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import com.google.zxing.client.android.view.ScanSurfaceView
import android.view.LayoutInflater
import cn.ggband.flutter_scan.R
import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import android.util.Log
import android.widget.LinearLayout
import io.flutter.FlutterInjector.Builder
import  com.google.zxing.client.android.model.MNScanConfig
import com.google.zxing.client.android.other.OnScanCallback
import android.graphics.Bitmap

/** ScanView */
class ScanView(private val context: Context, private val id: Int, private val params: Map<String, Any>?, val activityPluginBinding: ActivityPluginBinding, private val messenger: BinaryMessenger, private val containerView: View?) : PlatformView, MethodChannel.MethodCallHandler {

    private val channel: MethodChannel = MethodChannel(messenger, "cn.ggband/scanView_$id")

    private lateinit var mScanView: ScanSurfaceView
    private var mToggleFlash = false

    private val mActivity: Activity = activityPluginBinding.getActivity()

    companion object {
        const val CAMERA_REQUEST_ID = 513469796
    }


    init {
        channel.setMethodCallHandler(this)
        activityPluginBinding.addRequestPermissionsResultListener(CameraRequestPermissionsListener())
        checkAndRequestPermission()
        registerActivityLifecycleCallbacks()
    }


    override fun getView(): View {
        return buildContentView()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "toggleFlash" -> {
                //val arg = call.arguments as Map<String, Any>
                //  val text = arg["text"].toString()
                //  scanView.text = text
                Log.d("ggband", "toggleFlash:" + mToggleFlash);
                if (mToggleFlash) {

                } else {

                }
                mToggleFlash = !mToggleFlash
                result.success(null)
            }
            "startScan" -> {
                //val arg = call.arguments as Map<String, Any>
                //  val text = arg["text"].toString()
                //  scanView.text = text
                Log.d("ggband", "start");
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun buildContentView(): View {
        mScanView = ScanSurfaceView(context)
        mScanView.init(mActivity)
        configScanView()
        mScanView.onResume()
        //  scanView.text = params["text"].toString()
        //  channel.invokeMethod("onRecognizeQR", "f8y478hefbdufbergerg")
        return mScanView
    }

    private fun configScanView() {
        val scanConfig: MNScanConfig = MNScanConfig.Builder()
                .setSupportZoom(false)
                .builder()
        mScanView.setScanConfig(scanConfig)
        mScanView.setOnScanCallback(object : OnScanCallback {
            override fun onScanSuccess(resultTxt: String, barcode: Bitmap?) {

            }

            override fun onFail(msg: String) {

            }
        })

    }

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_ID)
        }
    }


    private fun hasCameraPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                mActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private inner class CameraRequestPermissionsListener : PluginRegistry.RequestPermissionsResultListener {
        override fun onRequestPermissionsResult(id: Int, permissions: Array<String>, grantResults: IntArray): Boolean {

            if (permissions.size > 0 && id == CAMERA_REQUEST_ID && grantResults[0] == PERMISSION_GRANTED) {
                return true
            }
            return false
        }
    }


    private fun registerActivityLifecycleCallbacks() {
        mActivity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity?) {

                if (p0 == mActivity) {

                }
            }

            override fun onActivityResumed(p0: Activity?) {


                if (p0 == mActivity) {

                }
            }

            override fun onActivityStarted(p0: Activity?) {


            }

            override fun onActivityDestroyed(p0: Activity?) {

            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityStopped(p0: Activity?) {

                if (p0 == mActivity) {

                }
            }

            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
            }
        })
    }

    override fun dispose() {
        channel.setMethodCallHandler(null)
        mScanView.onDestroy()
    }
}


//package cn.ggband.flutter_scan
//
//import android.os.Build
//import android.view.View
//import android.app.Activity
//import android.content.Context
//import android.app.Application
//import io.flutter.plugin.common.BinaryMessenger
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//import io.flutter.plugin.platform.PlatformView
//import  cn.bingoogolapple.qrcode.zxing.ZXingView
//import cn.bingoogolapple.qrcode.core.QRCodeView;
//import android.view.LayoutInflater
//import cn.ggband.flutter_scan.R
//import android.Manifest
//import android.content.pm.PackageManager
//import android.content.pm.PackageManager.PERMISSION_GRANTED
//import android.os.Bundle
//import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
//import io.flutter.plugin.common.PluginRegistry
//import android.util.Log
//import android.widget.LinearLayout
//
//
///** ScanView */
//class ScanView(private val context: Context, private val id: Int, private val params: Map<String, Any>?, val activityPluginBinding: ActivityPluginBinding, private val messenger: BinaryMessenger, private val containerView: View?) : PlatformView, MethodChannel.MethodCallHandler {
//
//    private val channel: MethodChannel = MethodChannel(messenger, "cn.ggband/scanView_$id")
//
//    private lateinit var mScanView: ZXingView
//    private var mToggleFlash = false
//
//    private val mActivity: Activity = activityPluginBinding.getActivity()
//
//    companion object {
//        const val CAMERA_REQUEST_ID = 513469796
//    }
//
//
//    init {
//        channel.setMethodCallHandler(this)
//        activityPluginBinding.addRequestPermissionsResultListener(CameraRequestPermissionsListener())
//        checkAndRequestPermission()
//        registerActivityLifecycleCallbacks()
//    }
//
//
//    override fun getView(): View {
//        return buildContentView()
//    }
//
//    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
//        when (call.method) {
//            "toggleFlash" -> {
//                //val arg = call.arguments as Map<String, Any>
//                //  val text = arg["text"].toString()
//                //  scanView.text = text
//                Log.d("ggband", "toggleFlash:" + mToggleFlash);
//                if (mToggleFlash) {
//                    mScanView.stopSpot()
//                    mScanView.stopCamera()
//                    mScanView.closeFlashlight()
//                } else {
//                    mScanView.startSpot()
//                    mScanView.openFlashlight()
//                }
//                mToggleFlash = !mToggleFlash
//                result.success(null)
//            }
//            "startScan" -> {
//                //val arg = call.arguments as Map<String, Any>
//                //  val text = arg["text"].toString()
//                //  scanView.text = text
//                Log.d("ggband", "start");
//                mScanView.startSpot()
//                result.success(null)
//            }
//            else -> {
//                result.notImplemented()
//            }
//        }
//    }
//
//    private fun buildContentView(): View {
//        val contentView = LayoutInflater.from(context).inflate(R.layout.view_scan, null)
//        mScanView = contentView.findViewById<ZXingView>(R.id.scanView)
//        configScanView()
//        //  scanView.text = params["text"].toString()
//        //  channel.invokeMethod("onRecognizeQR", "f8y478hefbdufbergerg")
//        return contentView
//    }
//
//    private fun configScanView() {
//        mScanView.setDelegate(Delegate())
//        mScanView.postDelayed({
//            Log.d("ggband", "startSpot");
//            mScanView.startSpot()
//        }, 500)
//
//    }
//
//    private fun checkAndRequestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mActivity.requestPermissions(
//                    arrayOf(Manifest.permission.CAMERA),
//                    CAMERA_REQUEST_ID)
//        }
//    }
//
//
//    private fun hasCameraPermission(): Boolean {
//        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
//                mActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private inner class CameraRequestPermissionsListener : PluginRegistry.RequestPermissionsResultListener {
//        override fun onRequestPermissionsResult(id: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
//
//            if (permissions.size > 0 && id == CAMERA_REQUEST_ID && grantResults[0] == PERMISSION_GRANTED) {
//                return true
//            }
//            return false
//        }
//    }
//
//    private inner class Delegate : QRCodeView.Delegate {
//
//        override fun onScanQRCodeSuccess(result: String) {
//            Log.d("ggband", "result:" + result);
//        }
//
//
//        override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
//
//        }
//
//        override fun onScanQRCodeOpenCameraError() {
//            Log.d("ggband", "打开相机出错");
//        }
//    }
//
//
//    private fun registerActivityLifecycleCallbacks() {
//        mActivity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
//            override fun onActivityPaused(p0: Activity?) {
//
//                if (p0 == mActivity) {
//
//                }
//            }
//
//            override fun onActivityResumed(p0: Activity?) {
//
//
//                if (p0 == mActivity) {
//
//                }
//            }
//
//            override fun onActivityStarted(p0: Activity?) {
//
//
//            }
//
//            override fun onActivityDestroyed(p0: Activity?) {
//
//            }
//
//            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
//            }
//
//            override fun onActivityStopped(p0: Activity?) {
//
//                if (p0 == mActivity) {
//
//                }
//            }
//
//            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
//            }
//        })
//    }
//
//    override fun dispose() {
//        channel.setMethodCallHandler(null)
//        mScanView.onDestroy()
//    }
//}
