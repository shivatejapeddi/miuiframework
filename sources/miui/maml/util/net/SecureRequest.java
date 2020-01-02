package miui.maml.util.net;

import android.telecom.Logging.Session;
import android.util.Base64;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import miui.maml.util.net.SimpleRequest.MapContent;
import miui.maml.util.net.SimpleRequest.StringContent;

public class SecureRequest {
    private static final String UTF8 = "utf-8";

    public static StringContent getAsString(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody, String security) throws IOException, CipherException, AccessDeniedException, InvalidResponseException, AuthenticationFailureException {
        return processStringResponse(SimpleRequest.getAsString(url, encryptParams("GET", url, params, security), cookies, readBody), security);
    }

    public static MapContent getAsMap(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody, String security) throws IOException, CipherException, AccessDeniedException, InvalidResponseException, AuthenticationFailureException {
        return SimpleRequest.convertStringToMap(getAsString(url, params, cookies, readBody, security));
    }

    public static StringContent postAsString(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody, String security) throws IOException, CipherException, AccessDeniedException, InvalidResponseException, AuthenticationFailureException {
        return processStringResponse(SimpleRequest.postAsString(url, encryptParams("POST", url, params, security), cookies, readBody), security);
    }

    public static MapContent postAsMap(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody, String security) throws IOException, AccessDeniedException, InvalidResponseException, CipherException, AuthenticationFailureException {
        return SimpleRequest.convertStringToMap(postAsString(url, params, cookies, readBody, security));
    }

    private static StringContent processStringResponse(StringContent stringResponse, String security) throws IOException, InvalidResponseException, CipherException {
        if (stringResponse != null) {
            String body = stringResponse.getBody();
            if (body != null) {
                StringContent resultContent = new StringContent(decryptResponse(body, security));
                resultContent.putHeaders(stringResponse.getHeaders());
                return resultContent;
            }
            throw new InvalidResponseException("invalid response from server");
        }
        throw new IOException("no response from server");
    }

    public static Map<String, String> encryptParams(String method, String url, Map<String, String> params, String security) throws CipherException {
        Cipher cipher = CloudCoder.newAESCipher(security, 1);
        if (cipher != null) {
            HashMap<String, String> requestParams = new HashMap();
            if (!(params == null || params.isEmpty())) {
                for (Entry<String, String> entry : params.entrySet()) {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (!(key == null || value == null)) {
                        if (!key.startsWith(Session.SESSION_SEPARATION_CHAR_CHILD)) {
                            try {
                                value = Base64.encodeToString(cipher.doFinal(value.getBytes("utf-8")), 2);
                            } catch (Exception e) {
                                throw new CipherException("failed to encrypt request params", e);
                            }
                        }
                        requestParams.put(key, value);
                    }
                }
            }
            requestParams.put("signature", CloudCoder.generateSignature(method, url, requestParams, security));
            return requestParams;
        }
        throw new CipherException("failed to init cipher");
    }

    private static String decryptResponse(String body, String security) throws CipherException, InvalidResponseException {
        Cipher cipher = CloudCoder.newAESCipher(security, 2);
        if (cipher != null) {
            String responseData = null;
            try {
                responseData = new String(cipher.doFinal(Base64.decode(body, 2)), "utf-8");
            } catch (Exception e) {
            }
            if (responseData != null) {
                return responseData;
            }
            throw new InvalidResponseException("failed to decrypt response");
        }
        throw new CipherException("failed to init cipher");
    }
}
