package com.oxande.commons.oxutils.stream;

import java.io.*;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * The input easy stream is a stream with adaptive coding based on the extension and URL or file.
 */
public class EasyInputStream extends InputStream {

    final InputStream input;

    public EasyInputStream(File f) throws IOException {
        this(f.getPath());
    }

    public EasyInputStream(String path) throws IOException {
        InputStream in;
        if (path.equalsIgnoreCase("stdin:")) {
            in = System.in;
        } else if (path.equalsIgnoreCase("null:")) {
            in = new ByteArrayInputStream(new byte[0]);
        } else if (path.matches("[a-zA-Z][a-zA-Z]+:")) {
            URL url = new URL(path);
            in = url.openStream();
        } else {
            in = new FileInputStream(new File(path));
        }
        if (path.endsWith(".gz")) {
            in = new GZIPInputStream(in);
        }
        this.input = in;
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return input.read(buf, off, len);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}
