package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();

        company.setName(companyRequest.getName());
        company.setEmployeeNumber(companyRequest.getEmployeeNumber());
        company.setEmployees(companyRequest.getEmployees());

        return company;
    }

    public CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();

        companyResponse.setId(company.getId());
        companyResponse.setName(company.getName());
        companyResponse.setEmployeeNumber(company.getEmployeeNumber());
        companyResponse.setEmployees(company.getEmployees());

        return companyResponse;
    }
}
