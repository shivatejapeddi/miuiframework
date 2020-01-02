package miui.maml.util.net;

import android.net.Uri;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CloudCoder {
    private static final Integer INT_0 = Integer.valueOf(0);
    private static final String RC4_ALGORITHM_NAME = "RC4";

    public static Cipher newAESCipher(String aesKey, int opMode) {
        SecretKeySpec keySpec = new SecretKeySpec(Base64.decode(aesKey, (int) 2), KeyProperties.KEY_ALGORITHM_AES);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(opMode, keySpec, new IvParameterSpec("0102030405060708".getBytes()));
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvalidKeyException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static String hash4SHA1(String plain) {
        try {
            return Base64.encodeToString(MessageDigest.getInstance("SHA1").digest(plain.getBytes("UTF-8")), 2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException("failed to SHA1");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            throw new IllegalStateException("failed to SHA1");
        }
    }

    public static String generateSignature(String method, String requestUrl, Map<String, String> params, String security) {
        if (TextUtils.isEmpty(security)) {
            throw new InvalidParameterException("security is not nullable");
        }
        List<String> exps = new ArrayList();
        if (method != null) {
            exps.add(method.toUpperCase());
        }
        if (requestUrl != null) {
            exps.add(Uri.parse(requestUrl).getEncodedPath());
        }
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : new TreeMap(params).entrySet()) {
                exps.add(String.format("%s=%s", new Object[]{entry.getKey(), entry.getValue()}));
            }
        }
        exps.add(security);
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (String s : exps) {
            if (!first) {
                sb.append('&');
            }
            sb.append(s);
            first = false;
        }
        return hash4SHA1(sb.toString());
    }
}
