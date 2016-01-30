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

/**
 * TCHF (Traffic Channel, Full rate) Channel
 * @author bastien.enjalbert
 */
public class Traffic extends Channels{

    /**
     * Create an abstract version of a TCHF (Traffic Channel, Full rate) without any
     * data inside.
     * @param timeslot The timeslot
     * @param subslot The sub-slot
     */
    public Traffic(int timeslot, int subslot) {
        this.timeslot = timeslot;
        this.subslot = subslot;
        this.recordedFrames = null;
    }
    
    /**
     * Create an abstract version of a TCHF (Traffic Channel, Full rate) without any
     * data inside.
     */
    public Traffic() {
        this.timeslot = -1;
        this.subslot = -1;
        this.recordedFrames = null;
    }
    
    /**
     * Initialize TCHF channel without data
     * @param timeslot
     * @param subslot
     * @return an empty TCHF that wait to be filled (start method)
     */
    @Override
    public Channels decode(int timeslot, int subslot) {
        return new NonCombined(timeslot, subslot);
    }
    
    /**
     * Get all frame from the channel (TCHF)
     */
    @Override
    public void start() {
        // TODO : use airprobe_decode.py with a cell, a capture file and rtlsdrconf
    }
}
