package com.nansha.largobjecttransclient.util;

import com.nansha.largobjecttransclient.model.MessageState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class CsvUtilsTest {
    @Test
    void testConvertListToCsv_WithValidInput_ShouldReturnCorrectCsvString() {
        // Setup test data
        List<MessageState> testData = Arrays.asList(new MessageState("a", "b", "c", "d"), new MessageState("e", "f", "g", "h"));
        String expectedCsv = """
                a,b,c,d
                e,f,g,h
                """; // Expected CSV string format

        // Execute the method under test
        String actualCsv = CsvUtils.convertListToCsv(testData, MessageState.class);

        // Assert the outcome
        Assertions.assertEquals(expectedCsv, actualCsv, "The generated CSV string does not match the expected format.");
    }

}