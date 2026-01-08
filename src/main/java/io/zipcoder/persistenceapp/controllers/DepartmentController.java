package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/API/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    // ==================== BASIC CRUD ====================
    
    // CREATE department
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department created = departmentService.createDepartment(department);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    // GET all departments
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    // GET department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // GET department by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        Optional<Department> department = departmentService.getDepartmentByName(name);
        return department.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE department
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== DEPARTMENT OPERATIONS ====================
    
    // SET department manager
    @PutMapping("/{deptNum}/manager/{empNum}")
    public ResponseEntity<Department> setDepartmentManager(
            @PathVariable Long deptNum, 
            @PathVariable Long empNum) {
        Department updated = departmentService.setDepartmentManager(deptNum, empNum);
        return ResponseEntity.ok(updated);
    }
    
    // CHANGE department name
    @PutMapping("/{deptNum}/name")
    public ResponseEntity<Department> changeDepartmentName(
            @PathVariable Long deptNum, 
            @RequestBody String newName) {
        Department updated = departmentService.changeDepartmentName(deptNum, newName);
        return ResponseEntity.ok(updated);
    }
    
    // MERGE departments (A absorbs B)
    @PostMapping("/merge")
    public ResponseEntity<Department> mergeDepartments(
            @RequestParam String deptAName, 
            @RequestParam String deptBName) {
        Department merged = departmentService.mergeDepartments(deptAName, deptBName);
        return ResponseEntity.ok(merged);
    }
    
    // DELETE all employees from department
    @DeleteMapping("/{deptNum}/employees")
    public ResponseEntity<Void> removeAllEmployeesFromDepartment(@PathVariable Long deptNum) {
        departmentService.removeAllEmployeesFromDepartment(deptNum);
        return ResponseEntity.noContent().build();
    }
}