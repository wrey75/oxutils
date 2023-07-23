package com.oxande.commons.oxutils.stream;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class EasyOutputStream extends OutputStream {
    final OutputStream output;

    public EasyOutputStream(File f) throws IOException {
        this(f.getPath());
    }

    public EasyOutputStream(String path) throws IOException {
        OutputStream out;
        if (path.equalsIgnoreCase("stdout:")) {
            out = System.out;
        } else if (path.equalsIgnoreCase("stderr:")) {
            out = System.err;
        } else if (path.equalsIgnoreCase("null:")) {
            out = new NullOutputStream();
        } else {
            out = new FileOutputStream(new File(path));
        }
        if(path.endsWith(".gz")){
            out = new GZIPOutputStream(out);
        }
        this.output = out;
    }

    @Override
    public void write(int i) throws IOException {
        output.write(i);
    }

    @Override
    public void write(byte buf[], int off, int len) throws IOException {
        output.write(buf, off, len);
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
