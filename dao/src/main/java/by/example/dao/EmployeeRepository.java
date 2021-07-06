package by.example.dao;

import by.example.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sergey Tsynin
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}