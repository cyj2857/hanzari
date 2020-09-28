//package service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import dto.EmployeeDto;
//import model.Employee;
//import repository.EmployeeRepository;
//
//public class EmployeeServiceImpl implements EmployeeService {
//
//	@Autowired
//	private EmployeeRepository employeeRepository;
//
//	@Override
//	public List<EmployeeDto> findAll() {
//		List<EmployeeDto> employee = new ArrayList<>();
//		employeeRepository.findAll().forEach(
//				e -> employee.add(
//						new EmployeeDto(
//								e.getEmployee_id(),
//								e.getAuthority(),
//								e.getEmployee_name(),
//								e.getDepartment().getDepartment_id(),
//								e.getExtension_number(),
//								e.seatIdList()
//						)
//					)
//				);
//		return employee;
//	}
//
//	@Override
//	public EmployeeDto findById(String employee_id) throws Exception {
//		Optional<Employee> optional = employeeRepository.findById(employee_id);
//		if(optional.isPresent()) {
//			Employee result = optional.get();
//			EmployeeDto employeeDto = new EmployeeDto(
//					result.getEmployee_id(),
//					result.getAuthority(),
//					result.getEmployee_name(),
//					result.getDepartment().getDepartment_id(),
//					result.getExtension_number(),
//					result.seatIdList()
//			);
//			return employeeDto;
//		} else {
//			throw new Exception();
//		}
//	}
//
//	@Override
//	public void deleteById(String employee_id) {
//		employeeRepository.deleteById(employee_id);
//	}
//
//	@Override
//	public EmployeeDto save(EmployeeDto employee) {
//		employee.toEntity
//		employeeRepository.save(employee)
//	}
//
//	@Override
//	public void updateById(String employee_id) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
