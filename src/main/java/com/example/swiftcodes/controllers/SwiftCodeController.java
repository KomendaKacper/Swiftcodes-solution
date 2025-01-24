package com.example.swiftcodes.controllers;

import com.example.swiftcodes.models.SwiftCode;
import com.example.swiftcodes.services.SwiftCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/swift-codes")
@Tag(name = "Swift Codes", description = "Manage SWIFT codes in the system")
public class SwiftCodeController {

    private final SwiftCodeService service;

    public SwiftCodeController(SwiftCodeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get SWIFT code details",
            description = "Returns detailed information about a SWIFT code, including bank address and associated branches, if applicable."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SWIFT code details"),
            @ApiResponse(responseCode = "404", description = "SWIFT code not found")
    })
    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCodeDetails(@Parameter(description = "The SWIFT code to retrieve details for") @PathVariable String swiftCode) {
        return ResponseEntity.ok(service.getSwiftCodeDetails(swiftCode));
    }

    @Operation(
            summary = "Get SWIFT codes by country",
            description = "Returns a list of SWIFT codes associated with a given country, based on the country ISO2 code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of SWIFT codes for the country"),
            @ApiResponse(responseCode = "404", description = "No SWIFT codes found for the country")
    })
    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<?> getSwiftCodesByCountry(@Parameter(description = "The ISO2 country code to retrieve SWIFT codes") @PathVariable String countryISO2) {
        return ResponseEntity.ok(service.getSwiftCodesByCountry(countryISO2));
    }

    @Operation(
            summary = "Add a new SWIFT code",
            description = "Adds a new SWIFT code to the system. Ensures the SWIFT code does not already exist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SWIFT code added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or existing SWIFT code"),
            @ApiResponse(responseCode = "409", description = "SWIFT code already exists")
    })
    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody SwiftCode swiftCode) {
        return ResponseEntity.ok(service.addSwiftCode(swiftCode));
    }

    @Operation(
            summary = "Delete a SWIFT code",
            description = "Deletes a SWIFT code and its associated branches (if it is a headquarter)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SWIFT code deleted successfully"),
            @ApiResponse(responseCode = "404", description = "SWIFT code not found")
    })
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(
            @Parameter(description = "The SWIFT code to delete") @PathVariable String swiftCode,
            @RequestParam String bankName,
            @RequestParam String countryISO2) {
        return ResponseEntity.ok(service.deleteSwiftCode(swiftCode, bankName, countryISO2));
    }
}
