package com.bandhuram.backend.controller;

import com.bandhuram.backend.dto.ContactResponse;
import com.bandhuram.backend.dto.ContactStatusUpdateRequest;
import com.bandhuram.backend.service.ContactService;
import com.bandhuram.backend.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactService contactService;
    private final FeedbackService feedbackService;

    @GetMapping("/contact")
    public List<ContactResponse> listInquiries() {
        return contactService.listAll();
    }

    @PutMapping("/contact/{id}/status")
    public ContactResponse updateStatus(@PathVariable Long id, @Valid @RequestBody ContactStatusUpdateRequest request) {
        return contactService.updateStatus(id, request.status());
    }

    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}