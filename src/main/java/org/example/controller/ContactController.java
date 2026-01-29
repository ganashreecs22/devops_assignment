package org.example.controller;

import org.example.model.Contact;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final Map<String, Contact> contacts = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Contact> all() {
        return contacts.values();
    }

    @PostMapping
    public Contact create(@RequestBody Contact c) {
        String id = UUID.randomUUID().toString();
        c.setId(id);
        contacts.put(id, c);
        return c;
    }
}