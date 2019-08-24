package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.GoodsInfo;

public interface GoodsInfoRepos extends JpaRepository<GoodsInfo, Integer>
{
}
