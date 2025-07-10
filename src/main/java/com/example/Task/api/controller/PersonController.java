package com.example.Task.api.controller;

import com.example.Task.api.model.Person;
import com.example.Task.api.response.ApiResponse;
import com.example.Task.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    @Autowired
    private PersonService personService;

    // Create Person - directly save the URL in imageUrl
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPerson(
            @RequestPart("person") Person personDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        // handle file storage here, set personDTO.setDocumentUrl(...)
        personDTO.setCreatedDate(new Date());
        personDTO.setUpdateddate(new Date());
        String saved = personService.savePerson(personDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Document updated successfully"));
    }

    @PutMapping(value = "/updateById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePerson(
            @PathVariable String id,
            @RequestPart("person") Person personDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        Optional<Person> opt = personService.getPersonById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();

        Person p = opt.get();
        if (personDTO.getDocumentName() != null) p.setDocumentName(personDTO.getDocumentName());
        if (personDTO.getOwnerofDocument() != null) p.setOwnerofDocument(personDTO.getOwnerofDocument());
        if (file != null && !file.isEmpty()) {
            // Save file and set URL logic here
        }
        p.setUpdateddate(new Date());
        personService.savePerson(p);

        // ✅ return JSON, not plain string
        return ResponseEntity.ok(new ApiResponse(true, "Document updated successfully"));
    }



    // Get All Persons
    @GetMapping("/get-all")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    // Get Person by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPerson(@PathVariable String id) {
        Optional<Person> personOpt = personService.getPersonById(id);
        return personOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete Person
    @DeleteMapping("/deleteByID/{id}")
    public ResponseEntity<ApiResponse> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);  // Make sure this returns void or doesn't return plain text
        return ResponseEntity.ok(new ApiResponse(true, "Person deleted successfully"));
    }



}
