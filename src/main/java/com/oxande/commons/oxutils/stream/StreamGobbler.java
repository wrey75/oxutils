package com.oxande.commons.oxutils.stream;



import com.oxande.commons.oxutils.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A thread in charge of the copy of an input stream to an output stream.
 *
 */
public class StreamGobbler extends Thread {
    private InputStream in;
    private OutputStream out;
    private long totalBytes = -1L;


    public StreamGobbler(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Create the object and starts to pipe immediately.
     *
     * @param in the input stream
     * @param out the output stream
     * @return the stream gobbler
     */
    public static StreamGobbler pipe(InputStream in, OutputStream out){
        StreamGobbler gobbler = new StreamGobbler(in, out);
        gobbler.start();
        return gobbler;
    }

    /**
     * The number of bytes transferred so far. The value is updated AFTER
     * the bytes are written to the output.
     *
     * @return the number of bytes transferred.
     */
    public long transferred(){
        return Math.max(totalBytes, 0L);
    }

    /**
     * Checks if the thread has started.
     *
     * @return true if the thread has started to read th input.
     */
    public boolean isStarted(){
        return totalBytes >= 0;
    }

    @Override
    public void run() {
        byte[] buf = new byte[2048];
        try {
            int nb;
            this.totalBytes = 0;
            while ((nb = in.read(buf)) > 0) {
                out.write(buf, 0, nb);
                this.totalBytes += nb;
            }
            Assert.isTrue(nb == -1, "We expected a value of -1 for EOF!");
            in.close();
            out.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("I/O Error", ex);
        }
    }
}