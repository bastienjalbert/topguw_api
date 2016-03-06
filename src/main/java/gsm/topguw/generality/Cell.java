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

import static gsm.topguw.tools.Scanner.RGX_KAL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cell information (band, frequency, arfcn, power)s
 *
 * @author bastie.enjalbert
 */
public class Cell {

    // List of available bands with gr-gsm
    public static final List<String> BANDS
            = Arrays.asList("P-GSM", "DCS1800", "PCS1900", "R-GSM", "E-GSM", "GSM450", "GSM480", "GSM850", "GSM900");

    /**
     * frequency (in Hz)
     */
    private String freq;

    /**
     * ARFCN
     */
    private int arfcn;

    /**
     * Intensity, power
     */
    private String power;

    /**
     * GSM Band
     */
    private String band;

    /**
     * Create a cell without any information
     *
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
        } else if (arfcn < 0 || arfcn > 1023) {
            throw new IllegalArgumentException("ARFCN argument seems not to be "
                    + "a valid ARFCN");
        } else if (!BANDS.contains(band)) {
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
        return "band : " + this.getBand() + ", freq : " + this.getFreq() + ", arfcn : "
                + this.getArfcn() + ", power : " + this.getPower();
    }

    /**
     *
     * @return a Cell from a string format
     * @param a the string representation of a cell (getted via toString)
     */
    public Cell StringTo(String a) {
        // pattern of toString
        Pattern cellString
                = Pattern.compile("band : ([0-9]*), freq : ([0-9]*), arfcn : ([0-9]*), power : ([0-9]*)");
        
        Matcher m = cellString.matcher(a);
        // check for match with pattern and a
        if (m.matches()) {
            return new Cell(m.group(1), Integer.parseInt(m.group(2)), m.group(3), m.group(4));
        } else {
            return null;
        }

    }

    /**
     * @return the freq
     */
    public String getFreq() {
        return freq;
    }

    /**
     * @param freq the freq to set
     */
    public void setFreq(String freq) {
        this.freq = freq;
    }

    /**
     * @return the arfcn
     */
    public int getArfcn() {
        return arfcn;
    }

    /**
     * @param arfcn the arfcn to set
     */
    public void setArfcn(int arfcn) {
        this.arfcn = arfcn;
    }

    /**
     * @return the power
     */
    public String getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(String power) {
        this.power = power;
    }

    /**
     * @return the band
     */
    public String getBand() {
        return band;
    }

    /**
     * @param band the band to set
     */
    public void setBand(String band) {
        this.band = band;
    }

}
