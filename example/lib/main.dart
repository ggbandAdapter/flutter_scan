
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
  String _content = 'loading...';
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
        body: Center(
            child: Column(
          children: [
            Text('Running on: $_content\n'),
            Expanded(
                child: ScanWidget(
              key: GlobalKey(debugLabel: 'QR'),
              text: 'init ScanView',
              onScanWidgetCreated: _onScanWidgetCreated,
            ))
          ],
        )),
      ),
    );
  }

  ///ScanWidget 构建回调
  ///[controller] ScanWidgetController
  void _onScanWidgetCreated(ScanWidgetController controller) {
    this._scanWidgetController = controller;
    controller.scannedDataStream.listen((scanData) {
      print('scannedDataStream:$scanData');
      setState(() {
        _content = scanData;
      });
    });
  }
}
