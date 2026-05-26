package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;

/**
 * Service interface for BFHL data processing.
 * Defines the contract for processing input data arrays and returning
 * categorized results with computed values.
 */
public interface BfhlService {

    /**
     * Processes the input data array and returns a categorized response.
     *
     * @param request the request containing the data array to process
     * @return response with categorized numbers, alphabets, special characters,
     *         sum, and concatenated string
     */
    BfhlResponse processData(BfhlRequest request);
}
