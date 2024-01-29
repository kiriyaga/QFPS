package com.qfsp.QFSP.controller;

import com.qfsp.QFSP.Page;
import com.qfsp.QFSP.PageData;
import com.qfsp.QFSP.Pageable;
import com.qfsp.QFSP.query.model.Queries;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class TestController {

    @RequestMapping(path = "/eyewears-dto-pageable", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<EyewearDTO> indexPage(
            @RequestParam(value = "q", required = false) Queries q,
            @RequestParam int page,
            @Valid @Max(1500) int size,
            LookupOptions options,
            BindingResult result) {
        if (result.hasErrors()) {
            return new Page<>(new PageData(page, size), new ArrayList<>());
        }
        return getPageableEyewears(new Pageable(new PageData(page, size), q), new CacheFetcher.FetchOptions(options.getIncludes()));
    }




    //Service component
    public Page<EyewearDTO> getPageableEyewears(
            Pageable pageable,
            CacheFetcher.FetchOptions options) {

        var query = eyewearDtoQueryService.generateQueries(pageable);

        List<EyewearDTO> result = mongoClient.getByQuery(query, EyewearDTO.class, FinderUtils.asCollection(Eyewear.class), options));

        return new Page<>(pageable.getPageData(), result);
    }
}
