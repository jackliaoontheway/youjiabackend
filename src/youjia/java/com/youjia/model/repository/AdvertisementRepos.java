package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Advertisement;

public interface AdvertisementRepos extends JpaRepository<Advertisement, Integer>
{
}
