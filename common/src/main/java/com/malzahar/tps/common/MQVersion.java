package com.malzahar.tps.common;

public class MQVersion {

    public static final int CURRENT_VERSION = Version.V1_0_1_SNAPSHOT.ordinal();

    public static String getVersionDesc(int value) {
        int length = Version.values().length;
        if (value >= length) {
            return Version.values()[length - 1].name();
        }

        return Version.values()[value].name();
    }

    public static Version value2Version(int value) {
        int length = Version.values().length;
        if (value >= length) {
            return Version.values()[length - 1];
        }

        return Version.values()[value];
    }

    public enum Version {

        V1_0_1_SNAPSHOT,
        V1_0_1,

        V1_0_2_SNAPSHOT,
        V1_0_2,

        HIGHER_VERSION
    }
}
