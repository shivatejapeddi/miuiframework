package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.graphics.Rect;
import java.io.PrintWriter;

@SystemApi
public abstract class PresentationParams {

    @SystemApi
    public static abstract class Area {
        private final Rect mBounds;
        public final AutofillProxy proxy;

        private Area(AutofillProxy proxy, Rect bounds) {
            this.proxy = proxy;
            this.mBounds = bounds;
        }

        public Rect getBounds() {
            return this.mBounds;
        }

        public String toString() {
            return this.mBounds.toString();
        }
    }

    public static final class SystemPopupPresentationParams extends PresentationParams {
        private final Area mSuggestionArea;

        public SystemPopupPresentationParams(AutofillProxy proxy, Rect rect) {
            this.mSuggestionArea = new Area(proxy, rect) {
            };
        }

        public Area getSuggestionArea() {
            return this.mSuggestionArea;
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, PrintWriter pw) {
            pw.print(prefix);
            pw.print("area: ");
            pw.println(this.mSuggestionArea);
        }
    }

    public abstract void dump(String str, PrintWriter printWriter);

    PresentationParams() {
    }

    public Area getSuggestionArea() {
        return null;
    }
}
