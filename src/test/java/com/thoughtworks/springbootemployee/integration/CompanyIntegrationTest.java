package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    public void should_return_all_companies_when_get_all_companies_given_companies() throws Exception {
        //given
        Company company = new Company("ABC Company", 1000, new ArrayList<Employee>());
        companyRepository.save(company);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value("ABC Company"))
                .andExpect(jsonPath("$[0].employeeNumber").value(1000))
                .andExpect(jsonPath("$[0].employees", hasSize(0)));
    }

    @Test
    public void should_return_company_when_create_company_given_company() throws Exception {
        //given
        String companyAsJson = "{\n" +
                "    \"name\" : \"ABC Company\",\n" +
                "    \"employeeNumber\" : \"1200\",\n" +
                "    \"employees\" : [\n" +
                "        {\n" +
                "            \"id\" : \"1\",\n" +
                "            \"name\" : \"Victor\",\n" +
                "            \"age\"   : \"18\",\n" +
                "            \"salary\" : \"1000\",\n" +
                "            \"gender\" : \"male\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("ABC Company"))
                .andExpect(jsonPath("$.employeeNumber").value(1200))
                .andExpect(jsonPath("$.employees", hasSize(1)));

        List<Company> companyList = companyRepository.findAll();
        assertEquals(1, companyList.size());
        assertEquals("ABC Company", companyList.get(0).getName());
        assertEquals(1200, companyList.get(0).getEmployeeNumber());
        assertEquals(1, companyList.get(0).getEmployees().size());
    }

    @Test
    public void should_return_specific_company_when_get_company_given_company_id() throws Exception {
        //given
        Company company = companyRepository.save(new Company("ABC Company", 1000, new ArrayList<Employee>()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("ABC Company"))
                .andExpect(jsonPath("$.employeeNumber").value(1000))
                .andExpect(jsonPath("$.employees", hasSize(0)));
    }


    @Test
    public void should_return_specific_company_employee_list_when_get_company_employee_list_given_company_id() throws Exception {
        //given
        List<Employee> expected = new ArrayList<Employee>();
        Employee employee1 = new Employee("Victor", 18, 1000, "male");
        Employee employee2 = new Employee("Mary", 19, 2000, "female");

        expected.add(employee1);
        expected.add(employee2);

        Company company = companyRepository.save(new Company("ABC Company", 1000, expected));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + company.getId() + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(employee1.getName()))
                .andExpect(jsonPath("$[0].age").value(employee1.getAge()))
                .andExpect(jsonPath("$[0].salary").value(employee1.getSalary()))
                .andExpect(jsonPath("$[0].gender").value(employee1.getGender()))
                .andExpect(jsonPath("$[1].name").value(employee2.getName()))
                .andExpect(jsonPath("$[1].age").value(employee2.getAge()))
                .andExpect(jsonPath("$[1].salary").value(employee2.getSalary()))
                .andExpect(jsonPath("$[1].gender").value(employee2.getGender()));
    }

    @Test
    public void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() throws Exception {
        //given
        companyRepository.save(new Company("ABC Company", 1000, new ArrayList<Employee>()));
        companyRepository.save(new Company("ABCD Company", 1100, new ArrayList<Employee>()));
        companyRepository.save(new Company("AB Company", 100, new ArrayList<Employee>()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies")
                .param("page", "1")
                .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }
}
