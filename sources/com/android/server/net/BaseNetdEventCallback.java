package com.android.server.net;

import android.net.INetdEventCallback.Stub;

public class BaseNetdEventCallback extends Stub {
    public void onDnsEvent(int netId, int eventType, int returnCode, String hostname, String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) {
    }

    public void onNat64PrefixEvent(int netId, boolean added, String prefixString, int prefixLength) {
    }

    public void onPrivateDnsValidationEvent(int netId, String ipAddress, String hostname, boolean validated) {
    }

    public void onConnectEvent(String ipAddr, int port, long timestamp, int uid) {
    }
}
