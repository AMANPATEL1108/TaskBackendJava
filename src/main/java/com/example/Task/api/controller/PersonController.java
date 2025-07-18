package com.example.Task.api.controller;

import com.example.Task.api.model.Person;
import com.example.Task.api.response.ApiResponse;
import com.example.Task.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user/person")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    @Autowired
    private PersonService personService;



    @PutMapping(value = "/updateById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePerson(
            @PathVariable String id,
            @RequestPart("person") Person personDTO, // The person object that comes as JSON
            @RequestPart(value = "file", required = false) MultipartFile file) { // File part

        Optional<Person> opt = personService.getPersonById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();

        Person p = opt.get();

        // If a file is uploaded, save it and set the pdfUrl
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Save the file and get the filename
            p.setPdfUrl(fileName);           // Update the file URL
        }

        // Update other fields if provided
        if (personDTO.getDocumentName() != null) p.setDocumentName(personDTO.getDocumentName());
        if (personDTO.getOwnerofDocument() != null) p.setOwnerofDocument(personDTO.getOwnerofDocument());

        p.setUpdateddate(new Date());
        personService.savePerson(p);

        return ResponseEntity.ok(new ApiResponse(true, "Document updated successfully"));
    }

    private String saveFile(MultipartFile file) {
        // Ensure the file is a PDF
        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed!");
        }

        // Clean the file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Absolute path to the upload folder
        String uploadDir = "C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads";  // <-- Use an absolute path

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // Create the directory if it doesn't exist
            }

            Path targetLocation = uploadPath.resolve(fileName);

            file.transferTo(targetLocation.toFile());

            return targetLocation.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store the file " + fileName);
        }
    }



    // Get All Persons

    // Get Person by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPerson(@PathVariable String id) {
        Optional<Person> personOpt = personService.getPersonById(id);
        return personOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
