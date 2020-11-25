package cn.ggband.flutter_scan


import android.app.Activity
import android.content.Context
import android.view.View
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger


/** ScanViewFactory */
class ScanViewFactory(private val activityPluginBinding: ActivityPluginBinding, private val messenger: BinaryMessenger, val containerView: View?) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, id: Int, args: Any?): PlatformView {
        return ScanView(context,id, args as Map<String, Any>?, activityPluginBinding, messenger, containerView)
    }
}
