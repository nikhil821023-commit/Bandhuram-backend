// service/ContactService.java — add SimpMessagingTemplate + one line
package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.ContactRequest;
import com.bandhuram.backend.dto.ContactResponse;
import com.bandhuram.backend.entity.ContactInquiry;
import com.bandhuram.backend.entity.InquiryStatus;
import com.bandhuram.backend.exception.ResourceNotFoundException;
import com.bandhuram.backend.repository.ContactInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactInquiryRepository contactRepository;
    private final SimpMessagingTemplate messagingTemplate;   // ← new

    public ContactResponse submit(ContactRequest req) {
        ContactInquiry saved = contactRepository.save(
                ContactInquiry.builder()
                        .name(req.name().trim())
                        .phone(req.phone().trim())
                        .email(req.email())
                        .message(req.message().trim())
                        .build()
        );
        ContactResponse dto = toDto(saved);
        messagingTemplate.convertAndSend("/topic/contact", dto);   // ← new
        return dto;
    }

    public List<ContactResponse> listAll() {
        return contactRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    public ContactResponse updateStatus(Long id, InquiryStatus status) {
        ContactInquiry inquiry = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found: " + id));
        inquiry.setStatus(status);
        return toDto(inquiry);
    }

    private ContactResponse toDto(ContactInquiry c) {
        return new ContactResponse(c.getId(), c.getName(), c.getPhone(), c.getEmail(),
                c.getMessage(), c.getStatus(), c.getCreatedAt());
    }
}