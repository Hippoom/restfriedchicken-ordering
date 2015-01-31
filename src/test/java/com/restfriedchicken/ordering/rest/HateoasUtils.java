package com.restfriedchicken.ordering.rest;

import com.jayway.jsonpath.JsonPath;

import java.util.List;

public final class HateoasUtils {
    private HateoasUtils() {
    }

    public static String selfLinkOf(String json, String element) {
        return linkOf(json, "self", element);
    }

    public static String linkOf(String json, String rel, String element) {
        final List<String> hrefs = JsonPath.read(json, "$.links[?(@.rel == '" + rel + "')]." + element);
        return hrefs.isEmpty() ? "" : hrefs.get(0);
    }
}