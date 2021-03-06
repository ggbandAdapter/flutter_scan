//
//  ScanViewFactory.swift
//  flutter_qr
//
//  Created by Julius Canute on 21/12/18.
//

//import Foundation
import Flutter
import UIKit

public class ScanViewFactory: NSObject, FlutterPlatformViewFactory {

    var registrar: FlutterPluginRegistrar!;

    @objc public init(registrar: FlutterPluginRegistrar?){
        super.init()
        self.registrar = registrar
    }

    public func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        var dictionary =  args as! Dictionary<String, Double>
        return ScanView(withFrame: CGRect(x: 0, y: 0, width: dictionary["width"] ?? 0, height: dictionary["height"] ?? 0), withRegistrar: registrar!,withId: viewId)
    }

    public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
        return FlutterStandardMessageCodec(readerWriter: FlutterStandardReaderWriter())
    }
}
