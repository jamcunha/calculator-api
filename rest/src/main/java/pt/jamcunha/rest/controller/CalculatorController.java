package pt.jamcunha.rest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.jamcunha.rest.dto.OperationDTO;
import pt.jamcunha.rest.service.CalculatorService;

@RestController
@RequestMapping("/")
public class CalculatorController {
	private final CalculatorService calculatorService;

	public CalculatorController(final CalculatorService calculatorService) {
		this.calculatorService = calculatorService;
	}

	@GetMapping("/sum")
	public Map<String, BigDecimal> sum(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b)
			throws IOException {
		BigDecimal result = calculatorService.sendOperation(new OperationDTO("SUM", a, b, MDC.get("requestId")));
		return Map.of("result", result);
	}

	@GetMapping("/sub")
	public Map<String, BigDecimal> sub(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b)
			throws IOException {
		BigDecimal result = calculatorService.sendOperation(new OperationDTO("SUB", a, b, MDC.get("requestId")));
		return Map.of("result", result);
	}

	@GetMapping("/mul")
	public Map<String, BigDecimal> mul(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b)
			throws IOException {
		BigDecimal result = calculatorService.sendOperation(new OperationDTO("MUL", a, b, MDC.get("requestId")));
		return Map.of("result", result);
	}

	@GetMapping("/div")
	public Map<String, BigDecimal> div(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b)
			throws IOException {
		BigDecimal result = calculatorService.sendOperation(new OperationDTO("DIV", a, b, MDC.get("requestId")));
		return Map.of("result", result);
	}
}
