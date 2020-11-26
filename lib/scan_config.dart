class ScanConfig {
  final bool isShowVibrate;
  final bool isShowBeep;

  final bool isFullScreenScan;

  final int scanFrameHeightOffsets;
  final String scanHintText;

  const ScanConfig(
      {this.isShowVibrate = true,
      this.isShowBeep = false,
      this.isFullScreenScan = false,
      this.scanFrameHeightOffsets = 200,
      this.scanHintText = ''});
}
