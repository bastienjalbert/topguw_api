/* Scanner.java
 * --------------------------------- DISCLAMER ---------------------------------
 * Copyright (c) 2015, Bastien Enjalbert All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * The views and conclusions contained in the software and documentation are 
 * those of the authors and should not be interpreted as representing official 
 * policies, either expressed or implied, of the FreeBSD Project.
 * @author Bastien Enjalbert
 */
package gsm.topguw.tools;
import gsm.topguw.conf.RtlsdrConf;
import gsm.topguw.generality.Cell;
import gsm.topguw.rtlerr.RtlsdrError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implement airprobe_rtlsdr_scanner.py from GR-GSM tools
 * @author bastien.enjalbert
 */
public class Scanner {
    
    /**
     * Start a GSM cell scan and get found cells
     * @param whichGsm the GSM band to scan
     * @param conf the configuration of the rtl_sdr dongle to use
     * @return an arraylist that might contain found cell 
     * or null if an error happen
     */
    public ArrayList<Cell> scanForCell(String whichGsm, RtlsdrConf conf) {
        // scan choice (=> only kalibrate-rtl test available now ...)
        try {
            return getGsmCells(whichGsm, conf);
        } catch(RtlsdrError | IOException e) {
            System.err.println(e);
        }
        return null;
    }
    
    /******************************************************************
     ********************* Kalibrate-rtl method ***********************
     *****************************************************************/
    
     /**
     * base regex for a kalibrate output group 1 nothing group 2 cell fequency
     * group 3 freq type (- or +) group 4 freq correction group 5 power
     */
    public static Pattern RGX_KAL
            = Pattern.compile(".*chan: ([0-9])* \\(([0-9]*.[0-9]*)MHz (-+) ([0-9]*.[0-9]*)kHz\\)	power: ([0-9]*.[0-9]*)");

    /**
     * Start kalibrate-rtl (kal) to get GSM tower
     *
     * @param whichGsm GSM type (900, 1800, ..)
     * @param conf the rtl_sdr configuration 
     * @return an arraylist containing GSM tower detected by kal index 1 : freq
     * (corrected) index 2 : power
     * @throws RtlsdrError if RTL-SDR device is not plugged
     * @throws IOException if the process (kal) cannot start
     */
    public static ArrayList<Cell> getGsmCells(String whichGsm, RtlsdrConf conf) 
                                               throws RtlsdrError, IOException {
        // Will contains gsm cells found
        ArrayList<Cell> gsmCells = new ArrayList<>();
        
        // ppm error 
        String ppm;
        
        if(conf.getPpm() == -1) {
            ppm = "0";
        } else {
            ppm = Integer.toString(conf.getPpm());
        }

        // start the kal command
        ProcessBuilder pb = new ProcessBuilder("kal", "-s", whichGsm, "-g", Integer.toString(conf.getGain()), "-e", ppm);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        p.getOutputStream().flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        // capture line from standard output from kal execution
        String line = new String();
        while ((line = reader.readLine()) != null) {
            // if there is not RTL_SDR detected
            if (line.equals("No supported devices found.")) {
                throw new RtlsdrError("Please plug-in your RTL-SDR device (not detected).");
            }
            // if line of gsm cell is detecte from stdout
            Matcher m = RGX_KAL.matcher(line);
            // initialize
            Cell discoveredCell;
            if (m.matches()) {
                // add the correct frequency	
                BigDecimal add;
                // substract or add depending on symbol to get good frequency
                if (m.group(3).equals("+")) {
                    add = new BigDecimal(Double.parseDouble(m.group(4)));
                } else {
                    add = new BigDecimal(-(Double.parseDouble(m.group(4))));
                }
                BigDecimal big = new BigDecimal(Double.parseDouble(m.group(2)) * 1000000);
                big = big.add(add);
                // can throw an error
                discoveredCell = new Cell(Long.toString(big.longValue()), Integer.parseInt(m.group(1)), whichGsm, m.group(5));
                gsmCells.add(discoveredCell);
            }
        }
        // delete process
        p.destroy();
        p.destroyForcibly();
        // assert p.getInputStream().read() == -1;
        return gsmCells;
    }


    /******************************************************************
     ********************* airprobe_rtlsdr_scanner.py *****************
     *****************************************************************/
    
}
