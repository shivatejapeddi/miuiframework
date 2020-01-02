package android.accounts;

import android.annotation.UnsupportedAppUsage;

public class AccountAndUser {
    @UnsupportedAppUsage
    public Account account;
    @UnsupportedAppUsage
    public int userId;

    @UnsupportedAppUsage
    public AccountAndUser(Account account, int userId) {
        this.account = account;
        this.userId = userId;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountAndUser)) {
            return false;
        }
        AccountAndUser other = (AccountAndUser) o;
        if (!(this.account.equals(other.account) && this.userId == other.userId)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.account.hashCode() + this.userId;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.account.toString());
        stringBuilder.append(" u");
        stringBuilder.append(this.userId);
        return stringBuilder.toString();
    }
}
