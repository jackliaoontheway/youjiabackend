package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Room;
import com.youjia.model.service.RoomService;

@Service
public class RoomServiceImpl extends EntityServiceImpl<Room, Integer>
        implements RoomService
{
}
