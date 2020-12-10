import 'package:flutter/material.dart';
import 'package:flutter_scan_example/scan_page.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: HomePage()),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  List<int> timeList = [];

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          RaisedButton(
            onPressed: () => _navScanPage(),
            child: Text('GO SCAN'),
          ),
          Expanded(
              child: ListView.builder(
            itemBuilder: // 有分割线
                (context, index) {
              return new Container(
                child: ListTile(title: Text('$index 耗时ms:${timeList[index]}')),
              );
            },
            itemCount: timeList.length,
          ))
        ],
      ),
    );
  }

  _navScanPage() async {
    int scanTime =
        await Navigator.of(context).push(MaterialPageRoute(builder: (context) {
      return ScanPage();
    }));
    timeList.add(scanTime);
    setState(() {
      timeList = [...timeList];
    });
  }
}
