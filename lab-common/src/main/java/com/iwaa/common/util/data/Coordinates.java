package com.iwaa.common.util.data;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable, Comparable<Coordinates> {
    private float x; //Максимальное значение поля: 245
    private Float y; //Максимальное значение поля: 362, Поле не может быть null

    public Coordinates(Float y, float x) {
        this.y = y;
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public int compareTo(Coordinates o) {
        if (((Math.pow(x, 2) + Math.pow(y, 2)) - (Math.pow(o.x, 2) + Math.pow(o.y, 2))) > 0) {
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
        if (!(o instanceof Coordinates)) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return Float.compare(that.getX(), getX()) == 0 && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
