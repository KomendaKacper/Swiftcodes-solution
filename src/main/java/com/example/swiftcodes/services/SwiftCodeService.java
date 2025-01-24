package com.example.swiftcodes.services;

import com.example.swiftcodes.exceptions.GlobalExceptionHandler;
import com.example.swiftcodes.models.SwiftCode;
import com.example.swiftcodes.repositories.SwiftCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SwiftCodeService {

    private final SwiftCodeRepository repository;

    public SwiftCodeService(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    public Object getSwiftCodeDetails(String swiftCode) {
        SwiftCode code = repository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new GlobalExceptionHandler.SwiftCodeNotFoundException("SWIFT code not found: " + swiftCode));

        if (code.isHeadquarter()) {
            List<SwiftCode> branches = repository.findByRelatedHeadquarterId(code.getId());

            return Map.of(
                    "address", code.getAddress(),
                    "bankName", code.getBankName(),
                    "countryISO2", code.getCountryISO2(),
                    "countryName", code.getCountryName(),
                    "isHeadquarter", true,
                    "swiftCode", code.getSwiftCode(),
                    "branches", branches.stream().map(branch -> Map.of(
                            "address", branch.getAddress(),
                            "bankName", branch.getBankName(),
                            "countryISO2", branch.getCountryISO2(),
                            "isHeadquarter", branch.isHeadquarter(),
                            "swiftCode", branch.getSwiftCode()
                    )).toList()
            );
        }

        return Map.of(
                "address", code.getAddress(),
                "bankName", code.getBankName(),
                "countryISO2", code.getCountryISO2(),
                "countryName", code.getCountryName(),
                "isHeadquarter", false,
                "swiftCode", code.getSwiftCode()
        );
    }


    public Map<String, Object> getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCode> codes = repository.findByCountryISO2(countryISO2.toUpperCase());
        if (codes.isEmpty()) {
            throw new GlobalExceptionHandler.SwiftCodeNotFoundException("No SWIFT codes found for country: " + countryISO2);
        }

        return Map.of(
                "countryISO2", countryISO2.toUpperCase(),
                "countryName", codes.get(0).getCountryName(),
                "swiftCodes", codes
        );
    }


    public SwiftCode addSwiftCode(SwiftCode swiftCode) {
        if (repository.existsBySwiftCode(swiftCode.getSwiftCode())) {
            throw new GlobalExceptionHandler.SwiftCodeAlreadyExistsException("SWIFT code already exists: " + swiftCode.getSwiftCode());
        }

        if (swiftCode.getSwiftCode() == null || swiftCode.getSwiftCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SWIFT code cannot be empty or null");
        }

        return repository.save(swiftCode);
    }

    public Map<String, String> deleteSwiftCode(String swiftCode, String bankName, String countryISO2) {
        SwiftCode code = repository.findBySwiftCodeAndBankNameAndCountryISO2(
                swiftCode, bankName, countryISO2.toUpperCase()
        ).orElseThrow(() -> new GlobalExceptionHandler.SwiftCodeNotFoundException("SWIFT code not found with the given details"));

        if (code.isHeadquarter()) {
            List<SwiftCode> branches = repository.findByRelatedHeadquarterId(code.getId());
            repository.deleteAll(branches);
        }
        repository.delete(code);
        return Map.of("message", "SWIFT code deleted successfully");
    }
}
