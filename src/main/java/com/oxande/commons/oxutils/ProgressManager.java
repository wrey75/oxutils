package com.oxande.commons.oxutils;

import java.util.Date;

public class ProgressManager {
    public float progress;
    public Date started;
    public int processedLines;
    public String workInProgress;
    public int maxLines = -1;

    public ProgressManager(int maxLines){
        this.maxLines = maxLines;
        setProgress(0);
    }

    void setProgress(int v){
        processedLines = v;
    }

    public float getProgress() {
        return (maxLines > 0 ? ((float) processedLines / maxLines) : this.progress);
    }
}
