package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for the POST /bfhl endpoint.
 * Contains categorized data (numbers, alphabets, special characters),
 * computed values (sum, concat_string), and user identity fields.
 *
 * Uses @JsonInclude(ALWAYS) to ensure empty arrays serialize as []
 * rather than being omitted from the JSON output.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BfhlResponse {

    private boolean success;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    @JsonProperty("alphabets")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    @JsonProperty("sum")
    private String sum;

    @JsonProperty("concat_string")
    private String concatString;

    public BfhlResponse() {
    }

    public BfhlResponse(boolean success, String userId, String email, String rollNumber,
                         List<String> oddNumbers, List<String> evenNumbers,
                         List<String> alphabets, List<String> specialCharacters,
                         String sum, String concatString) {
        this.success = success;
        this.userId = userId;
        this.email = email;
        this.rollNumber = rollNumber;
        this.oddNumbers = oddNumbers;
        this.evenNumbers = evenNumbers;
        this.alphabets = alphabets;
        this.specialCharacters = specialCharacters;
        this.sum = sum;
        this.concatString = concatString;
    }

    // --- Getters and Setters ---

    @JsonProperty("is_success")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public List<String> getOddNumbers() {
        return oddNumbers;
    }

    public void setOddNumbers(List<String> oddNumbers) {
        this.oddNumbers = oddNumbers;
    }

    public List<String> getEvenNumbers() {
        return evenNumbers;
    }

    public void setEvenNumbers(List<String> evenNumbers) {
        this.evenNumbers = evenNumbers;
    }

    public List<String> getAlphabets() {
        return alphabets;
    }

    public void setAlphabets(List<String> alphabets) {
        this.alphabets = alphabets;
    }

    public List<String> getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(List<String> specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getConcatString() {
        return concatString;
    }

    public void setConcatString(String concatString) {
        this.concatString = concatString;
    }
}
