package com.qfsp.QFSP.preview;

import eu.surgetech.front.ewc.bc.network.model.asset.Content;
import eu.surgetech.front.ewc.bc.network.model.GenericContent;
import eu.surgetech.front.ewc.bc.network.model.PreviewImageContent;
import eu.surgetech.front.ewc.dam.client.model.content.ContentFilter;
import eu.surgetech.front.ewc.dam.client.model.content.ContentLinkResponse;
import eu.surgetech.front.ewc.dam.service.ContentService;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class GenericContentPreviewImageProvider {

    private final ContentService contentService;

    public GenericContentPreviewImageProvider(ContentService contentService) {
        this.contentService = contentService;
    }

    public Map<String, String> getUrls(List<String> contentIds) {
        Map<String, String> urlMap = new HashMap<>();

        List<PreviewImageContent> allPreviewContent = getAllPreviewImages();

        Map<String, String> previewIdsByContentId = new HashMap<>();

        findContentByIds(contentIds).forEach(c -> {
            Optional<String> maybePreviewContentId = resolvePreviewContent(c, allPreviewContent);
            maybePreviewContentId.ifPresent(previewContentId -> previewIdsByContentId.put(c.getId(), previewContentId));
        });

        Map<String, String> urlsByPreviewId = new HashMap<>();

        if (previewIdsByContentId.size() > 0) {
            contentService.generateUrls(new HashSet<>(previewIdsByContentId.values())).getItems().stream()
                    .filter(ContentLinkResponse::isSuccess)
                    .forEach(r -> urlsByPreviewId.put(r.getContentId(), r.getUrl().toString()));
        }

        contentIds.forEach(contentId -> {
            String previewContentId = previewIdsByContentId.get(contentId);

            if (previewContentId == null) {
                urlMap.put(contentId, null);
            }

            String previewUrl = urlsByPreviewId.get(previewContentId);

            urlMap.put(contentId, previewUrl);
        });

        return urlMap;
    }

    private Optional<String> resolvePreviewContent(Content content, List<PreviewImageContent> previewContent) {
        if (content.getContentType().startsWith("image")) {
            return Optional.of(content.getId());
        }

        return previewContent.stream()
                .filter(c -> c.getPreviewContentType().equals(content.getContentType()))
                .map(PreviewImageContent::getContentId)
                .findFirst();
    }

    private List<PreviewImageContent> getAllPreviewImages() {
        return contentService.findAllItems(new ContentFilter(), PreviewImageContent.class);
    }

    private List<Content<GenericContent>> findContentByIds(List<String> ids) {
        ContentFilter contentFilter = new ContentFilter();
        contentFilter.setIds(ids);

        return contentService.findAll(contentFilter, GenericContent.class);
    }

}
