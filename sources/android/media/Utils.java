package android.media;

import android.content.Context;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

class Utils {
    private static final String TAG = "Utils";

    Utils() {
    }

    public static <T extends Comparable<? super T>> void sortDistinctRanges(Range<T>[] ranges) {
        Arrays.sort(ranges, new Comparator<Range<T>>() {
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sample rate ranges must be distinct (");
                stringBuilder.append(lhs);
                stringBuilder.append(" and ");
                stringBuilder.append(rhs);
                stringBuilder.append(")");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        });
    }

    public static <T extends Comparable<? super T>> Range<T>[] intersectSortedDistinctRanges(Range<T>[] one, Range<T>[] another) {
        int ix = 0;
        Vector<Range<T>> result = new Vector();
        for (Range<T> range : another) {
            while (ix < one.length && one[ix].getUpper().compareTo(range.getLower()) < 0) {
                ix++;
            }
            while (ix < one.length && one[ix].getUpper().compareTo(range.getUpper()) < 0) {
                result.add(range.intersect(one[ix]));
                ix++;
            }
            if (ix == one.length) {
                break;
            }
            if (one[ix].getLower().compareTo(range.getUpper()) <= 0) {
                result.add(range.intersect(one[ix]));
            }
        }
        return (Range[]) result.toArray(new Range[result.size()]);
    }

    public static <T extends Comparable<? super T>> int binarySearchDistinctRanges(Range<T>[] ranges, T value) {
        return Arrays.binarySearch(ranges, Range.create(value, value), new Comparator<Range<T>>() {
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                return 0;
            }
        });
    }

    static int gcd(int a, int b) {
        if (a == 0 && b == 0) {
            return 1;
        }
        if (b < 0) {
            b = -b;
        }
        if (a < 0) {
            a = -a;
        }
        while (a != 0) {
            int c = b % a;
            b = a;
            a = c;
        }
        return b;
    }

