package com.android.id.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.android.id.IdProvider;
import miui.util.IOUtils;

public class IdProviderImpl implements IdProvider {
    private static final String AUTHORITY = "content://com.miui.idprovider";
    private static final String COLUMN_NAME = "uniform_id";
    private static final String PATH_AAID = "/aaid";
    private static final String PATH_OAID = "/oaid";
    private static final String PATH_UDID = "/udid";
    private static final String PATH_VAID = "/vaid";
    private static final String TAG = "IdProviderImpl";

    public String getUDID(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        String str2 = TAG;
        Log.d(str2, "getUDID");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.miui.idprovider/udid"), null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            try {
                str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                return str;
            } catch (Exception e) {
                Log.e(str2, "get UDID exception", e);
            } finally {
                IOUtils.closeQuietly(cursor);
            }
        }
        return str;
    }

    public String getOAID(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        String str2 = TAG;
        Log.d(str2, "getOAID");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.miui.idprovider/oaid"), null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            try {
                str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                return str;
            } catch (Exception e) {
                Log.e(str2, "get OAID exception", e);
            } finally {
                IOUtils.closeQuietly(cursor);
            }
        }
        return str;
    }

    public String getVAID(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        String str2 = TAG;
        Log.d(str2, "getVAID");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.miui.idprovider/vaid"), null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            try {
                str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                return str;
            } catch (Exception e) {
                Log.e(str2, "get VAID exception", e);
            } finally {
                IOUtils.closeQuietly(cursor);
            }
        }
        return str;
    }

    public String getAAID(Context context) {
        String str = null;
        if (context == null) {
            return null;
        }
        String str2 = TAG;
        Log.d(str2, "getAAID");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.miui.idprovider/aaid"), null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            try {
                str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                return str;
            } catch (Exception e) {
                Log.e(str2, "get AAID exception", e);
            } finally {
                IOUtils.closeQuietly(cursor);
            }
        }
        return str;
    }
}
