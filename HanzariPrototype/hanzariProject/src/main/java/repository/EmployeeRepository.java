package repository;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

import model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
	List <Employee> findByName(String name);
	List <Employee> findByDepartmentId(String department_id);
	List <Employee> findByExtensionNumber(String extension_number);
	
	Optional<Employee> findById(String employee_id);
}
