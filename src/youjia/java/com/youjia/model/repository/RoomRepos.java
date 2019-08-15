package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Room;

public interface RoomRepos extends JpaRepository<Room, Integer>
{
}
