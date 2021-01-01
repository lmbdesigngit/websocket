package com.lmbdesign.service;

import com.lmbdesign.controller.rest.model.Player;
import com.lmbdesign.controller.rest.model.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RoomService {

    private List<Room> rooms = new ArrayList<>();

    public Room getRoom() {
        if(!rooms.isEmpty()) {
            return rooms.get(0);
        }

        Room room = new Room();
        room.setRoomId("test");

        room.setPlayers(Arrays.asList(new Player("tester")));

        rooms.add(room);
        return room;
    }
}
