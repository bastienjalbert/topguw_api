# topguw_api
Topguw implements Gr-gsm tools in Java and makes possible to use gsm datas directly from Java

# Version 0.1 initial
First version, still need improvment !

# Examples 
Don't forget to import needed classes

You can easily extract all frames from a specific channel/timeslot/subslot (Decode module)
```java 
// set which cell you are working on 
Cell cell = new Cell("951360000", 121, "GSM900", "152200");
// set the configuration of the RTL-SDR device 
RtlsdrConf conf = new RtlsdrConf(0, 1000000, 0);

// initialize the channel object
Channels broadcast = null;
// prepare the decoder module
Decode dec = new Decode();

try {
  // specify informations about what data you want to get
  broadcast = dec.getChannel("combined", 0, 0, new File("/root/gsm/testfile.cfile"));
  // start extraction of frames (frames will be stored into the broadcast object)
  broadcast.start(cell, conf, new String[0]);
  } 
// display some data (here frame number 2153976)
System.out.println(broadcast.getRecordedFrames().get(new Integer(2153976)));
} catch (IOException | ChannelError e) {
  System.err.println(e.getMessage());
}
```
You can also capture data with your own RTL-SDR device (Capture module)
```java 
// specify the destination file where frames will be stored  
File dest = new File("/root/sniffing-capturing.test");
// initialize a basic rtlsdr configuration
RtlsdrConf conf = new RtlsdrConf();
// specify the cell you want to sniff 
Cell cell = new Cell("936360997", 7, "GSM900", "0");
/* capture options : index[0] 0 means you want to capture from the frequency, 1 means arfcn capture
                     index[1] 0 means you want to capture into a bursts file, 1 means cfile record
*/
int[] options = {0, 1};
// start the capture
Process result = Capture.captureCell(dest, conf, cell, options);
// capturing 30 seconds
Thread.sleep(30000);
// close process <=> stop sniffing
result.destroy();
result.destroyForcibly();
```
Finaly you can scan cells around yourself (Scanner module)
```java 
// specify information about the scan, gsm bands are same as kalibrate-rtl actually,
// note : you can uses more bands with the Decoder (all bands are in Cell class)
String whichGsm = "GSM900";
RtlsdrConf conf = new RtlsdrConf();
// start the scan and stored Cell object into result
ArrayList<Cell> result = Scanner.scanForCell(whichGsm, conf);
// display founded cells informations
result.stream().forEach((aCell) -> {
  System.out.println(aCell.toString());
}); 
