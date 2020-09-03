package com.home;

public final class GlobalConstant {
    public static final int FRONT_LASER_PIN = 32;
    public static final int BACK_LASER_PIN = 33;
    public static final int NO_CROSSED = 0;
    public static final int CROSSED = 1024;

    public static final char FRONT_OCCUPIED = 'o';
    public static final char FRONT_FREE = 'f';
    public static final char BACK_OCCUPIED = 'b';
    public static final char BACK_FREE = 'v';

    public static final char[] PERSON_COME_INSIDE = {FRONT_OCCUPIED, BACK_OCCUPIED, FRONT_FREE, BACK_FREE};
    public static final char[] PERSON_COME_OUTSIDE = {BACK_OCCUPIED, FRONT_OCCUPIED, BACK_FREE, FRONT_FREE};

    private GlobalConstant() {}
}
