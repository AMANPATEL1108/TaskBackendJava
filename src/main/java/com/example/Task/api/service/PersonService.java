package com.example.Task.api.service;

import com.example.Task.api.model.Person;
import com.example.Task.api.repositoey.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    public String savePerson(Person person) {
        if (person.getCreatedDate() == null) {
            person.setCreatedDate(new Date());  // Set only if new entity
        }
        person.setUpdateddate(new Date());      // Always update updateddate
        personRepository.save(person);
        return "Document Created Successfully";
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(String id) {
        return personRepository.findById(Long.valueOf(id));
    }

    public String deletePerson(String id) {
        personRepository.deleteById(Long.valueOf(id));
        return "Document Deleted Successfully";
    }

    public String personUpdatedById(Integer id, Person person) {
        Optional<Person> optionalPerson = personRepository.findById(Long.valueOf(id));

        if (optionalPerson.isPresent()) {
            Person existingUser = optionalPerson.get();

            if (person.getDocumentName() != null) {
                existingUser.setDocumentName(person.getDocumentName());
            }
            if (person.getOwnerofDocument() != null) {
                existingUser.setOwnerofDocument(person.getOwnerofDocument());
            }
            if (person.getPdfUrl() != null) {
                existingUser.setPdfUrl(person.getPdfUrl());
            }

            existingUser.setUpdateddate(new Date()); // update time

            personRepository.save(existingUser);
            return "User details successfully updated.";
        } else {
            return "User not found with ID: " + id;
        }
    }


}
