package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    public void should_return_all_employees_when_get_all_given_employees() throws Exception {
        //given
        Employee employee = new Employee("Victor", 18, 1000, "male");
        employeeRepository.save(employee);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value("Victor"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].salary").value(1000))
                .andExpect(jsonPath("$[0].gender").value("male"));
        //then
    }

    @Test
    public void should_return_employee_when_add_employee_given_employee() throws Exception {
        //given
        String employeeAsJson = "{\n" +
                "    \"name\" : \"Victor\",\n" +
                "    \"age\"   : \"18\",\n" +
                "    \"salary\" : \"1000\",\n" +
                "    \"gender\" : \"male\"\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isString())
                .andExpect(jsonPath("name").value("Victor"))
                .andExpect(jsonPath("age").value(18))
                .andExpect(jsonPath("salary").value(1000))
                .andExpect(jsonPath("gender").value("male"));
        List<Employee> employeeList = employeeRepository.findAll();
        assertEquals(1, employeeList.size());
        assertEquals("Victor", employeeList.get(0).getName());
        assertEquals(18, employeeList.get(0).getAge());
        assertEquals(1000, employeeList.get(0).getSalary());
        assertEquals("male", employeeList.get(0).getGender());
    }

    @Test
    public void should_return_specific_employee_when_get_employee_given_employee_id() throws Exception {
        //given
        Employee employee = employeeRepository.save(new Employee("Victor", 18, 1000, "male"));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isString())
                .andExpect(jsonPath("name").value("Victor"))
                .andExpect(jsonPath("age").value(18))
                .andExpect(jsonPath("salary").value(1000))
                .andExpect(jsonPath("gender").value("male"));
    }


    @Test
    public void should_return_not_found_when_get_employee_given_invalid_employee_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/" + "6fc9d0e060e64c0326fd2e92"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_all_male_employees_when_get_all_employee_by_gender_given_employees_male() throws Exception {
        //given
        employeeRepository.save(new Employee("Victor", 18, 1000, "male"));
        employeeRepository.save(new Employee("Vic", 18, 1000, "male"));
        employeeRepository.save(new Employee("Victor", 18, 1000, "female"));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees").param("gender", "male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() throws Exception {
        //given
        Employee employee1 = employeeRepository.save(new Employee("Victor", 18, 1000, "male"));
        Employee employee2 = employeeRepository.save(new Employee("Mary", 19, 2000, "female"));
        employeeRepository.save(new Employee("Mandy", 18, 1000, "female"));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                .param("page", "1")
                .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value(employee1.getName()))
                .andExpect(jsonPath("$[0].age").value(employee1.getAge()))
                .andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
                .andExpect(jsonPath("$[0].gender").value(employee1.getGender()))
                .andExpect(jsonPath("$[1].id").isString())
                .andExpect(jsonPath("$[1].name").value(employee2.getName()))
                .andExpect(jsonPath("$[1].age").value(employee2.getAge()))
                .andExpect(jsonPath("$[1].salary").value(employee2.getSalary()))
                .andExpect(jsonPath("$[1].gender").value(employee2.getGender()));
    }


    @Test
    public void should_return_updated_employee_when_updated_employee_given_employees_new_employee() throws Exception {
        //given

        Employee employee = employeeRepository.save(new Employee("Victor", 18, 1000, "male"));

        String employeeAsJson = "{\n" +
                "    \"name\" : \"Mary\",\n" +
                "    \"age\"   : \"19\",\n" +
                "    \"salary\" : \"10000\",\n" +
                "    \"gender\" : \"female\"\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.name").value("Mary"))
                .andExpect(jsonPath("$.age").value(19))
                .andExpect(jsonPath("$.salary").value(10000))
                .andExpect(jsonPath("$.gender").value("female"));
        List<Employee> employeeList = employeeRepository.findAll();
        assertEquals(1, employeeList.size());
        assertEquals("Mary", employeeList.get(0).getName());
        assertEquals(19, employeeList.get(0).getAge());
        assertEquals(10000, employeeList.get(0).getSalary());
        assertEquals("female", employeeList.get(0).getGender());
    }

    @Test
    public void should_return_not_found_when_updated_employee_given_employees_invalid_employee_id() throws Exception {
        //given

        String employeeAsJson = "{\n" +
                "    \"name\" : \"Mary\",\n" +
                "    \"age\"   : \"19\",\n" +
                "    \"salary\" : \"10000\",\n" +
                "    \"gender\" : \"female\"\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/" + "6fc9d0e060e64c0326fd2e92")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_null_when_delete_employee_given_employees_new_employee() throws Exception {
        //given
        Employee employee = employeeRepository.save(new Employee("Victor", 18, 1000, "male"));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/" + employee.getId()))
                .andExpect(status().isNoContent());
        List<Employee> employeeList = employeeRepository.findAll();
        assertEquals(0, employeeList.size());
    }

}
