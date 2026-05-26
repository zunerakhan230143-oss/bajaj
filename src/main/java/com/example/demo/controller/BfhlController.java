package com.example.demo.controller;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.BfhlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for the BFHL API.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>POST /bfhl — Process data array and return categorized response</li>
 *   <li>GET /bfhl — Return operation code (health-check style)</li>
 *   <li>GET / — Health check for Railway/Render platform monitoring</li>
 * </ul>
 */
@RestController
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl — Main endpoint.
     * Accepts a JSON body with a "data" array and returns categorized results.
     *
     * @param request the request containing the data array
     * @return HTTP 200 with the categorized response
     */
    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> processData(@RequestBody BfhlRequest request) {
        log.info("POST /bfhl received");
        BfhlResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /bfhl — Returns an operation code.
     * Can be used as a simple health check for the /bfhl route.
     *
     * @return HTTP 200 with operation_code: 1
     */
    @GetMapping("/bfhl")
    public ResponseEntity<Map<String, Integer>> getOperationCode() {
        log.info("GET /bfhl received");
        return ResponseEntity.ok(Map.of("operation_code", 1));
    }

    /**
     * GET / — Health check endpoint.
     * Railway and Render use this to verify the service is alive.
     * Also useful for uptime monitors (UptimeRobot, BetterStack).
     *
     * @return HTTP 200 with status: healthy
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }
}
