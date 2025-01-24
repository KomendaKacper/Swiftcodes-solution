package com.example.swiftcodes.models;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SwiftCodeParser {

    public List<SwiftCode> parseExcelFile(String resourcePath) throws Exception {
        List<SwiftCode> swiftCodes = new ArrayList<>();

        try (InputStream is = new ClassPathResource(resourcePath).getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                SwiftCode swiftCode = new SwiftCode();
                swiftCode.setSwiftCode(row.getCell(1).getStringCellValue().trim());
                swiftCode.setBankName(row.getCell(3).getStringCellValue().trim());
                swiftCode.setAddress(row.getCell(4).getStringCellValue().trim());
                swiftCode.setCountryISO2(row.getCell(0).getStringCellValue().toUpperCase().trim());
                swiftCode.setCountryName(row.getCell(5).getStringCellValue().toUpperCase().trim());
                swiftCode.setHeadquarter(swiftCode.getSwiftCode().endsWith("XXX"));

                swiftCodes.add(swiftCode);
            }
        }

        return swiftCodes;
    }
}
