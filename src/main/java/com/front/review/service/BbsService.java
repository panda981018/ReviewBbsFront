package com.front.review.service;

import com.front.review.handler.BbsRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class BbsService {

    private final BbsRequestHandler bbsRequestHandler;

    // home bbs list
    public ResponseEntity<List> getHomeBbsDtoList(int perPage) {
        return bbsRequestHandler.sendHomeBbsDtoListRequest(perPage);
    }

    // update a bbs views
    public void updateBbsViews(HashMap<String, String> bbsObj) {
        bbsRequestHandler.updateBbsViewsRequest(bbsObj);
    }

    // view bbs
    public HashMap<String, Object> viewBbs(Long id, Long memberId) {
        return bbsRequestHandler.viewBbs(id, memberId);
    }
}
