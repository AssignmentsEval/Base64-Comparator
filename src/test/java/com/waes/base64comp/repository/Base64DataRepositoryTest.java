package com.waes.base64comp.repository;

import com.waes.base64comp.model.Base64Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.waes.base64comp.controller.Base64compController.LEFT_DATA;
import static com.waes.base64comp.controller.Base64compController.RIGHT_DATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The Base64DataRepositoryTest class is Base64DataRepository class unit test.
 */
@SpringBootTest
class Base64DataRepositoryTest {

    /**
     * The base64 encode test data.
     */
    private static final String BASE64_TEST_DATA = "YWJjZGVmZw==";

    @Autowired
    Base64DataRepository repository;

    /**
     * Test insert data into database.
     */
    @Test
    void insertBase64Data() {
        int result = repository.insertBase64Data(1, LEFT_DATA, BASE64_TEST_DATA);

        assertEquals(1, result);
    }

    /**
     * Test update data into database.
     */
    @Test
    void updateBase64Data() {
        repository.insertBase64Data(2, LEFT_DATA, BASE64_TEST_DATA);
        int result = repository.updateBase64Data(2, RIGHT_DATA, BASE64_TEST_DATA);

        assertEquals(1, result);
    }

    /**
     * Test query data from database and the data exist.
     */
    @Test
    void getBase64DataByIdTestExistId() {
        repository.insertBase64Data(3, LEFT_DATA, BASE64_TEST_DATA);
        repository.updateBase64Data(3, RIGHT_DATA, BASE64_TEST_DATA);
        Base64Data data1 = repository.getBase64DataById(3);
        Base64Data data2 = new Base64Data(3, BASE64_TEST_DATA, BASE64_TEST_DATA);

        assertThat(data1).isEqualToComparingFieldByField(data2);
    }

    /**
     * Test query data from database but the data does not exist.
     */
    @Test
    void getBase64DataByIdTestNotExistId() {
        Base64Data data = repository.getBase64DataById(4);

        assertEquals(null, data);
    }

}