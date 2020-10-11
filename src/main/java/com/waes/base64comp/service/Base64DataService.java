package com.waes.base64comp.service;

import com.waes.base64comp.model.Base64Data;
import com.waes.base64comp.model.Result;
import com.waes.base64comp.repository.Base64DataRepository;
import com.waes.base64comp.util.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Base64DataService class handle the requests from controller.
 */
@Service
public class Base64DataService {
    private static final Logger logger = LoggerFactory.getLogger(Base64DataService.class);

    /**
     * Use to access the database.
     */
    @Autowired
    private Base64DataRepository base64DataRepository;

    /**
     * Save the data into database.
     * @param id the primary key of the data.
     * @param column the part of the data (left_data/right_data).
     * @param data the base64 encode data.
     * @return the number of rows affected in database.
     */
    public int saveBase64Data(int id, String column, String data) {
        logger.debug("saveBase64Data: id = " + id + ", column = " + column + ", data = " + data);

        int row = 0;

        // check data exist in database or not via id.
        if (getBase64Data(id) == null) { // if data is not in database insert data.
            logger.info("insertBase64Data");
            row = base64DataRepository.insertBase64Data(id, column, data);
        } else { //if data is already in database, then update the data.
            logger.info("updateBase64Data");
            row = base64DataRepository.updateBase64Data(id, column, data);
        }

        return row;
    }

    /**
     * Compare two parts of data.
     *
     * @param id the data id would like to compare.
     * @return compare result which store in Result class.
     */
    public Result compareBase64Data(int id) {
        logger.debug("compareBase64Data: id = " + id);

        // get data by id.
        Base64Data data = getBase64Data(id);
        logger.info("data = " + data);

        // call compare function to compare data.
        Result result = Base64Utils.compare(data);

        return result;
    }

    /**
     * Get base64 encode data from database by id.
     *
     * @param id the data id would like to get.
     * @return the data save in Base64Data class.
     */
    public Base64Data getBase64Data(int id) {
        logger.debug("getBase64Data: id = " + id);

        return base64DataRepository.getBase64DataById(id);
    }

}
