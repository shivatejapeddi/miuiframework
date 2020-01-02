package org.egret.plugin.mi.android.util.launcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private static final int BUFFER_SIZE = 1024;

    public static boolean Copy(File src, File dst) {
        if (src == null || dst == null) {
            return false;
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer, 0, 1024);
                int read2 = read;
                if (read <= 0) {
                    break;
                }
                out.write(buffer, 0, read2);
            }
            try {
                in.close();
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return true;
        } catch (IOException ioe2) {
            ioe2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe3) {
                    ioe3.printStackTrace();
                    return false;
                }
            }
            if (out != null) {
                out.close();
            }
            return false;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe22) {
                    ioe22.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static boolean CopyToRoot(File src, File dstRoot) {
        if (src == null || dstRoot == null) {
            return false;
        }
        return Copy(src, new File(dstRoot, src.getName()));
    }

    public static boolean Move(File src, File dst) {
        return (src == null || dst == null || !src.renameTo(dst)) ? false : true;
    }

    public static boolean MoveToRoot(File src, File dstRoot) {
        if (src == null || dstRoot == null) {
            return false;
        }
        return Move(src, new File(dstRoot, src.getName()));
    }

    public static boolean writeFile(File dst, String content) {
        FileWriter out = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = new FileWriter(dst);
            bufferedWriter = new BufferedWriter(out);
            bufferedWriter.write(content);
            try {
                bufferedWriter.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } catch (IOException e2) {
            e2.printStackTrace();
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return false;
                }
            }
            if (out != null) {
                out.close();
            }
            return false;
        } catch (Throwable th) {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static String readFile(File file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            String readLine;
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder strBuffer = new StringBuilder();
            while (true) {
                readLine = bufferedReader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                strBuffer.append(line);
            }
            readLine = strBuffer.toString();
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return readLine;
        } catch (Exception e2) {
            e2.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return null;
                }
            }
            if (fileReader != null) {
                fileReader.close();
            }
            return null;
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }
}
