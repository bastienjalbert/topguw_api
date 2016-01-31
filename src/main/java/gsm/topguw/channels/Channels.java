/* Channels.java - 15 janv. 2016  -  UTF-8 - 
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

import gsm.topguw.conf.RtlsdrConf;
import gsm.topguw.generality.Cell;
import gsm.topguw.generality.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Channels implementation for gr-gsm and uses
 * @author root
 */
public abstract class Channels {
    
    /** basic frame regex */
    public static Pattern RGX_FRAME
            = Pattern.compile("([0-9]*) ([0-9]*):  (([0-9a-fA-F][0-9a-fA-F] )*([0-9a-fA-F][0-9a-fA-F]))");
    
   
    
    /** Channels timeslot */
    protected int timeslot;
    
    /** Channels subslot */
    protected int subslot;
    
    /** Initialized data into Array */
    protected ArrayList<Frame> recordedFrames;
    
    /** Cfile linked to the channel */
    protected File cfile;
    
    /**
     * Return a channel to work on
     * @param timeslot the timeslot
     * @param subslot the sub-slot
     * @return an empty version of the channels (without data)
     */
    public abstract Channels decode(int timeslot, int subslot, File cfile);
    
    /**
     * Get all frame from the channel (into recordedFrames)
     * @param cell the cell where the cfile was captured
     * @param rtlconf the rtl sdr device configuration
     * @param key the key and the A5 version (1/2/3)
     * @throws IOException 
     */
    public abstract void start(Cell cell, RtlsdrConf rtlconf, String[] key) throws IOException;

    /**
     * @return the recordedFrames
     */
    public ArrayList<Frame> getRecordedFrames() {
        return recordedFrames;
    }

    /**
     * @param recordedFrames the recordedFrames to set
     */
    public void setRecordedFrames(ArrayList<Frame> recordedFrames) {
        this.recordedFrames = recordedFrames;
    }
    
}
