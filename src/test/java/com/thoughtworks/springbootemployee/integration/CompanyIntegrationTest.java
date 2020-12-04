package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.bson.types.ObjectId;
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
        Company company = new Company("ABC Company", 1000, new ArrayList<>());
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
                "        \"123\"\n" +
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
        Company company = companyRepository.save(new Company("ABC Company", 1000, new ArrayList<>()));
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
    public void should_return_not_found_when_get_company_given_invalid_company_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + "6fc9d0e060e64c0326fd2e92"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_specific_company_employee_list_when_get_company_employee_list_given_company_id() throws Exception {
        //given
        List<String> expected = new ArrayList<>();
        String employeeId1 = new ObjectId().toString();
        String employeeId2 = new ObjectId().toString();

        expected.add(employeeId1);
        expected.add(employeeId2);

        Company company = companyRepository.save(new Company("ABC Company", 1000, expected));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + company.getId() + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void should_return_first_2_employee_when_get_employee_by_page_given_employees_page1_pageSize2() throws Exception {
        //given
        companyRepository.save(new Company("ABC Company", 1000, new ArrayList<>()));
        companyRepository.save(new Company("ABCD Company", 1100, new ArrayList<>()));
        companyRepository.save(new Company("AB Company", 100, new ArrayList<>()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies")
                .param("page", "1")
                .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void should_return_updated_company_when_update_company_given_company_company_id() throws Exception {
        //given
        Company company = companyRepository.save(new Company("ABC Company", 1000, new ArrayList<>()));
        String companyAsJson = "{\n" +
                "    \"name\" : \"ABCD Company\",\n" +
                "    \"employeeNumber\" : \"1200\",\n" +
                "    \"employees\" : [\n" +
                "        \"123\"\n" +
                "    ]\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("ABCD Company"))
                .andExpect(jsonPath("$.employeeNumber").value(1200))
                .andExpect(jsonPath("$.employees", hasSize(1)));

        List<Company> companyList = companyRepository.findAll();
        assertEquals(1, companyList.size());
        assertEquals("ABCD Company", companyList.get(0).getName());
        assertEquals(1200, companyList.get(0).getEmployeeNumber());
        assertEquals(1, companyList.get(0).getEmployees().size());
    }

    @Test
    public void should_return_not_found_when_update_company_given_company_invalid_company_id() throws Exception {
        //given
        String companyAsJson = "{\n" +
                "    \"name\" : \"ABCD Company\",\n" +
                "    \"employeeNumber\" : \"1200\",\n" +
                "    \"employees\" : [\n" +
                "        \"123\"\n" +
                "    ]\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/companies/" + "6fc9d0e060e64c0326fd2e92")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_null_when_delete_company_given_companies_new_company() throws Exception {
        //given
        Company company = companyRepository.save(new Company("ABC Company", 1000, new ArrayList<>()));

        //when
        //then
        //todo: check response no content
        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/" + company.getId()));
        List<Company> companyList = companyRepository.findAll();
        assertEquals(0, companyList.size());
    }
}
