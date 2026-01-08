package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.repositories.DepartmentRepository;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // ==================== BASIC CRUD ====================
    
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }
    
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name);
    }
    
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
    
    // ==================== DEPARTMENT OPERATIONS ====================
    
    // Set the manager of a department
    public Department setDepartmentManager(Long deptNum, Long empNum) {
        Department department = departmentRepository.findById(deptNum)
            .orElseThrow(() -> new RuntimeException("Department not found"));
        Employee manager = employeeRepository.findById(empNum)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        department.setManager(manager);
        
        // Also set this employee's department
        manager.setDepartment(department);
        employeeRepository.save(manager);
        
        return departmentRepository.save(department);
    }
    
    // Change department name
    public Department changeDepartmentName(Long deptNum, String newName) {
        Department department = departmentRepository.findById(deptNum)
            .orElseThrow(() -> new RuntimeException("Department not found"));
        
        department.setDepartmentName(newName);
        return departmentRepository.save(department);
    }
    
    // Merge two departments (A absorbs B)
    public Department mergeDepartments(String deptAName, String deptBName) {
        Department deptA = departmentRepository.findByDepartmentName(deptAName)
            .orElseThrow(() -> new RuntimeException("Department A not found"));
        Department deptB = departmentRepository.findByDepartmentName(deptBName)
            .orElseThrow(() -> new RuntimeException("Department B not found"));
        
        // Get all employees from department B
        List<Employee> deptBEmployees = employeeRepository.findByDepartment(deptB);
        
        // Move them all to department A
        for (Employee emp : deptBEmployees) {
            emp.setDepartment(deptA);
            employeeRepository.save(emp);
        }
        
        // Make B's manager report to A's manager
        if (deptB.getManager() != null && deptA.getManager() != null) {
            deptB.getManager().setManager(deptA.getManager());
            employeeRepository.save(deptB.getManager());
        }
        
        // Delete department B
        departmentRepository.delete(deptB);
        
        return deptA;
    }
    
    // Remove all employees from a department
    public void removeAllEmployeesFromDepartment(Long deptNum) {
        List<Employee> employees = employeeRepository.findByDepartment_DepartmentNumber(deptNum);
        employeeRepository.deleteAll(employees);
    }
}