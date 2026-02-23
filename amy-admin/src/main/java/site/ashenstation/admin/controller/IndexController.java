package site.ashenstation.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousGetMapping;

@RestController
@RequestMapping("/")
public class IndexController {

    @AnonymousGetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("hello amy admin!");
    }
}
