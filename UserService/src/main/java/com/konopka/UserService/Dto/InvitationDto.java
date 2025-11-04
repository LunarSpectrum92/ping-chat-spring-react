package com.konopka.UserService.Dto;

import com.konopka.UserService.Entities.User;

public record InvitationDto(
        int senderId,
        int reciverId
) {
}
