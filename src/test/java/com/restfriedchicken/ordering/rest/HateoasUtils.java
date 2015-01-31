package com.restfriedchicken.ordering.rest;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Optional;

public final class HateoasUtils {
    private HateoasUtils() {
    }

    public static Optional<String> selfLinkOf(String json) {
        final List<String> hrefs = JsonPath.read(json, "$.links[?(@.rel == 'self')].href");
        return hrefs.isEmpty() ? Optional.<String>empty() : Optional.ofNullable(hrefs.get(0));
    }
}