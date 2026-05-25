package in.ramesh.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import in.ramesh.payload.Request;
import in.ramesh.payload.Response;
import in.ramesh.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

	private final EmployeeService employeeService;

	// ✅ Create Employee
	@PostMapping
	public ResponseEntity<Response> createEmployee(@Valid @RequestBody Request request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
	}

	// ✅ Get all Employees (NON-PAGINATED)
	@GetMapping
	public ResponseEntity<List<Response>> getAllEmployees() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	// ✅ PAGINATED + SORTED API (🔥 NEW INDUSTRY STANDARD)
	@GetMapping("/page")
	public ResponseEntity<Page<Response>> getEmployeesPaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		return ResponseEntity.ok(employeeService.getAllEmployees(page, size, sortBy, direction));
	}

	// ✅ Get Employee by ID
	@GetMapping("/{id}")
	public ResponseEntity<Response> getEmployeeById(
			@PathVariable @Min(value = 1, message = "Id must be greater than 0") Integer id) {

		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}

	// ✅ Update Employee
	@PutMapping("/{id}")
	public ResponseEntity<Response> updateEmployee(@PathVariable @Min(1) Integer id,
			@Valid @RequestBody Request request) {

		return ResponseEntity.ok(employeeService.updateEmployee(id, request));
	}

	// ✅ Delete Employee
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable @Min(1) Integer id) {

		employeeService.deleteEmployeeById(id);
		return ResponseEntity.noContent().build();
	}
}