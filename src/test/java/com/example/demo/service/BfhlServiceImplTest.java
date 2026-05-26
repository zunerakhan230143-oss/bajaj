package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.impl.BfhlServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BfhlServiceImpl}.
 * Tests all 3 examples from the task specification plus edge cases.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
        // Inject @Value fields via reflection for unit testing
        ReflectionTestUtils.setField(service, "userId", "zunera_khan_26052006");
        ReflectionTestUtils.setField(service, "email", "zunerakhan230140@acropolis.in");
        ReflectionTestUtils.setField(service, "rollNumber", "0827CY231078");
    }

    // ==================== EXAMPLE TESTS FROM TASK SPEC ====================

    @Test
    @DisplayName("Example A: mixed numbers, letters, and special characters")
    void testExampleA() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals("zunera_khan_26052006", response.getUserId());
        assertEquals("zunerakhan230140@acropolis.in", response.getEmail());
        assertEquals("0827CY231078", response.getRollNumber());
        assertEquals(List.of("1"), response.getOddNumbers());
        assertEquals(List.of("334", "4"), response.getEvenNumbers());
        assertEquals(List.of("A", "R"), response.getAlphabets());
        assertEquals(List.of("$"), response.getSpecialCharacters());
        assertEquals("339", response.getSum());
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    @DisplayName("Example B: includes empty string, multiple specials")
    void testExampleB() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("2", "a", "y", "4", "&", "-", "", "5", "92", "b"));
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(List.of("5"), response.getOddNumbers());
        assertEquals(List.of("2", "4", "92"), response.getEvenNumbers());
        assertEquals(List.of("A", "Y", "B"), response.getAlphabets());
        assertEquals(List.of("&", "-", ""), response.getSpecialCharacters());
        assertEquals("103", response.getSum());
        assertEquals("ByA", response.getConcatString());
    }

    @Test
    @DisplayName("Example C: multi-character alphabets, concat_string splits letters")
    void testExampleC() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals(List.of("A", "ABCD", "DOE"), response.getAlphabets());
        assertEquals(Collections.emptyList(), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        // Letters: A, A,B,C,D, D,O,E → reversed: E,O,D,D,C,B,A,A
        // Alt caps: E,o,D,d,C,b,A,a → "EoDdCbAa"
        assertEquals("EoDdCbAa", response.getConcatString());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Empty data array: all arrays empty, sum 0, concat empty")
    void testEmptyArray() {
        BfhlRequest request = new BfhlRequest(Collections.emptyList());
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals(Collections.emptyList(), response.getAlphabets());
        assertEquals(Collections.emptyList(), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Null data field: treated as empty, no crash")
    void testNullData() {
        BfhlRequest request = new BfhlRequest(null);
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals(Collections.emptyList(), response.getAlphabets());
        assertEquals(Collections.emptyList(), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Empty string element: classified as special character")
    void testEmptyStringElement() {
        BfhlRequest request = new BfhlRequest(List.of(""));
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(List.of(""), response.getSpecialCharacters());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals(Collections.emptyList(), response.getAlphabets());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Zero is even: '0' goes to even_numbers")
    void testZeroIsEven() {
        BfhlRequest request = new BfhlRequest(List.of("0"));
        BfhlResponse response = service.processData(request);

        assertEquals(List.of("0"), response.getEvenNumbers());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals("0", response.getSum());
    }

    @Test
    @DisplayName("Large number: no int overflow, sum computed correctly with long")
    void testLargeNumber() {
        BfhlRequest request = new BfhlRequest(List.of("99999999999"));
        BfhlResponse response = service.processData(request);

        // 99999999999 is odd
        assertEquals(List.of("99999999999"), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals("99999999999", response.getSum());
    }

    @Test
    @DisplayName("Mixed element 'a1b': special character, but letters extracted for concat")
    void testMixedElement() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a1b", "x"));
        BfhlResponse response = service.processData(request);

        // "a1b" is mixed → special character
        // "x" is pure letter → alphabet "X"
        assertEquals(List.of("a1b"), response.getSpecialCharacters());
        assertEquals(List.of("X"), response.getAlphabets());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        // sum: only numeric elements count, "a1b" is NOT a number → sum = 0
        assertEquals("0", response.getSum());
        // concat letters: a, b (from "a1b"), x (from "x") → reversed: x, b, a
        // Alt caps: X(upper), b(lower), A(upper) → "XbA"
        assertEquals("XbA", response.getConcatString());
    }

    @Test
    @DisplayName("Negative number '-5': classified as special character, not a number")
    void testNegativeNumber() {
        BfhlRequest request = new BfhlRequest(List.of("-5"));
        BfhlResponse response = service.processData(request);

        assertEquals(List.of("-5"), response.getSpecialCharacters());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals("0", response.getSum());
    }

    @Test
    @DisplayName("Decimal number '1.5': classified as special character, not a number")
    void testDecimalNumber() {
        BfhlRequest request = new BfhlRequest(List.of("1.5"));
        BfhlResponse response = service.processData(request);

        assertEquals(List.of("1.5"), response.getSpecialCharacters());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals("0", response.getSum());
    }

    @Test
    @DisplayName("Only special characters: all arrays empty except specials")
    void testOnlySpecialCharacters() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("@", "#", "!"));
        BfhlResponse response = service.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Collections.emptyList(), response.getOddNumbers());
        assertEquals(Collections.emptyList(), response.getEvenNumbers());
        assertEquals(Collections.emptyList(), response.getAlphabets());
        assertEquals(List.of("@", "#", "!"), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }
}
