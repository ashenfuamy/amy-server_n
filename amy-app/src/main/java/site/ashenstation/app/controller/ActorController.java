package site.ashenstation.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.app.dto.CreateActorDto;
import site.ashenstation.app.service.ActorService;
import site.ashenstation.entity.ActorTag;

import java.util.List;

@RestController
@RequestMapping("/api/actor")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping("/tags")
    public ResponseEntity<List<ActorTag>> getActorTags() {
        return ResponseEntity.ok(actorService.getActorTags());
    }

    @PostMapping("/create-actor")
    public ResponseEntity<Boolean> createActor(CreateActorDto dto) {
        return ResponseEntity.ok(actorService.createActor(dto));
    }
}
