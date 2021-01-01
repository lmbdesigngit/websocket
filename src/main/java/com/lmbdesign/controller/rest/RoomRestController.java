package com.lmbdesign.controller.rest;

import com.lmbdesign.controller.rest.model.Room;
import com.lmbdesign.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomRestController {

    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/room")
    public Room getRoom() {
        return roomService.getRoom();
    }
}
