package com.iwaa.common.util.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Route implements Comparable<Route>, Serializable {

    private String author;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private Date creationDate = new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Location from; //Поле не может быть null
    private final Location to; //Поле не может быть null
    private final Long distance; //Поле не может быть null, Значение поля должно быть больше 1

    public Route(String name, Coordinates coordinates,
                 Location from, Location to,
                 Long distance) {
        this.name = name;
        this.from = from;
        this.coordinates = coordinates;
        this.to = to;
        this.distance = distance;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Route{" + "author=" + author + ", id=" + id + ", name='" + name + '\'' + ", coordinates=" + coordinates + ", creationDate=" + creationDate + ", from=" + from + ", to=" + to + ", distance=" + distance + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Route route) {
        Long routeValue = route.getDistance();
        Long thisValue = this.getDistance();

        if (routeValue == null) {
            routeValue = -1L;
        }
        if (thisValue - routeValue < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(id, route.id) && Objects.equals(name, route.name) && Objects.equals(coordinates, route.coordinates) && Objects.equals(creationDate, route.creationDate) && Objects.equals(from, route.from) && Objects.equals(to, route.to) && Objects.equals(distance, route.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }

    public String currentValues() {
        return name + " \"" + coordinates + "\" " + distance + " " + from + " " + to + " " + creationDate + " \"" + id + "\"";
    }
}
