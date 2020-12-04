package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee add(Employee requestEmployee) {
        return employeeRepository.save(requestEmployee);
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee get(String employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }

    public Employee update(String employeeId, Employee updateEmployee) {
        Employee foundEmployee = this.get(employeeId);
        foundEmployee.setAge(updateEmployee.getAge());
        foundEmployee.setGender(updateEmployee.getGender());
        foundEmployee.setName(updateEmployee.getName());
        foundEmployee.setSalary(updateEmployee.getSalary());
        return employeeRepository.save(foundEmployee);
    }

    public void remove(String employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public List<Employee> getAllByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public Page<Employee> getAllByPage(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page - 1, pageSize));
    }
}
