package io.zipcoder.persistenceapp.repositories;

import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Find all employees who report directly to a manager
    List<Employee> findByManager(Employee manager);
    
    // Find all employees with no manager
    List<Employee> findByManagerIsNull();
    
    // Find all employees in a department
    List<Employee> findByDepartment(Department department);
    
    // Find by department number
    List<Employee> findByDepartment_DepartmentNumber(Long deptNum);
    
    // Custom query to find all employees under a manager (we'll use this for recursive queries)
    @Query("SELECT e FROM Employee e WHERE e.manager.employeeNumber = :managerId")
    List<Employee> findDirectReportsByManagerId(@Param("managerId") Long managerId);
}