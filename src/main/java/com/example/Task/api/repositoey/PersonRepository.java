package com.example.Task.api.repositoey;

import com.example.Task.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.Document;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
