package android.os;

import android.annotation.UnsupportedAppUsage;
import com.android.internal.util.FastPrintWriter;
import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class ShellCommand {
    static final boolean DEBUG = false;
    static final String TAG = "ShellCommand";
    private int mArgPos;
    private String[] mArgs;
    private String mCmd;
    private String mCurArgData;
    private FileDescriptor mErr;
    private FastPrintWriter mErrPrintWriter;
    private FileOutputStream mFileErr;
    private FileInputStream mFileIn;
    private FileOutputStream mFileOut;
    private FileDescriptor mIn;
    private InputStream mInputStream;
    private FileDescriptor mOut;
    private FastPrintWriter mOutPrintWriter;
    private ResultReceiver mResultReceiver;
    private ShellCallback mShellCallback;
    private Binder mTarget;

    public abstract int onCommand(String str);

    public abstract void onHelp();

    public void init(Binder target, FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, int firstArgPos) {
        this.mTarget = target;
        this.mIn = in;
        this.mOut = out;
        this.mErr = err;
        this.mArgs = args;
        this.mShellCallback = callback;
        this.mResultReceiver = null;
        this.mCmd = null;
        this.mArgPos = firstArgPos;
        this.mCurArgData = null;
        this.mFileIn = null;
        this.mFileOut = null;
        this.mFileErr = null;
        this.mOutPrintWriter = null;
        this.mErrPrintWriter = null;
        this.mInputStream = null;
    }

    /* JADX WARNING: Missing block: B:16:0x0041, code skipped:
            if (r0 != null) goto L_0x0043;
     */
    /* JADX WARNING: Missing block: B:17:0x0043, code skipped:
            r0.send(r2, null);
     */
    /* JADX WARNING: Missing block: B:28:0x0067, code skipped:
            if (r0 == null) goto L_0x00a0;
     */
    /* JADX WARNING: Missing block: B:39:0x009d, code skipped:
            if (r0 == null) goto L_0x00a0;
     */
    /* JADX WARNING: Missing block: B:40:0x00a0, code skipped:
            return r2;
     */
    public int exec(android.os.Binder r14, java.io.FileDescriptor r15, java.io.FileDescriptor r16, java.io.FileDescriptor r17, java.lang.String[] r18, android.os.ShellCallback r19, android.os.ResultReceiver r20) {
        /*
        r13 = this;
        r9 = r13;
        r10 = r18;
        if (r10 == 0) goto L_0x000f;
    L_0x0005:
        r0 = r10.length;
        if (r0 <= 0) goto L_0x000f;
    L_0x0008:
        r0 = 0;
        r0 = r10[r0];
        r1 = 1;
        r12 = r0;
        r11 = r1;
        goto L_0x0013;
    L_0x000f:
        r0 = 0;
        r1 = 0;
        r12 = r0;
        r11 = r1;
    L_0x0013:
        r1 = r13;
        r2 = r14;
        r3 = r15;
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r7 = r19;
        r8 = r11;
        r1.init(r2, r3, r4, r5, r6, r7, r8);
        r9.mCmd = r12;
        r1 = r20;
        r9.mResultReceiver = r1;
        r2 = -1;
        r3 = 0;
        r0 = r9.mCmd;	 Catch:{ SecurityException -> 0x006a, all -> 0x0047 }
        r0 = r13.onCommand(r0);	 Catch:{ SecurityException -> 0x006a, all -> 0x0047 }
        r2 = r0;
        r0 = r9.mOutPrintWriter;
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r0.flush();
    L_0x0038:
        r0 = r9.mErrPrintWriter;
        if (r0 == 0) goto L_0x003f;
    L_0x003c:
        r0.flush();
    L_0x003f:
        r0 = r9.mResultReceiver;
        if (r0 == 0) goto L_0x00a0;
    L_0x0043:
        r0.send(r2, r3);
        goto L_0x00a0;
    L_0x0047:
        r0 = move-exception;
        r4 = r13.getErrPrintWriter();	 Catch:{ all -> 0x00a1 }
        r4.println();	 Catch:{ all -> 0x00a1 }
        r5 = "Exception occurred while executing:";
        r4.println(r5);	 Catch:{ all -> 0x00a1 }
        r0.printStackTrace(r4);	 Catch:{ all -> 0x00a1 }
        r0 = r9.mOutPrintWriter;
        if (r0 == 0) goto L_0x005e;
    L_0x005b:
        r0.flush();
    L_0x005e:
        r0 = r9.mErrPrintWriter;
        if (r0 == 0) goto L_0x0065;
    L_0x0062:
        r0.flush();
    L_0x0065:
        r0 = r9.mResultReceiver;
        if (r0 == 0) goto L_0x00a0;
    L_0x0069:
        goto L_0x0043;
    L_0x006a:
        r0 = move-exception;
        r4 = r13.getErrPrintWriter();	 Catch:{ all -> 0x00a1 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a1 }
        r5.<init>();	 Catch:{ all -> 0x00a1 }
        r6 = "Security exception: ";
        r5.append(r6);	 Catch:{ all -> 0x00a1 }
        r6 = r0.getMessage();	 Catch:{ all -> 0x00a1 }
        r5.append(r6);	 Catch:{ all -> 0x00a1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00a1 }
        r4.println(r5);	 Catch:{ all -> 0x00a1 }
        r4.println();	 Catch:{ all -> 0x00a1 }
        r0.printStackTrace(r4);	 Catch:{ all -> 0x00a1 }
        r0 = r9.mOutPrintWriter;
        if (r0 == 0) goto L_0x0094;
    L_0x0091:
        r0.flush();
    L_0x0094:
        r0 = r9.mErrPrintWriter;
        if (r0 == 0) goto L_0x009b;
    L_0x0098:
        r0.flush();
    L_0x009b:
        r0 = r9.mResultReceiver;
        if (r0 == 0) goto L_0x00a0;
    L_0x009f:
        goto L_0x0043;
    L_0x00a0:
        return r2;
    L_0x00a1:
        r0 = move-exception;
        r4 = r9.mOutPrintWriter;
        if (r4 == 0) goto L_0x00a9;
    L_0x00a6:
        r4.flush();
    L_0x00a9:
        r4 = r9.mErrPrintWriter;
        if (r4 == 0) goto L_0x00b0;
    L_0x00ad:
        r4.flush();
    L_0x00b0:
        r4 = r9.mResultReceiver;
        if (r4 == 0) goto L_0x00b7;
    L_0x00b4:
        r4.send(r2, r3);
    L_0x00b7:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.ShellCommand.exec(android.os.Binder, java.io.FileDescriptor, java.io.FileDescriptor, java.io.FileDescriptor, java.lang.String[], android.os.ShellCallback, android.os.ResultReceiver):int");
    }

    public ResultReceiver adoptResultReceiver() {
        ResultReceiver rr = this.mResultReceiver;
        this.mResultReceiver = null;
        return rr;
    }

    public FileDescriptor getOutFileDescriptor() {
        return this.mOut;
    }

    public OutputStream getRawOutputStream() {
        if (this.mFileOut == null) {
            this.mFileOut = new FileOutputStream(this.mOut);
        }
        return this.mFileOut;
    }

    public PrintWriter getOutPrintWriter() {
        if (this.mOutPrintWriter == null) {
            this.mOutPrintWriter = new FastPrintWriter(getRawOutputStream());
        }
        return this.mOutPrintWriter;
    }

    public FileDescriptor getErrFileDescriptor() {
        return this.mErr;
    }

    public OutputStream getRawErrorStream() {
        if (this.mFileErr == null) {
            this.mFileErr = new FileOutputStream(this.mErr);
        }
        return this.mFileErr;
    }

    public PrintWriter getErrPrintWriter() {
        if (this.mErr == null) {
            return getOutPrintWriter();
        }
        if (this.mErrPrintWriter == null) {
            this.mErrPrintWriter = new FastPrintWriter(getRawErrorStream());
        }
        return this.mErrPrintWriter;
    }

    public FileDescriptor getInFileDescriptor() {
        return this.mIn;
    }

    public InputStream getRawInputStream() {
        if (this.mFileIn == null) {
            this.mFileIn = new FileInputStream(this.mIn);
        }
        return this.mFileIn;
    }

    public InputStream getBufferedInputStream() {
        if (this.mInputStream == null) {
            this.mInputStream = new BufferedInputStream(getRawInputStream());
        }
        return this.mInputStream;
    }

    public ParcelFileDescriptor openFileForSystem(String path, String mode) {
        PrintWriter errPrintWriter;
        StringBuilder stringBuilder;
        try {
            ParcelFileDescriptor pfd = getShellCallback().openFile(path, "u:r:system_server:s0", mode);
            if (pfd != null) {
                return pfd;
            }
        } catch (RuntimeException e) {
            errPrintWriter = getErrPrintWriter();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failure opening file: ");
            stringBuilder.append(e.getMessage());
            errPrintWriter.println(stringBuilder.toString());
        }
        PrintWriter errPrintWriter2 = getErrPrintWriter();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Error: Unable to open file: ");
        stringBuilder2.append(path);
        errPrintWriter2.println(stringBuilder2.toString());
        String suggestedPath = "/data/local/tmp/";
        if (path == null || !path.startsWith(suggestedPath)) {
            errPrintWriter = getErrPrintWriter();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Consider using a file under ");
            stringBuilder.append(suggestedPath);
            errPrintWriter.println(stringBuilder.toString());
        }
        return null;
    }

    public String getNextOption() {
        String arg;
        if (this.mCurArgData == null) {
            arg = this.mArgPos;
            String[] strArr = this.mArgs;
            if (arg >= strArr.length) {
                return null;
            }
            arg = strArr[arg];
            if (!arg.startsWith("-")) {
                return null;
            }
            this.mArgPos++;
            if (arg.equals("--")) {
                return null;
            }
            if (arg.length() <= 1 || arg.charAt(1) == '-') {
                this.mCurArgData = null;
                return arg;
            } else if (arg.length() > 2) {
                this.mCurArgData = arg.substring(2);
                return arg.substring(0, 2);
            } else {
                this.mCurArgData = null;
                return arg;
            }
        }
        arg = this.mArgs[this.mArgPos - 1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No argument expected after \"");
        stringBuilder.append(arg);
        stringBuilder.append("\"");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public String getNextArg() {
        if (this.mCurArgData != null) {
            String arg = this.mCurArgData;
            this.mCurArgData = null;
            return arg;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return null;
        }
        this.mArgPos = i + 1;
        return strArr[i];
    }

    @UnsupportedAppUsage
    public String peekNextArg() {
        String str = this.mCurArgData;
        if (str != null) {
            return str;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i < strArr.length) {
            return strArr[i];
        }
        return null;
    }

    public String getNextArgRequired() {
        String arg = getNextArg();
        if (arg != null) {
            return arg;
        }
        String prev = this.mArgs[this.mArgPos - 1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Argument expected after \"");
        stringBuilder.append(prev);
        stringBuilder.append("\"");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public ShellCallback getShellCallback() {
        return this.mShellCallback;
    }

    public int handleDefaultCommands(String cmd) {
        if ("dump".equals(cmd)) {
            String[] strArr = this.mArgs;
            String[] newArgs = new String[(strArr.length - 1)];
            System.arraycopy(strArr, 1, newArgs, 0, strArr.length - 1);
            this.mTarget.doDump(this.mOut, getOutPrintWriter(), newArgs);
            return 0;
        }
        if (cmd == null || "help".equals(cmd) || "-h".equals(cmd)) {
            onHelp();
        } else {
            PrintWriter outPrintWriter = getOutPrintWriter();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown command: ");
            stringBuilder.append(cmd);
            outPrintWriter.println(stringBuilder.toString());
        }
        return -1;
    }
}
