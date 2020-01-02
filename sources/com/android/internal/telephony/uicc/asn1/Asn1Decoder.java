package com.android.internal.telephony.uicc.asn1;

import com.android.internal.telephony.uicc.IccUtils;

public final class Asn1Decoder {
    private final int mEnd;
    private int mPosition;
    private final byte[] mSrc;

    public Asn1Decoder(String hex) {
        this(IccUtils.hexStringToBytes(hex));
    }

    public Asn1Decoder(byte[] src) {
        this(src, 0, src.length);
    }

    public Asn1Decoder(byte[] bytes, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > bytes.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Out of the bounds: bytes=[");
            stringBuilder.append(bytes.length);
            stringBuilder.append("], offset=");
            stringBuilder.append(offset);
            stringBuilder.append(", length=");
            stringBuilder.append(length);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        this.mSrc = bytes;
        this.mPosition = offset;
        this.mEnd = offset + length;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public boolean hasNextNode() {
        return this.mPosition < this.mEnd;
    }

    public Asn1Node nextNode() throws InvalidAsn1DataException {
        if (this.mPosition < this.mEnd) {
            int i;
            byte b = this.mPosition;
            byte tagStart = b;
            int offset = b + 1;
            if ((this.mSrc[b] & 31) == 31) {
                while (offset < this.mEnd) {
                    i = this.mSrc[offset] & 128;
                    offset++;
                    if (i == 0) {
                        break;
                    }
                }
            }
            if (offset < this.mEnd) {
                StringBuilder stringBuilder;
                try {
                    i = IccUtils.bytesToInt(this.mSrc, tagStart, offset - tagStart);
                    int dataLen = this.mSrc;
                    int offset2 = offset + 1;
                    b = dataLen[offset];
                    if ((b & 128) == 0) {
                        offset = b;
                    } else {
                        offset = b & 127;
                        String str = "Cannot parse length at position: ";
                        if (offset2 + offset <= this.mEnd) {
                            try {
                                dataLen = IccUtils.bytesToInt(dataLen, offset2, offset);
                                offset2 += offset;
                                offset = dataLen;
                            } catch (IllegalArgumentException e) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str);
                                stringBuilder2.append(offset2);
                                throw new InvalidAsn1DataException(i, stringBuilder2.toString(), e);
                            }
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(offset2);
                        throw new InvalidAsn1DataException(i, stringBuilder.toString());
                    }
                    if (offset2 + offset <= this.mEnd) {
                        Asn1Node root = new Asn1Node(i, this.mSrc, offset2, offset);
                        this.mPosition = offset2 + offset;
                        return root;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Incomplete data at position: ");
                    stringBuilder.append(offset2);
                    stringBuilder.append(", expected bytes: ");
                    stringBuilder.append(offset);
                    stringBuilder.append(", actual bytes: ");
                    stringBuilder.append(this.mEnd - offset2);
                    throw new InvalidAsn1DataException(i, stringBuilder.toString());
                } catch (IllegalArgumentException e2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot parse tag at position: ");
                    stringBuilder.append(tagStart);
                    throw new InvalidAsn1DataException(0, stringBuilder.toString(), e2);
                }
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Invalid length at position: ");
            stringBuilder3.append(offset);
            throw new InvalidAsn1DataException(0, stringBuilder3.toString());
        }
        throw new IllegalStateException("No bytes to parse.");
    }
}
