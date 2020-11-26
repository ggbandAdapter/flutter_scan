import Flutter
import UIKit

public class SwiftFlutterScanPlugin: NSObject, FlutterPlugin {

  public static func register(with registrar: FlutterPluginRegistrar) {
   registrar.register(ScanViewFactory(registrar: registrar), withId: "cn.ggband.ruilong/scanView")
  }

  public func applicationDidEnterBackground(_ application: UIApplication) {
  }

  public func applicationWillTerminate(_ application: UIApplication) {
  }

}
