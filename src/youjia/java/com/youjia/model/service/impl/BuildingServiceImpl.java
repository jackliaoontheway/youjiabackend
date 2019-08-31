package com.youjia.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Building;
import com.youjia.model.Community;
import com.youjia.model.repository.BuildingRepos;
import com.youjia.model.service.BuildingService;
import com.youjia.model.service.CommunityService;

@Service
public class BuildingServiceImpl extends EntityServiceImpl<Building, Integer> implements BuildingService {

	@Autowired
	private CommunityService communityService;

	public Building create(Building entity, Integer operId, String languageId) {

		if (entity != null && entity.getId() == null) {
			entity.setCreatedBy(operId);
			entity.initDefaultValueBeforeCreation();

			Community community = entity.getCommunity();
			if (community != null && community.getId() != null) {
				community = communityService.getById(community.getId(), languageId);
				entity.setLabel(community.getName() + entity.getBuildingNo());
			}

			BuildingRepos repos = (BuildingRepos) this.getRepos();
			return repos.save(entity);

		}
		return null;
	}

}
