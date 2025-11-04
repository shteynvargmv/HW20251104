package com.example.hw20251104.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkAddResponse {
    private Integer added;
    private Integer failed;
    private Map<String, List<String>> errors;

    public BulkAddResponse() {
        this.added = 0;
        this.failed = 0;
        this.errors = new HashMap<>();
    }

    public void resultAdd() {
        this.added += 1;
    }

    public void resultAdd(String key, List<String> validationErrors) {
        this.failed += 1;
        this.errors.put(key, validationErrors);
    }

    public Integer getAdded() {
        return added;
    }

    public Integer getFailed() {
        return failed;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
