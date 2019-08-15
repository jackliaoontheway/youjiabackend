package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.Room;
import com.youjia.model.service.RoomService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/rooms")
public class RoomController
        extends ModelController<Room, Integer, RoomService>
{
    public RoomController()
    {
        super(Room.class);
    }
}
