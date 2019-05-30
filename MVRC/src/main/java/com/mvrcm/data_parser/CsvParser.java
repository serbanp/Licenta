package com.mvrcm.data_parser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class CsvParser {

    public static List<String[]> getParsedData(String filePath) {
        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReader csvReader=new CSVReaderBuilder(new InputStreamReader(new FileInputStream(filePath))).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).withCSVParser(rfc4180Parser)
                    .withSkipLines(1)
                    .build();
            return csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
