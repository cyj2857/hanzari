package dto;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Employee;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

	@NotBlank
	String employee_id;

	@NotBlank
	String authority;

	@NotBlank
	String employee_name;

	@NotBlank
	String department_id;

	String extension_number;

	List<String> seatIdList;
	
//	public Employee toEntity() {
//		new Employee(employee_id, authority, employee_name, extension_number, department_id);
//	}
}