    static Range<Integer> factorRange(Range<Integer> range, int factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Integer.valueOf(divUp(((Integer) range.getLower()).intValue(), factor)), Integer.valueOf(((Integer) range.getUpper()).intValue() / factor));
    }

    static Range<Long> factorRange(Range<Long> range, long factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Long.valueOf(divUp(((Long) range.getLower()).longValue(), factor)), Long.valueOf(((Long) range.getUpper()).longValue() / factor));
    }

    private static Rational scaleRatio(Rational ratio, int num, int den) {
        int common = gcd(num, den);
        return new Rational((int) (((double) ratio.getNumerator()) * ((double) (num / common))), (int) (((double) ratio.getDenominator()) * ((double) (den / common))));
    }

    static Range<Rational> scaleRange(Range<Rational> range, int num, int den) {
        if (num == den) {
            return range;
        }
        return Range.create(scaleRatio((Rational) range.getLower(), num, den), scaleRatio((Rational) range.getUpper(), num, den));
    }

    static Range<Integer> alignRange(Range<Integer> range, int align) {
        return range.intersect(Integer.valueOf(divUp(((Integer) range.getLower()).intValue(), align) * align), Integer.valueOf((((Integer) range.getUpper()).intValue() / align) * align));
    }

    static int divUp(int num, int den) {
        return ((num + den) - 1) / den;
    }

    static long divUp(long num, long den) {
        return ((num + den) - 1) / den;
    }

    private static long lcm(int a, int b) {
        if (a != 0 && b != 0) {
            return (((long) a) * ((long) b)) / ((long) gcd(a, b));
        }
        throw new IllegalArgumentException("lce is not defined for zero arguments");
    }

    static Range<Integer> intRangeFor(double v) {
        return Range.create(Integer.valueOf((int) v), Integer.valueOf((int) Math.ceil(v)));
    }

    static Range<Long> longRangeFor(double v) {
        return Range.create(Long.valueOf((long) v), Long.valueOf((long) Math.ceil(v)));
    }

    static Size parseSize(Object o, Size fallback) {
        try {
            return Size.parseSize((String) o);
        } catch (ClassCastException | NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse size '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        }
    }

    static int parseIntSafely(Object o, int fallback) {
        if (o == null) {
            return fallback;
        }
        try {
            return Integer.parseInt((String) o);
        } catch (ClassCastException | NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse integer '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        }
    }

    static Range<Integer> parseIntRange(Object o, Range<Integer> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Integer.valueOf(Integer.parseInt(s.substring(0, ix), 10)), Integer.valueOf(Integer.parseInt(s.substring(ix + 1), 10)));
            }
            int value = Integer.parseInt(s);
            return Range.create(Integer.valueOf(value), Integer.valueOf(value));
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse integer range '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return fallback;
        }
    }

    static Range<Long> parseLongRange(Object o, Range<Long> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Long.valueOf(Long.parseLong(s.substring(0, ix), 10)), Long.valueOf(Long.parseLong(s.substring(ix + 1), 10)));
            }
            long value = Long.parseLong(s);
            return Range.create(Long.valueOf(value), Long.valueOf(value));
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse long range '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return fallback;
        }
    }

    static Range<Rational> parseRationalRange(Object o, Range<Rational> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Rational.parseRational(s.substring(0, ix)), Rational.parseRational(s.substring(ix + 1)));
            }
            Rational value = Rational.parseRational(s);
            return Range.create(value, value);
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse rational range '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return fallback;
        }
    }

    static Pair<Size, Size> parseSizeRange(Object o) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Pair.create(Size.parseSize(s.substring(0, ix)), Size.parseSize(s.substring(ix + 1)));
            }
            Size value = Size.parseSize(s);
            return Pair.create(value, value);
        } catch (NullPointerException e) {
            return null;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not parse size range '");
            stringBuilder.append(o);
            stringBuilder.append("'");
            Log.w(TAG, stringBuilder.toString());
            return null;
        }
    }

    public static File getUniqueExternalFile(Context context, String subdirectory, String fileName, String mimeType) {
        File externalStorage = Environment.getExternalStoragePublicDirectory(subdirectory);
        externalStorage.mkdirs();
        try {
            return FileUtils.buildUniqueFile(externalStorage, mimeType, fileName);
        } catch (FileNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get a unique file name: ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:20:0x0049, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:21:0x004a, code skipped:
            r1.addSuppressed(r5);
     */
    static java.lang.String getFileDisplayNameFromUri(android.content.Context r8, android.net.Uri r9) {
        /*
        r0 = r9.getScheme();
        r1 = "file";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0011;
    L_0x000c:
        r1 = r9.getLastPathSegment();
        return r1;
    L_0x0011:
        r1 = "content";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0053;
    L_0x0019:
        r1 = "_display_name";
        r4 = new java.lang.String[]{r1};
        r2 = r8.getContentResolver();
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r3 = r9;
        r2 = r2.query(r3, r4, r5, r6, r7);
        if (r2 == 0) goto L_0x004e;
    L_0x002d:
        r3 = r2.getCount();	 Catch:{ all -> 0x0042 }
        if (r3 == 0) goto L_0x004e;
    L_0x0033:
        r2.moveToFirst();	 Catch:{ all -> 0x0042 }
        r1 = r2.getColumnIndex(r1);	 Catch:{ all -> 0x0042 }
        r1 = r2.getString(r1);	 Catch:{ all -> 0x0042 }
        r2.close();
        return r1;
    L_0x0042:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0044 }
    L_0x0044:
        r3 = move-exception;
        r2.close();	 Catch:{ all -> 0x0049 }
        goto L_0x004d;
    L_0x0049:
        r5 = move-exception;
        r1.addSuppressed(r5);
    L_0x004d:
        throw r3;
    L_0x004e:
        if (r2 == 0) goto L_0x0053;
    L_0x0050:
        r2.close();
    L_0x0053:
        r1 = r9.toString();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Utils.getFileDisplayNameFromUri(android.content.Context, android.net.Uri):java.lang.String");
    }
}
