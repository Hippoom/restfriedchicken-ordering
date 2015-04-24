package com.restfriedchicken.ordering.rest;

import com.restfriedchicken.ordering.meta.MetaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MetaController {

    @Autowired
    private MetaStore metaStore;

    @RequestMapping(value = "/meta", method = GET)
    protected HttpEntity<ApplicationMeta> getMeta() {
        return new ResponseEntity<>(metaStore.load(), OK);
    }
}
