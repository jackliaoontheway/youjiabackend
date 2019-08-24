package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.I18nResource;

public interface I18nResourceRepos extends JpaRepository<I18nResource, Integer>
{
//    I18nResource findFirstByI18nKeyAndLanguageId(String i18nKey, String languageId);
//
//    List<I18nResource> findByI18nKeyLike(String i18nKeyPattern);
//
//    List<I18nResource> findByI18nKey(String i18nKey);
}
