package com.example.swiftcodes.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String swiftCode;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String countryISO2;

    @Column(nullable = false)
    private String countryName;

    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "related_headquarter_id")
    private SwiftCode relatedHeadquarter;

    public SwiftCode(String swiftCode, String bankName, String address, String countryISO2, String countryName, boolean isHeadquarter, SwiftCode relatedHeadquarter) {
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.address = address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.relatedHeadquarter = relatedHeadquarter;
    }
}
