package io.zipcoder.persistenceapp.repositories;

import io.zipcoder.persistenceapp.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Find department by name
    Optional<Department> findByDepartmentName(String name);
    
    // Check if department exists by name
    boolean existsByDepartmentName(String name);
}