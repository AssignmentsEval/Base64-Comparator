package com.waes.base64comp.util;

import com.waes.base64comp.model.Base64Data;
import com.waes.base64comp.model.Diff;
import com.waes.base64comp.model.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static com.waes.base64comp.util.Base64Utils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The Base64UtilsTest class is Base64Utils class unit test.
 */
@SpringBootTest
class Base64UtilsTest {

    /**
     * The base64 encode test data.
     */
    private static final String BASE64_TEST_DATA_1 = "YWJjZGVmZw==";     // decode: abcdefg
    private static final String BASE64_TEST_DATA_2 = "YWJkY2VoZw==";     // decode: abdcehg
    private static final String BASE64_TEST_DATA_3 = "YWJjZGVmZ2hpag=="; // decode: abcdefghij
    private static final String BASE64_TEST_DATA_4 = "YWJjZGVmAAAA/ZZw=="; // can not decode

    /**
     * Test decode function with equal, different, and different size cases.
     */
    @Test
    void decode() {
        String decodeString1 = Base64Utils.decode(BASE64_TEST_DATA_1);
        String decodeString2 = Base64Utils.decode(BASE64_TEST_DATA_2);
        String decodeString3 = Base64Utils.decode(BASE64_TEST_DATA_3);

        assertEquals("abcdefg", decodeString1);
        assertEquals("abdcehg", decodeString2);
        assertEquals("abcdefghij", decodeString3);
    }

    /**
     * Test decode function with the data cannot decode.
     */
    @Test
    void decodeFail() {
        Throwable thrown = assertThrows(Exception.class, () -> Base64Utils.decode(BASE64_TEST_DATA_4));
        assertThat(thrown).isInstanceOf(Exception.class);
    }

    /**
     * Test compare function with the data that two parts of the data are equal.
     */
    @Test
    void compareTestEqual() {
        Result result = Base64Utils.compare(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_1));

        assertEquals(EQUAL, result.getStatus());
        assertEquals(null, result.getMessage());
        assertEquals(null, result.getDiffList());
    }

    /**
     * Test compare function with the data that two parts of the data are different but the lengths are the same.
     */
    @Test
    void compareTestDifferent() {
        Result result = Base64Utils.compare(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_2));
        ArrayList<Diff> diffList = result.getDiffList();

        assertEquals(DIFFERENT, result.getStatus());
        assertEquals(null, result.getMessage());
        assertNotEquals(null, result.getDiffList());
        assertEquals(2, result.getDiffList().get(0).getOffset());
        assertEquals(2, result.getDiffList().get(0).getLength());
        assertEquals(5, result.getDiffList().get(1).getOffset());
        assertEquals(1, result.getDiffList().get(1).getLength());
    }

    /**
     * Test compare function with the data that two parts of the data are different size.
     */
    @Test
    void compareTestDifferentSize() {
        Result result = Base64Utils.compare(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_3));

        assertEquals(DIFFERENT_SIZE, result.getStatus());
        assertEquals(null, result.getMessage());
        assertEquals(null, result.getDiffList());
    }
}