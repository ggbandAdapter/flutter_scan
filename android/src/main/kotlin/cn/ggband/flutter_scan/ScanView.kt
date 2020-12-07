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
import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import android.util.Log
import io.flutter.FlutterInjector.Builder
import com.google.zxing.client.android.model.MNScanConfig
import com.google.zxing.client.android.other.OnScanCallback
import android.graphics.Bitmap
import android.view.LayoutInflater
import cn.ggband.flutter_scan.R
import android.widget.FrameLayout


/** ScanView */
class ScanView(private val context: Context, private val id: Int, private val params: Map<String, Any>?, val activityPluginBinding: ActivityPluginBinding, private val messenger: BinaryMessenger) : PlatformView, MethodChannel.MethodCallHandler {

    private val channel: MethodChannel = MethodChannel(messenger, "cn.ggband.ruilong/scanView_$id")

    private var mScanView: ScanSurfaceView? = null
    //手电筒是否开启
    private var isLightOn = false

    private val mActivity: Activity = activityPluginBinding.getActivity()

    companion object {
        const val CAMERA_REQUEST_ID = 513469796
    }


    init {
        channel.setMethodCallHandler(this)
        activityPluginBinding.addRequestPermissionsResultListener(CameraRequestPermissionsListener())
        registerActivityLifecycleCallbacks()
    }


    override fun getView(): View {
        return buildContentView()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        //val arg = call.arguments as Map<String, Any>
        when (call.method) {
            "toggleFlash" -> {
                toggleFlash()
                result.success(isLightOn)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun buildContentView(): View {
        if (mScanView == null) {
            mScanView = ScanSurfaceView(context)
            mScanView?.init(mActivity)
            configScanView()
            checkPermissionScan()
        }
        return mScanView!!
    }

    private fun configScanView() {
        val scanConfig: MNScanConfig = MNScanConfig.Builder()
                .setSupportZoom(false)
                .isShowVibrate(params?.get("isShowVibrate") as Boolean ?: true)
                .isShowBeep(params?.get("isShowBeep") as Boolean ?: false)
                .setFullScreenScan(params?.get("isFullScreenScan") as Boolean ?: false)
                .setScanFrameHeightOffsets(params?.get("scanFrameHeightOffsets") as Int ?: 0)
                .setScanHintText(params?.get("scanHintText")?.toString() ?: "")
                .setLaserStyle(MNScanConfig.LaserStyle.Grid)
                //    .isShowLightController(true)
                .builder()
        mScanView?.setScanConfig(scanConfig)
        mScanView?.setOnScanCallback(object : OnScanCallback {
            override fun onScanSuccess(resultTxt: String, barcode: Bitmap?) {
                channel.invokeMethod("onRecognizeQR", resultTxt)
            }

            override fun onFail(msg: String) {
                Log.d("ggband", "onFail:" + msg)
            }
        })
    }

    private fun checkPermissionScan() {
        if (hasCameraPermission()) {
            Log.d("ggband", "=========hasCameraPermission============")
            mScanView?.onResume()
        } else {
            checkAndRequestPermission()
        }
    }

    /**
     * 切换手电筒
     */
    private fun toggleFlash() {
        Log.d("ggband", "toggleFlash:" + isLightOn + "-----------" + Thread.currentThread().getName())
        if (isLightOn) {
            mScanView?.getCameraManager()?.offLight()
        } else {
            mScanView?.getCameraManager()?.openLight()
        }
        isLightOn = !isLightOn
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
            Log.d("ggband", "=========onRequestPermissionsResult============")
            if (grantResults.size > 0 && id == CAMERA_REQUEST_ID && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ggband", "=========onRequestPermissionsResult=======true=====")
                return true
            }
            Log.d("ggband", "=========onRequestPermissionsResult=======false=====")
            return false
        }
    }

    private fun registerActivityLifecycleCallbacks() {
        mActivity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity?) {
                targetActivityRun(p0) {
                    Log.d("ggband", "=========onActivityPaused======")
                    mScanView?.onPause()
                }
            }

            override fun onActivityResumed(p0: Activity?) {
                targetActivityRun(p0) {
                    Log.d("ggband", "=========onActivityResumed======")
                    mScanView?.onResume()
                }
            }

            override fun onActivityStarted(p0: Activity?) {
                targetActivityRun(p0) {}
            }

            override fun onActivityDestroyed(p0: Activity?) {
                targetActivityRun(p0) {
                    mScanView?.onDestroy()
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityStopped(p0: Activity?) {

                targetActivityRun(p0) {}
            }

            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {

            }
        })
    }

    private fun targetActivityRun(targetActivity: Activity?, action: () -> Unit) {
        if (mActivity == targetActivity) {
            Log.d("ggband", "targetActivityRun currentThreadName:" + Thread.currentThread().getName())
            action()
        }
    }

    override fun dispose() {
        channel.setMethodCallHandler(null)
        mScanView?.onDestroy()
    }
}
