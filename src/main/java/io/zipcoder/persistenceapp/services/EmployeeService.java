package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // ==================== BASIC CRUD ====================
    
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existing = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        existing.setFirstName(updatedEmployee.getFirstName());
        existing.setLastName(updatedEmployee.getLastName());
        existing.setTitle(updatedEmployee.getTitle());
        existing.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setHireDate(updatedEmployee.getHireDate());
        
        return employeeRepository.save(existing);
    }
    
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    
    // ==================== MANAGER OPERATIONS ====================
    
    public Employee setManager(Long employeeId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
        
        employee.setManager(manager);
        
        // When setting a manager, also update department to match manager's department
        if (manager.getDepartment() != null) {
            employee.setDepartment(manager.getDepartment());
        }
        
        return employeeRepository.save(employee);
    }
    
    public List<Employee> getDirectReports(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
        return employeeRepository.findByManager(manager);
    }
    
    // ==================== HIERARCHICAL QUERIES ====================
    
    // Get reporting hierarchy (employee -> manager -> manager's manager -> etc.)
    public List<Employee> getReportingHierarchy(Long employeeId) {
        List<Employee> hierarchy = new ArrayList<>();
        Employee current = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Walk up the chain
        while (current != null) {
            hierarchy.add(current);
            current = current.getManager();
        }
        
        return hierarchy;
    }
    
    // Get all reports (direct + indirect) under a manager
    public List<Employee> getAllReports(Long managerId) {
        List<Employee> allReports = new ArrayList<>();
        collectAllReports(managerId, allReports);
        return allReports;
    }
    
    // Recursive helper to collect all reports
    private void collectAllReports(Long managerId, List<Employee> accumulator) {
        List<Employee> directReports = employeeRepository.findDirectReportsByManagerId(managerId);
        
        for (Employee report : directReports) {
            accumulator.add(report);
            // Recursively get their reports
            collectAllReports(report.getEmployeeNumber(), accumulator);
        }
    }
    
    // ==================== DEPARTMENT OPERATIONS ====================
    
    public List<Employee> getEmployeesByDepartment(Long deptNum) {
        return employeeRepository.findByDepartment_DepartmentNumber(deptNum);
    }
    
    public List<Employee> getEmployeesWithNoManager() {
        return employeeRepository.findByManagerIsNull();
    }
    
    // ==================== COMPLEX DELETIONS ====================
    
    // Remove all employees under a manager (including indirect reports)
    public void removeAllUnderManager(Long managerId) {
        List<Employee> allReports = getAllReports(managerId);
        employeeRepository.deleteAll(allReports);
    }
    
    // Remove direct reports only, reassign their reports to next manager up
    public void removeDirectReportsOnly(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
        
        List<Employee> directReports = employeeRepository.findByManager(manager);
        
        for (Employee report : directReports) {
            // Get this employee's direct reports
            List<Employee> theirReports = employeeRepository.findByManager(report);
            
            // Reassign them to the next manager up (the original manager)
            for (Employee subReport : theirReports) {
                subReport.setManager(manager);
                employeeRepository.save(subReport);
            }
            
            // Now delete the middle manager
            employeeRepository.delete(report);
        }
    }
    
    // Remove all employees from a department
    public void removeAllFromDepartment(Long deptNum) {
        List<Employee> employees = employeeRepository.findByDepartment_DepartmentNumber(deptNum);
        employeeRepository.deleteAll(employees);
    }
    
    // Delete a list of employees by IDs
    public void deleteEmployees(List<Long> employeeIds) {
        employeeRepository.deleteAllById(employeeIds);
    }
}