package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.admin.CreatePageRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdatePageRequest;
import org.codeacademy.baltaragisapi.entity.Page;
import org.codeacademy.baltaragisapi.exception.ConflictException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.repository.PageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class AdminPageService {

    private final PageRepository pageRepository;

    public AdminPageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public List<Page> getAllPages() {
        return pageRepository.findAll();
    }

    public Page getPageById(Long id) {
        return pageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Page not found"));
    }

    public Page createPage(CreatePageRequest request) {
        // Check for duplicate slug
        if (pageRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Page with slug '" + request.getSlug() + "' already exists");
        }

        Page page = new Page();
        page.setTitle(request.getTitle());
        page.setSlug(request.getSlug());
        page.setContentMd(request.getContentMd());
        page.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);
        
        OffsetDateTime now = OffsetDateTime.now();
        page.setCreatedAt(now);
        page.setUpdatedAt(now);

        return pageRepository.save(page);
    }

    public Page updatePage(Long id, UpdatePageRequest request) {
        Page page = getPageById(id);
        
        if (request.getTitle() != null) {
            page.setTitle(request.getTitle());
        }
        if (request.getContentMd() != null) {
            page.setContentMd(request.getContentMd());
        }
        if (request.getIsPublished() != null) {
            page.setIsPublished(request.getIsPublished());
        }
        
        page.setUpdatedAt(OffsetDateTime.now());
        return pageRepository.save(page);
    }

    public void deletePage(Long id) {
        Page page = getPageById(id);
        pageRepository.delete(page);
    }
}
