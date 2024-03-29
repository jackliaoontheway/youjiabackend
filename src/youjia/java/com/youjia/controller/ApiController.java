package com.youjia.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.polarj.common.CommonConstant;
import com.polarj.model.component.PersonalPhoto;
import com.youjia.model.Building;
import com.youjia.model.Lease;
import com.youjia.model.RentBill;
import com.youjia.model.Renter;
import com.youjia.model.RenterStatus;
import com.youjia.model.service.BuildingService;
import com.youjia.model.service.LeaseService;
import com.youjia.model.service.RentBillService;
import com.youjia.model.service.RenterService;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private RenterService renterService;

	@Autowired
	private LeaseService leaseService;

	@Autowired
	private RentBillService rentBillService;

	@Autowired
	private BuildingService buildingService;

	@PostMapping("/login")
	public @ResponseBody boolean login(HttpServletRequest request, String loginName, String password,
			HttpServletResponse response) {
		if (loginName == null || password == null) {
			return false;
		}

		Renter renter = renterService.findByLoginName(loginName);
		if (renter == null) {
			return false;
		}

		if (!renter.matchPassword(password)) {
			return false;
		}
		return true;
	}

	@PostMapping("/register")
	public @ResponseBody boolean register(HttpServletRequest request, String name, String phone, String idCard,
			String confirmedPassword, @RequestParam("files") MultipartFile[] files) {
		Renter renter = new Renter();
		renter.setName(name);
		renter.setIdCard(idCard);
		renter.setPhone(phone);
		renter.setPassword(confirmedPassword);
		PersonalPhoto photo = new PersonalPhoto();
		if (files != null && files.length > 0) {
			MultipartFile file = files[0];
			photo.setFileName("D:\\temp\\" + file.getOriginalFilename());
			photo.setFileType(file.getOriginalFilename().substring(photo.getFileName().indexOf(".")));
			try {
				File newFile = new File("D:\\temp\\" + file.getOriginalFilename());
				FileOutputStream out = new FileOutputStream(newFile);
				out.write(file.getBytes());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			renter.setPhoto(photo);
		}
		renter.setStatus(RenterStatus.待审核.name());
		renter = renterService.create(renter, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
		return renter.getId() != null;
	}

	/**
	 * 获取当月租金账单信息
	 * 
	 * @param request
	 * @param loginName
	 * @param response
	 * @return
	 */
	@PostMapping("/fetchRentBill")
	public @ResponseBody RentBill fetchRent(HttpServletRequest request, String loginName,
			HttpServletResponse response) {

		Renter renter = renterService.findByLoginName(loginName);
		if (renter == null) {
			return null;
		}
		return rentBillService.findByRenter(renter);
	}

	/**
	 * @param loginName
	 * @param response
	 * @return
	 */
	@PostMapping("/fetchBuilding")
	public @ResponseBody List<Building> fetchBuilding(HttpServletRequest request, HttpServletResponse response) {
		List<Building> list = buildingService.list(CommonConstant.defaultSystemLanguage);
		return list;
	}

	/**
	 * 租户信息 租金 时间等
	 * 
	 * @param request
	 * @param loginName
	 * @param response
	 * @return
	 */
	@PostMapping("/fetchLease")
	public @ResponseBody Lease fetchLease(HttpServletRequest request, String loginName, HttpServletResponse response) {
		Renter renter = renterService.findByLoginName(loginName);
		if (renter == null) {
			return null;
		}
		Lease dblease = leaseService.findByRenter(renter);
		Lease lease = new Lease();

		lease.setBuildingNumber(dblease.getRoom().getLabel());
		SimpleDateFormat format = new SimpleDateFormat(CommonConstant.DateFormatString);
		lease.setEffetiveDateString(format.format(dblease.getEffetiveDate()));
		lease.setExpiredDateString(format.format(dblease.getExpiredDate()));
		lease.setLeaseStatus(dblease.getLeaseStatus());
		lease.setDeposit(dblease.getDeposit());
		return lease;
	}

	/**
	 * 申请退租
	 * 
	 * @param loginName
	 * @param response
	 * @return
	 */
	@PostMapping("/withdrawLease")
	public @ResponseBody boolean withdrawRequest(HttpServletRequest request, String loginName,
			HttpServletResponse response) {
		Renter renter = renterService.findByLoginName(loginName);
		if (renter == null) {
			return false;
		}
		return leaseService.withdrawRequest(renter);
	}
}
