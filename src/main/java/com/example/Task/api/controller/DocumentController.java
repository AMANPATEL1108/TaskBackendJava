    package com.example.Task.api.controller;

    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.net.MalformedURLException;
    import java.nio.file.Path;
    import java.nio.file.Paths;

    @RestController
    @RequestMapping("/user")
    @CrossOrigin(origins = "http://localhost:4200")  // allow Angular dev server origin
    public class DocumentController {

        @GetMapping("/document/view")
        public ResponseEntity<Resource> viewDocument(@RequestParam String filename) {
            try {
                Path file = Paths.get("C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads").resolve(filename).normalize();
                Resource resource = new UrlResource(file.toUri());

                if (!resource.exists()) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);

            } catch (MalformedURLException e) {
                return ResponseEntity.badRequest().build();
            }
        }
    }
