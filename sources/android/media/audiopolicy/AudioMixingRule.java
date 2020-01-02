package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.media.AudioAttributes;
import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@SystemApi
public class AudioMixingRule {
    public static final int RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET = 32770;
    public static final int RULE_EXCLUDE_ATTRIBUTE_USAGE = 32769;
    public static final int RULE_EXCLUDE_UID = 32772;
    private static final int RULE_EXCLUSION_MASK = 32768;
    public static final int RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET = 2;
    public static final int RULE_MATCH_ATTRIBUTE_USAGE = 1;
    public static final int RULE_MATCH_UID = 4;
    @UnsupportedAppUsage
    private boolean mAllowPrivilegedPlaybackCapture;
    @UnsupportedAppUsage
    private final ArrayList<AudioMixMatchCriterion> mCriteria;
    private final int mTargetMixType;

    public static final class AudioMixMatchCriterion {
        @UnsupportedAppUsage
        final AudioAttributes mAttr;
        @UnsupportedAppUsage
        final int mIntProp;
        @UnsupportedAppUsage
        final int mRule;

        AudioMixMatchCriterion(AudioAttributes attributes, int rule) {
            this.mAttr = attributes;
            this.mIntProp = Integer.MIN_VALUE;
            this.mRule = rule;
        }

