/* Cell.java
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
package gsm.topguw.generality;

import java.util.Arrays;
import java.util.List;

/**
 * Cell information (band, frequency, arfcn, power)s
 * @author bastien enjalbert
 */
public class Cell {
    
    // List of available bands with gr-gsm
    private static final List<String> BANDS
            = Arrays.asList("P-GSM","DCS1800","PCS1900","R-GSM","E-GSM","GSM450","GSM480","GSM850","GSM900");

    /**
     * frequency (in Hz)
     */
    private final String freq;

    /**
     * ARFCN
     */
    private final int arfcn;
    
    /**
     * Intensity, power
     */
    private final String power;
    
    /** GSM Band */
    private final String band;

    /**
     * Create a cell without any information
     * @param freq the cell's frequency
     * @param arfcn the cell's arfcn
     * @param band the cell's band
     * @param power the cell's power
     * @throws IllegalArgumentException if an invalid argument try to be pass
     */
    public Cell(String freq, int arfcn, String band, String power) throws IllegalArgumentException {
        if (!isFreq(freq)) {
            throw new IllegalArgumentException("Freq argument seems not to be "
                    + "a valide frequency");
        } else if(arfcn < 0 || arfcn > 1023) {
            throw new IllegalArgumentException("ARFCN argument seems not to be "
                    + "a valid ARFCN");
        } else if(!BANDS.contains(band)) {
            throw new IllegalArgumentException("BAND argument seems not to be "
                    + "a valid band");
        }
        this.freq = freq;
        this.arfcn = arfcn;
        this.band = band;
        this.power = power;
    }

    /**
     * Determine if a String is frequency formatted (in Hz, and any GSM band)
     *
     * @param freq
     * @return
     */
    public static boolean isFreq(String freq) {
        // if the frequency is too long or too short
        if (freq.length() > 11 || freq.length() < 9) {
            return false;
        }
        // if the frequency isn't a number
        try {
            Integer.parseInt(freq);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return String reprensentation of the cell
     */
    @Override
    public String toString() {
        return "band : " + this.band + ", freq : " + this.freq + ", arfcn : " 
                + this.arfcn + ", power : " + this.power;
    }
    
}
