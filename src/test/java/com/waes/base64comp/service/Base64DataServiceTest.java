package com.waes.base64comp.service;

import com.waes.base64comp.model.Base64Data;
import com.waes.base64comp.model.Result;
import com.waes.base64comp.repository.Base64DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.waes.base64comp.controller.Base64compController.LEFT_DATA;
import static com.waes.base64comp.controller.Base64compController.RIGHT_DATA;
import static com.waes.base64comp.util.Base64Utils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * The Base64DataServiceTest class is Base64DataService class unit test.
 */
@SpringBootTest
class Base64DataServiceTest {

    /**
     * The base64 encode test data.
     */
    private static final String BASE64_TEST_DATA_1 = "YWJjZGVmZw==";     // decode: abcdefg
    private static final String BASE64_TEST_DATA_2 = "YWJkY2VoZw==";     // decode: abdcehg
    private static final String BASE64_TEST_DATA_3 = "YWJjZGVmZ2hpag=="; // decode: abcdefghij
    private static final String BASE64_TEST_DATA_4 = "YWJjZGVmAAAA/ZZw=="; // can not decode

    @Autowired
    private Base64DataService service;

    @MockBean
    private Base64DataRepository repository;

    /**
     * Test saveBase64Data function with the data id does not exist in the database.
     */
    @Test
    void saveBase64DataTestNotExistId() {
        when(repository.getBase64DataById(1)).thenReturn(null);
        when(repository.insertBase64Data(1, LEFT_DATA, BASE64_TEST_DATA_1)).thenReturn(1);
        int row = service.saveBase64Data(1, LEFT_DATA, BASE64_TEST_DATA_1);

        assertEquals(1, row);
    }

    /**
     * Test saveBase64Data function with the data id exist in the database.
     */
    @Test
    void saveBase64DataTestTestExistId() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, null));
        when(repository.updateBase64Data(1, RIGHT_DATA, BASE64_TEST_DATA_1)).thenReturn(1);
        int row = service.saveBase64Data(1, RIGHT_DATA, BASE64_TEST_DATA_1);

        assertEquals(1, row);
    }

    /**
     * Test compareBase64Data function with the data id does not exist in the database.
     */
    @Test
    void compareBase64DataTestNotExistId() {
        when(repository.getBase64DataById(1)).thenReturn(null);
        Result result = service.compareBase64Data(1);

        assertEquals(ERROR, result.getStatus());
        assertEquals(ERROR_MSG_1, result.getMessage());
        assertEquals(null, result.getDiffList());
    }

    /**
     * Test compareBase64Data function with the data cannot decode.
     */
    @Test
    void compareBase64DataTestDecodeFail() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_4));
        Result result = service.compareBase64Data(1);

        assertEquals(ERROR, result.getStatus());
        assertEquals(ERROR_MSG_2, result.getMessage());
        assertEquals(null, result.getDiffList());
    }

    /**
     * Test compareBase64Data function with the data that two parts are equal.
     */
    @Test
    void compareBase64DataTestDecodeEqual() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_1));
        Result result = service.compareBase64Data(1);

        assertEquals(EQUAL, result.getStatus());
        assertEquals(null, result.getMessage());
        assertEquals(null, result.getDiffList());
    }

    /**
     * Test compareBase64Data function with the data that two parts are different but the lengths are the same.
     */
    @Test
    void compareBase64DataTestDecodeDifferent() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_2));
        Result result = service.compareBase64Data(1);

        assertEquals(DIFFERENT, result.getStatus());
        assertEquals(null, result.getMessage());
        assertNotEquals(null, result.getDiffList());
    }

    /**
     * Test compareBase64Data function with the data that two parts are different size.
     */
    @Test
    void compareBase64DataTestDecodeDifferentSize() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_3));
        Result result = service.compareBase64Data(1);

        assertEquals(DIFFERENT_SIZE, result.getStatus());
        assertEquals(null, result.getMessage());
        assertEquals(null, result.getDiffList());
    }

    /**
     * Test getBase64Data function with the data exists in the database.
     */
    @Test
    void getBase64DataTestExistId() {
        when(repository.getBase64DataById(1)).thenReturn(new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_1));
        Base64Data data1 = service.getBase64Data(1);
        Base64Data data2 = new Base64Data(1, BASE64_TEST_DATA_1, BASE64_TEST_DATA_1);

        assertThat(data1).isEqualToComparingFieldByField(data2);
    }

    /**
     * Test getBase64Data function with the data does not exist in the database.
     */
    @Test
    void getBase64DataTestNotExistId() {
        when(repository.getBase64DataById(1)).thenReturn(null);
        Base64Data data = service.getBase64Data(1);

        assertEquals(data, null);
    }

}