package com.miui.mishare.app.util;

import android.app.slice.SliceItem;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;
import miui.maml.util.net.SimpleRequest;

public class MiShareFileHelper {
    public static boolean isImageCanPrint(Context context, Uri uri) {
        String fileExtension = getFileExtensionFromUri(context, uri);
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") || fileExtension.equals("gif") || fileExtension.equals("bmp") || fileExtension.equals("webp") || fileExtension.equals("wbmp");
    }

    public static boolean isOfficeFile(Context context, Uri uri) {
        String fileExtension = getFileExtensionFromUri(context, uri);
        return fileExtension.equals("doc") || fileExtension.equals("docx") || fileExtension.equals("xls") || fileExtension.equals("xlsx") || fileExtension.equals("ppt") || fileExtension.equals("pptx");
    }

    public static boolean isVideoCanScreenThrow(Context context, Uri uri) {
        String fileExtension = getFileExtensionFromUri(context, uri);
        return fileExtension.equals("3gp") || fileExtension.equals("mp4") || fileExtension.equals("mov");
    }

    public static boolean isFilePdf(Context context, Uri uri) {
        return getFileExtensionFromUri(context, uri).equals("pdf");
    }

    public static boolean isFileImageOverview(Context context, Uri uri) {
        String fileExtension = getFileExtensionFromUri(context, uri);
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") || fileExtension.equals("gif") || fileExtension.equals("bmp") || fileExtension.equals("webp") || fileExtension.equals("wbmp");
    }

    public static boolean isAllImageOverview(Context context, List<Uri> files) {
        if (files == null) {
            return false;
        }
        for (Uri uri : files) {
            if (uri == null || !isFileImageOverview(context, uri)) {
                return false;
            }
        }
        return true;
    }

    public static String getFileNameFromUri(Context context, Uri uri) {
        if (!(uri == null || TextUtils.isEmpty(uri.toString()))) {
            String url = uri.toString();
            int fragment = url.lastIndexOf(35);
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            int query = url.lastIndexOf(63);
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf(47);
            String fileName = filenamePos >= 0 ? url.substring(filenamePos + 1) : url;
            if (!TextUtils.isEmpty(fileName)) {
                try {
                    return URLDecoder.decode(fileName, SimpleRequest.UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getFileExtensionFromUri(Context context, Uri uri) {
        if (!(uri == null || TextUtils.isEmpty(uri.toString()))) {
            String url = uri.toString();
            int fragment = url.lastIndexOf(35);
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            int query = url.lastIndexOf(63);
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf(47);
            String filename = filenamePos >= 0 ? url.substring(filenamePos + 1) : url;
            if (!filename.isEmpty() && Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf(46);
                if (dotPos >= 0) {
                    return filename.substring(dotPos + 1).toLowerCase();
                }
            }
        }
        return "";
    }

    public static String getPath(Context context, Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String str = ":";
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(str);
                if (DocumentsContract.EXTERNAL_STORAGE_PRIMARY_EMULATED_ROOT_ID.equalsIgnoreCase(split[0])) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Environment.getExternalStorageDirectory());
                    stringBuilder.append("/");
                    stringBuilder.append(split[1]);
                    return stringBuilder.toString();
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (isMediaDocument(uri)) {
                str = DocumentsContract.getDocumentId(uri).split(str)[0];
                Uri contentUri = null;
                if (SliceItem.FORMAT_IMAGE.equals(str)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        int column_index = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{column_index}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow(column_index));
            cursor.close();
            return string;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return DocumentsContract.EXTERNAL_STORAGE_PROVIDER_AUTHORITY.equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
