#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint flutter_scan.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'flutter_scan'
  s.version          = '0.0.1'
  s.summary          = 'A QR scan Flutter plugin.'
  s.description      = <<-DESC
A QR scan Flutter plugin.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'MTBBarcodeScanner'
  s.platform = :ios, '8.0'
  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end