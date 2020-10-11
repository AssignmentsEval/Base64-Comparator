package com.waes.base64comp.util;

import com.waes.base64comp.model.Base64Data;
import com.waes.base64comp.model.Diff;
import com.waes.base64comp.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;

/**
 * The Base64Utils class provide the functions to handle base64 encode data.
 */
public class Base64Utils {
    private static final Logger logger = LoggerFactory.getLogger(Base64Utils.class);

    /**
     * The constant variables define the comparison result type.
     */
    public static final String EQUAL = "EQUAL";
    public static final String DIFFERENT = "DIFFERENT";
    public static final String DIFFERENT_SIZE = "DIFFERENT_SIZE";
    public static final String ERROR = "ERROR";

    /**
     * The constant variables define the error messages.
     */
    public static final String ERROR_MSG_1 = "ID does not exist";
    public static final String ERROR_MSG_2 = "data can not decode to compare";

    /**
     * Decode the base64 encode data.
     *
     * @param base64Data the data would like to decode.
     * @return the decode data.
     */
    public static String decode (String base64Data) {
        logger.debug("decode");

        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String decodedString = new String(decodedBytes);
        logger.debug(base64Data + " -> " + decodedString);

        return decodedString;
    }

    /**
     * Compare two parts of the data.
     *
     * @param data the data would like to compare.
     * @return the compare result save in Result class.
     */
    public static Result compare (Base64Data data) {
        logger.debug("compare");

        Result result = new Result();

        // check data exist or not
        if (data == null) { // data does not exist return error
            logger.info("id not exist");
            result.setStatus(ERROR);
            result.setMessage(ERROR_MSG_1);
        } else { // data exist
            String left = "";
            String right = "";

            // decode the data
            try {
                left = Base64Utils.decode(data.getLeft_data());
                right = Base64Utils.decode(data.getRight_data());

                logger.info("after decode left_data = " + left + ", right_data = " + right);
                logger.debug("left_data length = " + left.length() + ", right_data length = " + right.length());
            } catch (Exception ex) { // decode fail return error
                logger.info("data decode fail");

                result.setStatus(ERROR);
                result.setMessage(ERROR_MSG_2);

                return result;
            }

            // compare start
            if (left.equals(right)) { // equal
                logger.debug("equal");
                result.setStatus(EQUAL);

            } else if (left.length() != right.length()) { // different size
                logger.debug("different size");
                result.setStatus(DIFFERENT_SIZE);

            } else { // different
                logger.debug("different");
                result.setStatus(DIFFERENT);
                ArrayList<Diff> diffList = new ArrayList<Diff>();

                // calculate offset and length
                int offset = -1;
                int length = 0;

                for (int i = 0; i < left.length(); i++) {
                    if (left.charAt(i) != right.charAt(i)) {
                        if (offset < 0) {
                            offset = i;
                        }

                        length++;
                    } else {
                        if (offset >= 0) {
                            logger.debug("offset = " + offset + ", length = " + length);

                            diffList.add(new Diff(offset, length));
                            offset = -1;
                            length = 0;
                        }
                    }
                }

                if (offset >= 0) {
                    logger.debug("offset = " + offset + ", length = " + length);

                    diffList.add(new Diff(offset, length));
                }

                result.setDiffList(diffList);
            }
        }

        return result;
    }
}
