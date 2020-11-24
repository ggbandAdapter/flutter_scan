package cn.ggband.flutter_scan


import android.app.Activity
import android.content.Context
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

/** ScanViewFactory */
class ScanViewFactory(private val msg: BinaryMessenger, private val activity: Activity, private val containerView: View?) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, id: Int, args: Any?): PlatformView {
        return ScanView(activity, id, args as Map<String, Any>, msg, containerView)
    }
}
