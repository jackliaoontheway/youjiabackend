package com.youjia.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.common.CommonConstant;
import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Lease;
import com.youjia.model.LeaseStatus;
import com.youjia.model.Renter;
import com.youjia.model.Room;
import com.youjia.model.RoomStatus;
import com.youjia.model.repository.LeaseRepos;
import com.youjia.model.service.LeaseService;
import com.youjia.model.service.RoomService;

@Service
public class LeaseServiceImpl extends EntityServiceImpl<Lease, Integer> implements LeaseService {

	@Autowired
	private RoomService roomService;

	@Override
	public Lease findByRenter(Renter renter) {
		LeaseRepos repos = (LeaseRepos) this.getRepos();
		return repos.findFirstByRenter(renter);
	}

	@Override
	public boolean withdrawRequest(Renter renter) {
		Lease lease = findByRenter(renter);
		lease.setLeaseStatus(LeaseStatus.申请退租.name());
		this.update(lease.getId(), lease, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);

		Room room = lease.getRoom();
		room.setRoomStatus(RoomStatus.正在退租.name());

		roomService.update(room.getId(), room, CommonConstant.systemUserAccountId,
				CommonConstant.defaultSystemLanguage);

		return true;
	}
}
