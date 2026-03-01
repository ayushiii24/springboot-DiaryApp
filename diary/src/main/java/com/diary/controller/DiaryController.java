package com.diary.controller;
import com.diary.dto.DiaryDto;
import com.diary.entity.Diary;
import com.diary.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    @Autowired
    private DiaryService diaryService;

    @PostMapping
    public Diary createDiary(@RequestBody DiaryDto request, Principal principal){
        String loggedInUser = principal.getName();
        return diaryService.createDiary(loggedInUser, request);
    }
    @GetMapping("/id/{id}")
    public Optional<Diary> getDiary(@PathVariable Long id){
        return diaryService.getDiary(id);
    }

    @GetMapping("/user/{email}")
    public List<Diary> getDiariesByUserEmail(@PathVariable String email){
        return diaryService.getDiariesByUserEmail();
    }
    @PutMapping("/{id}")
    public Diary updateDiary(@PathVariable Long id, @RequestBody DiaryDto updatedDiary, Principal principal){
        String authenticatedUserEmail = principal.getName();
        return diaryService.updateDiary(id, updatedDiary, authenticatedUserEmail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long id, Principal principal){
        try {
            diaryService.deleteDiary(id, principal.getName());
            return ResponseEntity.ok("Deleted successfully :D");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

}
