/*
 * 7 jan 2016                   initial version
 * Rtlsdrconf.java
 */
package gsm.topguw.conf;

/**
 * Add or get the current rtl-sdr dongle configuration for gr-gsm
 * @author bastien enjalbert
 */
public class Rtlsdrconf {
    
    /** frequency correction (ppm) */
    private int ppm;
    
    /** sample rate */
    private long samprate;
  
    /**
     * Create a configuration with some basic information
     * @param ppm the frequency correction of the dongle
     * @param samplerate the sample rate of the dongle
     */
    public Rtlsdrconf(int ppm, long samplerate) {
        this.ppm = ppm;
        this.samprate = samplerate;
    }
    
    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public long getSamprate() {
        return samprate;
    }

    public void setSamprate(long samprate) {
        this.samprate = samprate;
    }
    
}
