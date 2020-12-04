package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public Company add(Company requestCompany) {
        return this.companyRepository.save(requestCompany);
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company get(String companyId) {
        return companyRepository.findById(companyId).orElseThrow(CompanyNotFoundException::new);
    }

    public List<String> getEmployeeList(String companyId) {
        return companyRepository.findById(companyId).orElseThrow(CompanyNotFoundException::new).getEmployees();
    }

    public Page<Company> getAllByPage(int page, int pageSize) {
        return companyRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Company update(String companyId, Company updateCompany) {
        if (companyRepository.existsById(companyId)) {
            updateCompany.setId(companyId);
            return companyRepository.save(updateCompany);
        }
        throw new CompanyNotFoundException();
    }

    public void remove(String companyId) {
        companyRepository.deleteById(companyId);
    }
}
