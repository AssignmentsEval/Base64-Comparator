package com.waes.base64comp.controller;

import com.waes.base64comp.model.Diff;
import com.waes.base64comp.model.Result;
import com.waes.base64comp.service.Base64DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static com.waes.base64comp.controller.Base64compController.LEFT_DATA;
import static com.waes.base64comp.controller.Base64compController.RIGHT_DATA;
import static com.waes.base64comp.util.Base64Utils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The Base64compControllerTest class is Base64compController class unit test.
 */
@SpringBootTest
class Base64compControllerTest {

    /**
     * The base64 encode test data.
     */
    private static final String BASE64_TEST_DATA = "YWJjZGVmZw=="; // decode: abcdefg

    private MockMvc mockMvc;

    @Autowired
    private Base64compController controller;

    @MockBean
    private Base64DataService service;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(this.controller).build();
    }

    /**
     * Test left endpoint - normal case.
     *
     * @throws Exception
     */
    @Test
    void saveLeftData() throws Exception {
        // Mocking service
        when(this.service.saveBase64Data(1, LEFT_DATA, BASE64_TEST_DATA)).thenReturn(1);

        ResultActions resultActions = mockMvc.perform(post("/v1/diff/1/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA))
                .andExpect(status().isCreated());
    }

    /**
     * Test left endpoint - update db fail.
     *
     * @throws Exception
     */
    @Test
    void saveLeftDataTestFail() throws Exception {
        // Mocking service
        when(this.service.saveBase64Data(1, LEFT_DATA, BASE64_TEST_DATA)).thenReturn(0);

        ResultActions resultActions = mockMvc.perform(post("/v1/diff/1/left").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test right endpoint - normal case.
     *
     * @throws Exception
     */
    @Test
    void saveRightData() throws Exception {
        // Mocking service
        when(this.service.saveBase64Data(1, RIGHT_DATA, BASE64_TEST_DATA)).thenReturn(1);

        ResultActions resultActions = mockMvc.perform(post("/v1/diff/1/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA))
                .andExpect(status().isCreated());
    }

    /**
     * Test right endpoint - update db fail.
     *
     * @throws Exception
     */
    @Test
    void saveRightDataTestFail() throws Exception {
        // Mocking service
        when(this.service.saveBase64Data(1, RIGHT_DATA, BASE64_TEST_DATA)).thenReturn(0);

        ResultActions resultActions = mockMvc.perform(post("/v1/diff/1/right").contentType(MediaType.TEXT_PLAIN).content(BASE64_TEST_DATA))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test compare data - equal case.
     *
     * @throws Exception
     */
    @Test
    void compDataByIdTestEqual() throws Exception {
        // Mocking service
        when(this.service.compareBase64Data(1)).thenReturn(new Result(EQUAL, null, null));

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(EQUAL)))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test compare data - difference case.
     *
     * @throws Exception
     */
    @Test
    void compDataByIdTestDifferent() throws Exception {
        // create ArrayList of difference detail to set in Result object.
        ArrayList<Diff> diffList = new ArrayList<Diff>();
        diffList.add(new Diff(2, 2));
        diffList.add(new Diff(5, 1));

        // Mocking service
        when(this.service.compareBase64Data(1)).thenReturn(new Result(DIFFERENT, null, diffList));

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
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
     * Test compare data - difference size case.
     *
     * @throws Exception
     */
    @Test
    void compDataByIdTestDifferentSize() throws Exception {
        // Mocking service
        when(this.service.compareBase64Data(1)).thenReturn(new Result(DIFFERENT_SIZE, null, null));

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(DIFFERENT_SIZE)))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test compare data - data does not exist case.
     *
     * @throws Exception
     */
    @Test
    void compDataByIdTestNotExistId() throws Exception {
        // Mocking service
        when(this.service.compareBase64Data(1)).thenReturn(new Result(ERROR, ERROR_MSG_1, null));

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(ERROR)))
                .andExpect(jsonPath("$.message", is(ERROR_MSG_1)))
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test compare data - data decode fail case.
     *
     * @throws Exception
     */
    @Test
    void compDataByIdTestDecodeFail() throws Exception {
        // Mocking service
        when(this.service.compareBase64Data(1)).thenReturn(new Result(ERROR, ERROR_MSG_2, null));

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(ERROR)))
                .andExpect(jsonPath("$.message", is(ERROR_MSG_2)))
                .andExpect(jsonPath("$.diffList").doesNotExist());
    }

    /**
     * Test runtime error happened case.
     *
     * @throws Exception
     */
    @Test
    void handleAllExceptions() throws Exception {
        // Mocking service
        when(this.service.compareBase64Data(1)).thenThrow(new RuntimeException());

        ResultActions resultActions = mockMvc.perform(get("/v1/diff/1"))
                .andExpect(status().isInternalServerError());
    }
}