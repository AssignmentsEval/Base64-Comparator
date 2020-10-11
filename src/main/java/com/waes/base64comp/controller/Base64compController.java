package com.waes.base64comp.controller;

import com.waes.base64comp.model.Result;
import com.waes.base64comp.service.Base64DataService;
import com.waes.base64comp.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.waes.base64comp.util.Base64Utils.ERROR;

/**
* The Base64compController class control all requests.
*/
@RestController
@RequestMapping(value = "/v1/diff", produces = MediaType.APPLICATION_JSON_VALUE)
public class Base64compController {
    private static final Logger logger = LoggerFactory.getLogger(Base64compController.class);

    /**
     * The constants to specify the part of data (left/right).
     */
    public static final String LEFT_DATA = "left_data";
    public static final String RIGHT_DATA = "right_data";

    @Autowired
    Base64DataService base64DataService;

    /**
     * Handle left endpoint.
     *
     * @param id    the ID of the left data.
     * @param data  the base64 encode binary data.
     * @return HttpStatus.CREATED
     * @exception RuntimeException if update db fail.
     */
    @PostMapping("{id}/left")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLeftData(@PathVariable("id") int id, @RequestBody String data) {
        logger.info("saveLeftData: id = " + id + ", data = " + data);

        int row = base64DataService.saveBase64Data(id, LEFT_DATA, data);
        logger.info("row = " + row);

        if (row <= 0) {
            throw new RuntimeException("update db fail");
        }
    }

    /**
     * Handle right endpoint.
     *
     * @param id    the ID of the right data.
     * @param data  the base64 encode binary data.
     * @return HttpStatus.CREATED
     * @exception RuntimeException if update db fail.
     */
    @PostMapping("{id}/right")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRightData(@PathVariable("id") int id, @RequestBody String data) {
        logger.info("saveRightData: id = " + id + ", data = " + data);

        int row = base64DataService.saveBase64Data(id, RIGHT_DATA, data);
        logger.info("row = " + row);

        if (row <= 0) {
            throw new RuntimeException("update db fail");
        }
    }

    /**
     * Handle third endpoint (compare base64 encode binary data by id).
     *
     * @param id the ID of the compare data.
     * @return HttpStatus.OK/HttpStatus.BAD_REQUEST
     */
    @GetMapping("{id}")
    public ResponseEntity<String> compDataById(@PathVariable("id") int id) {
        logger.info("compDataById: id = " + id);

        Result result = base64DataService.compareBase64Data(id);
        String output = JsonConverter.convertCompResultToJson(result);
        logger.info("output = " + output);

        if (ERROR.equals(result.getStatus())) {
            return ResponseEntity.badRequest().body(output);
        } else {
            return ResponseEntity.ok().body(output);
        }
    }

    /**
     * Handle runtime exception.
     *
     * @param ex the RuntimeException object.
     * @return HttpStatus.INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        logger.error("RuntimeException: " + ex.getMessage());

        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
