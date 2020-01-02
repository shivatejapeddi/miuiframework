package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.service.notification.NotificationListenerService.Ranking;
import android.service.notification.NotificationListenerService.RankingMap;

public class NotificationRankingUpdate implements Parcelable {
    public static final Creator<NotificationRankingUpdate> CREATOR = new Creator<NotificationRankingUpdate>() {
        public NotificationRankingUpdate createFromParcel(Parcel parcel) {
            return new NotificationRankingUpdate(parcel);
        }

        public NotificationRankingUpdate[] newArray(int size) {
            return new NotificationRankingUpdate[size];
        }
    };
    private final RankingMap mRankingMap;

    public NotificationRankingUpdate(Ranking[] rankings) {
        this.mRankingMap = new RankingMap(rankings);
    }

    public NotificationRankingUpdate(Parcel in) {
        this.mRankingMap = (RankingMap) in.readParcelable(getClass().getClassLoader());
    }

    public RankingMap getRankingMap() {
        return this.mRankingMap;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.mRankingMap.equals(((NotificationRankingUpdate) o).mRankingMap);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mRankingMap, flags);
    }
}
