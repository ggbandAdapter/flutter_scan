import 'package:flutter/material.dart';
import 'package:flutter_scan/scan_widget.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ScanWidgetController _scanWidgetController;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Stack(
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
                  RaisedButton(
                      child: Text('startScan'),
                      onPressed: () => _scanWidgetController.startScan()),
                ],
              ),
            )
          ],
        ),
      ),
    );
  }

  ///ScanWidget 构建回调
  ///[controller] ScanWidgetController
  void _onScanWidgetCreated(ScanWidgetController controller) {
    this._scanWidgetController = controller;
    controller.scannedDataStream.listen((scanData) {
      print('scannedDataStream:$scanData');
      setState(() {});
    });
  }
}
