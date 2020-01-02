package android.gesture;

import android.graphics.RectF;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public final class GestureUtils {
    private static final float NONUNIFORM_SCALE = ((float) Math.sqrt(2.0d));
    private static final float SCALING_THRESHOLD = 0.26f;

    private GestureUtils() {
    }

    static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(GestureConstants.LOG_TAG, "Could not close stream", e);
            }
        }
    }

    public static float[] spatialSampling(Gesture gesture, int bitmapSize) {
        return spatialSampling(gesture, bitmapSize, false);
    }

    public static float[] spatialSampling(Gesture gesture, int bitmapSize, boolean keepAspectRatio) {
        float scale;
        float scale2;
        int i = bitmapSize;
        float targetPatchSize = (float) (i - 1);
        float[] sample = new float[(i * i)];
        Arrays.fill(sample, 0.0f);
        RectF rect = gesture.getBoundingBox();
        float gestureWidth = rect.width();
        float gestureHeight = rect.height();
        float sx = targetPatchSize / gestureWidth;
        float sy = targetPatchSize / gestureHeight;
        if (keepAspectRatio) {
            scale = sx < sy ? sx : sy;
            sx = scale;
            sy = scale;
        } else {
            scale = gestureWidth / gestureHeight;
            if (scale > 1.0f) {
                scale = 1.0f / scale;
            }
            if (scale < SCALING_THRESHOLD) {
                scale2 = sx < sy ? sx : sy;
                sx = scale2;
                sy = scale2;
            } else if (sx > sy) {
                scale2 = NONUNIFORM_SCALE * sy;
                if (scale2 < sx) {
                    sx = scale2;
                }
            } else {
                scale2 = NONUNIFORM_SCALE * sx;
                if (scale2 < sy) {
                    sy = scale2;
                }
            }
        }
        scale = -rect.centerX();
        scale2 = -rect.centerY();
        float postDx = targetPatchSize / 2.0f;
        float postDy = targetPatchSize / 2.0f;
        ArrayList<GestureStroke> strokes = gesture.getStrokes();
        int count = strokes.size();
        int index = 0;
        while (index < count) {
            int i2;
            float segmentStartY;
            int size;
            float preDy;
            float[] pts;
            GestureStroke stroke = (GestureStroke) strokes.get(index);
            RectF rect2 = rect;
            float[] strokepoints = stroke.points;
            int size2 = strokepoints.length;
            float gestureWidth2 = gestureWidth;
            float[] pts2 = new float[size2];
            float gestureHeight2 = gestureHeight;
            for (i2 = 0; i2 < size2; i2 += 2) {
                pts2[i2] = ((strokepoints[i2] + scale) * sx) + postDx;
                pts2[i2 + 1] = ((strokepoints[i2 + 1] + scale2) * sy) + postDy;
            }
            float segmentEndX = -1.0f;
            i2 = 0;
            float sx2 = sx;
            sx = -1.0f;
            while (i2 < size2) {
                float segmentStartX;
                float segmentStartX2 = pts2[i2] < 0.0f ? 0.0f : pts2[i2];
                segmentStartY = pts2[i2 + 1] < 0.0f ? 0.0f : pts2[i2 + 1];
                if (segmentStartX2 > targetPatchSize) {
                    size = size2;
                    segmentStartX = targetPatchSize;
                } else {
                    size = size2;
                    segmentStartX = segmentStartX2;
                }
                if (segmentStartY > targetPatchSize) {
                    segmentStartY = targetPatchSize;
                    segmentStartX2 = targetPatchSize;
                } else {
                    segmentStartX2 = targetPatchSize;
                    targetPatchSize = segmentStartY;
                }
                plot(segmentStartX, targetPatchSize, sample, i);
                if (segmentEndX != -1.0f) {
                    if (segmentEndX > segmentStartX) {
                        segmentStartY = scale;
                        preDy = scale2;
                        scale = (float) Math.ceil((double) segmentStartX);
                        scale2 = (sx - targetPatchSize) / (segmentEndX - segmentStartX);
                        while (scale < segmentEndX) {
                            pts = pts2;
                            plot(scale, ((scale - segmentStartX) * scale2) + targetPatchSize, sample, i);
                            scale += 1.0f;
                            pts2 = pts;
                        }
                        pts = pts2;
                    } else {
                        pts = pts2;
                        segmentStartY = scale;
                        preDy = scale2;
                        if (segmentEndX < segmentStartX) {
                            scale = (sx - targetPatchSize) / (segmentEndX - segmentStartX);
                            for (pts2 = (float) Math.ceil((double) segmentEndX); pts2 < segmentStartX; pts2 += 1.0f) {
                                plot(pts2, ((pts2 - segmentStartX) * scale) + targetPatchSize, sample, i);
                            }
                        }
                    }
                    if (sx > targetPatchSize) {
                        scale = (segmentEndX - segmentStartX) / (sx - targetPatchSize);
                        for (pts2 = (float) Math.ceil((double) targetPatchSize); pts2 < sx; pts2 += 1.0f) {
                            plot(((pts2 - targetPatchSize) * scale) + segmentStartX, pts2, sample, i);
                        }
                    } else if (sx < targetPatchSize) {
                        scale = (segmentEndX - segmentStartX) / (sx - targetPatchSize);
                        for (pts2 = (float) Math.ceil((double) sx); pts2 < targetPatchSize; pts2 += 1.0f) {
                            plot(((pts2 - targetPatchSize) * scale) + segmentStartX, pts2, sample, i);
                        }
                    }
                } else {
                    pts = pts2;
                    segmentStartY = scale;
                    preDy = scale2;
                }
                segmentEndX = segmentStartX;
                sx = targetPatchSize;
                i2 += 2;
                targetPatchSize = segmentStartX2;
                scale = segmentStartY;
                size2 = size;
                scale2 = preDy;
                pts2 = pts;
            }
            size = size2;
            pts = pts2;
            segmentStartY = scale;
            preDy = scale2;
            index++;
            rect = rect2;
            Object obj = null;
            gestureWidth = gestureWidth2;
            gestureHeight = gestureHeight2;
            sx = sx2;
        }
        return sample;
    }

    private static void plot(float x, float y, float[] sample, int sampleSize) {
        float y2 = 0.0f;
        float x2 = x < 0.0f ? 0.0f : x;
        if (y >= 0.0f) {
            y2 = y;
        }
        int xFloor = (int) Math.floor((double) x2);
        int xCeiling = (int) Math.ceil((double) x2);
        int yFloor = (int) Math.floor((double) y2);
        int yCeiling = (int) Math.ceil((double) y2);
        if (x2 == ((float) xFloor) && y2 == ((float) yFloor)) {
            int index = (yCeiling * sampleSize) + xCeiling;
            if (sample[index] < 1.0f) {
                sample[index] = 1.0f;
            }
            y = y2;
            x = x2;
            return;
        }
        double xFloorSq = Math.pow((double) (((float) xFloor) - x2), 2.0d);
        double yFloorSq = Math.pow((double) (((float) yFloor) - y2), 2.0d);
        double xCeilingSq = Math.pow((double) (((float) xCeiling) - x2), 2.0d);
        double yCeilingSq = Math.pow((double) (((float) yCeiling) - y2), 2.0d);
        float topLeft = (float) Math.sqrt(xFloorSq + yFloorSq);
        y = y2;
        x = x2;
        y2 = (float) Math.sqrt(xCeilingSq + yFloorSq);
        x2 = (float) Math.sqrt(xFloorSq + yCeilingSq);
        float btmRight = (float) Math.sqrt(xCeilingSq + yCeilingSq);
        float sum = ((topLeft + y2) + x2) + btmRight;
        float value = topLeft / sum;
        int index2 = (yFloor * sampleSize) + xFloor;
        if (value > sample[index2]) {
            sample[index2] = value;
        }
        value = y2 / sum;
        int index3 = (yFloor * sampleSize) + xCeiling;
        if (value > sample[index3]) {
            sample[index3] = value;
        }
        value = x2 / sum;
        index2 = (yCeiling * sampleSize) + xFloor;
        if (value > sample[index2]) {
            sample[index2] = value;
        }
        value = btmRight / sum;
        index3 = (yCeiling * sampleSize) + xCeiling;
        if (value > sample[index3]) {
            sample[index3] = value;
        }
    }

    public static float[] temporalSampling(GestureStroke stroke, int numPoints) {
        float lstPointY;
        int i;
        GestureStroke gestureStroke = stroke;
        float increment = gestureStroke.length / ((float) (numPoints - 1));
        int vectorLength = numPoints * 2;
        float[] vector = new float[vectorLength];
        float distanceSoFar = 0.0f;
        float[] pts = gestureStroke.points;
        float lstPointX = pts[0];
        int distance = 1;
        float lstPointY2 = pts[1];
        float currentPointX = Float.MIN_VALUE;
        float currentPointY = Float.MIN_VALUE;
        vector[0] = lstPointX;
        int index = 0 + 1;
        vector[index] = lstPointY2;
        index++;
        int i2 = 0;
        int count = pts.length / 2;
        while (i2 < count) {
            int i3;
            if (currentPointX == Float.MIN_VALUE) {
                i2++;
                if (i2 >= count) {
                    lstPointY = lstPointY2;
                    i = count;
                    break;
                }
                currentPointX = pts[i2 * 2];
                currentPointY = pts[(i2 * 2) + distance];
            }
            float deltaX = currentPointX - lstPointX;
            float deltaY = currentPointY - lstPointY2;
            lstPointY = lstPointY2;
            int i4 = i2;
            i = count;
            float distance2 = (float) Math.hypot((double) deltaX, (double) deltaY);
            if (distanceSoFar + distance2 >= increment) {
                lstPointY2 = (increment - distanceSoFar) / distance2;
                float nx = (lstPointY2 * deltaX) + lstPointX;
                float ny = lstPointY + (lstPointY2 * deltaY);
                vector[index] = nx;
                index++;
                vector[index] = ny;
                i3 = 1;
                index++;
                lstPointX = nx;
                distanceSoFar = 0.0f;
                lstPointY2 = ny;
            } else {
                i3 = 1;
                lstPointX = currentPointX;
                lstPointY2 = currentPointY;
                currentPointX = Float.MIN_VALUE;
                currentPointY = Float.MIN_VALUE;
                distanceSoFar += distance2;
            }
            distance = i3;
            count = i;
            i2 = i4;
        }
        lstPointY = lstPointY2;
        i = count;
        for (distance = index; distance < vectorLength; distance += 2) {
            vector[distance] = lstPointX;
            vector[distance + 1] = lstPointY;
        }
        return vector;
    }

    static float[] computeCentroid(float[] points) {
        float centerX = 0.0f;
        float centerY = 0.0f;
        int i = 0;
        while (i < points.length) {
            centerX += points[i];
            i++;
            centerY += points[i];
            i++;
        }
        return new float[]{(centerX * 2.0f) / ((float) points.length), (2.0f * centerY) / ((float) points.length)};
    }

    private static float[][] computeCoVariance(float[] points) {
        float[][] array = (float[][]) Array.newInstance(float.class, new int[]{2, 2});
        array[0][0] = 0.0f;
        array[0][1] = 0.0f;
        array[1][0] = 0.0f;
        array[1][1] = 0.0f;
        int count = points.length;
        int i = 0;
        while (i < count) {
            float x = points[i];
            i++;
            float y = points[i];
            float[] fArr = array[0];
            fArr[0] = fArr[0] + (x * x);
            fArr = array[0];
            fArr[1] = fArr[1] + (x * y);
            array[1][0] = array[0][1];
            fArr = array[1];
            fArr[1] = fArr[1] + (y * y);
            i++;
        }
        float[] fArr2 = array[0];
        fArr2[0] = fArr2[0] / ((float) (count / 2));
        fArr2 = array[0];
        fArr2[1] = fArr2[1] / ((float) (count / 2));
        fArr2 = array[1];
        fArr2[0] = fArr2[0] / ((float) (count / 2));
        float[] fArr3 = array[1];
        fArr3[1] = fArr3[1] / ((float) (count / 2));
        return array;
    }

    static float computeTotalLength(float[] points) {
        float sum = 0.0f;
        for (int i = 0; i < points.length - 4; i += 2) {
            sum = (float) (((double) sum) + Math.hypot((double) (points[i + 2] - points[i]), (double) (points[i + 3] - points[i + 1])));
        }
        return sum;
    }

    static float computeStraightness(float[] points) {
        return ((float) Math.hypot((double) (points[2] - points[0]), (double) (points[3] - points[1]))) / computeTotalLength(points);
    }

    static float computeStraightness(float[] points, float totalLen) {
        return ((float) Math.hypot((double) (points[2] - points[0]), (double) (points[3] - points[1]))) / totalLen;
    }

    static float squaredEuclideanDistance(float[] vector1, float[] vector2) {
        float squaredDistance = 0.0f;
        int size = vector1.length;
        for (int i = 0; i < size; i++) {
            float difference = vector1[i] - vector2[i];
            squaredDistance += difference * difference;
        }
        return squaredDistance / ((float) size);
    }

    static float cosineDistance(float[] vector1, float[] vector2) {
        float sum = 0.0f;
        for (int i = 0; i < vector1.length; i++) {
            sum += vector1[i] * vector2[i];
        }
        return (float) Math.acos((double) sum);
    }

    static float minimumCosineDistance(float[] vector1, float[] vector2, int numOrientations) {
        float[] fArr = vector1;
        int i = numOrientations;
        float a = 0.0f;
        float b = 0.0f;
        for (int i2 = 0; i2 < fArr.length; i2 += 2) {
            a += (fArr[i2] * vector2[i2]) + (fArr[i2 + 1] * vector2[i2 + 1]);
            b += (fArr[i2] * vector2[i2 + 1]) - (fArr[i2 + 1] * vector2[i2]);
        }
        if (a == 0.0f) {
            return 1.5707964f;
        }
        float tan = b / a;
        double angle = Math.atan((double) tan);
        if (i > 2 && Math.abs(angle) >= 3.141592653589793d / ((double) i)) {
            return (float) Math.acos((double) a);
        }
        double cosine = Math.cos(angle);
        return (float) Math.acos((((double) a) * cosine) + (((double) b) * (((double) tan) * cosine)));
    }

    public static OrientedBoundingBox computeOrientedBoundingBox(ArrayList<GesturePoint> originalPoints) {
        int count = originalPoints.size();
        float[] points = new float[(count * 2)];
        for (int i = 0; i < count; i++) {
            GesturePoint point = (GesturePoint) originalPoints.get(i);
            int index = i * 2;
            points[index] = point.x;
            points[index + 1] = point.y;
        }
        return computeOrientedBoundingBox(points, computeCentroid(points));
    }

    public static OrientedBoundingBox computeOrientedBoundingBox(float[] originalPoints) {
        int size = originalPoints.length;
        float[] points = new float[size];
        for (int i = 0; i < size; i++) {
            points[i] = originalPoints[i];
        }
        return computeOrientedBoundingBox(points, computeCentroid(points));
    }

    private static OrientedBoundingBox computeOrientedBoundingBox(float[] points, float[] centroid) {
        float angle;
        float[] fArr = points;
        translate(fArr, -centroid[0], -centroid[1]);
        float[] targetVector = computeOrientation(computeCoVariance(points));
        if (targetVector[0] == 0.0f && targetVector[1] == 0.0f) {
            angle = -1.5707964f;
        } else {
            angle = (float) Math.atan2((double) targetVector[1], (double) targetVector[0]);
            rotate(fArr, -angle);
        }
        float minx = Float.MAX_VALUE;
        float miny = Float.MAX_VALUE;
        float maxx = Float.MIN_VALUE;
        float maxy = Float.MIN_VALUE;
        int count = fArr.length;
        int i = 0;
        while (i < count) {
            if (fArr[i] < minx) {
                minx = fArr[i];
            }
            if (fArr[i] > maxx) {
                maxx = fArr[i];
            }
            i++;
            if (fArr[i] < miny) {
                miny = fArr[i];
            }
            if (fArr[i] > maxy) {
                maxy = fArr[i];
            }
            i++;
        }
        return new OrientedBoundingBox((float) (((double) (180.0f * angle)) / 3.141592653589793d), centroid[0], centroid[1], maxx - minx, maxy - miny);
    }

    private static float[] computeOrientation(float[][] covarianceMatrix) {
        float[] targetVector = new float[2];
        if (covarianceMatrix[0][1] == 0.0f || covarianceMatrix[1][0] == 0.0f) {
            targetVector[0] = 1.0f;
            targetVector[1] = 0.0f;
        }
        float value = ((-covarianceMatrix[0][0]) - covarianceMatrix[1][1]) / 2.0f;
        float rightside = (float) Math.sqrt(Math.pow((double) value, 2.0d) - ((double) ((covarianceMatrix[0][0] * covarianceMatrix[1][1]) - (covarianceMatrix[0][1] * covarianceMatrix[1][0]))));
        float lambda1 = (-value) + rightside;
        float lambda2 = (-value) - rightside;
        if (lambda1 == lambda2) {
            targetVector[0] = 0.0f;
            targetVector[1] = 0.0f;
        } else {
            float lambda = lambda1 > lambda2 ? lambda1 : lambda2;
            targetVector[0] = 1.0f;
            targetVector[1] = (lambda - covarianceMatrix[0][0]) / covarianceMatrix[0][1];
        }
        return targetVector;
    }

    static float[] rotate(float[] points, float angle) {
        float cos = (float) Math.cos((double) angle);
        float sin = (float) Math.sin((double) angle);
        int size = points.length;
        for (int i = 0; i < size; i += 2) {
            float y = (points[i] * sin) + (points[i + 1] * cos);
            points[i] = (points[i] * cos) - (points[i + 1] * sin);
            points[i + 1] = y;
        }
        return points;
    }

    static float[] translate(float[] points, float dx, float dy) {
        int size = points.length;
        for (int i = 0; i < size; i += 2) {
            points[i] = points[i] + dx;
            int i2 = i + 1;
            points[i2] = points[i2] + dy;
        }
        return points;
    }

    static float[] scale(float[] points, float sx, float sy) {
        int size = points.length;
        for (int i = 0; i < size; i += 2) {
            points[i] = points[i] * sx;
            int i2 = i + 1;
            points[i2] = points[i2] * sy;
        }
        return points;
    }
}
