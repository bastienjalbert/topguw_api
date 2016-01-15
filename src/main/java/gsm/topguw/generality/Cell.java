/*
 * 7 jan 2016                   initial version
 * Cell.java
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
            = Arrays.asList("P-GSM","DCS1800","PCS1900","R-GSM","E-GSM","GSM450","GSM480","GSM850");

    /**
     * frequency (in Hz)
     */
    private final String freq;

    /**
     * ARFCN
     */
    private final int arfcn;
    
    /** GSM Band */
    private final String band;

    /**
     * Create a cell without any information
     * @param freq the cell's frequency
     * @param arfcn the cell's arfcn
     * @param band the cell's band
     * @throws IllegalArgumentException if an invalid argument try to be pass
     */
    public Cell(String freq, int arfcn, String band) throws IllegalArgumentException {
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

}
