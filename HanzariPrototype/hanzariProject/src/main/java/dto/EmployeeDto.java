package dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	String department_name;

	String extension_number;
}
