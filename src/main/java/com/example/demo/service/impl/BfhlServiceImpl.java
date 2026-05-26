package com.example.demo.service.impl;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.BfhlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link BfhlService}.
 *
 * <p>Core logic:
 * <ol>
 *   <li>Iterates through each element in the data array</li>
 *   <li>Classifies as number (all digits), alphabet (all letters), or special character (everything else)</li>
 *   <li>For numbers: parses to long, checks odd/even, accumulates sum</li>
 *   <li>For alphabets: converts to uppercase for the alphabets array</li>
 *   <li>Extracts individual letter characters from ALL elements for concat_string</li>
 *   <li>Reverses collected letters, applies alternating caps starting with uppercase</li>
 * </ol>
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Logger log = LoggerFactory.getLogger(BfhlServiceImpl.class);

    @Value("${bfhl.user-id}")
    private String userId;

    @Value("${bfhl.email}")
    private String email;

    @Value("${bfhl.roll-number}")
    private String rollNumber;

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        log.info("Processing BFHL request with data: {}", request.getData());

        List<String> data = request.getData();
        if (data == null) {
            data = Collections.emptyList();
        }

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long sum = 0;
        List<Character> allLetters = new ArrayList<>();

        for (String element : data) {
            if (element == null) {
                // Treat null elements as special characters (defensive)
                specialCharacters.add(null);
                continue;
            }

            // --- Operation A: Categorize the whole element ---
            if (element.matches("^[0-9]+$")) {
                // Pure digits → number
                long value = Long.parseLong(element);
                sum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(element);
                } else {
                    oddNumbers.add(element);
                }
            } else if (element.matches("^[a-zA-Z]+$")) {
                // Pure letters → alphabet (stored as uppercase)
                alphabets.add(element.toUpperCase());
            } else {
                // Everything else: mixed, symbols, empty string "", "-5", "1.5", etc.
                specialCharacters.add(element);
            }

            // --- Operation B: Extract individual letters for concat_string ---
            // This runs for EVERY element (numbers, alphabets, specials — all of them)
            for (char c : element.toCharArray()) {
                if (Character.isLetter(c)) {
                    allLetters.add(c);
                }
            }
        }

        // Build concat_string: reverse the letter list, then apply alternating caps
        Collections.reverse(allLetters);
        StringBuilder concat = new StringBuilder();
        for (int i = 0; i < allLetters.size(); i++) {
            char c = allLetters.get(i);
            if (i % 2 == 0) {
                // Even index (0, 2, 4, ...) → UPPERCASE
                concat.append(Character.toUpperCase(c));
            } else {
                // Odd index (1, 3, 5, ...) → lowercase
                concat.append(Character.toLowerCase(c));
            }
        }

        BfhlResponse response = new BfhlResponse(
                true,
                userId,
                email,
                rollNumber,
                oddNumbers,
                evenNumbers,
                alphabets,
                specialCharacters,
                String.valueOf(sum),
                concat.toString()
        );

        log.info("BFHL response: is_success={}, sum={}, concat_string={}",
                response.isSuccess(), response.getSum(), response.getConcatString());

        return response;
    }
}
