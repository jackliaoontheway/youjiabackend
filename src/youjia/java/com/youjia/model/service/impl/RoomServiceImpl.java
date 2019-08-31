package com.youjia.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Building;
import com.youjia.model.Room;
import com.youjia.model.repository.RoomRepos;
import com.youjia.model.service.BuildingService;
import com.youjia.model.service.RoomService;

@Service
public class RoomServiceImpl extends EntityServiceImpl<Room, Integer> implements RoomService {

	@Autowired
	private BuildingService buildingService;

	public Room create(Room entity, Integer operId, String languageId) {

		if (entity != null && entity.getId() == null) {
			entity.setCreatedBy(operId);
			entity.initDefaultValueBeforeCreation();

			Building building = entity.getBuilding();
			if (building != null && building.getId() != null) {
				building = buildingService.getById(building.getId(), languageId);
				entity.setLabel(building.getLabel() + entity.getRoomNo());
			}

			RoomRepos repos = (RoomRepos) this.getRepos();
			return repos.save(entity);

		}
		return null;
	}

}
