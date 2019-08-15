package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Community;

public interface CommunityRepos extends JpaRepository<Community, Integer>
{
}
