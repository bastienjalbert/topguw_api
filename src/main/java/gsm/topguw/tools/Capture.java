/* Capture.java - 15 janv. 2016  -  UTF-8 - 
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
import gsm.topguw.err.RtlsdrError;
import static gsm.topguw.tools.Scanner.RGX_KAL;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Capture cfile/bursts file with gr-gsm
 *
 * @author bastien.enjalbert
 */
public class Capture {

    /**
     * Start a GSM cell capture (cfile or bursts file), you must close the
     * returned process if you want to stop sniffing/capturing
     *
     * @param dest capture file destination
     * @param conf the rtl_sdr configuration
     * @param cell the cell to capture
     * @param options : <ul><li> index 0 : arfcn (1 = yes, 0 = frequency)</li>
     * <li> index 1 : cfile (1 = yes, 0 = bursts file)</li></ul>
     * @return an arraylist containing GSM tower detected by kal index 1 : freq
     * (corrected) index 2 : power
     * @throws RtlsdrError if RTL-SDR device is not plugged
     * @throws IOException if the process (kal) cannot start
     */
    public static Process captureCell(File dest, RtlsdrConf conf, Cell cell,
            int[] options) throws RtlsdrError, IOException {

        ProcessBuilder pb = null;

        // start the capture command
        if (options[0] == 0) {
            if (options[1] == 1) {
                pb = new ProcessBuilder("airprobe_rtlsdr_capture.py", "-f", cell.getFreq(), "-c",
                        dest.getAbsolutePath());

            } else { // if == 0
                pb = new ProcessBuilder("airprobe_rtlsdr_capture.py", "-f", cell.getFreq(), "-b",
                        dest.getAbsolutePath());
            }
        } else if (options[0] == 1) {
            if (options[1] == 1) {
                pb = new ProcessBuilder("airprobe_rtlsdr_capture.py", "-a", Integer.toString(cell.getArfcn()), "-c",
                        dest.getAbsolutePath(), "-p", Integer.toString(conf.getPpm()), "-g", Integer.toString(conf.getGain()),
                        "-s", Long.toString(conf.getSamprate()));

            } else { // if == 0
                pb = new ProcessBuilder("airprobe_rtlsdr_capture.py", "-a", Integer.toString(cell.getArfcn()), "-b",
                        dest.getAbsolutePath(), "-p", Integer.toString(conf.getPpm()), "-g", Integer.toString(conf.getGain()),
                        "-s", Long.toString(conf.getSamprate()));
            }
        }
        //pb.redirectErrorStream(true);
        Process p = pb.start();/*
        p.getOutputStream().flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = new String();
        while ((line = reader.readLine()) != null) {
            // if there is not RTL_SDR detected
            if (line.equals("No supported devices found.")) {
                throw new RtlsdrError("Please plug-in your RTL-SDR device (not detected).");
            }
        }*/

        // delete process
        /*p.destroy();
        p.destroyForcibly();*/
        return p;
    }

}
