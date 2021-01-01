package com.lmbdesign.controller.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class Room {

    String roomId;
    List<Player> players;
}
