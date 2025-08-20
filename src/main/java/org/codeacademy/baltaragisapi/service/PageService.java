package org.codeacademy.baltaragisapi.service;

import java.util.List;
import java.util.stream.Collectors;
import org.codeacademy.baltaragisapi.dto.PageDto;
import org.codeacademy.baltaragisapi.entity.Page;
import org.codeacademy.baltaragisapi.mapper.PageMapper;
import org.codeacademy.baltaragisapi.repository.PageRepository;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;

    public PageService(PageRepository pageRepository, PageMapper pageMapper) {
        this.pageRepository = pageRepository;
        this.pageMapper = pageMapper;
    }

    public List<PageDto> listPublished() {
        return pageRepository.findAll().stream()
                .filter(Page::getIsPublished)
                .map(pageMapper::toDto)
                .collect(Collectors.toList());
    }

    public PageDto getBySlug(String slug) {
        return pageRepository.findBySlugAndIsPublishedTrue(slug)
                .map(pageMapper::toDto)
                .orElse(null);
    }
}


