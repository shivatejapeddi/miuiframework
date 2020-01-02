package android.service.autofill.augmented;

import android.annotation.SystemApi;

@SystemApi
public final class FillResponse {
    private final FillWindow mFillWindow;

    @SystemApi
    public static final class Builder {
        private FillWindow mFillWindow;

        public Builder setFillWindow(FillWindow fillWindow) {
            this.mFillWindow = fillWindow;
            return this;
        }

        public FillResponse build() {
            return new FillResponse(this);
        }
    }

    private FillResponse(Builder builder) {
        this.mFillWindow = builder.mFillWindow;
    }

    /* Access modifiers changed, original: 0000 */
    public FillWindow getFillWindow() {
        return this.mFillWindow;
    }
}
