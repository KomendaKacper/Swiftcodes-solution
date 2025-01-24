package com.example.swiftcodes;

import com.example.swiftcodes.models.SwiftCode;
import com.example.swiftcodes.repositories.SwiftCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        SwiftCode hq = new SwiftCode(
                "HQ123XXX",
                "Test Bank HQ",
                "123 HQ Street",
                "US",
                "UNITED STATES",
                true,
                null
        );

        SwiftCode branch = new SwiftCode(
                "HQ123001",
                "Test Bank Branch",
                "456 Branch Street",
                "US",
                "UNITED STATES",
                false,
                hq
        );

        repository.saveAll(List.of(hq, branch));
    }

    @Test
    void testGetSwiftCodeDetails_HQ() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/HQ123XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("HQ123XXX"))
                .andExpect(jsonPath("$.bankName").value("Test Bank HQ"))
                .andExpect(jsonPath("$.isHeadquarter").value(true))
                .andExpect(jsonPath("$.countryISO2").value("US"))
                .andExpect(jsonPath("$.branches", hasSize(1)))
                .andExpect(jsonPath("$.branches[0].swiftCode").value("HQ123001"));
    }

    @Test
    void testGetSwiftCodeDetails_Branch() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/HQ123001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("HQ123001"))
                .andExpect(jsonPath("$.bankName").value("Test Bank Branch"))
                .andExpect(jsonPath("$.isHeadquarter").value(false))
                .andExpect(jsonPath("$.branches").doesNotExist());
    }

    @Test
    void testGetSwiftCodeDetails_NotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/INVALID123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found: INVALID123"));
    }

    @Test
    void testGetSwiftCodesByCountry() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("US"))
                .andExpect(jsonPath("$.countryName").value("UNITED STATES"))
                .andExpect(jsonPath("$.swiftCodes", hasSize(2)))
                .andExpect(jsonPath("$.swiftCodes[*].swiftCode", containsInAnyOrder("HQ123XXX", "HQ123001")));
    }

    @Test
    void testGetSwiftCodesByCountry_NotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No SWIFT codes found for country: INVALID"));
    }

    @Test
    void testAddSwiftCode() throws Exception {
        SwiftCode newCode = new SwiftCode(
                "NEW123XXX",
                "New Bank HQ",
                "789 New Street",
                "GB",
                "UNITED KINGDOM",
                true,
                null
        );

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("NEW123XXX"))
                .andExpect(jsonPath("$.bankName").value("New Bank HQ"));
    }

    @Test
    void testAddSwiftCode_Duplicate() throws Exception {
        String duplicatePayload = """
        {
            "swiftCode": "HQ123XXX",
            "bankName": "Duplicate Bank HQ",
            "address": "123 Duplicate Street",
            "countryISO2": "US",
            "countryName": "UNITED STATES",
            "isHeadquarter": true
        }
        """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicatePayload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SWIFT code already exists: HQ123XXX"));
    }

    @Test
    void testDeleteSwiftCode_HQ_WithBranches() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/HQ123XXX")
                        .param("bankName", "Test Bank HQ")
                        .param("countryISO2", "US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));

        mockMvc.perform(get("/v1/swift-codes/HQ123001"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSwiftCode_Branch() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/HQ123001")
                        .param("bankName", "Test Bank Branch")
                        .param("countryISO2", "US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));
    }

    @Test
    void testDeleteSwiftCode_NotFound() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/INVALID123")
                        .param("bankName", "Invalid Bank")
                        .param("countryISO2", "US"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found with the given details"));
    }

    @Test
    void testAddBranchForHeadquarter() throws Exception {
        String branchPayload = """
    {
        "swiftCode": "HQ123002",
        "bankName": "Test Bank Branch 2",
        "address": "789 Branch Street",
        "countryISO2": "US",
        "countryName": "UNITED STATES",
        "isHeadquarter": false
    }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("HQ123002"))
                .andExpect(jsonPath("$.bankName").value("Test Bank Branch 2"));
    }

    @Test
    void testDeleteHeadquarterWithoutBranches() throws Exception {
        String newHQPayload = """
    {
        "swiftCode": "HQ999XXX",
        "bankName": "New Bank HQ",
        "address": "999 HQ Street",
        "countryISO2": "US",
        "countryName": "UNITED STATES",
        "isHeadquarter": true
    }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newHQPayload))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/v1/swift-codes/HQ999XXX")
                        .param("bankName", "New Bank HQ")
                        .param("countryISO2", "US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));
    }

    @Test
    void testAddSwiftCode_InvalidPayload() throws Exception {
        String invalidPayload = """
    {
        "swiftCode": "",
        "bankName": "Invalid Bank",
        "address": "",
        "countryISO2": "",
        "countryName": "",
        "isHeadquarter": true
    }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
