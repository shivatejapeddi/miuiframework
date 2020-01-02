package android.miui;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.util.Log;

public class BiometricConnect {
    public static boolean DEBUG_LOG = true;
    public static final String MSG_BUNDLE_KEY = "tag";
    public static final int MSG_BUNDLE_VALUE_DB = 3;
    public static final int MSG_BUNDLE_VALUE_DB_COMMIT = 4;
    public static final int MSG_BUNDLE_VALUE_DEPTHMAP = 5;
    public static final int MSG_BUNDLE_VALUE_ENROLL_PARAM = 6;
    public static final int MSG_BUNDLE_VALUE_FACE = 2;
    public static final int MSG_BUNDLE_VALUE_HASFACE = 7;
    public static final int MSG_BUNDLE_VALUE_INIT = 0;
    public static final int MSG_BUNDLE_VALUE_SURFACE = 1;
    public static final int MSG_CALLBACK_BUNDLE = 1005;
    public static final int MSG_CALLBACK_EVENT = 1004;
    public static final String MSG_CB_BUNDLE_DB_GROUP = "group";
    public static final String MSG_CB_BUNDLE_DB_GROUP_ID_MAX = "group_id_max";
    public static final String MSG_CB_BUNDLE_DB_TEMPLATE = "template";
    public static final String MSG_CB_BUNDLE_DB_TEMPLATE_ID_MAX = "template_id_max";
    public static final String MSG_CB_BUNDLE_DEPTHMAP_BIT_PER_POINT = "bitPerPoint";
    public static final String MSG_CB_BUNDLE_DEPTHMAP_DATA = "data";
    public static final String MSG_CB_BUNDLE_DEPTHMAP_H = "h";
    public static final String MSG_CB_BUNDLE_DEPTHMAP_TYPE = "type";
    public static final String MSG_CB_BUNDLE_DEPTHMAP_W = "w";
    public static final String MSG_CB_BUNDLE_ENROLL_PARAM_DETECT_DEPTHMAP = "detect_depthmap";
    public static final String MSG_CB_BUNDLE_ENROLL_PARAM_DETECT_DISTANCE = "detect_distance";
    public static final String MSG_CB_BUNDLE_ENROLL_PARAM_DETECT_FACE = "detect_face";
    public static final String MSG_CB_BUNDLE_ENROLL_PARAM_DETECT_ZONE = "detect_zone";
    public static final String MSG_CB_BUNDLE_ENROLL_PARAM_WAITING_UI = "wait_ui";
    public static final String MSG_CB_BUNDLE_FACE_DISTANCE = "distance";
    public static final String MSG_CB_BUNDLE_FACE_FLOAT_EYE_DIST = "eye_dist";
    public static final String MSG_CB_BUNDLE_FACE_FLOAT_PITCH = "pitch";
    public static final String MSG_CB_BUNDLE_FACE_FLOAT_ROLL = "roll";
    public static final String MSG_CB_BUNDLE_FACE_FLOAT_YAW = "yaw";
    public static final String MSG_CB_BUNDLE_FACE_HAS_FACE = "has_face";
    public static final String MSG_CB_BUNDLE_FACE_IS_IR = "is_ir_detect";
    public static final String MSG_CB_BUNDLE_FACE_POINTS_ARRAY = "points_array";
    public static final String MSG_CB_BUNDLE_FACE_RECT_BOUND = "bound";
    public static final String MSG_CB_BUNDLE_SHAREMEM_FD = "fd";
    public static final String MSG_CB_BUNDLE_SHAREMEM_SIZE = "fd_size";
    public static final int MSG_CB_EVENT_DATABASE = 43;
    public static final int MSG_CB_EVENT_ENROLL_ERROR = 31;
    public static final int MSG_CB_EVENT_ENROLL_INFO = 32;
    public static final int MSG_CB_EVENT_ENROLL_SUCCESS = 30;
    public static final int MSG_CB_EVENT_IR_CAM_CLOSED = 23;
    public static final int MSG_CB_EVENT_IR_CAM_ERROR = 24;
    public static final int MSG_CB_EVENT_IR_CAM_FACE_DETECT_BASE_SIZE = 22;
    public static final int MSG_CB_EVENT_IR_CAM_OPEND = 20;
    public static final int MSG_CB_EVENT_IR_CAM_PREVIEW_SIZE = 21;
    public static final int MSG_CB_EVENT_RGB_CAM_CLOSED = 3;
    public static final int MSG_CB_EVENT_RGB_CAM_ERROR = 4;
    public static final int MSG_CB_EVENT_RGB_CAM_FACE_DETECT_BASE_SIZE = 2;
    public static final int MSG_CB_EVENT_RGB_CAM_OPEND = 0;
    public static final int MSG_CB_EVENT_RGB_CAM_PREVIEW_SIZE = 1;
    public static final int MSG_CB_EVENT_VERIFY_ERROR = 41;
    public static final int MSG_CB_EVENT_VERIFY_INFO = 42;
    public static final int MSG_CB_EVENT_VERIFY_SUCCESS = 40;
    public static final int MSG_COMMAND_DB_PREPARE = 9;
    public static final int MSG_COMMAND_DB_RESET = 10;
    public static final int MSG_COMMAND_DEINIT_CALLBACK = 2;
    public static final int MSG_COMMAND_ENROLL_START = 7;
    public static final int MSG_COMMAND_ENROLL_STOP = 8;
    public static final int MSG_COMMAND_FAKE_IR_CB = 11;
    public static final int MSG_COMMAND_INIT_CALLBACK = 1;
    public static final int MSG_COMMAND_RESUME_ENROLL_LOGIC = 14;
    public static final int MSG_COMMAND_RGB_CAM_CLOSE = 4;
    public static final int MSG_COMMAND_RGB_CAM_OPEN = 3;
    public static final int MSG_COMMAND_SHARE_MEM_USED = 12;
    public static final int MSG_COMMAND_TEST = 100;
    public static final int MSG_COMMAND_UPDATE_DEPTHMAP_TYPE = 13;
    public static final int MSG_COMMAND_VERIFY_START = 5;
    public static final int MSG_COMMAND_VERIFY_STOP = 6;
    public static final int MSG_CONNECT_TEST = 9999;
    public static final String MSG_REPLY_ARG1 = "arg1";
    public static final String MSG_REPLY_ARG2 = "arg2";
    public static final String MSG_REPLY_BUNDLE_KEY = "key";
    public static final String MSG_REPLY_EVENT = "event";
    public static final String MSG_REPLY_MODULE_ID = "module_id";
    public static final String MSG_REPLY_NO_SEND_WAIT = "auto_reply";
    public static final String MSG_REPLY_RESULT = "result";
    public static final int MSG_SEND_BUNDLE = 1002;
    public static final int MSG_SEND_COMMAND = 1001;
    public static final int MSG_SET_DEBUG_PREVIEW_SURFACE = 2000;
    public static final int MSG_SET_PREVIEW_SURFACE = 1003;
    public static final int MSG_SEVICE_VERSION = 1000;
    public static final int MSG_VER_MODULE_ID_STRUCT_LIGHT = 1;
    public static final String MSG_VER_MODULE_MAJ = "module_ver_maj";
    public static final String MSG_VER_MODULE_MIN = "module_ver_min";
    public static final String MSG_VER_SER_MAJ = "svc_ver_maj";
    public static final String MSG_VER_SER_MIN = "svc_ver_min";
    public static final String SERVICE_NAME = "BiometricService";
    public static final String SERVICE_PACKAGE_NAME = "com.xiaomi.biometric";

