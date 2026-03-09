package com.sms.dto.response;

public class ReportSmsResponse {
    private long completed;
    private long failed;
    private long processing;
    private long none;
    private long totalSms;
    // getters/setters
    public long getCompleted() { return completed; }
    public void setCompleted(long completed) { this.completed = completed; }
    public long getFailed() { return failed; }
    public void setFailed(long failed) { this.failed = failed; }
    public long getProcessing() { return processing; }
    public void setProcessing(long processing) { this.processing = processing; }
    public long getNone() { return none; }
    public void setNone(long none) { this.none = none; }
    public long getTotalSms() { return totalSms; }
    public void setTotalSms(long totalSms) { this.totalSms = totalSms; }
}
