package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.exception.DuplicatedIdException;
import com.thoughtworks.springbootemployee.exception.NotFoundException;
import com.thoughtworks.springbootemployee.exception.OutOfRangeException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.CompanyService;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeTests {

	@InjectMocks
	private EmployeeService employeeService;
	@Mock
	private EmployeeRepository employeeRepository;

	@Test
	void should_return_employees_when_add_employee_given_no_employees() throws DuplicatedIdException {
		//given
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		when(employeeRepository.add(employee)).thenReturn(employee);

		//when
		final Employee actual = employeeService.add(employee);

		//then
		assertEquals(employee, actual);
	}

	@Test
	void should_return_all_employees_when_get_all_employee_given_employees() throws DuplicatedIdException {
		//given
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		final List<Employee> expected = Collections.singletonList(employee);
		employeeService.add(employee);
		when(employeeRepository.getAll()).thenReturn(expected);
		//when
		final List<Employee> actual = employeeService.getAll();

		//then
		assertEquals(expected, actual);

	}

	@Test
	void should_return_specific_employees_when_get_employee_given_employees_employee_id() throws DuplicatedIdException {
		//given
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		employeeService.add(employee);
		when(employeeRepository.get(1)).thenReturn(employee);

		//when
		final Employee actual = employeeService.get(1);

		//then
		assertEquals(employee, actual);

	}

	@Test
	void should_return_all_male_employees_when_get_all_employee_by_gender_given_employees_male() throws DuplicatedIdException {
		//given

		employeeService.add(new Employee(1, "test", 18, 1000, "male"));
		employeeService.add(new Employee(2, "test", 18, 1000, "male"));
		employeeService.add(new Employee(3, "test", 18, 1000, "female"));
		List<Employee> employees = Arrays.asList(new Employee(1, "test", 18, 1000, "male"), new Employee(2, "test", 18, 1000, "male"));
		when(employeeRepository.getAllByGender("male")).thenReturn(employees);
		//when
		final List<Employee> actual = employeeService.getAllByGender("male");

		//then
		assertEquals(employees.size(), actual.size());

	}

	@Test
	void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() throws DuplicatedIdException, OutOfRangeException {
		//given
		Employee employee1 = new Employee(1, "test", 18, 1000, "male");
		Employee employee2 = new Employee(2, "test", 18, 1000, "female");

		employeeService.add(employee1);
		employeeService.add(employee2);
		employeeService.add(new Employee(3, "test", 18, 1000, "female"));

		final List<Employee> expected = Arrays.asList(employee1, employee2);
		when(employeeRepository.getAllByPage(1, 2)).thenReturn(expected);
		//when
		final List<Employee> actual = employeeService.getAllByPage(1, 2);

		//then
		assertEquals(expected, actual);

	}
}