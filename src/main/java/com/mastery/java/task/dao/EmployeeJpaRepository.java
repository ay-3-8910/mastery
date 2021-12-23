package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sergey Tsynin
 */
@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee, Integer> {

}
