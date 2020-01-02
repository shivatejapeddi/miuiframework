package miui.maml.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import miui.maml.ScreenElementRoot;
import miui.maml.elements.ListScreenElement;
import miui.maml.elements.ListScreenElement.ColumnInfo;
import miui.maml.elements.ListScreenElement.ColumnInfo.Type;
import miui.maml.util.JSONPath;
import miui.maml.util.TextFormatter;
import miui.maml.util.Utils;
import miui.maml.util.net.IOUtils;
import miui.maml.util.net.SimpleRequest;
import miui.maml.util.net.SimpleRequest.StreamContent;
import miui.net.ConnectivityHelper;
import miui.os.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WebServiceBinder extends VariableBinder {
    private static final String LOG_TAG = "WebServiceBinder";
    public static final String TAG_NAME = "WebServiceBinder";
    private boolean mAuthentication;
    IndexedVariable mContentStringVar;
    public String mEncryptedUser;
    IndexedVariable mErrorCodeVar;
    IndexedVariable mErrorStringVar;
    private long mLastQueryTime;
    private List mList;
    private TextFormatter mParamsFormatter;
    private ResponseProtocol mProtocol = ResponseProtocol.XML;
    private boolean mQueryInProgress;
    private Thread mQueryThread;
    RequestMethod mRequestMethod = RequestMethod.POST;
    private boolean mSecure;
    public String mSecurity;
    private String mServiceId;
    public String mServiceToken;
    IndexedVariable mStatusCodeVar;
    private int mUpdateInterval = -1;
    protected TextFormatter mUriFormatter;
    private int mUseNetwork = 2;
    private Expression mUseNetworkExp;

    /* renamed from: miui.maml.data.WebServiceBinder$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$WebServiceBinder$RequestMethod = new int[RequestMethod.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol = new int[ResponseProtocol.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$RequestMethod[RequestMethod.GET.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$RequestMethod[RequestMethod.POST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol[ResponseProtocol.XML.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol[ResponseProtocol.JSONobj.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol[ResponseProtocol.JSONarray.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol[ResponseProtocol.BITMAP.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.DOUBLE.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.INTEGER.ordinal()] = 4;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.LONG.ordinal()] = 5;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    public static final class AuthToken {
        public final String authToken;
        public final String security;

        private AuthToken(String authToken, String security) {
            this.authToken = authToken;
            this.security = security;
        }

        public static AuthToken parse(String plain) {
            if (TextUtils.isEmpty(plain)) {
                return null;
            }
            String[] parts = plain.split(",");
            if (parts.length != 2) {
                return null;
            }
            return new AuthToken(parts[0], parts[1]);
        }
    }

    private static class List {
        public String mDataPath;
        private ListScreenElement mList;
        private String mName;
        private ScreenElementRoot mRoot;

        public List(Element node, ScreenElementRoot root) {
            this.mDataPath = node.getAttribute("path");
            if (TextUtils.isEmpty(this.mDataPath)) {
                this.mDataPath = node.getAttribute("xpath");
            }
            this.mName = node.getAttribute("name");
            this.mRoot = root;
        }

        public void fill(NodeList nodeList) {
            if (nodeList != null) {
                if (this.mList == null) {
                    this.mList = (ListScreenElement) this.mRoot.findElement(this.mName);
                    if (this.mList == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("fail to find list: ");
                        stringBuilder.append(this.mName);
                        Log.e("WebServiceBinder", stringBuilder.toString());
                        return;
                    }
                }
                this.mList.removeAllItems();
                ArrayList<ColumnInfo> columnsInfo = this.mList.getColumnsInfo();
                int size = columnsInfo.size();
                Object[] objects = new Object[size];
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element ele = (Element) nodeList.item(i);
                    for (int j = 0; j < size; j++) {
                        objects[j] = null;
                        ColumnInfo columnInfo = (ColumnInfo) columnsInfo.get(j);
                        Element child = Utils.getChild(ele, columnInfo.mVarName);
                        if (child != null) {
                            String textValue = child.getTextContent();
                            if (textValue != null) {
                                try {
                                    int i2 = AnonymousClass1.$SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[columnInfo.mType.ordinal()];
                                    if (i2 == 1) {
                                        objects[j] = textValue;
                                    } else if (i2 == 2) {
                                        objects[j] = Double.valueOf(textValue);
                                    } else if (i2 == 3) {
                                        objects[j] = Float.valueOf(textValue);
                                    } else if (i2 == 4) {
                                        objects[j] = Integer.valueOf(textValue);
                                    } else if (i2 == 5) {
                                        objects[j] = Long.valueOf(textValue);
                                    }
                                } catch (NumberFormatException e) {
                                }
                            }
                        }
                    }
                    this.mList.addItem(objects);
                }
            }
        }

        public void fill(JSONArray arr) {
            String str = "WebServiceBinder";
            if (this.mList == null) {
                this.mList = (ListScreenElement) this.mRoot.findElement(this.mName);
                if (this.mList == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fail to find list: ");
                    stringBuilder.append(this.mName);
                    Log.e(str, stringBuilder.toString());
                    return;
                }
            }
            this.mList.removeAllItems();
            if (arr.length() != 0) {
                ArrayList<ColumnInfo> columnsInfo = this.mList.getColumnsInfo();
                int columnSize = columnsInfo.size();
                Object[] objects = new Object[columnSize];
                int i = 0;
                while (i < arr.length()) {
                    try {
                        JSONObject object = arr.get(i);
                        if (object instanceof JSONObject) {
                            JSONObject ele = object;
                            for (int j = 0; j < columnSize; j++) {
                                objects[j] = null;
                                ColumnInfo columnInfo = (ColumnInfo) columnsInfo.get(j);
                                int i2 = AnonymousClass1.$SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[columnInfo.mType.ordinal()];
                                if (i2 == 1) {
                                    objects[j] = ele.optString(columnInfo.mVarName);
                                } else if (i2 == 2 || i2 == 3) {
                                    objects[j] = Double.valueOf(ele.optDouble(columnInfo.mVarName));
                                } else if (i2 == 4) {
                                    objects[j] = Integer.valueOf(ele.optInt(columnInfo.mVarName));
                                } else if (i2 == 5) {
                                    objects[j] = Long.valueOf(ele.optLong(columnInfo.mVarName));
                                }
                            }
                            this.mList.addItem(objects);
                            i++;
                        } else {
                            i++;
                        }
                    } catch (JSONException e1) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("JSON error: ");
                        stringBuilder2.append(e1.toString());
                        Log.e(str, stringBuilder2.toString());
                    }
                }
            }
        }
    }

    private class QueryThread extends Thread {
        private static final int ERROR_IO_EXCEPTION = 8;
        private static final int ERROR_OK = 0;
        private static final int ERROR_SECURE_ACCOUNT_AUTHTOKEN_FAIL = 5;
        private static final int ERROR_SECURE_ACCOUNT_NOT_LOGIN = 4;
        private static final int ERROR_SECURE_CIPHER_EXCEPTION = 6;
        private static final int ERROR_SECURE_INVALID_RESPONSE = 7;
        public static final int ERROR_USE_NETWORK_FORBIDDEN = 3;
        private static final String KEY_ENCRYPTED_USER_ID = "encrypted_user_id";

        public QueryThread() {
            super("WebServiceBinder QueryThread");
        }

        public void run() {
            String str = "WebServiceBinder";
            Log.i(str, "QueryThread start");
            WebServiceBinder.this.setErrorCode(0);
            WebServiceBinder.this.setStatusCode(0);
            String uriStr = WebServiceBinder.this.mUriFormatter.getText();
            uriStr = uriStr != null ? uriStr.replaceAll(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, "") : null;
            if (TextUtils.isEmpty(uriStr)) {
                Log.w(str, "url is null");
            } else {
                Object response = doRequest(uriStr, WebServiceBinder.this.mRequestMethod, WebServiceBinder.this.mAuthentication, WebServiceBinder.this.mSecure);
                if (WebServiceBinder.this.mContentStringVar != null) {
                    if (response == null || !(response instanceof String)) {
                        WebServiceBinder.this.mContentStringVar.set(null);
                    } else {
                        WebServiceBinder.this.mContentStringVar.set((String) response);
                    }
                }
                if (response != null) {
                    int i = AnonymousClass1.$SwitchMap$miui$maml$data$WebServiceBinder$ResponseProtocol[WebServiceBinder.this.mProtocol.ordinal()];
                    if (i == 1) {
                        WebServiceBinder.this.processResponseXml((String) response);
                    } else if (i == 2 || i == 3) {
                        WebServiceBinder.this.processResponseJson((String) response);
                    } else if (i == 4) {
                        WebServiceBinder.this.processResponseBitmap((StreamContent) response);
                    }
                }
            }
            WebServiceBinder.this.onUpdateComplete();
            WebServiceBinder.this.mQueryInProgress = false;
            Log.i(str, "QueryThread end");
        }

        /* JADX WARNING: Removed duplicated region for block: B:81:0x018a A:{Splitter:B:47:0x0114, ExcHandler: AuthenticationFailureException (r0_35 'e' miui.maml.util.net.AuthenticationFailureException)} */
        /* JADX WARNING: Removed duplicated region for block: B:79:0x0181 A:{Splitter:B:47:0x0114, ExcHandler: Exception (r0_34 'e' java.lang.Exception)} */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:79:0x0181, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:80:0x0182, code skipped:
            miui.maml.data.WebServiceBinder.access$1100(r7.this$0, r0);
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:81:0x018a, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:82:0x018b, code skipped:
            miui.maml.data.WebServiceBinder.access$1100(r7.this$0, r0);
            miui.maml.data.WebServiceBinder.access$300(r7.this$0, 400);
            r6.invalidateAuthToken(r2, r7.this$0.mServiceToken);
            r7.this$0.mServiceToken = null;
     */
        /* JADX WARNING: Missing block: B:83:0x01a3, code skipped:
            if (r24 != false) goto L_0x01a5;
     */
        /* JADX WARNING: Missing block: B:84:0x01a5, code skipped:
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:85:0x01b6, code skipped:
            return doRequest(r20, r21, r22, r23, false);
     */
        /* JADX WARNING: Missing block: B:86:0x01b7, code skipped:
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:87:0x01b9, code skipped:
            r0 = e;
     */
        /* JADX WARNING: Missing block: B:88:0x01ba, code skipped:
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:90:0x01c7, code skipped:
            r0 = e;
     */
        /* JADX WARNING: Missing block: B:91:0x01c8, code skipped:
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:93:0x01d6, code skipped:
            r0 = e;
     */
        /* JADX WARNING: Missing block: B:94:0x01d7, code skipped:
            r14 = r6;
     */
        /* JADX WARNING: Missing block: B:96:0x01e4, code skipped:
            r0 = e;
     */
        /* JADX WARNING: Missing block: B:97:0x01e5, code skipped:
            r14 = r6;
     */
        private java.lang.Object doRequest(java.lang.String r20, miui.maml.data.WebServiceBinder.RequestMethod r21, boolean r22, boolean r23, boolean r24) {
            /*
            r19 = this;
            r7 = r19;
            r8 = r20;
            r0 = "WebServiceBinder";
            r1 = "doRequest";
            android.util.Log.i(r0, r1);
            r1 = 0;
            r2 = miui.maml.data.WebServiceBinder.this;
            r2 = r2.getContext();
            r9 = r2.mContext;
            r6 = android.accounts.AccountManager.get(r9);
            r2 = "com.xiaomi";
            r3 = 0;
            r4 = 0;
            if (r22 == 0) goto L_0x00d8;
        L_0x001e:
            r5 = miui.maml.data.WebServiceBinder.this;
            r5 = r5.mEncryptedUser;
            if (r5 == 0) goto L_0x002a;
        L_0x0024:
            r5 = miui.maml.data.WebServiceBinder.this;
            r5 = r5.mServiceToken;
            if (r5 != 0) goto L_0x00bd;
        L_0x002a:
            r5 = 0;
            r15 = r6.getAccountsByType(r2);
            r10 = r15.length;
            if (r10 <= 0) goto L_0x0034;
        L_0x0032:
            r5 = r15[r3];
        L_0x0034:
            if (r5 != 0) goto L_0x0043;
        L_0x0036:
            r2 = miui.maml.data.WebServiceBinder.this;
            r3 = 4;
            r2.setErrorCode(r3);
            r2 = "xiaomi account not login";
            android.util.Log.e(r0, r2);
            return r4;
        L_0x0043:
            r10 = miui.maml.data.WebServiceBinder.this;
            r11 = "encrypted_user_id";
            r11 = r6.getUserData(r5, r11);
            r10.mEncryptedUser = r11;
            r10 = miui.maml.data.WebServiceBinder.this;
            r12 = r10.mServiceId;
            r13 = 0;
            r14 = 1;
            r16 = 0;
            r17 = 0;
            r10 = r6;
            r11 = r5;
            r18 = r15;
            r15 = r16;
            r16 = r17;
            r10 = r10.getAuthToken(r11, r12, r13, r14, r15, r16);
            r11 = 0;
            if (r10 == 0) goto L_0x008a;
        L_0x0068:
            r12 = r10.getResult();	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
            r12 = (android.os.Bundle) r12;	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
            if (r12 == 0) goto L_0x007c;
        L_0x0070:
            r0 = "authtoken";
            r0 = r12.getString(r0);	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
            r13 = miui.maml.data.WebServiceBinder.AuthToken.parse(r0);	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
            r11 = r13;
            goto L_0x0081;
        L_0x007c:
            r13 = "getAuthToken: future getResult is null";
            android.util.Log.d(r0, r13);	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
        L_0x0081:
            goto L_0x008f;
        L_0x0082:
            r0 = move-exception;
            goto L_0x0090;
        L_0x0084:
            r0 = move-exception;
            goto L_0x0096;
        L_0x0086:
            r0 = move-exception;
            goto L_0x009c;
        L_0x0088:
            r0 = move-exception;
            goto L_0x00a2;
        L_0x008a:
            r12 = "getAuthToken: future is null";
            android.util.Log.d(r0, r12);	 Catch:{ OperationCanceledException -> 0x0088, AuthenticatorException -> 0x0086, IOException -> 0x0084, Exception -> 0x0082 }
        L_0x008f:
            goto L_0x00a8;
        L_0x0090:
            r12 = miui.maml.data.WebServiceBinder.this;
            r12.handleException(r0);
            goto L_0x00a8;
        L_0x0096:
            r12 = miui.maml.data.WebServiceBinder.this;
            r12.handleException(r0);
            goto L_0x008f;
        L_0x009c:
            r12 = miui.maml.data.WebServiceBinder.this;
            r12.handleException(r0);
            goto L_0x008f;
        L_0x00a2:
            r12 = miui.maml.data.WebServiceBinder.this;
            r12.handleException(r0);
            goto L_0x008f;
        L_0x00a8:
            if (r11 != 0) goto L_0x00b1;
        L_0x00aa:
            r0 = miui.maml.data.WebServiceBinder.this;
            r2 = 5;
            r0.setErrorCode(r2);
            return r4;
        L_0x00b1:
            r0 = miui.maml.data.WebServiceBinder.this;
            r12 = r11.authToken;
            r0.mServiceToken = r12;
            r0 = miui.maml.data.WebServiceBinder.this;
            r12 = r11.security;
            r0.mSecurity = r12;
        L_0x00bd:
            r0 = new java.util.HashMap;
            r0.<init>();
            r1 = r0;
            r0 = miui.maml.data.WebServiceBinder.this;
            r0 = r0.mEncryptedUser;
            r5 = "cUserId";
            r1.put(r5, r0);
            r0 = miui.maml.data.WebServiceBinder.this;
            r0 = r0.mServiceToken;
            r5 = "serviceToken";
            r1.put(r5, r0);
            r10 = r1;
            goto L_0x00d9;
        L_0x00d8:
            r10 = r1;
        L_0x00d9:
            r0 = new java.util.HashMap;
            r0.<init>();
            r11 = r0;
            r0 = miui.maml.data.WebServiceBinder.this;
            r0 = r0.mParamsFormatter;
            r12 = r0.getText();
            r0 = android.text.TextUtils.isEmpty(r12);
            r1 = 2;
            if (r0 != 0) goto L_0x0114;
        L_0x00f0:
            r0 = ",";
            r0 = r12.split(r0);
            r13 = r0.length;
            r14 = r3;
        L_0x00f8:
            if (r14 >= r13) goto L_0x0114;
        L_0x00fa:
            r15 = r0[r14];
            r4 = ":";
            r4 = r15.split(r4);
            r5 = r4.length;
            if (r5 == r1) goto L_0x0106;
        L_0x0105:
            goto L_0x010f;
        L_0x0106:
            r5 = r4[r3];
            r17 = 1;
            r3 = r4[r17];
            r11.put(r5, r3);
        L_0x010f:
            r14 = r14 + 1;
            r3 = 0;
            r4 = 0;
            goto L_0x00f8;
        L_0x0114:
            r0 = miui.maml.data.WebServiceBinder.this;	 Catch:{ IOException -> 0x01e4, CipherException -> 0x01d6, AccessDeniedException -> 0x01c7, InvalidResponseException -> 0x01b9, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r0 = r0.mProtocol;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r3 = miui.maml.data.WebServiceBinder.ResponseProtocol.BITMAP;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r0 != r3) goto L_0x012e;
        L_0x0120:
            r1 = 0;
            r0 = miui.maml.util.net.SimpleRequest.getAsStream(r8, r11, r1);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            if (r0 == 0) goto L_0x012d;
        L_0x0127:
            r1 = miui.maml.data.WebServiceBinder.this;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r1.setStatusCode(r4);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            return r0;
        L_0x012d:
            goto L_0x0171;
        L_0x012e:
            r0 = 0;
            r3 = miui.maml.data.WebServiceBinder.AnonymousClass1.$SwitchMap$miui$maml$data$WebServiceBinder$RequestMethod;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r5 = r21.ordinal();	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r3 = r3[r5];	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r5 = 1;
            if (r3 == r5) goto L_0x0151;
        L_0x013a:
            if (r3 == r1) goto L_0x013d;
        L_0x013c:
            goto L_0x0165;
        L_0x013d:
            if (r23 == 0) goto L_0x014a;
        L_0x013f:
            r1 = miui.maml.data.WebServiceBinder.this;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r1 = r1.mSecurity;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r3 = 1;
            r1 = miui.maml.util.net.SecureRequest.postAsString(r8, r11, r10, r3, r1);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r0 = r1;
            goto L_0x0165;
        L_0x014a:
            r1 = 1;
            r1 = miui.maml.util.net.SimpleRequest.postAsString(r8, r11, r10, r1);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r0 = r1;
            goto L_0x0165;
        L_0x0151:
            if (r23 == 0) goto L_0x015e;
        L_0x0153:
            r1 = miui.maml.data.WebServiceBinder.this;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r1 = r1.mSecurity;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r3 = 1;
            r1 = miui.maml.util.net.SecureRequest.getAsString(r8, r11, r10, r3, r1);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r0 = r1;
            goto L_0x0165;
        L_0x015e:
            r1 = 1;
            r1 = miui.maml.util.net.SimpleRequest.getAsString(r8, r11, r10, r1);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r0 = r1;
        L_0x0165:
            if (r0 == 0) goto L_0x0171;
        L_0x0167:
            r1 = miui.maml.data.WebServiceBinder.this;	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r1.setStatusCode(r4);	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            r1 = r0.getBody();	 Catch:{ IOException -> 0x017d, CipherException -> 0x017a, AccessDeniedException -> 0x0177, InvalidResponseException -> 0x0174, AuthenticationFailureException -> 0x018a, Exception -> 0x0181 }
            return r1;
        L_0x0171:
            r14 = r6;
            goto L_0x01f3;
        L_0x0174:
            r0 = move-exception;
            r14 = r6;
            goto L_0x01bb;
        L_0x0177:
            r0 = move-exception;
            r14 = r6;
            goto L_0x01c9;
        L_0x017a:
            r0 = move-exception;
            r14 = r6;
            goto L_0x01d8;
        L_0x017d:
            r0 = move-exception;
            r14 = r6;
            goto L_0x01e6;
        L_0x0181:
            r0 = move-exception;
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r14 = r6;
            goto L_0x01f3;
        L_0x018a:
            r0 = move-exception;
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r1 = miui.maml.data.WebServiceBinder.this;
            r3 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r1.setStatusCode(r3);
            r1 = miui.maml.data.WebServiceBinder.this;
            r1 = r1.mServiceToken;
            r6.invalidateAuthToken(r2, r1);
            r1 = miui.maml.data.WebServiceBinder.this;
            r2 = 0;
            r1.mServiceToken = r2;
            if (r24 == 0) goto L_0x01b7;
        L_0x01a5:
            r13 = 0;
            r1 = r19;
            r2 = r20;
            r3 = r21;
            r4 = r22;
            r5 = r23;
            r14 = r6;
            r6 = r13;
            r1 = r1.doRequest(r2, r3, r4, r5, r6);
            return r1;
        L_0x01b7:
            r14 = r6;
            goto L_0x01f2;
        L_0x01b9:
            r0 = move-exception;
            r14 = r6;
        L_0x01bb:
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r1 = miui.maml.data.WebServiceBinder.this;
            r2 = 7;
            r1.setErrorCode(r2);
            goto L_0x01f2;
        L_0x01c7:
            r0 = move-exception;
            r14 = r6;
        L_0x01c9:
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r1 = miui.maml.data.WebServiceBinder.this;
            r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
            r1.setStatusCode(r2);
            goto L_0x01f2;
        L_0x01d6:
            r0 = move-exception;
            r14 = r6;
        L_0x01d8:
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r1 = miui.maml.data.WebServiceBinder.this;
            r2 = 6;
            r1.setErrorCode(r2);
            goto L_0x01f2;
        L_0x01e4:
            r0 = move-exception;
            r14 = r6;
        L_0x01e6:
            r1 = miui.maml.data.WebServiceBinder.this;
            r1.handleException(r0);
            r1 = miui.maml.data.WebServiceBinder.this;
            r2 = 8;
            r1.setErrorCode(r2);
        L_0x01f3:
            r1 = 0;
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.WebServiceBinder$QueryThread.doRequest(java.lang.String, miui.maml.data.WebServiceBinder$RequestMethod, boolean, boolean, boolean):java.lang.Object");
        }

        private Object doRequest(String url, RequestMethod method, boolean auth, boolean secure) {
            return doRequest(url, method, auth, secure, true);
        }
    }

    enum RequestMethod {
        INVALID,
        POST,
        GET
    }

    enum ResponseProtocol {
        XML,
        JSONobj,
        JSONarray,
        BITMAP
    }

    public static class Variable extends miui.maml.data.VariableBinder.Variable {
        private boolean mCache;
        private String mInnerPath;
        public String mPath;

        public Variable(Element node, Variables var) {
            super(node, var);
            this.mPath = node.getAttribute("xpath");
            if (TextUtils.isEmpty(this.mPath)) {
                this.mPath = node.getAttribute("path");
            }
            this.mInnerPath = node.getAttribute("innerPath");
            this.mCache = Boolean.parseBoolean(node.getAttribute("cache"));
        }

        public void saveCache(InputStream response, String cacheDir) {
            String str = "WebServiceBinder";
            if (hasCache(cacheDir)) {
                File cacheDirFile = new File(cacheDir);
                if (!cacheDirFile.exists()) {
                    FileUtils.mkdirs(cacheDirFile, 493, -1, -1);
                }
                OutputStream fo;
                try {
                    File file = new File(cacheDir, getCacheName());
                    file.delete();
                    if (response != null) {
                        fo = new FileOutputStream(file, false);
                        byte[] buff = new byte[65536];
                        while (true) {
                            int read = response.read(buff, 0, 65536);
                            int read2 = read;
                            if (read > 0) {
                                fo.write(buff, 0, read2);
                            } else {
                                IOUtils.closeQuietly(fo);
                                return;
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    Log.e(str, e.toString());
                    e.printStackTrace();
                } catch (IOException e2) {
                    Log.e(str, e2.toString());
                    e2.printStackTrace();
                } catch (OutOfMemoryError e3) {
                    Log.e(str, e3.toString());
                    e3.printStackTrace();
                } catch (Throwable th) {
                    IOUtils.closeQuietly(fo);
                }
            }
        }

        private final String getCacheName() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".cache");
            return stringBuilder.toString();
        }

        private boolean hasCache(String cacheDir) {
            return this.mCache && !TextUtils.isEmpty(cacheDir);
        }

        public void loadCache(String cacheDir) {
            String str = "WebServiceBinder";
            if (this.mType == 7 && hasCache(cacheDir)) {
                InputStream is = null;
                Bitmap bm = null;
                try {
                    is = new FileInputStream(new File(cacheDir, getCacheName()));
                    str = BitmapFactory.decodeStream(is);
                    bm = str;
                } catch (FileNotFoundException e) {
                    Log.e(str, e.toString());
                } catch (OutOfMemoryError e2) {
                    Log.e(str, e2.toString());
                } catch (Throwable th) {
                    IOUtils.closeQuietly(is);
                }
                IOUtils.closeQuietly(is);
                set(bm);
            }
        }

        public void set(Object value) {
            if (isArray() && (value instanceof JSONArray)) {
                JSONArray arr = (JSONArray) value;
                Object obj = this.mVar.getVariables().get(this.mVar.getIndex());
                int count = 0;
                boolean isNumberArr = false;
                if (obj instanceof double[]) {
                    count = ((double[]) obj).length;
                    isNumberArr = true;
                } else if (obj instanceof String[]) {
                    count = ((String[]) obj).length;
                }
                for (int i = 0; i < count; i++) {
                    String object = null;
                    try {
                        if (i < arr.length()) {
                            object = arr.get(i);
                        }
                    } catch (JSONException e) {
                    }
                    double d = 0.0d;
                    Object s = null;
                    if (object != null) {
                        if (object != JSONObject.NULL && (object instanceof JSONObject)) {
                            String o = new JSONPath((JSONObject) object).get(this.mInnerPath);
                            if (o instanceof String) {
                                s = o;
                                if (isNumberArr) {
                                    try {
                                        d = Utils.parseDouble(s);
                                    } catch (NumberFormatException e2) {
                                    }
                                }
                            }
                        } else if (object instanceof String) {
                            s = object;
                            if (isNumberArr) {
                                try {
                                    d = Utils.parseDouble(s);
                                } catch (NumberFormatException e3) {
                                }
                            }
                        }
                    }
                    if (isNumberArr) {
                        this.mVar.setArr(i, d);
                    } else {
                        this.mVar.setArr(i, s);
                    }
                }
                return;
            }
            super.set(value);
        }
    }

    public WebServiceBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    public void init() {
        super.init();
        this.mEncryptedUser = null;
        this.mServiceToken = null;
        this.mSecurity = null;
        Expression expression = this.mUseNetworkExp;
        if (expression != null) {
            this.mUseNetwork = (int) expression.evaluate();
        }
        if (this.mVariables.size() > 0) {
            ((Variable) this.mVariables.get(0)).loadCache(this.mRoot.getCacheDir());
        }
        if (this.mQueryAtStart) {
            this.mLastQueryTime = 0;
            tryStartQuery();
        }
    }

    public void finish() {
        super.finish();
    }

    public void pause() {
        super.pause();
    }

    public void resume() {
        super.resume();
        if (this.mQueryAtStart) {
            tryStartQuery();
        }
    }

    public void refresh() {
        super.refresh();
        startQuery();
    }

    private void tryStartQuery() {
        long time = System.currentTimeMillis() - this.mLastQueryTime;
        if (time < 0) {
            this.mLastQueryTime = 0;
        }
        if (this.mLastQueryTime != 0) {
            int i = this.mUpdateInterval;
            if (i <= 0 || time <= ((long) (i * 1000))) {
                return;
            }
        }
        startQuery();
    }

    private void load(Element node) {
        String str = "WebServiceBinder";
        if (node != null) {
            if ("get".equalsIgnoreCase(node.getAttribute("requestMethod"))) {
                this.mRequestMethod = RequestMethod.GET;
            }
            this.mQueryAtStart = Boolean.parseBoolean(node.getAttribute("queryAtStart"));
            Variables vars = getVariables();
            Variables variables = vars;
            this.mUriFormatter = new TextFormatter(variables, node.getAttribute("uri"), node.getAttribute("uriFormat"), node.getAttribute("uriParas"), Expression.build(vars, node.getAttribute("uriExp")), Expression.build(vars, node.getAttribute("uriFormatExp")));
            this.mParamsFormatter = new TextFormatter(vars, node.getAttribute("params"), node.getAttribute("paramsFormat"), node.getAttribute("paramsParas"));
            this.mUpdateInterval = Utils.getAttrAsInt(node, "updateInterval", -1);
            parseProtocol(node.getAttribute("protocol"));
            this.mAuthentication = Boolean.parseBoolean(node.getAttribute("authentication"));
            this.mSecure = Boolean.parseBoolean(node.getAttribute("secure"));
            this.mServiceId = node.getAttribute("serviceID");
            String useNetwork = node.getAttribute("useNetwork");
            if (TextUtils.isEmpty(useNetwork) || "all".equalsIgnoreCase(useNetwork)) {
                this.mUseNetwork = 2;
            } else if ("wifi".equalsIgnoreCase(useNetwork)) {
                this.mUseNetwork = 1;
            } else if ("none".equalsIgnoreCase(useNetwork)) {
                this.mUseNetwork = 0;
            } else {
                this.mUseNetworkExp = Expression.build(vars, useNetwork);
            }
            loadVariables(node);
            if (!TextUtils.isEmpty(this.mName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".statusCode");
                this.mStatusCodeVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".errorCode");
                this.mErrorCodeVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(this.mName);
                stringBuilder2.append(".errorString");
                this.mErrorStringVar = new IndexedVariable(stringBuilder2.toString(), vars, false);
                if (Boolean.parseBoolean(node.getAttribute("dbgContentString"))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mName);
                    stringBuilder.append(".contentString");
                    this.mContentStringVar = new IndexedVariable(stringBuilder.toString(), vars, false);
                }
            }
            Element list = Utils.getChild(node, ListScreenElement.TAG_NAME);
            if (list != null) {
                try {
                    this.mList = new List(list, this.mRoot);
                    return;
                } catch (IllegalArgumentException e) {
                    Log.e(str, "invalid List");
                    return;
                }
            }
            return;
        }
        Log.e(str, "WebServiceBinder node is null");
        throw new NullPointerException("node is null");
    }

    private void parseProtocol(String protocol) {
        if ("xml".equalsIgnoreCase(protocol)) {
            this.mProtocol = ResponseProtocol.XML;
        } else if ("json/obj".equalsIgnoreCase(protocol)) {
            this.mProtocol = ResponseProtocol.JSONobj;
        } else if ("json/array".equalsIgnoreCase(protocol)) {
            this.mProtocol = ResponseProtocol.JSONarray;
        } else if ("bitmap".equalsIgnoreCase(protocol)) {
            this.mProtocol = ResponseProtocol.BITMAP;
        }
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }

    public void startQuery() {
        String str = "WebServiceBinder";
        if (!this.mRoot.getCapability(1)) {
            Log.w(str, "capability disabled: webservice");
        } else if (!this.mQueryInProgress) {
            this.mLastQueryTime = System.currentTimeMillis();
            if (canUseNetwork()) {
                this.mQueryInProgress = true;
                this.mQueryThread = new QueryThread();
                this.mQueryThread.start();
                return;
            }
            IndexedVariable indexedVariable = this.mErrorCodeVar;
            if (indexedVariable != null) {
                indexedVariable.set(3.0d);
            }
            indexedVariable = this.mErrorStringVar;
            String str2 = "cancel query because current network is forbidden by useNetwork config: ";
            if (indexedVariable != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(this.mUseNetwork);
                indexedVariable.set(stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(this.mUseNetwork);
            Log.w(str, stringBuilder2.toString());
        }
    }

    private boolean canUseNetwork() {
        int i = this.mUseNetwork;
        if (i == 2) {
            return true;
        }
        if (i != 0 && i == 1 && ConnectivityHelper.getInstance().isWifiConnected()) {
            return true;
        }
        return false;
    }

    private void processResponseXml(String response) {
        String str;
        String str2;
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputStream is = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            is = new ByteArrayInputStream(response.getBytes(SimpleRequest.UTF8));
            Document doc = db.parse(is);
            Iterator it = this.mVariables.iterator();
            while (true) {
                str = " :";
                str2 = "WebServiceBinder";
                if (!it.hasNext()) {
                    break;
                }
                Variable v = (Variable) ((miui.maml.data.VariableBinder.Variable) it.next());
                try {
                    v.set(xpath.evaluate(v.mPath, doc, XPathConstants.STRING));
                } catch (XPathExpressionException e) {
                    v.set(null);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fail to get variable: ");
                    stringBuilder.append(v.mName);
                    stringBuilder.append(str);
                    stringBuilder.append(e.toString());
                    Log.e(str2, stringBuilder.toString());
                }
            }
            if (this.mList != null) {
                try {
                    this.mList.fill((NodeList) xpath.evaluate(this.mList.mDataPath, doc, XPathConstants.NODESET));
                } catch (XPathExpressionException e2) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("fail to get list: ");
                    stringBuilder2.append(this.mList.mName);
                    stringBuilder2.append(str);
                    stringBuilder2.append(e2.toString());
                    Log.e(str2, stringBuilder2.toString());
                }
            }
            try {
                is.close();
            } catch (IOException e3) {
            }
        } catch (ParserConfigurationException e4) {
            handleException(e4);
            if (is != null) {
                is.close();
            }
        } catch (SAXException e5) {
            handleException(e5);
            if (is != null) {
                is.close();
            }
        } catch (UnsupportedEncodingException e6) {
            handleException(e6);
            if (is != null) {
                is.close();
            }
        } catch (IOException e7) {
            handleException(e7);
            if (is != null) {
                is.close();
            }
        } catch (Exception e8) {
            handleException(e8);
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e9) {
                }
            }
        }
    }

    private void handleException(Exception e) {
        Log.e("WebServiceBinder", e.toString());
        e.printStackTrace();
        setErrorString(e.toString());
    }

    private void processResponseJson(String response) {
        try {
            JSONPath jpath;
            if (this.mProtocol == ResponseProtocol.JSONobj) {
                jpath = new JSONPath(new JSONObject(response));
            } else {
                jpath = new JSONPath(new JSONArray(response));
            }
            Iterator it = this.mVariables.iterator();
            while (it.hasNext()) {
                Variable v = (Variable) ((miui.maml.data.VariableBinder.Variable) it.next());
                v.set(jpath.get(v.mPath));
            }
            if (this.mList != null) {
                Object obj = jpath.get(this.mList.mDataPath);
                if (obj != null && (obj instanceof JSONArray)) {
                    this.mList.fill((JSONArray) obj);
                }
            }
        } catch (JSONException e) {
            handleException(e);
        }
    }

    public void processResponseBitmap(StreamContent response) {
        String str = "WebServiceBinder";
        if (this.mVariables.size() < 1) {
            Log.w(str, "no image element var");
            return;
        }
        InputStream is = response.getStream();
        Variable v = (Variable) this.mVariables.get(0);
        String cacheDir = this.mRoot.getCacheDir();
        if (v.hasCache(cacheDir)) {
            v.saveCache(is, cacheDir);
            v.loadCache(cacheDir);
        } else {
            Bitmap bitmap = null;
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
                if (bitmap == null) {
                    Log.w(str, "decoded bitmap is null");
                }
            } else {
                Log.w(str, "response stream is null");
            }
            v.set(bitmap);
        }
        IOUtils.closeQuietly(is);
    }

    private void setErrorCode(int code) {
        IndexedVariable indexedVariable = this.mErrorCodeVar;
        if (indexedVariable != null) {
            indexedVariable.set((double) code);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("QueryThread error: #");
        stringBuilder.append(code);
        Log.e("WebServiceBinder", stringBuilder.toString());
    }

    private void setErrorString(String str) {
        IndexedVariable indexedVariable = this.mErrorStringVar;
        if (indexedVariable != null) {
            indexedVariable.set((Object) str);
        }
    }

    private void setStatusCode(int code) {
        IndexedVariable indexedVariable = this.mStatusCodeVar;
        if (indexedVariable != null) {
            indexedVariable.set((double) code);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("QueryThread status code: #");
        stringBuilder.append(code);
        Log.d("WebServiceBinder", stringBuilder.toString());
    }
}
