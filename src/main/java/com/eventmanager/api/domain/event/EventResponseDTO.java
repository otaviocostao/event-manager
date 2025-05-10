package com.eventmanager.api.domain.event;

import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, java.util.Date date, String city, String state, Boolean remote, String eventUrl, String imgUrl) {
}
