package android.telephony.emergency;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class EmergencyNumber implements Parcelable, Comparable<EmergencyNumber> {
    public static final Creator<EmergencyNumber> CREATOR = new Creator<EmergencyNumber>() {
        public EmergencyNumber createFromParcel(Parcel in) {
            return new EmergencyNumber(in);
        }

        public EmergencyNumber[] newArray(int size) {
            return new EmergencyNumber[size];
        }
    };
    public static final int EMERGENCY_CALL_ROUTING_EMERGENCY = 1;
    public static final int EMERGENCY_CALL_ROUTING_NORMAL = 2;
    public static final int EMERGENCY_CALL_ROUTING_UNKNOWN = 0;
    public static final int EMERGENCY_NUMBER_SOURCE_DATABASE = 16;
    public static final int EMERGENCY_NUMBER_SOURCE_DEFAULT = 8;
    public static final int EMERGENCY_NUMBER_SOURCE_MODEM_CONFIG = 4;
    public static final int EMERGENCY_NUMBER_SOURCE_NETWORK_SIGNALING = 1;
    private static final Set<Integer> EMERGENCY_NUMBER_SOURCE_SET = new HashSet();
    public static final int EMERGENCY_NUMBER_SOURCE_SIM = 2;
    public static final int EMERGENCY_NUMBER_SOURCE_TEST = 32;
    public static final int EMERGENCY_SERVICE_CATEGORY_AIEC = 64;
    public static final int EMERGENCY_SERVICE_CATEGORY_AMBULANCE = 2;
    public static final int EMERGENCY_SERVICE_CATEGORY_FIRE_BRIGADE = 4;
    public static final int EMERGENCY_SERVICE_CATEGORY_MARINE_GUARD = 8;
    public static final int EMERGENCY_SERVICE_CATEGORY_MIEC = 32;
    public static final int EMERGENCY_SERVICE_CATEGORY_MOUNTAIN_RESCUE = 16;
    public static final int EMERGENCY_SERVICE_CATEGORY_POLICE = 1;
    private static final Set<Integer> EMERGENCY_SERVICE_CATEGORY_SET = new HashSet();
    public static final int EMERGENCY_SERVICE_CATEGORY_UNSPECIFIED = 0;
    private static final String LOG_TAG = "EmergencyNumber";
    private final String mCountryIso;
    private final int mEmergencyCallRouting;
    private final int mEmergencyNumberSourceBitmask;
    private final int mEmergencyServiceCategoryBitmask;
    private final List<String> mEmergencyUrns;
    private final String mMnc;
    private final String mNumber;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EmergencyCallRouting {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EmergencyNumberSources {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EmergencyServiceCategories {
    }

    static {
        Set set = EMERGENCY_SERVICE_CATEGORY_SET;
        Integer valueOf = Integer.valueOf(1);
        set.add(valueOf);
        set = EMERGENCY_SERVICE_CATEGORY_SET;
        Integer valueOf2 = Integer.valueOf(2);
        set.add(valueOf2);
        set = EMERGENCY_SERVICE_CATEGORY_SET;
        Integer valueOf3 = Integer.valueOf(4);
        set.add(valueOf3);
        set = EMERGENCY_SERVICE_CATEGORY_SET;
        Integer valueOf4 = Integer.valueOf(8);
        set.add(valueOf4);
        set = EMERGENCY_SERVICE_CATEGORY_SET;
        Integer valueOf5 = Integer.valueOf(16);
        set.add(valueOf5);
        EMERGENCY_SERVICE_CATEGORY_SET.add(Integer.valueOf(32));
        EMERGENCY_SERVICE_CATEGORY_SET.add(Integer.valueOf(64));
        EMERGENCY_NUMBER_SOURCE_SET.add(valueOf);
        EMERGENCY_NUMBER_SOURCE_SET.add(valueOf2);
        EMERGENCY_NUMBER_SOURCE_SET.add(valueOf5);
        EMERGENCY_NUMBER_SOURCE_SET.add(valueOf3);
        EMERGENCY_NUMBER_SOURCE_SET.add(valueOf4);
    }

    public EmergencyNumber(String number, String countryIso, String mnc, int emergencyServiceCategories, List<String> emergencyUrns, int emergencyNumberSources, int emergencyCallRouting) {
        this.mNumber = number;
        this.mCountryIso = countryIso;
        this.mMnc = mnc;
        this.mEmergencyServiceCategoryBitmask = emergencyServiceCategories;
        this.mEmergencyUrns = emergencyUrns;
        this.mEmergencyNumberSourceBitmask = emergencyNumberSources;
        this.mEmergencyCallRouting = emergencyCallRouting;
    }

    public EmergencyNumber(Parcel source) {
        this.mNumber = source.readString();
        this.mCountryIso = source.readString();
        this.mMnc = source.readString();
        this.mEmergencyServiceCategoryBitmask = source.readInt();
        this.mEmergencyUrns = source.createStringArrayList();
        this.mEmergencyNumberSourceBitmask = source.readInt();
        this.mEmergencyCallRouting = source.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mNumber);
        dest.writeString(this.mCountryIso);
        dest.writeString(this.mMnc);
        dest.writeInt(this.mEmergencyServiceCategoryBitmask);
        dest.writeStringList(this.mEmergencyUrns);
        dest.writeInt(this.mEmergencyNumberSourceBitmask);
        dest.writeInt(this.mEmergencyCallRouting);
    }

    public String getNumber() {
        return this.mNumber;
    }

    public String getCountryIso() {
        return this.mCountryIso;
    }

    public String getMnc() {
        return this.mMnc;
    }

    public int getEmergencyServiceCategoryBitmask() {
        return this.mEmergencyServiceCategoryBitmask;
    }

    public int getEmergencyServiceCategoryBitmaskInternalDial() {
        if (this.mEmergencyNumberSourceBitmask == 16) {
            return 0;
        }
        return this.mEmergencyServiceCategoryBitmask;
    }

    public List<Integer> getEmergencyServiceCategories() {
        List<Integer> categories = new ArrayList();
        if (serviceUnspecified()) {
            categories.add(Integer.valueOf(0));
            return categories;
        }
        for (Integer category : EMERGENCY_SERVICE_CATEGORY_SET) {
            if (isInEmergencyServiceCategories(category.intValue())) {
                categories.add(category);
            }
        }
        return categories;
    }

    public List<String> getEmergencyUrns() {
        return Collections.unmodifiableList(this.mEmergencyUrns);
    }

    private boolean serviceUnspecified() {
        return this.mEmergencyServiceCategoryBitmask == 0;
    }

    public boolean isInEmergencyServiceCategories(int categories) {
        if (categories == 0) {
            return serviceUnspecified();
        }
        boolean z = true;
        if (serviceUnspecified()) {
            return true;
        }
        if ((this.mEmergencyServiceCategoryBitmask & categories) != categories) {
            z = false;
        }
        return z;
    }

    public int getEmergencyNumberSourceBitmask() {
        return this.mEmergencyNumberSourceBitmask;
    }

    public List<Integer> getEmergencyNumberSources() {
        List<Integer> sources = new ArrayList();
        for (Integer source : EMERGENCY_NUMBER_SOURCE_SET) {
            if ((this.mEmergencyNumberSourceBitmask & source.intValue()) == source.intValue()) {
                sources.add(source);
            }
        }
        return sources;
    }

    public boolean isFromSources(int sources) {
        return (this.mEmergencyNumberSourceBitmask & sources) == sources;
    }

    public int getEmergencyCallRouting() {
        return this.mEmergencyCallRouting;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EmergencyNumber:Number-");
        stringBuilder.append(this.mNumber);
        stringBuilder.append("|CountryIso-");
        stringBuilder.append(this.mCountryIso);
        stringBuilder.append("|Mnc-");
        stringBuilder.append(this.mMnc);
        stringBuilder.append("|ServiceCategories-");
        stringBuilder.append(Integer.toBinaryString(this.mEmergencyServiceCategoryBitmask));
        stringBuilder.append("|Urns-");
        stringBuilder.append(this.mEmergencyUrns);
        stringBuilder.append("|Sources-");
        stringBuilder.append(Integer.toBinaryString(this.mEmergencyNumberSourceBitmask));
        stringBuilder.append("|Routing-");
        stringBuilder.append(Integer.toBinaryString(this.mEmergencyCallRouting));
        return stringBuilder.toString();
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!EmergencyNumber.class.isInstance(o)) {
            return false;
        }
        EmergencyNumber other = (EmergencyNumber) o;
        if (this.mNumber.equals(other.mNumber) && this.mCountryIso.equals(other.mCountryIso) && this.mMnc.equals(other.mMnc) && this.mEmergencyServiceCategoryBitmask == other.mEmergencyServiceCategoryBitmask && this.mEmergencyUrns.equals(other.mEmergencyUrns) && this.mEmergencyNumberSourceBitmask == other.mEmergencyNumberSourceBitmask && this.mEmergencyCallRouting == other.mEmergencyCallRouting) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mNumber, this.mCountryIso, this.mMnc, Integer.valueOf(this.mEmergencyServiceCategoryBitmask), this.mEmergencyUrns, Integer.valueOf(this.mEmergencyNumberSourceBitmask), Integer.valueOf(this.mEmergencyCallRouting)});
    }

    private int getDisplayPriorityScore() {
        int score = 0;
        if (isFromSources(1)) {
            score = 0 + 16;
        }
        if (isFromSources(2)) {
            score += 8;
        }
        if (isFromSources(16)) {
            score += 4;
        }
        if (isFromSources(8)) {
            score += 2;
        }
        if (isFromSources(4)) {
            return score + 1;
        }
        return score;
    }

    public int compareTo(EmergencyNumber emergencyNumber) {
        int i = -1;
        if (getDisplayPriorityScore() > emergencyNumber.getDisplayPriorityScore()) {
            return -1;
        }
        if (getDisplayPriorityScore() < emergencyNumber.getDisplayPriorityScore()) {
            return 1;
        }
        if (getNumber().compareTo(emergencyNumber.getNumber()) != 0) {
            return getNumber().compareTo(emergencyNumber.getNumber());
        }
        if (getCountryIso().compareTo(emergencyNumber.getCountryIso()) != 0) {
            return getCountryIso().compareTo(emergencyNumber.getCountryIso());
        }
        if (getMnc().compareTo(emergencyNumber.getMnc()) != 0) {
            return getMnc().compareTo(emergencyNumber.getMnc());
        }
        if (getEmergencyServiceCategoryBitmask() != emergencyNumber.getEmergencyServiceCategoryBitmask()) {
            if (getEmergencyServiceCategoryBitmask() <= emergencyNumber.getEmergencyServiceCategoryBitmask()) {
                i = 1;
            }
            return i;
        } else if (getEmergencyUrns().toString().compareTo(emergencyNumber.getEmergencyUrns().toString()) != 0) {
            return getEmergencyUrns().toString().compareTo(emergencyNumber.getEmergencyUrns().toString());
        } else {
            if (getEmergencyCallRouting() == emergencyNumber.getEmergencyCallRouting()) {
                return 0;
            }
            if (getEmergencyCallRouting() <= emergencyNumber.getEmergencyCallRouting()) {
                i = 1;
            }
            return i;
        }
    }

    public static void mergeSameNumbersInEmergencyNumberList(List<EmergencyNumber> emergencyNumberList) {
        if (emergencyNumberList != null) {
            int i;
            Set<Integer> duplicatedEmergencyNumberPosition = new HashSet();
            for (i = 0; i < emergencyNumberList.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (areSameEmergencyNumbers((EmergencyNumber) emergencyNumberList.get(i), (EmergencyNumber) emergencyNumberList.get(j))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Found unexpected duplicate numbers: ");
                        stringBuilder.append(emergencyNumberList.get(i));
                        stringBuilder.append(" vs ");
                        stringBuilder.append(emergencyNumberList.get(j));
                        Rlog.e(LOG_TAG, stringBuilder.toString());
                        emergencyNumberList.set(i, mergeSameEmergencyNumbers((EmergencyNumber) emergencyNumberList.get(i), (EmergencyNumber) emergencyNumberList.get(j)));
                        duplicatedEmergencyNumberPosition.add(Integer.valueOf(j));
                    }
                }
            }
            for (i = emergencyNumberList.size() - 1; i >= 0; i--) {
                if (duplicatedEmergencyNumberPosition.contains(Integer.valueOf(i))) {
                    emergencyNumberList.remove(i);
                }
            }
            Collections.sort(emergencyNumberList);
        }
    }

    public static boolean areSameEmergencyNumbers(EmergencyNumber first, EmergencyNumber second) {
        if (!first.getNumber().equals(second.getNumber()) || !first.getCountryIso().equals(second.getCountryIso()) || !first.getMnc().equals(second.getMnc()) || first.getEmergencyServiceCategoryBitmask() != second.getEmergencyServiceCategoryBitmask() || !first.getEmergencyUrns().equals(second.getEmergencyUrns()) || first.getEmergencyCallRouting() != second.getEmergencyCallRouting()) {
            return false;
        }
        if ((second.isFromSources(32) ^ first.isFromSources(32)) != 0) {
            return false;
        }
        return true;
    }

    public static EmergencyNumber mergeSameEmergencyNumbers(EmergencyNumber first, EmergencyNumber second) {
        if (!areSameEmergencyNumbers(first, second)) {
            return null;
        }
        return new EmergencyNumber(first.getNumber(), first.getCountryIso(), first.getMnc(), first.getEmergencyServiceCategoryBitmask(), first.getEmergencyUrns(), second.getEmergencyNumberSourceBitmask() | first.getEmergencyNumberSourceBitmask(), first.getEmergencyCallRouting());
    }

    public static boolean validateEmergencyNumberAddress(String address) {
        if (address == null) {
            return false;
        }
        for (char c : address.toCharArray()) {
            if (!PhoneNumberUtils.isDialable(c)) {
                return false;
            }
        }
        return true;
    }
}
