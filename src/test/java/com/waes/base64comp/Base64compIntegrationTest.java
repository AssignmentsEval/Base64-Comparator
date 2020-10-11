package com.waes.base64comp;

import com.waes.base64comp.controller.Base64compController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.waes.base64comp.util.Base64Utils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The Base64compIntegrationTest class is the integration test.
 */
@SpringBootTest
public class Base64compIntegrationTest {
    private static final String BASE64_TEST_DATA_1 = "YWJjZGVmZw==";       // decode: abcdefg
    private static final String BASE64_TEST_DATA_2 = "YWJkY2VoZw==";       // decode: abdcehg
    private static final String BASE64_TEST_DATA_3 = "YWJjZGVmZ2hpag==";   // decode: abcdefghij
    private static final String BASE64_TEST_DATA_4 = "YWJjZGVmAAAA/ZZw=="; // can not decode

    private MockMvc mockMvc;

    @Autowired
    private Base64compController controller;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(this.controller).build();
    }

    /**
     * Test with the data that two parts are the same.
     *
     * @throws Exception
     */
    @Test
    void testEqualData() throws Exception {
        ResultActions resultActions1 = mockMvc.perform(post("/v1/diff/11/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_1))
                .andExpect(status().isCreated());

        ResultActions resultActions2 = mockMvc.perform(post("/v1/diff/11/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_1))
                .andExpect(status().isCreated());

        ResultActions resultActions3 = mockMvc.perform(get("/v1/diff/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(EQUAL)))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test with the data that two parts are different but the lengths are the same.
     *
     * @throws Exception
     */
    @Test
    void testDifferentData() throws Exception {
        ResultActions resultActions1 = mockMvc.perform(post("/v1/diff/12/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_1))
                .andExpect(status().isCreated());

        ResultActions resultActions2 = mockMvc.perform(post("/v1/diff/12/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_2))
                .andExpect(status().isCreated());

        ResultActions resultActions3 = mockMvc.perform(get("/v1/diff/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(DIFFERENT)))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.diffList").isArray())
                .andExpect(jsonPath("$.diffList", hasSize(2)))
                .andExpect(jsonPath("$.diffList[0].offset", is(2)))
                .andExpect(jsonPath("$.diffList[0].length", is(2)))
                .andExpect(jsonPath("$.diffList[1].offset", is(5)))
                .andExpect(jsonPath("$.diffList[1].length", is(1)));
    }

    /**
     * Test with the data that two parts are different size.
     *
     * @throws Exception
     */
    @Test
    void testDifferentSizeData() throws Exception {
        ResultActions resultActions1 = mockMvc.perform(post("/v1/diff/13/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_1))
                .andExpect(status().isCreated());

        ResultActions resultActions2 = mockMvc.perform(post("/v1/diff/13/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_3))
                .andExpect(status().isCreated());

        ResultActions resultActions3 = mockMvc.perform(get("/v1/diff/13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(DIFFERENT_SIZE)))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test with the data not post at first.
     *
     * @throws Exception
     */
    @Test
    void testNotExistData() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/diff/14"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(ERROR)))
                .andExpect(jsonPath("$.message", is(ERROR_MSG_1)))
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test with the data can not decode.
     *
     * @throws Exception
     */
    @Test
    void testDecodeFailData() throws Exception {
        ResultActions resultActions1 = mockMvc.perform(post("/v1/diff/15/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_1))
                .andExpect(status().isCreated());

        ResultActions resultActions2 = mockMvc.perform(post("/v1/diff/15/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA_4))
                .andExpect(status().isCreated());

        ResultActions resultActions3 = mockMvc.perform(get("/v1/diff/15"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(ERROR)))
                .andExpect(jsonPath("$.message", is(ERROR_MSG_2)))
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }
}
