package com.example.swiftcodes.repositories;

import com.example.swiftcodes.models.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    Optional<SwiftCode> findBySwiftCode(String swiftCode);

    List<SwiftCode> findByCountryISO2(String countryISO2);

    List<SwiftCode> findByRelatedHeadquarterId(Long headquarterId);

    Optional<SwiftCode> findBySwiftCodeAndBankNameAndCountryISO2(String swiftCode, String bankName, String countryISO2);

    boolean existsBySwiftCode(String swiftCode);

}
