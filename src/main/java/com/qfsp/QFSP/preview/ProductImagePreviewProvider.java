package com.qfsp.QFSP.preview;

import eu.surgetech.front.ewc.bc.network.model.LibraryAttribute;
import eu.surgetech.front.ewc.bc.network.model.RxSkuGroup;
import eu.surgetech.front.ewc.bc.network.model.RxSkuGroupWithLink;
import eu.surgetech.front.ewc.bc.network.model.asset.EyewearMediaImage;
import eu.surgetech.front.ewc.bc.network.model.asset.EyewearMediaImagePreviewRanking;
import eu.surgetech.front.ewc.catalog.image.ImageProvider;
import eu.surgetech.front.ewc.current.CurrentProperties;
import eu.surgetech.front.ewc.dam.client.ContentClient;
import eu.surgetech.front.ewc.dam.client.ContentProperties;
import eu.surgetech.front.ewc.dam.client.model.productimage.ProductImageLinkResponseList;
import eu.surgetech.front.ewc.dam.client.model.productimage.ProductImageRequest;
import eu.surgetech.front.ewc.dam.client.model.productimage.ProductImageRequestList;
import eu.surgetech.front.ewc.dam.model.EyewearMediaImageFilter;
import eu.surgetech.front.ewc.dam.service.EyewearMediaImagePreviewRankingService;
import eu.surgetech.front.ewc.dam.service.EyewearMediaImageService;
import eu.surgetech.front.ewc.dam.util.ProductImageContentComparatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ProductImagePreviewProvider implements ImageProvider {
    private final Logger logger = LoggerFactory.getLogger(ProductImagePreviewProvider.class);

    private final static String DEFAULT_PREVIEW_CONTENT_TYPE = "image/png";
    private final static String DEFAULT_PREVIEW_SIZE = "s";
    private final static String DEFAULT_PREVIEW_VIEW = "CAT";

    private final ContentClient productImageClient;
    private final EyewearMediaImageService eyewearMediaImageService;
    private final ContentProperties contentProperties;
    private final CurrentProperties currentProperties;
    private final EyewearMediaImagePreviewRankingService rankingService;

    @Autowired
    public ProductImagePreviewProvider(ContentClient productImageClient, ContentProperties contentProperties,
                                       @Lazy EyewearMediaImageService eyewearMediaImageService, CurrentProperties currentProperties,
                                       EyewearMediaImagePreviewRankingService rankingService) {
        this.productImageClient = productImageClient;
        this.eyewearMediaImageService = eyewearMediaImageService;
        this.contentProperties = contentProperties;
        this.currentProperties = currentProperties;
        this.rankingService = rankingService;
    }

    @Override
    public Map<String, String> getUrls(List<String> skus) {
        EyewearMediaImageFilter filter = new EyewearMediaImageFilter();
        filter.setSkus(skus);

        return getUrls(filter);
    }

    public List<RxSkuGroupWithLink> getUrlsRx(EyewearMediaImageFilter filter) {
        List<RxSkuGroupWithLink> urls = new ArrayList<>();

        Map<RxSkuGroup, EyewearMediaImage> imagesBySkuGroup = getPreviewImagesRx(filter);

        List<ProductImageRequest> requests = imagesBySkuGroup.values().stream()
                .map(img -> new ProductImageRequest(contentProperties.getConsumerId(), img))
                .collect(Collectors.toList());

        if (requests.size() > 0) {
            try {
                ProductImageLinkResponseList response = productImageClient.generateLinks(new ProductImageRequestList(requests));
                response.getItems().forEach(item -> urls.add(new RxSkuGroupWithLink(item.getSku(),
                        item.getRxLensColor(),
                        item.getRxLensGradient(),
                        item.isSuccess() ? item.getLink().toString() : null)));
            } catch (IOException e) {
                logger.error("Unable to fetch product image links", e);
            }
        }

        return urls;
    }

    @Override
    public Map<String, String> getUrls(EyewearMediaImageFilter filter) {
        Map<String, String> urlMap = new HashMap<>();

        Map<String, EyewearMediaImage> imagesBySku  = getPreviewImages(filter);

        List<ProductImageRequest> requests = imagesBySku .values().stream()
                .map(img -> new ProductImageRequest(contentProperties.getConsumerId(), img))
                .collect(Collectors.toList());

        if (requests.size() > 0) {
            try {
                ProductImageLinkResponseList response = productImageClient.generateLinks(new ProductImageRequestList(requests));
                response.getItems().forEach(item -> urlMap.put(item.getSku(), item.isSuccess() ? item.getLink().toString() : null));
            } catch (IOException e) {
                logger.error("Unable to fetch product image links", e);
            }
        }

        filter.getSkus().stream()
                .filter(sku -> !urlMap.containsKey(sku))
                .forEach(sku -> urlMap.put(sku, null));

        return urlMap;
    }

    private Map<RxSkuGroup, EyewearMediaImage> getPreviewImagesRx(EyewearMediaImageFilter filter) {

        Map<RxSkuGroup, EyewearMediaImage> previewImages = new HashMap<>();

        Comparator<EyewearMediaImage> comparator = getComparator(ComparatorType.RX);

        eyewearMediaImageService.getAll(filter).stream()
                .collect(groupingBy(s -> new RxSkuGroup(s.getSku(),
                        s.getRxLensColor() == null ? null : s.getRxLensColor().getId(),
                        s.getRxLensGradient() == null ? null : s.getRxLensGradient().getId())))
                .forEach((k, v) -> previewImages.put(k, Collections.min(v, comparator)));

        return previewImages;
    }

    private Map<String, EyewearMediaImage> getPreviewImages(EyewearMediaImageFilter filter) {

        Map<String, EyewearMediaImage> previewImages = new HashMap<>();

        Comparator<EyewearMediaImage> comparator = getComparator(ComparatorType.NORMAL);

        eyewearMediaImageService.getAll(filter).stream()
                .collect(groupingBy(EyewearMediaImage::getSku))
                .forEach((k, v) -> previewImages.put(k, Collections.min(v, comparator)));

        return previewImages;
    }

    private Comparator<EyewearMediaImage> getComparator(ComparatorType type) {
        EyewearMediaImagePreviewRanking ranking = rankingService.get(currentProperties.getName()).orElse(getDefaultRanking());

        if (ranking.getViewRankings() == null || ranking.getViewRankings().isEmpty()) {
            ranking.setViewRankings(getDefaultRanking().getViewRankings());
        }
        if (ranking.getContentTypeRankings() == null || ranking.getContentTypeRankings().isEmpty()) {
            ranking.setContentTypeRankings(getDefaultRanking().getContentTypeRankings());
        }
        if (ranking.getSizeRankings() == null || ranking.getSizeRankings().isEmpty()) {
            ranking.setSizeRankings(getDefaultRanking().getSizeRankings());
        }

        return type == ComparatorType.RX
                ? new ProductImageContentComparatorFactory(ranking).create2()
                : new ProductImageContentComparatorFactory(ranking).create3();
    }

    private EyewearMediaImagePreviewRanking getDefaultRanking() {
        EyewearMediaImagePreviewRanking ranking = new EyewearMediaImagePreviewRanking();

        ranking.setViewRankings(List.of(new EyewearMediaImagePreviewRanking.ViewRanking(new LibraryAttribute(DEFAULT_PREVIEW_VIEW), 1)));
        ranking.setSizeRankings(List.of(new EyewearMediaImagePreviewRanking.SizeRanking(new LibraryAttribute(DEFAULT_PREVIEW_SIZE), 1)));
        ranking.setContentTypeRankings(List.of(new EyewearMediaImagePreviewRanking.ContentTypeRanking(DEFAULT_PREVIEW_CONTENT_TYPE, 1)));

        return ranking;
    }

    private enum ComparatorType {
        RX, NORMAL
    }

}
