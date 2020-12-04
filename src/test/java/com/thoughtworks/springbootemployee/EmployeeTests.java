package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeTests {

    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void should_return_employees_when_add_employee_given_no_employees() {
        //given
        Employee employee = new Employee("test", 18, 1000, "male");
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        final Employee actual = employeeService.add(employee);

        //then
        assertEquals(employee, actual);
    }

    @Test
    void should_return_all_employees_when_get_all_employee_given_employees() {
        //given
        Employee employee = new Employee("test", 18, 1000, "male");
        final List<Employee> expected = Collections.singletonList(employee);
        employeeService.add(employee);
        when(employeeRepository.findAll()).thenReturn(expected);
        //when
        final List<Employee> actual = employeeService.getAll();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void should_return_specific_employees_when_get_employee_given_employees_employee_id() {
        //given
        Employee employee = new Employee("test", 18, 1000, "male");
        employeeService.add(employee);
        when(employeeRepository.findById("1")).thenReturn(java.util.Optional.of(employee));

        //when
        final Employee actual = employeeService.get("1");

        //then
        assertEquals(employee, actual);

    }

    @Test
    void should_return_all_male_employees_when_get_all_employee_by_gender_given_employees_male() {
        //given

        employeeService.add(new Employee("test", 18, 1000, "male"));
        employeeService.add(new Employee("test", 18, 1000, "male"));
        employeeService.add(new Employee("test", 18, 1000, "female"));
        List<Employee> employees = Arrays.asList(new Employee("test", 18, 1000, "male"), new Employee("test", 18, 1000, "male"));
        when(employeeRepository.findAllByGender("male")).thenReturn(employees);
        //when
        final List<Employee> actual = employeeService.getAllByGender("male");

        //then
        assertEquals(employees.size(), actual.size());

    }

    @Test
    void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() {
        //given
        Employee employee1 = new Employee("test", 18, 1000, "male");
        Employee employee2 = new Employee("test", 18, 1000, "female");

        employeeService.add(employee1);
        employeeService.add(employee2);
        employeeService.add(new Employee("test", 18, 1000, "female"));

        final Page<Employee> expected = new PageImpl<>(Arrays.asList(employee1, employee2));
        when(employeeRepository.findAll(PageRequest.of(0, 2))).thenReturn(expected);
        //when
        final Page<Employee> actual = employeeService.getAllByPage(1, 2);

        //then
        assertEquals(expected, actual);

    }

    @Test
    void should_return_updated_employee_when_updated_employee_given_employees_new_employee() {
        //given

        Employee oldEmployee = new Employee("test", 18, 1000, "male");
        oldEmployee.setId("1");

        Employee employee = new Employee("testA", 18, 1000, "male");
        when(employeeRepository.findById("1")).thenReturn(java.util.Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        //when
        final Employee actual = employeeService.update(oldEmployee.getId(), employee);

        //then
        assertEquals(employee, actual);

    }

    @Test
    void should_return_null_when_delete_employee_given_employees_new_employee() {
        //given
        Employee employee = new Employee("test", 18, 1000, "male");
        employeeService.add(employee);

        //when
        employeeService.remove("1");
        final ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        //then
        verify(employeeRepository, times(1)).deleteById("1");

    }
}
