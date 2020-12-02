package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.exception.DuplicatedIdException;
import com.thoughtworks.springbootemployee.exception.NotFoundException;
import com.thoughtworks.springbootemployee.exception.OutOfRangeException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SpringBootEmployeeApplicationTests {

	@Test
	void should_return_employees_when_add_employee_given_no_employees() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Employee employee = new Employee(1, "test", 18, 1000, "male");

		//when
		final Employee actual = employeeService.add(employee);

		//then
		assertEquals(employee, actual);
	}

	@Test
	void should_return_error_when_add_employee_given_employee_with_same_id() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		employeeService.add(employee);
		//when
		final DuplicatedIdException duplicatedIdException = assertThrows(DuplicatedIdException.class,
				() -> employeeService.add(employee)
		);
		//then
		assertEquals("Duplicated ID", duplicatedIdException.getMessage());

	}

	@Test
	void should_return_all_employees_when_get_all_employee_given_employees() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		final List<Employee> expected = Collections.singletonList(employee);
		employeeService.add(employee);
		//when
		final List<Employee> actual = employeeService.getAll();

		//then
		assertEquals(expected, actual);

	}
	@Test
	void should_return_exception_employees_when_get_employee_given_invalid_employee_id() {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		//when
		final NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> employeeService.get(1)
		);
		//then
		assertEquals("Not Found", notFoundException.getMessage());

	}

	@Test
	void should_return_specific_employees_when_get_employee_given_employees_employee_id() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Employee employee = new Employee(1, "test", 18, 1000, "male");
		employeeService.add(employee);

		//when
		final Employee actual = employeeService.get(1);

		//then
		assertEquals(employee, actual);

	}

	@Test
	void should_return_exception_when_update_employee_given_invalid_employee_id() {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		//when
		final NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> employeeService.update(1, new Employee())
		);
		//then
		assertEquals("Not Found", notFoundException.getMessage());

	}


	@Test
	void should_return_exception_when_delete_employee_and_get_given_employee() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		employeeService.add(new Employee(1, "test", 18, 1000, "male"));

		employeeService.remove(1);

		//when
		final NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> employeeService.get(1)
		);
		//then
		assertEquals("Not Found", notFoundException.getMessage());

	}

	@Test
	void should_return_all_male_employees_when_get_all_employee_by_gender_given_employees_male() throws DuplicatedIdException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		employeeService.add(new Employee(1, "test", 18, 1000, "male"));
		employeeService.add(new Employee(2, "test", 18, 1000, "male"));
		employeeService.add(new Employee(3, "test", 18, 1000, "female"));
		//when
		final List<Employee> actual = employeeService.getAllByGender("male");

		//then
		assertEquals(2, actual.size());

	}


	@Test
	void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() throws DuplicatedIdException, OutOfRangeException {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		Employee employee1 = new Employee(1, "test", 18, 1000, "male");
		Employee employee2 = new Employee(2, "test", 18, 1000, "female");

		employeeService.add(employee1);
		employeeService.add(employee2);
		employeeService.add(new Employee(3, "test", 18, 1000, "female"));

		final List<Employee> expected = Arrays.asList(employee1, employee2);
		//when
		final List<Employee> actual = employeeService.getAllByPage(0, 2);

		//then
		assertEquals(expected, actual);

	}

	@Test
	void should_return_exception_when_get_employee_by_page_given_employees_invalid_page() {
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		//when
		final OutOfRangeException outOfRangeException = assertThrows(OutOfRangeException.class,
				() -> employeeService.getAllByPage(-1,2)
		);
		//then
		assertEquals("Out of range", outOfRangeException.getMessage());

	}

	@Test
	void should_return_exception_when_get_employee_by_page_given_employees_invalid_page_size(){
		//given
		EmployeeRepository employeeRepository = new EmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);

		//when
		final OutOfRangeException outOfRangeException = assertThrows(OutOfRangeException.class,
				() -> employeeService.getAllByPage(2,-1)
		);
		//then
		assertEquals("Out of range", outOfRangeException.getMessage());

	}
}
