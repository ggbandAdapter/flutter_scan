import Flutter
import UIKit


public class SwiftFlutterScanPlugin: NSObject, FlutterPlugin {

  var factory: ScanViewFactory

  public init(with registrar: FlutterPluginRegistrar) {
    self.factory = ScanViewFactory(withRegistrar: registrar)
    registrar.register(factory, withId: "cn.ggband.ruilong/scanView")
  }

  public static func register(with registrar: FlutterPluginRegistrar) {
    registrar.addApplicationDelegate(SwiftFlutterScanPlugin(with: registrar))
  }

  public func applicationDidEnterBackground(_ application: UIApplication) {
  }

  public func applicationWillTerminate(_ application: UIApplication) {
  }

}
