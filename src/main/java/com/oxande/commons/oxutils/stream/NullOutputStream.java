package com.oxande.commons.oxutils.stream;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
    @Override
    public void write(int i) throws IOException {
        // Ignore when writing!
    }
}
