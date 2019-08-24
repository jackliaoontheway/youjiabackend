package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.Contact;
import com.polarj.model.service.ContactService;

@Service
public class ContactServiceImpl extends EntityServiceImpl<Contact, Integer> implements ContactService
{
}
