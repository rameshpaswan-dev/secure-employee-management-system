	package in.ramesh.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import in.ramesh.entity.Employee;
import in.ramesh.exception.ResourceNotFoundException;
import in.ramesh.payload.Request;
import in.ramesh.payload.Response;
import in.ramesh.repository.EmployeeRepository;
import in.ramesh.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "name", "department", "role", "salary");

    // ✅ CREATE
    @Override
    public Response createEmployee(Request request) {

        Employee employee = Employee.builder()
                .name(request.getName())
                .department(request.getDepartment())
                .role(request.getRole())
                .salary(request.getSalary())
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    // ✅ GET BY ID
    @Override
    public Response getEmployeeById(Integer id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        return mapToResponse(employee);
    }

    // ✅ UPDATE
    @Override
    public Response updateEmployee(Integer id, Request request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getRole());
        employee.setSalary(request.getSalary());

        return mapToResponse(employeeRepository.save(employee));
    }

    // ✅ DELETE
    @Override
    public void deleteEmployeeById(Integer id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        employeeRepository.deleteById(id);
    }

    // ✅ GET ALL (NON-PAGINATED)
    @Override
    public List<Response> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ PAGINATION + SORTING (IMPROVED)
    @Override
    public Page<Response> getAllEmployees(int page, int size, String sortBy, String direction) {

        String sortField = sortBy.toLowerCase();

        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // ✅ MAPPER (clean separation)
    private Response mapToResponse(Employee employee) {

        return Response.builder()
                .id(employee.getId())
                .name(employee.getName())
                .department(employee.getDepartment())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .build();
    }
}