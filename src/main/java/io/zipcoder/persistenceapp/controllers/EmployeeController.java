package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/API/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    // ==================== BASIC CRUD ====================
    
    // CREATE employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    // GET all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    // GET employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    // UPDATE employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }
    
    // DELETE employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    // DELETE multiple employees
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmployees(@RequestBody List<Long> employeeIds) {
        employeeService.deleteEmployees(employeeIds);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== MANAGER OPERATIONS ====================
    
    // SET employee's manager
    @PutMapping("/{id}/manager/{managerId}")
    public ResponseEntity<Employee> setManager(@PathVariable Long id, @PathVariable Long managerId) {
        Employee updated = employeeService.setManager(id, managerId);
        return ResponseEntity.ok(updated);
    }
    
    // GET direct reports of a manager
    @GetMapping("/manager/{managerId}/direct")
    public ResponseEntity<List<Employee>> getDirectReports(@PathVariable Long managerId) {
        List<Employee> reports = employeeService.getDirectReports(managerId);
        return ResponseEntity.ok(reports);
    }
    
    // GET all reports (direct + indirect) under a manager
    @GetMapping("/manager/{managerId}/all")
    public ResponseEntity<List<Employee>> getAllReports(@PathVariable Long managerId) {
        List<Employee> reports = employeeService.getAllReports(managerId);
        return ResponseEntity.ok(reports);
    }
    
    // DELETE all employees under a manager (including indirect)
    @DeleteMapping("/manager/{managerId}/all")
    public ResponseEntity<Void> removeAllUnderManager(@PathVariable Long managerId) {
        employeeService.removeAllUnderManager(managerId);
        return ResponseEntity.noContent().build();
    }
    
    // DELETE direct reports only (reassign their reports up)
    @DeleteMapping("/manager/{managerId}/direct")
    public ResponseEntity<Void> removeDirectReportsOnly(@PathVariable Long managerId) {
        employeeService.removeDirectReportsOnly(managerId);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== HIERARCHICAL QUERIES ====================
    
    // GET reporting hierarchy for an employee (up the chain)
    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<List<Employee>> getReportingHierarchy(@PathVariable Long id) {
        List<Employee> hierarchy = employeeService.getReportingHierarchy(id);
        return ResponseEntity.ok(hierarchy);
    }
    
    // ==================== DEPARTMENT QUERIES ====================
    
    // GET employees by department
    @GetMapping("/department/{deptNum}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable Long deptNum) {
        List<Employee> employees = employeeService.getEmployeesByDepartment(deptNum);
        return ResponseEntity.ok(employees);
    }
    
    // DELETE all employees from a department
    @DeleteMapping("/department/{deptNum}")
    public ResponseEntity<Void> removeAllFromDepartment(@PathVariable Long deptNum) {
        employeeService.removeAllFromDepartment(deptNum);
        return ResponseEntity.noContent().build();
    }
    
    // GET employees with no manager
    @GetMapping("/no-manager")
    public ResponseEntity<List<Employee>> getEmployeesWithNoManager() {
        List<Employee> employees = employeeService.getEmployeesWithNoManager();
        return ResponseEntity.ok(employees);
    }
}