import 'package:flutter/material.dart';
import 'package:flutter_scan/scan_widget.dart';

class ScanPage extends StatefulWidget {
  @override
  _ScanPageState createState() => _ScanPageState();
}

class _ScanPageState extends State<ScanPage> {
  ScanWidgetController _scanWidgetController;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        ScanWidget(
          key: GlobalKey(debugLabel: 'QR'),
          onScanWidgetCreated: _onScanWidgetCreated,
        ),
        Positioned(
          bottom: 0,
          child: Column(
            children: [
              RaisedButton(
                  child: Text('toggleFlash'),
                  onPressed: () => _scanWidgetController.toggleFlash()),
            ],
          ),
        )
      ],
    );
  }

  ///ScanWidget 构建回调
  ///[controller] ScanWidgetController
  void _onScanWidgetCreated(ScanWidgetController controller) {
    this._scanWidgetController = controller;
    controller.scannedDataStream.listen((scanData) {
      print('scannedDataStream:$scanData');
      Navigator.pop(context);
    });
  }
}
