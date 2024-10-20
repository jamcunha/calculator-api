package pt.jamcunha.calculator.dto;

import java.math.BigDecimal;

public record OperationDTO(String operation, BigDecimal a, BigDecimal b) {

}
