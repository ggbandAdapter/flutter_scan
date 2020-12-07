import 'package:flutter/material.dart';
import 'package:flutter_scan/qr_code_scanner.dart';

class ScanPage extends StatefulWidget {
  @override
  _ScanPageState createState() => _ScanPageState();
}

class _ScanPageState extends State<ScanPage> {
  ScanWidgetController qrController;

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
    controller.scannedDataStream.listen((scanData) {
      print('scannedDataStream:$scanData');
      Navigator.pop(context);
    });
  }

  @override
  void dispose() {
    qrController?.dispose();
    super.dispose();
  }
}
