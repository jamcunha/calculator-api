package pt.jamcunha.rest.dto;

import java.math.BigDecimal;

public record OperationDTO(String operation, BigDecimal a, BigDecimal b, String requestId) {

}
