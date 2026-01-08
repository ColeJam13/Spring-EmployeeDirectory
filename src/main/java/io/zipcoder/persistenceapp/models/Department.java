package io.zipcoder.persistenceapp.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_num")
    private Long departmentNumber;
    
    @Column(name = "dept_name", nullable = false, unique = true)
    private String departmentName;
    
    // One department has one manager (who is an Employee)
    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    // One department has many employees
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();
    
    // Constructors
    public Department() {}
    
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }
    
    // Getters and Setters
    public Long getDepartmentNumber() {
        return departmentNumber;
    }
    
    public void setDepartmentNumber(Long departmentNumber) {
        this.departmentNumber = departmentNumber;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public Employee getManager() {
        return manager;
    }
    
    public void setManager(Employee manager) {
        this.manager = manager;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}