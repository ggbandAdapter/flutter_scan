import 'package:flutter/material.dart';
import 'package:flutter_scan/qr_code_scanner.dart';

class ScanPage extends StatefulWidget {
  @override
  _ScanPageState createState() => _ScanPageState();
}

class _ScanPageState extends State<ScanPage> {
  ScanWidgetController qrController;

  bool isFlash = false;

  int frameMillisecondsSinceEpoch = 0;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        child: Stack(
      children: [
        ScanWidget(
          key: GlobalKey(debugLabel: 'qr_scan'),
          onScanWidgetCreated: _onScanWidgetCreated,
          overlay: QrScannerOverlayShape(
            borderColor: Colors.white,
            borderRadius: 2,
            borderLength: 12,
            borderWidth: 2,
            cutOutSize: 200,
          ),
        ),
        Positioned(
          bottom: 0,
          child: Column(
            children: [
              RaisedButton(
                  child: Text('toggleFlash'),
                  onPressed: () => qrController.toggleFlash()),
            ],
          ),
        )
      ],
    ));
  }

  ///ScanWidget 构建回调
  ///[controller] ScanWidgetController
  void _onScanWidgetCreated(ScanWidgetController controller) {
    this.qrController = controller;
    frameMillisecondsSinceEpoch = DateTime.now().millisecondsSinceEpoch;
    controller.scannedDataStream.listen((scanData) {
      int scanMillisecondsSinceEpoch = DateTime.now().millisecondsSinceEpoch;
      int scanTime = scanMillisecondsSinceEpoch - frameMillisecondsSinceEpoch;
      _closeFlash();
      qrController?.pauseCamera();
      qrController?.dispose();
      print('scannedDataStream:$scanData');
      Navigator.pop(context, scanTime);
    });
  }

  ///切换手电筒状态
  _toggleFlash() {
    qrController?.toggleFlash();
    isFlash = !isFlash;
  }

  ///关闭手电筒
  _closeFlash() {
    if (isFlash) {
      _toggleFlash();
    }
  }

  @override
  void dispose() {
    qrController?.dispose();
    super.dispose();
  }
}
