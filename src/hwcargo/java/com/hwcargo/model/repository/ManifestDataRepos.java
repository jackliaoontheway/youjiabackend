package com.hwcargo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hwcargo.model.ManifestData;

public interface ManifestDataRepos extends JpaRepository<ManifestData, Integer>
{

    ManifestData findFirstByPackingListNoLikeOrderByIdDesc(String string);
}
