// service/FeedbackService.java — same pattern
package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.FeedbackRequest;
import com.bandhuram.backend.dto.FeedbackResponse;
import com.bandhuram.backend.entity.Feedback;
import com.bandhuram.backend.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SimpMessagingTemplate messagingTemplate;   // ← new

    public FeedbackResponse submit(FeedbackRequest req) {
        Feedback saved = feedbackRepository.save(
                Feedback.builder()
                        .name(req.name().trim())
                        .rating(req.rating())
                        .comment(req.comment().trim())
                        .build()
        );
        FeedbackResponse dto = toDto(saved);
        messagingTemplate.convertAndSend("/topic/feedback", dto);   // ← new
        return dto;
    }

    public List<FeedbackResponse> listAll() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackResponse toDto(Feedback f) {
        return new FeedbackResponse(f.getId(), f.getName(), f.getRating(), f.getComment(), f.getCreatedAt());
    }
}