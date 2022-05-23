package com.iwaa.common.util.controllers;

import com.iwaa.common.util.data.Route;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CollectionAdmin {

    Set<Route> getCollection();
    void add(Route route);
    void clear(String username);
    Collection<Route> show();
    List<String> getInfo();
    String outputGroupsByDistance();
    boolean removeById(Long id);
    String outFields();
    String outFilter(Long distance);
    Route findById(Long id);
    void remove(Route route);
    void update(Route route);
}
