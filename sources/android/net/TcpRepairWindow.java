package android.net;

public final class TcpRepairWindow {
    public final int maxWindow;
    public final int rcvWnd;
    public final int rcvWndScale;
    public final int rcvWup;
    public final int sndWl1;
    public final int sndWnd;

    public TcpRepairWindow(int sndWl1, int sndWnd, int maxWindow, int rcvWnd, int rcvWup, int rcvWndScale) {
        this.sndWl1 = sndWl1;
        this.sndWnd = sndWnd;
        this.maxWindow = maxWindow;
        this.rcvWnd = rcvWnd;
        this.rcvWup = rcvWup;
        this.rcvWndScale = rcvWndScale;
    }
}
