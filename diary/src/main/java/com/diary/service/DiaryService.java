package com.diary.service;
import com.diary.dto.DiaryDto;
import com.diary.entity.Diary;
import com.diary.entity.User;
import com.diary.repository.DiaryRepository;
import com.diary.repository.UserRepository;
import com.diary.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class DiaryService {
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private UserRepository userRepository;

    public Diary createDiary(String email, DiaryDto diaryDto){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found :<"));
        Diary diary = new Diary();
        diary.setTitle(diaryDto.getTitle());
        diary.setContent(diaryDto.getContent());
        diary.setUser(user);
        diary.setCreatedAt(LocalDateTime.now());

        return diaryRepository.save(diary);
    }
    public Optional<Diary> getDiary(Long id){
        return diaryRepository.findById(id);
    }
    public List<Diary> getDiariesByUserEmail() {
        String email = SecurityUtil.getCurrentUserEmail();

        return diaryRepository.findByUserEmail(email);
    }
    public Diary updateDiary(Long id, DiaryDto updatedDiary, String authenticatedUserEmail){
        Diary existing = diaryRepository.findById(id).orElseThrow(() -> new RuntimeException("Diary not found :<"));;
        if (!existing.getUser().getEmail().equals(authenticatedUserEmail)) {
            throw new RuntimeException("Access Denied :(");
        }
        existing.setTitle(updatedDiary.getTitle());
        existing.setContent(updatedDiary.getContent());
        return diaryRepository.save(existing);
    }
    public void deleteDiary(Long id, String name) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Diary not found :("));
        String email = SecurityUtil.getCurrentUserEmail();
        if (!diary.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }
        diaryRepository.delete(diary);
    }
}
