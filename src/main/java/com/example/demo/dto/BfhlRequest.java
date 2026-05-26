package com.example.demo.dto;

import java.util.List;

/**
 * Request DTO for the POST /bfhl endpoint.
 * Contains a list of string elements to be processed and categorized.
 */
public class BfhlRequest {

    private List<String> data;

    public BfhlRequest() {
    }

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
