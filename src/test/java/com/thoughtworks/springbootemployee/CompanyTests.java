package com.thoughtworks.springbootemployee;


import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyTests {

    @InjectMocks
    private CompanyService companyService;
    @Mock
    private CompanyRepository companyRepository;

    @Test
    void should_return_company_when_add_company_given_no_company() {
        //given
        Company company = new Company("My Company", 1000, new ArrayList<>());
        when(companyRepository.save(company)).thenReturn(company);

        //when
        final Company actual = companyService.add(company);

        //then
        assertEquals(company, actual);
    }


    @Test
    void should_return_all_companies_when_get_all_companies_given_companies() {
        //given
        Company company = new Company("My Company", 1000, new ArrayList<>());
        final List<Company> expected = Collections.singletonList(company);
        companyService.add(company);

        when(companyRepository.findAll()).thenReturn(expected);
        //when
        final List<Company> actual = companyService.getAll();

        //then
        assertEquals(expected, actual);

    }

    @Test
    void should_return_specific_company_employee_list_when_get_company_employee_list_given_companies_company_id() {
        //given
        Company company = new Company("My Company", 1000, new ArrayList<>());
        List<String> expected = new ArrayList<>();
        when(companyRepository.findById("1")).thenReturn(java.util.Optional.of(company));

        companyService.add(new Company("My Company", 1000, expected));

        //when
        final List<String> actual = companyService.getEmployeeList("1");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_first_2_companies_when_get_companies_by_page_given_companies_page1_pageSize2() {
        //given

        Company company1 = new Company("My Company1", 1000, new ArrayList<>());
        Company company2 = new Company("My Company2", 1000, new ArrayList<>());

        companyService.add(company1);
        companyService.add(company2);
        companyService.add(new Company("My Company3", 1000, new ArrayList<>()));

        final Page<Company> expected = new PageImpl<>(Arrays.asList(company1, company2));

        when(companyRepository.findAll(PageRequest.of(0, 2))).thenReturn(expected);
        //when
        final Page<Company> actual = companyService.getAllByPage(1, 2);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_return_updated_employees_when_get_employee_given_employees_employee_id() {
        //given
        Company company = new Company("My New Company1", 10000, new ArrayList<>());
        when(companyRepository.existsById("1")).thenReturn(true);
        when(companyRepository.save(company)).thenReturn(company);

        //when
        final Company actual = companyService.update("1", company);

        //then
        assertEquals(company, actual);
    }
}
