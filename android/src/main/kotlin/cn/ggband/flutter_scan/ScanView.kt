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
import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import android.util.Log
import io.flutter.FlutterInjector.Builder

import com.google.zxing.ResultPoint
import android.hardware.Camera.CameraInfo
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView


/** ScanView */
class ScanView(private val context: Context, private val id: Int, private val params: Map<String, Any>?, val activityPluginBinding: ActivityPluginBinding, private val messenger: BinaryMessenger) : PlatformView, MethodChannel.MethodCallHandler {

    private val channel: MethodChannel = MethodChannel(messenger, "cn.ggband.ruilong/scanView_$id")

    private var mScanView: BarcodeView? = null
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
            "flipCamera" -> {
                flipCamera()
                result.success(null)
            }
            "pauseCamera" -> {
                pauseCamera()
                result.success(null)
            }
            "resumeCamera" -> {
                resumeCamera()
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun buildContentView(): View {
        if (mScanView == null) {
            mScanView = BarcodeView(context)
            configScanView()
            checkPermissionScan()
        }
        return mScanView!!
    }

    private fun configScanView() {
        var settings = mScanView?.cameraSettings
        settings?.requestedCameraId = CameraInfo.CAMERA_FACING_BACK
        mScanView?.cameraSettings = settings
        mScanView?.decodeContinuous(
                object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult) {
                        channel.invokeMethod("onRecognizeQR", result.text)
                    }

                    override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
                }
        )
    }

    private fun checkPermissionScan() {
        if (hasCameraPermission()) {
            mScanView?.resume()
        } else {
            checkAndRequestPermission()
        }
    }

    /**
     * 切换手电筒
     */
    private fun toggleFlash() {
        mScanView?.setTorch(!isLightOn)
        isLightOn = !isLightOn
    }

    fun flipCamera() {
        mScanView?.pause()
        var settings = mScanView?.cameraSettings
        if (settings?.requestedCameraId == CameraInfo.CAMERA_FACING_FRONT)
            settings?.requestedCameraId = CameraInfo.CAMERA_FACING_BACK
        else
            settings?.requestedCameraId = CameraInfo.CAMERA_FACING_FRONT

        mScanView?.cameraSettings = settings
        mScanView?.resume()
    }

    private fun pauseCamera() {
        if (mScanView!!.isPreviewActive) {
            mScanView?.pause()
        }
    }

    private fun resumeCamera() {
        if (!mScanView!!.isPreviewActive) {
            mScanView?.resume()
        }
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
            if (grantResults.size > 0 && id == CAMERA_REQUEST_ID && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }
    }

    private fun registerActivityLifecycleCallbacks() {
        mActivity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity?) {
                targetActivityRun(p0) {
                    mScanView?.pause()
                }
            }

            override fun onActivityResumed(p0: Activity?) {
                targetActivityRun(p0) {
                    mScanView?.resume()
                }
            }

            override fun onActivityStarted(p0: Activity?) {
                targetActivityRun(p0) {}
            }

            override fun onActivityDestroyed(p0: Activity?) {
                targetActivityRun(p0) {
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
            action()
        }
    }


    override fun dispose() {
        channel.setMethodCallHandler(null)
        mScanView?.pause()
    }
}
