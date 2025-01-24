package com.example.swiftcodes.initializer;

import com.example.swiftcodes.models.SwiftCode;
import com.example.swiftcodes.models.SwiftCodeParser;
import com.example.swiftcodes.repositories.SwiftCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SwiftCodeRepository repository;

    public DataInitializer(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            System.out.println("SWIFT codes already exist in the database. Initialization skipped.");
            return;
        }

        String resourcePath = "data/Interns_2025_SWIFT_CODES.xlsx";
        SwiftCodeParser parser = new SwiftCodeParser();

        List<SwiftCode> swiftCodes = parser.parseExcelFile(resourcePath);

        Map<String, SwiftCode> headquarterMap = swiftCodes.stream()
                .filter(SwiftCode::isHeadquarter)
                .collect(Collectors.toMap(
                        code -> code.getSwiftCode().substring(0, 8),
                        code -> code
                ));

        swiftCodes.forEach(code -> {
            if (!code.isHeadquarter()) {
                String hqCodePrefix = code.getSwiftCode().substring(0, 8);
                if (headquarterMap.containsKey(hqCodePrefix)) {
                    code.setRelatedHeadquarter(headquarterMap.get(hqCodePrefix));
                }
            }
        });

        repository.saveAll(swiftCodes);
        System.out.println("SWIFT codes successfully initialized with branch-headquarter relationships.");
    }
}