        AudioMixMatchCriterion(Integer intProp, int rule) {
            this.mAttr = null;
            this.mIntProp = intProp.intValue();
            this.mRule = rule;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mAttr, Integer.valueOf(this.mIntProp), Integer.valueOf(this.mRule)});
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcel(Parcel dest) {
            dest.writeInt(this.mRule);
            int match_rule = this.mRule & -32769;
            if (match_rule == 1) {
                dest.writeInt(this.mAttr.getUsage());
            } else if (match_rule == 2) {
                dest.writeInt(this.mAttr.getCapturePreset());
            } else if (match_rule != 4) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown match rule");
                stringBuilder.append(match_rule);
                stringBuilder.append(" when writing to Parcel");
                Log.e("AudioMixMatchCriterion", stringBuilder.toString());
                dest.writeInt(-1);
            } else {
                dest.writeInt(this.mIntProp);
            }
        }

        public AudioAttributes getAudioAttributes() {
            return this.mAttr;
        }

        public int getIntProp() {
            return this.mIntProp;
        }

        public int getRule() {
            return this.mRule;
        }
    }

    public static class Builder {
        private boolean mAllowPrivilegedPlaybackCapture = false;
        private ArrayList<AudioMixMatchCriterion> mCriteria = new ArrayList();
        private int mTargetMixType = -1;

        public Builder addRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                return checkAddRuleObjInternal(rule, attrToMatch);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal rule value ");
            stringBuilder.append(rule);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder excludeRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                return checkAddRuleObjInternal(32768 | rule, attrToMatch);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal rule value ");
            stringBuilder.append(rule);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder addMixRule(int rule, Object property) throws IllegalArgumentException {
            if (AudioMixingRule.isValidSystemApiRule(rule)) {
                return checkAddRuleObjInternal(rule, property);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal rule value ");
            stringBuilder.append(rule);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder excludeMixRule(int rule, Object property) throws IllegalArgumentException {
            if (AudioMixingRule.isValidSystemApiRule(rule)) {
                return checkAddRuleObjInternal(32768 | rule, property);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal rule value ");
            stringBuilder.append(rule);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder allowPrivilegedPlaybackCapture(boolean allow) {
            this.mAllowPrivilegedPlaybackCapture = allow;
            return this;
        }

        private Builder checkAddRuleObjInternal(int rule, Object property) throws IllegalArgumentException {
            if (property == null) {
                throw new IllegalArgumentException("Illegal null argument for mixing rule");
            } else if (!AudioMixingRule.isValidRule(rule)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal rule value ");
                stringBuilder.append(rule);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (AudioMixingRule.isAudioAttributeRule(-32769 & rule)) {
                if (property instanceof AudioAttributes) {
                    return addRuleInternal((AudioAttributes) property, null, rule);
                }
                throw new IllegalArgumentException("Invalid AudioAttributes argument");
            } else if (property instanceof Integer) {
                return addRuleInternal(null, (Integer) property, rule);
            } else {
                throw new IllegalArgumentException("Invalid Integer argument");
            }
        }

        /* JADX WARNING: Missing block: B:61:0x00f6, code skipped:
            return r9;
     */
        private android.media.audiopolicy.AudioMixingRule.Builder addRuleInternal(android.media.AudioAttributes r10, java.lang.Integer r11, int r12) throws java.lang.IllegalArgumentException {
            /*
            r9 = this;
            r0 = r9.mTargetMixType;
            r1 = 1;
            r2 = -1;
            if (r0 != r2) goto L_0x0013;
        L_0x0006:
            r0 = android.media.audiopolicy.AudioMixingRule.isPlayerRule(r12);
            if (r0 == 0) goto L_0x0010;
        L_0x000c:
            r0 = 0;
            r9.mTargetMixType = r0;
            goto L_0x002e;
        L_0x0010:
            r9.mTargetMixType = r1;
            goto L_0x002e;
        L_0x0013:
            if (r0 != 0) goto L_0x001b;
        L_0x0015:
            r0 = android.media.audiopolicy.AudioMixingRule.isPlayerRule(r12);
            if (r0 == 0) goto L_0x0026;
        L_0x001b:
            r0 = r9.mTargetMixType;
            if (r0 != r1) goto L_0x002e;
        L_0x001f:
            r0 = android.media.audiopolicy.AudioMixingRule.isPlayerRule(r12);
            if (r0 != 0) goto L_0x0026;
        L_0x0025:
            goto L_0x002e;
        L_0x0026:
            r0 = new java.lang.IllegalArgumentException;
            r1 = "Incompatible rule for mix";
            r0.<init>(r1);
            throw r0;
        L_0x002e:
            r0 = r9.mCriteria;
            monitor-enter(r0);
            r2 = r9.mCriteria;	 Catch:{ all -> 0x00f7 }
            r2 = r2.iterator();	 Catch:{ all -> 0x00f7 }
            r3 = -32769; // 0xffffffffffff7fff float:NaN double:NaN;
            r4 = r12 & r3;
        L_0x003c:
            r5 = r2.hasNext();	 Catch:{ all -> 0x00f7 }
            r6 = 4;
            r7 = 2;
            if (r5 == 0) goto L_0x00d1;
        L_0x0044:
            r5 = r2.next();	 Catch:{ all -> 0x00f7 }
            r5 = (android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion) r5;	 Catch:{ all -> 0x00f7 }
            r8 = r5.mRule;	 Catch:{ all -> 0x00f7 }
            r8 = r8 & r3;
            if (r8 == r4) goto L_0x0050;
        L_0x004f:
            goto L_0x003c;
        L_0x0050:
            if (r4 == r1) goto L_0x00a6;
        L_0x0052:
            if (r4 == r7) goto L_0x007d;
        L_0x0054:
            if (r4 == r6) goto L_0x0058;
        L_0x0056:
            goto L_0x00cf;
        L_0x0058:
            r6 = r5.mIntProp;	 Catch:{ all -> 0x00f7 }
            r7 = r11.intValue();	 Catch:{ all -> 0x00f7 }
            if (r6 != r7) goto L_0x00cf;
        L_0x0060:
            r1 = r5.mRule;	 Catch:{ all -> 0x00f7 }
            if (r1 != r12) goto L_0x0066;
        L_0x0064:
            monitor-exit(r0);	 Catch:{ all -> 0x00f7 }
            return r9;
        L_0x0066:
            r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x00f7 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
            r3.<init>();	 Catch:{ all -> 0x00f7 }
            r6 = "Contradictory rule exists for UID ";
            r3.append(r6);	 Catch:{ all -> 0x00f7 }
            r3.append(r11);	 Catch:{ all -> 0x00f7 }
            r3 = r3.toString();	 Catch:{ all -> 0x00f7 }
            r1.<init>(r3);	 Catch:{ all -> 0x00f7 }
            throw r1;	 Catch:{ all -> 0x00f7 }
        L_0x007d:
            r6 = r5.mAttr;	 Catch:{ all -> 0x00f7 }
            r6 = r6.getCapturePreset();	 Catch:{ all -> 0x00f7 }
            r7 = r10.getCapturePreset();	 Catch:{ all -> 0x00f7 }
            if (r6 != r7) goto L_0x00cf;
        L_0x0089:
            r1 = r5.mRule;	 Catch:{ all -> 0x00f7 }
            if (r1 != r12) goto L_0x008f;
        L_0x008d:
            monitor-exit(r0);	 Catch:{ all -> 0x00f7 }
            return r9;
        L_0x008f:
            r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x00f7 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
            r3.<init>();	 Catch:{ all -> 0x00f7 }
            r6 = "Contradictory rule exists for ";
            r3.append(r6);	 Catch:{ all -> 0x00f7 }
            r3.append(r10);	 Catch:{ all -> 0x00f7 }
            r3 = r3.toString();	 Catch:{ all -> 0x00f7 }
            r1.<init>(r3);	 Catch:{ all -> 0x00f7 }
            throw r1;	 Catch:{ all -> 0x00f7 }
        L_0x00a6:
            r6 = r5.mAttr;	 Catch:{ all -> 0x00f7 }
            r6 = r6.getUsage();	 Catch:{ all -> 0x00f7 }
            r7 = r10.getUsage();	 Catch:{ all -> 0x00f7 }
            if (r6 != r7) goto L_0x00cf;
        L_0x00b2:
            r1 = r5.mRule;	 Catch:{ all -> 0x00f7 }
            if (r1 != r12) goto L_0x00b8;
        L_0x00b6:
            monitor-exit(r0);	 Catch:{ all -> 0x00f7 }
            return r9;
        L_0x00b8:
            r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x00f7 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
            r3.<init>();	 Catch:{ all -> 0x00f7 }
            r6 = "Contradictory rule exists for ";
            r3.append(r6);	 Catch:{ all -> 0x00f7 }
            r3.append(r10);	 Catch:{ all -> 0x00f7 }
            r3 = r3.toString();	 Catch:{ all -> 0x00f7 }
            r1.<init>(r3);	 Catch:{ all -> 0x00f7 }
            throw r1;	 Catch:{ all -> 0x00f7 }
        L_0x00cf:
            goto L_0x003c;
        L_0x00d1:
            if (r4 == r1) goto L_0x00ea;
        L_0x00d3:
            if (r4 == r7) goto L_0x00ea;
        L_0x00d5:
            if (r4 != r6) goto L_0x00e2;
        L_0x00d7:
            r1 = r9.mCriteria;	 Catch:{ all -> 0x00f7 }
            r3 = new android.media.audiopolicy.AudioMixingRule$AudioMixMatchCriterion;	 Catch:{ all -> 0x00f7 }
            r3.<init>(r11, r12);	 Catch:{ all -> 0x00f7 }
            r1.add(r3);	 Catch:{ all -> 0x00f7 }
            goto L_0x00f5;
        L_0x00e2:
            r1 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00f7 }
            r3 = "Unreachable code in addRuleInternal()";
            r1.<init>(r3);	 Catch:{ all -> 0x00f7 }
            throw r1;	 Catch:{ all -> 0x00f7 }
        L_0x00ea:
            r1 = r9.mCriteria;	 Catch:{ all -> 0x00f7 }
            r3 = new android.media.audiopolicy.AudioMixingRule$AudioMixMatchCriterion;	 Catch:{ all -> 0x00f7 }
            r3.<init>(r10, r12);	 Catch:{ all -> 0x00f7 }
            r1.add(r3);	 Catch:{ all -> 0x00f7 }
        L_0x00f5:
            monitor-exit(r0);	 Catch:{ all -> 0x00f7 }
            return r9;
        L_0x00f7:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x00f7 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.audiopolicy.AudioMixingRule$Builder.addRuleInternal(android.media.AudioAttributes, java.lang.Integer, int):android.media.audiopolicy.AudioMixingRule$Builder");
        }

        /* Access modifiers changed, original: 0000 */
        public Builder addRuleFromParcel(Parcel in) throws IllegalArgumentException {
            int rule = in.readInt();
            int match_rule = -32769 & rule;
            AudioAttributes attr = null;
            Integer intProp = null;
            if (match_rule == 1) {
                attr = new android.media.AudioAttributes.Builder().setUsage(in.readInt()).build();
            } else if (match_rule == 2) {
                attr = new android.media.AudioAttributes.Builder().setInternalCapturePreset(in.readInt()).build();
            } else if (match_rule == 4) {
                intProp = new Integer(in.readInt());
            } else {
                in.readInt();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal rule value ");
                stringBuilder.append(rule);
                stringBuilder.append(" in parcel");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            return addRuleInternal(attr, intProp, rule);
        }

        public AudioMixingRule build() {
            return new AudioMixingRule(this.mTargetMixType, this.mCriteria, this.mAllowPrivilegedPlaybackCapture);
        }
    }

    private AudioMixingRule(int mixType, ArrayList<AudioMixMatchCriterion> criteria, boolean allowPrivilegedPlaybackCapture) {
        this.mAllowPrivilegedPlaybackCapture = false;
        this.mCriteria = criteria;
        this.mTargetMixType = mixType;
        this.mAllowPrivilegedPlaybackCapture = allowPrivilegedPlaybackCapture;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAffectingUsage(int usage) {
        Iterator it = this.mCriteria.iterator();
        while (it.hasNext()) {
            AudioMixMatchCriterion criterion = (AudioMixMatchCriterion) it.next();
            if ((criterion.mRule & 1) != 0 && criterion.mAttr != null && criterion.mAttr.getUsage() == usage) {
                return true;
            }
        }
        return false;
    }

    private static boolean areCriteriaEquivalent(ArrayList<AudioMixMatchCriterion> cr1, ArrayList<AudioMixMatchCriterion> cr2) {
        boolean z = false;
        if (cr1 == null || cr2 == null) {
            return false;
        }
        if (cr1 == cr2) {
            return true;
        }
        if (cr1.size() != cr2.size()) {
            return false;
        }
        if (cr1.hashCode() == cr2.hashCode()) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public int getTargetMixType() {
        return this.mTargetMixType;
    }

    public ArrayList<AudioMixMatchCriterion> getCriteria() {
        return this.mCriteria;
    }

    public boolean allowPrivilegedPlaybackCapture() {
        return this.mAllowPrivilegedPlaybackCapture;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioMixingRule that = (AudioMixingRule) o;
        if (!(this.mTargetMixType == that.mTargetMixType && areCriteriaEquivalent(this.mCriteria, that.mCriteria) && this.mAllowPrivilegedPlaybackCapture == that.mAllowPrivilegedPlaybackCapture)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mTargetMixType), this.mCriteria, Boolean.valueOf(this.mAllowPrivilegedPlaybackCapture)});
    }

    private static boolean isValidSystemApiRule(int rule) {
        if (rule == 1 || rule == 2 || rule == 4) {
            return true;
        }
        return false;
    }

    private static boolean isValidAttributesSystemApiRule(int rule) {
        if (rule == 1 || rule == 2) {
            return true;
        }
        return false;
    }

    private static boolean isValidRule(int rule) {
        int match_rule = -32769 & rule;
        if (match_rule == 1 || match_rule == 2 || match_rule == 4) {
            return true;
        }
        return false;
    }

    private static boolean isPlayerRule(int rule) {
        int match_rule = -32769 & rule;
        if (match_rule == 1 || match_rule == 4) {
            return true;
        }
        return false;
    }

    private static boolean isAudioAttributeRule(int match_rule) {
        if (match_rule == 1 || match_rule == 2) {
            return true;
        }
        return false;
    }
}
