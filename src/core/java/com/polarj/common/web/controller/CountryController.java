package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.CountryISO3166;
import com.polarj.model.service.CountryISO3166Service;

@RestController
@RequestMapping("/countrys")
public class CountryController extends ModelController<CountryISO3166, Integer, CountryISO3166Service>
{
    public CountryController()
    {
        super(CountryISO3166.class);
    }
}