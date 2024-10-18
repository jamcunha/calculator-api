package pt.jamcunha.rest.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CalculatorController {
	// TODO: Add service
	// TODO: Implement error handling

	@GetMapping("/sum")
	public Map<String, BigDecimal> sum(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b) {
		return Map.of("result", a.add(b));
	}

	@GetMapping("/sub")
	public Map<String, BigDecimal> sub(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b) {
		return Map.of("result", a.subtract(b));
	}

	@GetMapping("/mul")
	public Map<String, BigDecimal> mul(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b) {
		return Map.of("result", a.multiply(b));
	}

	@GetMapping("/div")
	public Map<String, BigDecimal> div(@RequestParam final BigDecimal a, @RequestParam final BigDecimal b) {
		return Map.of("result", a.divide(b));
	}
}
