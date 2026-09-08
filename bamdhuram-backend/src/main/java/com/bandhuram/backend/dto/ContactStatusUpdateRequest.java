package com.bandhuram.backend.dto;

import jakarta.validation.constraints.NotNull;
import com.bandhuram.backend.entity.InquiryStatus;

public record ContactStatusUpdateRequest(@NotNull InquiryStatus status) {}