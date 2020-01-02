package com.miui.translationservice.provider;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class TranslationResult implements Parcelable {
    public static final Creator<TranslationResult> CREATOR = new Creator<TranslationResult>() {
        public TranslationResult[] newArray(int size) {
            return new TranslationResult[size];
        }

        public TranslationResult createFromParcel(Parcel source) {
            return new TranslationResult(source, null);
        }
    };
    public static final int RESULT_ERROR_NETWORK = -2;
    public static final int RESULT_ERROR_UNKNOWN = -1;
    public static final int RESULT_SUCCESS = 0;
    private String mCopyright;
    private String mDetailLink;
    private List<Phrase> mPhrases;
    private int mStatus;
    private List<Symbol> mSymbols;
    private List<String> mTranslations;
    private String mWordName;

    public static class Part implements Parcelable {
        public static final Creator<Part> CREATOR = new Creator<Part>() {
            public Part[] newArray(int size) {
                return new Part[size];
            }

            public Part createFromParcel(Parcel source) {
                return new Part(source, null);
            }
        };
        private List<String> mMeans;
        private String mPart;

        /* synthetic */ Part(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Part(Parcel src) {
            setPart(src.readString());
            List<String> means = new ArrayList();
            src.readList(means, Part.class.getClassLoader());
            setMeans(means);
        }

        public void setPart(String part) {
            this.mPart = part;
        }

        public String getPart() {
            return this.mPart;
        }

        public void setMeans(List<String> means) {
            this.mMeans = means;
        }

        public List<String> getMeans() {
            return this.mMeans;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mPart);
            dest.writeList(this.mMeans);
        }
    }

    public static class Phrase implements Parcelable {
        public static final Creator<Phrase> CREATOR = new Creator<Phrase>() {
            public Phrase createFromParcel(Parcel source) {
                return new Phrase(source, null);
            }

            public Phrase[] newArray(int size) {
                return new Phrase[size];
            }
        };
        private List<String> mExplains;
        private String mPhrase;

        /* synthetic */ Phrase(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Phrase(Parcel src) {
            setPhrase(src.readString());
            List<String> explains = new ArrayList();
            src.readList(explains, Phrase.class.getClassLoader());
            setExplains(explains);
        }

        public void setPhrase(String phrase) {
            this.mPhrase = phrase;
        }

        public String getPhrase() {
            return this.mPhrase;
        }

        public void setExplains(List<String> explains) {
            this.mExplains = explains;
        }

        public List<String> getExplains() {
            return this.mExplains;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mPhrase);
            dest.writeList(this.mExplains);
        }
    }

    public static class Symbol implements Parcelable {
        public static final Creator<Symbol> CREATOR = new Creator<Symbol>() {
            public Symbol[] newArray(int size) {
                return new Symbol[size];
            }

            public Symbol createFromParcel(Parcel source) {
                return new Symbol(source, null);
            }
        };
        private List<Part> mParts;
        private String mPhAm;
        private String mPhEn;
        private String mWordSymbol;

        /* synthetic */ Symbol(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Symbol(Parcel src) {
            setPhEn(src.readString());
            setPhAm(src.readString());
            setWordSymbol(src.readString());
            List<Part> parts = new ArrayList();
            src.readList(parts, Symbol.class.getClassLoader());
            setParts(parts);
        }

        public void setPhEn(String phEn) {
            this.mPhEn = phEn;
        }

        public String getPhEn() {
            return this.mPhEn;
        }

        public void setPhAm(String phAm) {
            this.mPhAm = phAm;
        }

        public String getPhAm() {
            return this.mPhAm;
        }

        public void setWordSymbol(String wordSymbol) {
            this.mWordSymbol = wordSymbol;
        }

        public String getWordSymbol() {
            return this.mWordSymbol;
        }

        public void setParts(List<Part> parts) {
            this.mParts = parts;
        }

        public List<Part> getParts() {
            return this.mParts;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mPhEn);
            dest.writeString(this.mPhAm);
            dest.writeString(this.mWordSymbol);
            dest.writeList(this.mParts);
        }
    }

    /* synthetic */ TranslationResult(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private TranslationResult(Parcel src) {
        setCopyright(src.readString());
        setDetailLink(src.readString());
        setStatus(src.readInt());
        setWordName(src.readString());
        List<Symbol> symbols = new ArrayList();
        src.readList(symbols, TranslationResult.class.getClassLoader());
        setSymbols(symbols);
        List<Phrase> phrases = new ArrayList();
        src.readList(phrases, TranslationResult.class.getClassLoader());
        setPhrases(phrases);
        List<String> translations = new ArrayList();
        src.readList(translations, TranslationResult.class.getClassLoader());
        setTranslations(translations);
    }

    public void setCopyright(String copyright) {
        this.mCopyright = copyright;
    }

    public String getCopyright() {
        return this.mCopyright;
    }

    public String getDetailLink() {
        return this.mDetailLink;
    }

    public void setDetailLink(String detailLink) {
        this.mDetailLink = detailLink;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setWordName(String wordName) {
        this.mWordName = wordName;
    }

    public String getWordName() {
        return this.mWordName;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.mSymbols = symbols;
    }

    public List<Symbol> getSymbols() {
        return this.mSymbols;
    }

    public void setPhrases(List<Phrase> phrases) {
        this.mPhrases = phrases;
    }

    public List<Phrase> getPhrases() {
        return this.mPhrases;
    }

    public void setTranslations(List<String> translations) {
        this.mTranslations = translations;
    }

    public List<String> getTranslations() {
        return this.mTranslations;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCopyright);
        dest.writeString(this.mDetailLink);
        dest.writeInt(this.mStatus);
        dest.writeString(this.mWordName);
        dest.writeList(this.mSymbols);
        dest.writeList(this.mPhrases);
        dest.writeList(this.mTranslations);
    }
}
