package miui.maml.elements;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.Rating;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.elements.ButtonScreenElement.ButtonActionListener;
import miui.maml.elements.MusicController.OnClientUpdateListener;
import miui.maml.elements.MusicLyricParser.Lyric;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class MusicControlScreenElement extends ElementGroup implements ButtonActionListener {
    private static final String BUTTON_MUSIC_NEXT = "music_next";
    private static final String BUTTON_MUSIC_PAUSE = "music_pause";
    private static final String BUTTON_MUSIC_PLAY = "music_play";
    private static final String BUTTON_MUSIC_PREV = "music_prev";
    private static final String ELE_MUSIC_ALBUM_COVER = "music_album_cover";
    private static final String ELE_MUSIC_DISPLAY = "music_display";
    private static final int FRAMERATE_PLAYING = 30;
    private static final String LOG_TAG = "MusicControlScreenElement";
    private static final String MIUI_PLAYER_PACKAGE_NAME = "com.miui.player";
    public static final int MUSIC_STATE_PLAY = 1;
    public static final int MUSIC_STATE_STOP = 0;
    public static final String TAG_NAME = "MusicControl";
    private AlbumInfo mAlbumInfo = new AlbumInfo();
    private IndexedVariable mAlbumVar;
    private IndexedVariable mArtistVar;
    private IndexedVariable mArtworkVar;
    private boolean mAutoShow;
    private ButtonScreenElement mButtonNext = ((ButtonScreenElement) findElement(BUTTON_MUSIC_NEXT));
    private ButtonScreenElement mButtonPause = ((ButtonScreenElement) findElement(BUTTON_MUSIC_PAUSE));
    private ButtonScreenElement mButtonPlay = ((ButtonScreenElement) findElement(BUTTON_MUSIC_PLAY));
    private ButtonScreenElement mButtonPrev = ((ButtonScreenElement) findElement(BUTTON_MUSIC_PREV));
    private Bitmap mDefaultAlbumCoverBm;
    private Runnable mDelayToSetArtworkRunnable = new Runnable() {
        public void run() {
            MusicControlScreenElement musicControlScreenElement = MusicControlScreenElement.this;
            musicControlScreenElement.updateArtwork(musicControlScreenElement.mDefaultAlbumCoverBm);
        }
    };
    private boolean mDisableNext;
    private IndexedVariable mDisableNextVar;
    private boolean mDisablePlay;
    private IndexedVariable mDisablePlayVar;
    private boolean mDisablePrev;
    private IndexedVariable mDisablePrevVar;
    private IndexedVariable mDurationVar;
    private boolean mEnableLyric;
    private boolean mEnableProgress;
    private ImageScreenElement mImageAlbumCover = ((ImageScreenElement) findElement(ELE_MUSIC_ALBUM_COVER));
    private Lyric mLyric;
    private IndexedVariable mLyricAfterVar;
    private IndexedVariable mLyricBeforeVar;
    private IndexedVariable mLyricCurrentLineProgressVar;
    private IndexedVariable mLyricCurrentVar;
    private IndexedVariable mLyricLastVar;
    private IndexedVariable mLyricNextVar;
    private IndexedVariable mLyricPrevVar;
    private MediaMetadata mMetadata;
    private Context mMiuiMusicContext;
    private MusicController mMusicController;
    private IndexedVariable mMusicStateVar;
    private OnClientUpdateListener mMusicUpdateListener = new OnClientUpdateListener() {
        private boolean mClientChanged;

        public void onClientPlaybackActionUpdate(long action) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transportControlFlags: ");
            stringBuilder.append(action);
            Log.d(MusicControlScreenElement.LOG_TAG, stringBuilder.toString());
            boolean z = true;
            if (!((128 & action) != 0)) {
                MusicControlScreenElement.this.resetUserRating();
            }
            MusicControlScreenElement musicControlScreenElement = MusicControlScreenElement.this;
            boolean z2 = action != 0 && (519 & action) == 0;
            musicControlScreenElement.mDisablePlay = z2;
            musicControlScreenElement = MusicControlScreenElement.this;
            z2 = action != 0 && (16 & action) == 0;
            musicControlScreenElement.mDisablePrev = z2;
            musicControlScreenElement = MusicControlScreenElement.this;
            if (action == 0 || (32 & action) != 0) {
                z = false;
            }
            musicControlScreenElement.mDisableNext = z;
            if (MusicControlScreenElement.this.mHasName) {
                double d = 1.0d;
                MusicControlScreenElement.this.mDisablePlayVar.set(MusicControlScreenElement.this.mDisablePlay ? 1.0d : 0.0d);
                MusicControlScreenElement.this.mDisablePrevVar.set(MusicControlScreenElement.this.mDisablePrev ? 1.0d : 0.0d);
                IndexedVariable access$700 = MusicControlScreenElement.this.mDisableNextVar;
                if (!MusicControlScreenElement.this.mDisableNext) {
                    d = 0.0d;
                }
                access$700.set(d);
            }
        }

        public void onClientPlaybackStateUpdate(int state) {
            onStateUpdate(state);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("stateUpdate: ");
            stringBuilder.append(state);
            Log.d(MusicControlScreenElement.LOG_TAG, stringBuilder.toString());
        }

        /* Access modifiers changed, original: protected */
        public void onStateUpdate(int state) {
            boolean playing = false;
            boolean stateChange = false;
            if (state == 0) {
                MusicControlScreenElement.this.resetAll();
            } else if (state == 1 || state == 2) {
                playing = false;
                stateChange = true;
                MusicControlScreenElement.this.performAction("state_stop");
            } else if (state == 3) {
                playing = true;
                stateChange = true;
                MusicControlScreenElement.this.performAction("state_play");
            }
            if (stateChange) {
                MusicControlScreenElement.this.onMusicStateChange(playing);
            }
        }

        public void onClientMetadataUpdate(MediaMetadata data) {
            MusicControlScreenElement.this.mMetadata = data;
            if (MusicControlScreenElement.this.mMetadata != null) {
                boolean anotherSong = false;
                String title = MusicControlScreenElement.this.mMetadata.getString(MediaMetadata.METADATA_KEY_TITLE);
                String artist = MusicControlScreenElement.this.mMetadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
                String album = MusicControlScreenElement.this.mMetadata.getString(MediaMetadata.METADATA_KEY_ALBUM);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\ntitle: ");
                stringBuilder.append(title);
                stringBuilder.append(", artist: ");
                stringBuilder.append(artist);
                stringBuilder.append(", album: ");
                stringBuilder.append(album);
                String stringBuilder2 = stringBuilder.toString();
                String str = MusicControlScreenElement.LOG_TAG;
                Log.d(str, stringBuilder2);
                if (!(title == null && artist == null && album == null)) {
                    anotherSong = MusicControlScreenElement.this.mAlbumInfo.update(title, artist, album);
                    MusicControlScreenElement.this.updateAlbum(title, artist, album);
                }
                Bitmap artwork = MusicControlScreenElement.this.mMetadata.getBitmap(MediaMetadata.METADATA_KEY_ART);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("artwork: ");
                stringBuilder3.append(artwork != null ? artwork.toString() : "null");
                Log.d(str, stringBuilder3.toString());
                boolean artworkInfoValid = artwork != null || anotherSong;
                if (artworkInfoValid) {
                    if (artwork == null) {
                        MusicControlScreenElement.this.delayToSetDefaultArtwork(500);
                    } else {
                        MusicControlScreenElement.this.updateArtwork(artwork);
                    }
                }
                String raw = MusicControlScreenElement.this.mMetadata.getString(MediaMetadata.METADATA_KEY_LYRIC);
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("raw lyric: ");
                stringBuilder4.append(raw);
                Log.d(str, stringBuilder4.toString());
                Lyric lyric = MusicLyricParser.parseLyric(raw);
                if (lyric != null) {
                    lyric.decorate();
                }
                boolean lyricInfoValid = lyric != null || anotherSong;
                if (lyricInfoValid) {
                    MusicControlScreenElement.this.mLyric = lyric;
                    MusicControlScreenElement.this.updateLyric(lyric);
                }
                MusicControlScreenElement musicControlScreenElement = MusicControlScreenElement.this;
                musicControlScreenElement.requestFramerate(musicControlScreenElement.mLyric != null ? 30.0f : 0.0f);
                long duration = MusicControlScreenElement.this.mMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
                long position = MusicControlScreenElement.this.mMusicController.getEstimatedMediaPosition();
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("duration: ");
                stringBuilder3.append(duration);
                stringBuilder3.append(", position: ");
                stringBuilder3.append(position);
                Log.d(str, stringBuilder3.toString());
                boolean z = (duration >= 0 && position >= 0) || anotherSong;
                boolean progressInfoValid = z;
                if (progressInfoValid) {
                    MusicControlScreenElement.this.updateProgress(duration, position);
                }
                Rating rating = MusicControlScreenElement.this.mMetadata.getRating(MediaMetadata.METADATA_KEY_USER_RATING);
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("rating: ");
                stringBuilder3.append(rating);
                Log.d(str, stringBuilder3.toString());
                MusicControlScreenElement.this.updateUserRating(rating);
                if (!this.mClientChanged) {
                    onClientChange();
                }
            }
        }

        public void onClientChange() {
            this.mClientChanged = true;
            MusicControlScreenElement.this.resetAll();
            MusicControlScreenElement.this.readPackageName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("clientChange: ");
            String str = "null";
            stringBuilder.append(MusicControlScreenElement.this.mPlayerPackageVar != null ? MusicControlScreenElement.this.mPlayerPackageVar.getString() : str);
            stringBuilder.append("/");
            if (MusicControlScreenElement.this.mPlayerClassVar != null) {
                str = MusicControlScreenElement.this.mPlayerClassVar.getString();
            }
            stringBuilder.append(str);
            Log.d(MusicControlScreenElement.LOG_TAG, stringBuilder.toString());
        }

        public void onSessionDestroyed() {
            if (MusicControlScreenElement.this.mAutoShow) {
                MusicControlScreenElement.this.show(false);
            }
            MusicControlScreenElement.this.onMusicStateChange(false);
        }
    };
    private boolean mNeedUpdateLyric;
    private boolean mNeedUpdateProgress;
    private boolean mNeedUpdateUserRating;
    private IndexedVariable mPlayerClassVar;
    private IndexedVariable mPlayerPackageVar;
    private boolean mPlaying;
    private IndexedVariable mPositionVar;
    private Runnable mResetMusicControllerRunable = new Runnable() {
        public void run() {
            if (MusicControlScreenElement.this.mMusicController != null) {
                MusicControlScreenElement.this.mMusicController.reset();
            }
        }
    };
    private String mSender;
    private SpectrumVisualizerScreenElement mSpectrumVisualizer = findSpectrumVisualizer(this);
    private TextScreenElement mTextDisplay = ((TextScreenElement) findElement(ELE_MUSIC_DISPLAY));
    private IndexedVariable mTitleVar;
    private int mUpdateProgressInterval;
    private Runnable mUpdateProgressRunnable = new Runnable() {
        public void run() {
            if (MusicControlScreenElement.this.mPlaying && MusicControlScreenElement.this.mMetadata != null) {
                long duration = MusicControlScreenElement.this.mMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
                long position = MusicControlScreenElement.this.mMusicController.getEstimatedMediaPosition();
                if (duration > 0 && position >= 0) {
                    MusicControlScreenElement.this.mDurationVar.set((double) duration);
                    MusicControlScreenElement.this.mPositionVar.set((double) position);
                    if (MusicControlScreenElement.this.mNeedUpdateLyric && MusicControlScreenElement.this.mLyric != null) {
                        MusicControlScreenElement.this.updateLyricVar(position);
                    }
                    MusicControlScreenElement.this.requestUpdate();
                    MusicControlScreenElement.this.getContext().getHandler().postDelayed(this, (long) MusicControlScreenElement.this.mUpdateProgressInterval);
                }
            }
        }
    };
    private int mUserRatingStyle;
    private IndexedVariable mUserRatingStyleVar;
    private float mUserRatingValue;
    private IndexedVariable mUserRatingValueVar;

    private static class AlbumInfo {
        String album;
        String artist;
        String title;

        private AlbumInfo() {
        }

        /* synthetic */ AlbumInfo(AnonymousClass1 x0) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public boolean update(String title, String artist, String album) {
            title = title != null ? title.trim() : title;
            artist = artist != null ? artist.trim() : artist;
            album = album != null ? album.trim() : album;
            boolean change = (TextUtils.equals(title, this.title) && TextUtils.equals(artist, this.artist) && TextUtils.equals(album, this.album)) ? false : true;
            if (change) {
                this.title = title;
                this.artist = artist;
                this.album = album;
            }
            return change;
        }
    }

    public MusicControlScreenElement(Element ele, ScreenElementRoot root) {
        String strDefAlbumCoverBmp;
        super(ele, root);
        setupButton(this.mButtonPrev);
        setupButton(this.mButtonNext);
        setupButton(this.mButtonPlay);
        setupButton(this.mButtonPause);
        ButtonScreenElement buttonScreenElement = this.mButtonPause;
        boolean z = false;
        if (buttonScreenElement != null) {
            buttonScreenElement.show(false);
        }
        if (this.mImageAlbumCover != null) {
            strDefAlbumCoverBmp = ele.getAttribute("defAlbumCover");
            if (!TextUtils.isEmpty(strDefAlbumCoverBmp)) {
                this.mDefaultAlbumCoverBm = getContext().mResourceManager.getBitmap(strDefAlbumCoverBmp);
            }
            Bitmap bitmap = this.mDefaultAlbumCoverBm;
            if (bitmap != null) {
                bitmap.setDensity(this.mRoot.getResourceDensity());
            }
        }
        this.mAutoShow = Boolean.parseBoolean(ele.getAttribute("autoShow"));
        this.mEnableLyric = Boolean.parseBoolean(ele.getAttribute("enableLyric"));
        this.mEnableProgress = this.mEnableLyric ? true : Boolean.parseBoolean(ele.getAttribute("enableProgress"));
        this.mUpdateProgressInterval = getAttrAsInt(ele, "updateProgressInterval", 1000);
        if (this.mHasName) {
            Variables variables = getVariables();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".music_state");
            this.mMusicStateVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".title");
            this.mTitleVar = new IndexedVariable(stringBuilder.toString(), variables, false);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".artist");
            this.mArtistVar = new IndexedVariable(stringBuilder.toString(), variables, false);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".album");
            this.mAlbumVar = new IndexedVariable(stringBuilder.toString(), variables, false);
            if (this.mEnableLyric) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_current");
                this.mLyricCurrentVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_before");
                this.mLyricBeforeVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_after");
                this.mLyricAfterVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_last");
                this.mLyricLastVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_prev");
                this.mLyricPrevVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_next");
                this.mLyricNextVar = new IndexedVariable(stringBuilder.toString(), variables, false);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".lyric_current_line_progress");
                this.mLyricCurrentLineProgressVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            }
            if (this.mEnableProgress) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".music_duration");
                this.mDurationVar = new IndexedVariable(stringBuilder.toString(), variables, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".music_position");
                this.mPositionVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".user_rating_style");
            this.mUserRatingStyleVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".user_rating_value");
            this.mUserRatingValueVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".disable_play");
            this.mDisablePlayVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".disable_prev");
            this.mDisablePrevVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".disable_next");
            this.mDisableNextVar = new IndexedVariable(stringBuilder.toString(), variables, true);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".artwork");
            this.mArtworkVar = new IndexedVariable(stringBuilder.toString(), variables, false);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".package");
            this.mPlayerPackageVar = new IndexedVariable(stringBuilder.toString(), variables, false);
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".class");
            this.mPlayerClassVar = new IndexedVariable(stringBuilder.toString(), variables, false);
        }
        boolean z2 = this.mEnableLyric && this.mHasName;
        this.mNeedUpdateLyric = z2;
        if (this.mEnableProgress && this.mHasName) {
            z = true;
        }
        this.mNeedUpdateProgress = z;
        this.mNeedUpdateUserRating = this.mHasName;
        try {
            this.mMiuiMusicContext = getContext().mContext.createPackageContext(MIUI_PLAYER_PACKAGE_NAME, 2);
        } catch (Exception e) {
            Log.w(LOG_TAG, "fail to get MiuiMusic preference", e);
        }
        this.mMusicController = new MusicController(getContext().mContext, getContext().getHandler());
        strDefAlbumCoverBmp = this.mRoot.getRootTag();
        this.mSender = "maml";
        if ("gadget".equalsIgnoreCase(strDefAlbumCoverBmp)) {
            this.mSender = "home_widget";
        } else if (Context.STATUS_BAR_SERVICE.equalsIgnoreCase(strDefAlbumCoverBmp)) {
            this.mSender = "notification_bar";
        } else {
            String str = "lockscreen";
            if (str.equalsIgnoreCase(strDefAlbumCoverBmp)) {
                this.mSender = str;
            }
        }
    }

    private SpectrumVisualizerScreenElement findSpectrumVisualizer(ElementGroup g) {
        Iterator it = g.getElements().iterator();
        while (it.hasNext()) {
            ScreenElement se = (ScreenElement) it.next();
            if (se instanceof SpectrumVisualizerScreenElement) {
                return (SpectrumVisualizerScreenElement) se;
            }
            if (se instanceof ElementGroup) {
                SpectrumVisualizerScreenElement ret = findSpectrumVisualizer((ElementGroup) se);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    private void setupButton(ButtonScreenElement button) {
        if (button != null) {
            button.setListener(this);
            button.setParent(this);
        }
    }

    private void onMusicStateChange(boolean playing) {
        if (playing && this.mAutoShow && !isVisible()) {
            show(true);
        }
        this.mPlaying = playing;
        IndexedVariable indexedVariable = this.mMusicStateVar;
        if (indexedVariable != null) {
            indexedVariable.set(playing ? 1.0d : 0.0d);
        }
        ButtonScreenElement buttonScreenElement = this.mButtonPause;
        if (buttonScreenElement != null) {
            buttonScreenElement.show(playing);
        }
        buttonScreenElement = this.mButtonPlay;
        if (buttonScreenElement != null) {
            buttonScreenElement.show(playing ^ 1);
        }
        if (this.mNeedUpdateProgress) {
            startProgressUpdate(playing, playing ? 100 : 0);
        }
        SpectrumVisualizerScreenElement spectrumVisualizerScreenElement = this.mSpectrumVisualizer;
        if (spectrumVisualizerScreenElement != null) {
            spectrumVisualizerScreenElement.enableUpdate(playing);
        }
        float f = (!playing || this.mLyric == null) ? 0.0f : 30.0f;
        requestFramerate(f);
        requestUpdate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("music state change: playing=");
        stringBuilder.append(playing);
        Log.d(LOG_TAG, stringBuilder.toString());
    }

    private void startProgressUpdate(boolean start, long delay) {
        getContext().getHandler().removeCallbacks(this.mUpdateProgressRunnable);
        if (!start) {
            return;
        }
        if (delay > 0) {
            getContext().getHandler().postDelayed(this.mUpdateProgressRunnable, delay);
        } else {
            getContext().getHandler().post(this.mUpdateProgressRunnable);
        }
    }

    private void updateAlbum(String title, String artist, String album) {
        if (this.mHasName) {
            this.mTitleVar.set((Object) title);
            this.mArtistVar.set((Object) artist);
            this.mAlbumVar.set((Object) album);
        }
        if (this.mTextDisplay != null) {
            String trackInfo = "";
            if (TextUtils.isEmpty(title)) {
                trackInfo = artist;
            } else if (TextUtils.isEmpty(artist)) {
                trackInfo = title;
            } else {
                trackInfo = String.format("%s   %s", new Object[]{title, artist});
            }
            this.mTextDisplay.setText(trackInfo);
        }
        requestUpdate();
    }

    private void delayToSetDefaultArtwork(long delay) {
        Handler handler = getContext().getHandler();
        handler.removeCallbacks(this.mDelayToSetArtworkRunnable);
        handler.postDelayed(this.mDelayToSetArtworkRunnable, delay);
    }

    private void cancelSetDefaultArtwork() {
        getContext().getHandler().removeCallbacks(this.mDelayToSetArtworkRunnable);
    }

    private void updateArtwork(Bitmap artwork) {
        cancelSetDefaultArtwork();
        if (this.mHasName) {
            this.mArtworkVar.set((Object) artwork);
        }
        ImageScreenElement imageScreenElement = this.mImageAlbumCover;
        if (imageScreenElement != null) {
            imageScreenElement.setBitmap(artwork);
        }
        requestUpdate();
    }

    private void updateLyric(Lyric lyric) {
        if (!this.mNeedUpdateLyric) {
            return;
        }
        if (lyric == null) {
            resetLyric();
            return;
        }
        int[] timeArr = lyric.getTimeArr();
        ArrayList<CharSequence> lyricArr = lyric.getStringArr();
        Lyric lyric2 = this.mLyric;
        if (lyric2 != null) {
            lyric2.set(timeArr, lyricArr);
        }
    }

    private void updateProgress(long duration, long position) {
        if (!this.mNeedUpdateProgress) {
            return;
        }
        if (duration <= 0 || position < 0) {
            resetProgress();
            return;
        }
        this.mDurationVar.set((double) duration);
        this.mPositionVar.set((double) position);
        if (this.mNeedUpdateLyric) {
            if (this.mLyric != null) {
                updateLyricVar(position);
            } else {
                resetLyric();
            }
        }
        requestUpdate();
        startProgressUpdate(this.mPlaying, 0);
    }

    private void updateUserRating(Rating rating) {
        if (!this.mNeedUpdateUserRating) {
            return;
        }
        if (rating == null) {
            resetUserRating();
            return;
        }
        this.mUserRatingStyle = rating.getRatingStyle();
        float f = 1.0f;
        switch (this.mUserRatingStyle) {
            case 1:
                if (!rating.hasHeart()) {
                    f = 0.0f;
                }
                this.mUserRatingValue = f;
                break;
            case 2:
                if (!rating.isThumbUp()) {
                    f = 0.0f;
                }
                this.mUserRatingValue = f;
                break;
            case 3:
            case 4:
            case 5:
                this.mUserRatingValue = rating.getStarRating();
                break;
            case 6:
                this.mUserRatingValue = rating.getPercentRating();
                break;
            default:
                this.mUserRatingValue = 0.0f;
                break;
        }
        this.mUserRatingStyleVar.set((double) this.mUserRatingStyle);
        this.mUserRatingValueVar.set((double) this.mUserRatingValue);
        requestUpdate();
    }

    private void updateLyricVar(long position) {
        this.mLyricCurrentLineProgressVar.set(this.mLyric.getLyricShot(position).percent);
        this.mLyricCurrentVar.set(this.mLyric.getLine(position));
        this.mLyricBeforeVar.set(this.mLyric.getBeforeLines(position));
        this.mLyricAfterVar.set(this.mLyric.getAfterLines(position));
        Object last = this.mLyric.getLastLine(position);
        this.mLyricLastVar.set(last);
        this.mLyricPrevVar.set(last);
        this.mLyricNextVar.set(this.mLyric.getNextLine(position));
    }

    private void resetProgress() {
        if (this.mNeedUpdateProgress) {
            this.mDurationVar.set(0.0d);
            this.mPositionVar.set(0.0d);
        }
        if (this.mNeedUpdateLyric) {
            this.mLyricCurrentLineProgressVar.set(0.0d);
        }
    }

    private void resetLyric() {
        if (this.mNeedUpdateLyric) {
            this.mLyricBeforeVar.set(null);
            this.mLyricAfterVar.set(null);
            this.mLyricLastVar.set(null);
            this.mLyricPrevVar.set(null);
            this.mLyricNextVar.set(null);
            this.mLyricCurrentVar.set(null);
        }
    }

    private void resetUserRating() {
        if (this.mNeedUpdateUserRating) {
            this.mUserRatingStyle = 0;
            this.mUserRatingValue = 0.0f;
            this.mUserRatingStyleVar.set(0.0d);
            this.mUserRatingValueVar.set(0.0d);
        }
    }

    private void resetAll() {
        updateAlbum(null, null, null);
        resetProgress();
        resetLyric();
        resetUserRating();
        updateArtwork(this.mDefaultAlbumCoverBm);
        resetPackageName();
        resetMusicState();
    }

    private void resetMusicState() {
        onMusicStateChange(false);
    }

    private void resetPackageName() {
        IndexedVariable indexedVariable = this.mPlayerPackageVar;
        if (indexedVariable != null) {
            indexedVariable.set(null);
        }
        indexedVariable = this.mPlayerClassVar;
        if (indexedVariable != null) {
            indexedVariable.set(null);
        }
    }

    public void init() {
        super.init();
        initByPreference();
        this.mMusicController.registerListener(this.mMusicUpdateListener);
        if (this.mMusicController.isMusicActive()) {
            if (this.mAutoShow) {
                show(true);
            }
            onMusicStateChange(true);
            return;
        }
        onMusicStateChange(false);
    }

    public void initByPreference() {
        Context context = this.mMiuiMusicContext;
        if (context != null) {
            SharedPreferences preference = null;
            try {
                preference = context.getSharedPreferences("MiuiMusic", 4);
            } catch (IllegalStateException e) {
            }
            if (preference != null) {
                updateAlbum(preference.getString("songName", null), preference.getString("artistName", null), preference.getString("albumName", null));
                updateArtwork(this.mDefaultAlbumCoverBm);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        if (visible) {
            requestUpdate();
        } else {
            requestFramerate(0.0f);
        }
    }

    public void pause() {
        super.pause();
    }

    public void resume() {
        super.resume();
        requestUpdate();
    }

    public void finish() {
        super.finish();
        IndexedVariable indexedVariable = this.mArtworkVar;
        if (indexedVariable != null) {
            indexedVariable.set(null);
        }
        this.mMusicController.unregisterListener();
        this.mMusicController.finish();
        Handler handler = getContext().getHandler();
        handler.removeCallbacks(this.mUpdateProgressRunnable);
        handler.removeCallbacks(this.mDelayToSetArtworkRunnable);
        handler.removeCallbacks(this.mResetMusicControllerRunable);
    }

    public boolean onButtonDoubleClick(String name) {
        return false;
    }

    public boolean onButtonDown(String name) {
        return sendMediaKeyEvent(0, name);
    }

    public boolean onButtonLongClick(String name) {
        return false;
    }

    public boolean onButtonUp(String name) {
        if (!sendMediaKeyEvent(1, name)) {
            return false;
        }
        if (BUTTON_MUSIC_PREV.equals(name) || BUTTON_MUSIC_NEXT.equals(name)) {
            cancelSetDefaultArtwork();
            getContext().getHandler().removeCallbacks(this.mUpdateProgressRunnable);
        }
        return true;
    }

    private boolean sendMediaKeyEvent(int action, String key) {
        int code = 0;
        String str = BUTTON_MUSIC_PREV;
        boolean equals = str.equals(key);
        String str2 = BUTTON_MUSIC_PAUSE;
        String str3 = BUTTON_MUSIC_PLAY;
        String str4 = BUTTON_MUSIC_NEXT;
        if (equals) {
            code = 88;
        } else if (str4.equals(key)) {
            code = 87;
        } else if (str3.equals(key) || str2.equals(key)) {
            code = 85;
        }
        if (code == 88 && this.mDisablePrev) {
            return false;
        }
        if (code == 87 && this.mDisableNext) {
            return false;
        }
        if (code == 85 && this.mDisablePlay) {
            return false;
        }
        if (this.mMusicController.sendMediaKeyEvent(action, code)) {
            return true;
        }
        Log.d(LOG_TAG, "fail to dispatch by media controller, send to MiuiMusic.");
        if (action == 0) {
            return true;
        }
        Intent intent = null;
        if (str3.equals(key) || str2.equals(key)) {
            intent = new Intent("com.miui.player.musicservicecommand.togglepause");
        } else if (str.equals(key)) {
            intent = new Intent("com.miui.player.musicservicecommand.previous");
        } else if (str4.equals(key)) {
            intent = new Intent("com.miui.player.musicservicecommand.next");
        }
        if (intent == null) {
            return false;
        }
        intent.setPackage(MIUI_PLAYER_PACKAGE_NAME);
        intent.putExtra("intent_sender", this.mSender);
        Utils.startService(this.mRoot.getContext().mContext, intent);
        getContext().getHandler().postDelayed(this.mResetMusicControllerRunable, 1000);
        return true;
    }

    public void seekTo(double progress) {
        this.mMusicController.seekTo((long) (this.mDurationVar.getDouble() * progress));
    }

    public void ratingHeart() {
        Rating rating = true;
        if (this.mUserRatingStyle == 1) {
            if (this.mUserRatingValue == 1.0f) {
                rating = null;
            }
            rating = Rating.newHeartRating(rating);
            this.mMusicController.rating(rating);
            updateUserRating(rating);
        }
    }

    /* Access modifiers changed, original: protected */
    public void readPackageName() {
        if (this.mPlayerPackageVar != null && this.mPlayerClassVar != null) {
            Object pkg = this.mMusicController.getClientPackageName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("readPackage: ");
            stringBuilder.append(pkg);
            String stringBuilder2 = stringBuilder.toString();
            String str = LOG_TAG;
            Log.d(str, stringBuilder2);
            if (pkg != null) {
                Intent intent = this.mRoot.getContext().mContext.getPackageManager().getLaunchIntentForPackage(pkg);
                if (intent != null) {
                    ComponentName component = intent.getComponent();
                    this.mPlayerPackageVar.set(component.getPackageName());
                    this.mPlayerClassVar.set(component.getClassName());
                } else {
                    this.mPlayerPackageVar.set(pkg);
                    this.mPlayerClassVar.set(null);
                    Log.w(str, "set player info fail.");
                }
            }
        }
    }
}
