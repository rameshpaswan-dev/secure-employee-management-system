package in.ramesh.service;

import in.ramesh.payload.Request;
import in.ramesh.payload.Response;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    // Create
    Response createEmployee(Request request);

    // Read all
    List<Response> getAllEmployees();

    // Read by ID
    Response getEmployeeById(Integer id);

    // Update
    Response updateEmployee(Integer id, Request request);

    // Delete
    void deleteEmployeeById(Integer id);

    // Pagination + Sorting
    Page<Response> getAllEmployees(int page, int size, String sortBy, String direction);
}