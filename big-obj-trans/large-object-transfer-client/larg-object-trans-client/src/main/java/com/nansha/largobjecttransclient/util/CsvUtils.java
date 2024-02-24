package com.nansha.largobjecttransclient.util;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.util.List;

public class CsvUtils {
  public static <T> String convertListToCsv(List<T> data, Class<T> type) {
    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper.schemaFor(type).withoutHeader();
    ObjectWriter writer = mapper.writer(schema);
    try {
      return writer.writeValueAsString(data);
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert list to CSV", e);
    }
  }
}
