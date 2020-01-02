package miui.os;

import android.os.Environment;
import java.io.File;

@Deprecated
public class MiuiEnvironment {
    public static File getLegacyExternalStorageDirectory() {
        return Environment.getLegacyExternalStorageDirectory();
    }
}