    public static class DBGroup implements Parcelable {
        public static final Creator<DBGroup> CREATOR = new Creator<DBGroup>() {
            public DBGroup createFromParcel(Parcel in) {
                DBGroup r = new DBGroup();
                r.readFromParcel(in);
                return r;
            }

            public DBGroup[] newArray(int size) {
                return new DBGroup[size];
            }
        };
        public int mId;
        public String mName;

        public DBGroup(int id, String name) {
            this.mId = id;
            this.mName = name;
        }

        public void readFromParcel(Parcel in) {
            this.mId = in.readInt();
            this.mName = in.readString();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(this.mId);
            stringBuilder.append(".");
            stringBuilder.append(this.mName);
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.mId);
            out.writeString(this.mName);
        }
    }

    public static class DBTemplate implements Parcelable {
        public static final Creator<DBTemplate> CREATOR = new Creator<DBTemplate>() {
            public DBTemplate createFromParcel(Parcel in) {
                DBTemplate r = new DBTemplate();
                r.readFromParcel(in);
                return r;
            }

            public DBTemplate[] newArray(int size) {
                return new DBTemplate[size];
            }
        };
        public String mData;
        public int mGroupId;
        public int mId;
        public String mName;

        public DBTemplate(int id, String name, String Data, int group_id) {
            this.mId = id;
            this.mGroupId = group_id;
            this.mName = name;
            this.mData = Data;
        }

        public void readFromParcel(Parcel in) {
            this.mId = in.readInt();
            this.mGroupId = in.readInt();
            this.mName = in.readString();
            this.mData = in.readString();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(this.mId);
            stringBuilder.append(".");
            stringBuilder.append(this.mName);
            stringBuilder.append(":");
            stringBuilder.append(this.mData);
            stringBuilder.append(",");
            stringBuilder.append(this.mGroupId);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.mId);
            out.writeInt(this.mGroupId);
            out.writeString(this.mName);
            out.writeString(this.mData);
        }
    }

    public static void syncDebugLog() {
        DEBUG_LOG = SystemProperties.getBoolean("persist.biometric.debug.log", false);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DEBUG_LOG:");
        stringBuilder.append(DEBUG_LOG);
        Log.w("BiometricConnect", stringBuilder.toString());
    }
}
