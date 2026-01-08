package io.zipcoder.persistenceapp.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_number")
    private Long employeeNumber;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    // Self-referential: Employee reports to another Employee (manager)
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties({"directReports", "manager"})
    private Employee manager;
    
    // Inverse: One manager has many direct reports
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"directReports", "manager"}) 
    private List<Employee> directReports = new ArrayList<>();
    
    // Many employees belong to one department
    @ManyToOne
    @JoinColumn(name = "dept_num")
    @JsonIgnoreProperties({"employees", "manager"})
    private Department department;
    
    // Constructors
    public Employee() {}
    
    public Employee(String firstName, String lastName, String title, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getEmployeeNumber() {
        return employeeNumber;
    }
    
    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public Employee getManager() {
        return manager;
    }
    
    public void setManager(Employee manager) {
        this.manager = manager;
    }
    
    public List<Employee> getDirectReports() {
        return directReports;
    }
    
    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
}