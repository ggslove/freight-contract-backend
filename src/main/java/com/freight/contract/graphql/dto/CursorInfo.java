package com.freight.contract.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursorInfo {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDate dateOfReceipt;
}