/* StandaloneControlChannel.java - 15 janv. 2016  -  UTF-8 - 
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
package gsm.topguw.channels;

import static gsm.topguw.channels.Channels.RGX_FRAME;
import gsm.topguw.conf.RtlsdrConf;
import gsm.topguw.generality.Cell;
import gsm.topguw.generality.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * TCHF (Traffic Channel, Full rate) Channel
 * @author bastien.enjalbert
 */
public class Traffic extends Channels{

    private final String chanName = "TCHF";
    
    /**
     * Create an abstract version of a TCHF (Traffic Channel, Full rate) 
     * Assign the timeslot, subslot and the capture cfile
     * @param timeslot The timeslot
     * @param subslot The sub-slot
     * @param cfile the linked cfile to the channel
     */
    public Traffic(int timeslot, int subslot, File cfile) {
        this.timeslot = timeslot;
        this.subslot = subslot;
        this.recordedFrames = null;
        this.cfile = cfile;
    }
    
    /**
     * Create an abstract version of a TCHF (Traffic Channel, Full rate) without any
     * data inside.
     */
    public Traffic() {
        this.timeslot = -1;
        this.subslot = -1;
        this.recordedFrames = null;
        this.cfile = null;
    }
    
    /**
     * Initialize TCHF channel without data
     * @param timeslot
     * @param subslot
     * @param cfile the linked cfile to the channel
     * @return an empty TCHF that wait to be filled (start method)
     */
    @Override
    public Channels decode(int timeslot, int subslot, File cfile) {
        return new NonCombined(timeslot, subslot, cfile);
    }
    
    /**
     * Get all frame from the channel (into recordedFrames) on TCHF
     * 
     * @param cell the cell where the cfile was captured
     * @param rtlconf the rtl sdr device configuration
     * @param key the key and the A5 version (1/2/3)
     * @throws IOException with the airprobe_decode process
     */
    @Override
    public void start(Cell cell, RtlsdrConf rtlconf, String[] key) throws IOException {
        
        ArrayList<Frame> frames = new ArrayList<>();
        
        ProcessBuilder pb = null;
        
        if(key.length == 2) {
            pb = new ProcessBuilder("airprobe_decode.py", "-m", chanName,
                "-t", Integer.toString(this.timeslot), "-u",  Integer.toString(this.subslot),
                "-c", this.cfile.getAbsolutePath(), "-f", cell.getFreq(), "-s", rtlconf.getSamprateStr(),
                "-k", key[1], "-e", key[0], "-v");
            
            
        } else {
            // no key specified
            pb = new ProcessBuilder("airprobe_decode.py", "-m", chanName,
                "-t", Integer.toString(this.timeslot), "-u",  Integer.toString(this.subslot),
                "-c", this.cfile.getAbsolutePath(), "-f", cell.getFreq(),"-v");
            
        }
        
        pb.redirectErrorStream(true);
        Process p = pb.start();

        p.getOutputStream().flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            Matcher m = RGX_FRAME.matcher(ligne);
            if(m.matches()) {
                /// extract information and put them into the arraylist
                // maybe check before parsing fn into String to avoid problem .. ?
                frames.add(new Frame(Integer.parseInt(m.group(1)), 
                        Integer.parseInt(m.group(2)), 
                        m.group(3).split(" ")));
            }
        }
        setRecordedFrames(frames);
        p.destroy();
        p.destroyForcibly();
    }
}
