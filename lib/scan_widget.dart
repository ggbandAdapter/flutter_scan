import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef ScanWidgetCreatedCallback = void Function(ScanWidgetController);

class ScanWidget extends StatefulWidget {
  final String text;
  final ScanWidgetCreatedCallback onScanWidgetCreated;

  const ScanWidget({
    @required Key key,
    this.text,
    @required this.onScanWidgetCreated,
  })  : assert(key != null),
        assert(onScanWidgetCreated != null),
        super(key: key);

  @override
  State<StatefulWidget> createState() => _ScanWidgetState();
}

class _ScanWidgetState extends State<ScanWidget> {
  @override
  Widget build(BuildContext context) {
    return _buildPlatformWidget();
  }

  Widget _buildPlatformWidget() {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'cn.ggband/scanView',
        creationParamsCodec: const StandardMessageCodec(),
        creationParams: {
          'text': widget.text ?? '',
        },
        onPlatformViewCreated: _onPlatformViewCreated,
        layoutDirection: TextDirection.rtl,
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return Text('ios Not provided');
    } else {
      return Container();
    }
  }

  void _onPlatformViewCreated(int id) {
    if (widget.onScanWidgetCreated == null) {
      return;
    }
    widget.onScanWidgetCreated(ScanWidgetController._(id, widget.key));
  }
}

class ScanWidgetController {
  //扫描结果回调
  static const scanMethodCall = 'onRecognizeQR';

  ScanWidgetController._(int id, GlobalKey qrKey)
      : _channel = MethodChannel('cn.ggband/scanView_$id') {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      final RenderBox renderBox = qrKey.currentContext.findRenderObject();
      _channel.invokeMethod('setDimensions',
          {'width': renderBox.size.width, 'height': renderBox.size.height});
    }
    _channel.setMethodCallHandler(
      (call) async {
        switch (call.method) {
          case scanMethodCall:
            if (call.arguments != null) {
              _scanUpdateController.sink.add(call.arguments.toString());
            }
        }
      },
    );
  }

  final MethodChannel _channel;

  final StreamController<String> _scanUpdateController =
      StreamController<String>();

  Stream<String> get scannedDataStream => _scanUpdateController.stream;

  void flipCamera() {
    _channel.invokeMethod('flipCamera');
  }

  void toggleFlash() {
    _channel.invokeMethod('toggleFlash');
  }

  void pauseCamera() {
    _channel.invokeMethod('pauseCamera');
  }

  void resumeCamera() {
    _channel.invokeMethod('resumeCamera');
  }

  void dispose() {
    _scanUpdateController.close();
  }
}
