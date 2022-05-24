package com.iwaa.server.collection;

import com.iwaa.common.util.controllers.CollectionAdmin;
import com.iwaa.common.util.data.Route;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class CollectionAdminImpl implements CollectionAdmin {

    private static final String EMPTY_MESSAGE = "Your collection is empty";
    private final ZonedDateTime creationDate = ZonedDateTime.now();
    private Set<Route> routes;

    public CollectionAdminImpl(Set<Route> routes) {
        this.routes = routes;
    }

    @Override
    public Set<Route> getCollection() {
        return routes;
    }

    @Override
    public void add(Route route) {
        routes.add(route);
    }

    @Override
    public void clear(String username) {
        System.out.println(username);
        routes.removeIf(route -> Objects.equals(route.getAuthor(), username));
    }

    @Override
    public Collection<Route> show() {
        return routes.stream().
                sorted((route1, route2) -> route1.getName().compareToIgnoreCase(route2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Route findById(Long id) {
        Route route1 =  routes.stream().filter(route -> Objects.equals(route.getId(), id)).findFirst().orElse(new Route("", null, null, null, null));
        if (route1.getCoordinates() == null) {
            route1.setId(-1L);
        }
        return route1;
    }

    @Override
    public void remove(Route route) {
        routes.remove(route);
    }

    @Override
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add("Collection type: " + routes.getClass().getName());
        info.add("Date: " + creationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        info.add("Size: " + routes.size());
        return info;
    }

    @Override
    public String outputGroupsByDistance() {
        Map<Long, Long> group = new HashMap<>();
        for (Route route : routes) {
            Long key = route.getDistance();
            if (group.containsKey(key)) {
                Long number = group.get(key) + 1;
                group.put(key, number);
            } else {
                group.put(key, 1L);
            }
        }
        if (group.isEmpty()) {
            return EMPTY_MESSAGE;
        }
        StringJoiner result = new StringJoiner("\n");
        for (Map.Entry<Long, Long> entry: group.entrySet()) {
            result.add("Number of elements with distance " + entry);
        }
        return result.toString();
    }

    @Override
    public boolean removeById(Long id) {
        return routes.removeIf(n -> Objects.equals(n.getId(), id));
    }

    @Override
    public void update(Route updatedRoute) {
        if (removeById(updatedRoute.getId())) {
            routes.add(updatedRoute);
        }
    }

    @Override
    public String outFields() {
        StringJoiner result = new StringJoiner("\n");
        if (!routes.isEmpty()) {
            routes.stream().map(Route::getDistance).sorted(Comparator.reverseOrder()).forEach(it -> result.add(it.toString()));
            return result.toString();
        }
        return EMPTY_MESSAGE;
    }

    @Override
    public String outFilter(Long distance) {
        StringJoiner result = new StringJoiner("\n");
        if (!routes.isEmpty()) {
            routes.stream().filter(it -> it.getDistance() < distance).forEach(it -> result.add(it.toString()));
            return result.toString();
        }
        return EMPTY_MESSAGE;
    }
}
