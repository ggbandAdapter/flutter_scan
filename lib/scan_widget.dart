import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef ScanWidgetCreatedCallback = void Function(ScanWidgetController);

class ScanWidget extends StatefulWidget {
  final ScanWidgetCreatedCallback onScanWidgetCreated;
  final ShapeBorder overlay;

  const ScanWidget({
    @required Key key,
    @required this.onScanWidgetCreated,
    this.overlay,
  })  : assert(key != null),
        assert(onScanWidgetCreated != null),
        super(key: key);

  @override
  State<StatefulWidget> createState() => _ScanWidgetState();
}

class _ScanWidgetState extends State<ScanWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.black45,
      child: Stack(
        children: [
          _buildPlatformWidget(),
          if (widget.overlay != null)
            Container(
              decoration: ShapeDecoration(
                shape: widget.overlay,
              ),
            )
          else
            Container(),
        ],
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    // SystemChrome.setEnabledSystemUIOverlays([SystemUiOverlay.bottom]);
  }

  Widget _buildPlatformWidget() {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'cn.ggband.ruilong/scanView',
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'cn.ggband.ruilong/scanView',
        onPlatformViewCreated: _onPlatformViewCreated,
        creationParams: {
          'width': 0,
          'height': 0,
        },
        creationParamsCodec: StandardMessageCodec(),
      );
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
  static const scanError = 'onRequestPermission';
  final MethodChannel _channel;

  ScanWidgetController._(int id, GlobalKey qrKey)
      : _channel = MethodChannel('cn.ggband.ruilong/scanView_$id') {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      Future.delayed(Duration(milliseconds: 200)).then((value) {
        final RenderBox renderBox = qrKey.currentContext.findRenderObject();
        _channel.invokeMethod('setDimensions',
            {'width': renderBox.size.width, 'height': renderBox.size.height});
      });
    }
    _channel.setMethodCallHandler(
      (call) async {
        switch (call.method) {
          case scanMethodCall:
            if (call.arguments != null) {
              _scanUpdateController.sink.add(call.arguments.toString());
            }
            break;
          case scanError:
            break;
        }
      },
    );
  }

  final StreamController<String> _scanUpdateController =
      StreamController<String>();

  Stream<String> get scannedDataStream => _scanUpdateController.stream;

  void toggleFlash() {
    _channel.invokeMethod('toggleFlash');
  }

  void pauseCamera() {
    _channel.invokeMethod('pauseCamera');
  }

  void resumeCamera() {
    _channel.invokeMethod('resumeCamera');
  }

  void startScan() {
    _channel.invokeMethod('startScan');
  }

  void dispose() {
    _scanUpdateController.close();
  }
}
