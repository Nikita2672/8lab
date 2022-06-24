package com.iwaa.common.util.data;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable, Comparable<Location> {
    private Long x; //Поле не может быть null
    private int y;
    private Integer z; //Поле не может быть null

    public Location(Long x, int y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int compareTo(Location o) {
        if  (((Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))
                - (Math.pow(o.x, 2) + Math.pow(o.y, 2) + Math.pow(o.z, 2))) > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        Location location = (Location) o;
        return getY() == location.getY() && getX().equals(location.getX()) && getZ().equals(location.getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
