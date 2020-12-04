package com.thoughtworks.springbootemployee.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException() {
        super("Company Not Found");
    }
}
