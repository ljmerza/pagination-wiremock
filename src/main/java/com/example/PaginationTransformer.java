package com.example;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class PaginationTransformer extends ResponseTransformer {

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> posts = mapper.readValue(responseDefinition.getBody(), List.class);

            int page = Integer.parseInt(request.queryParameter("page").firstValue());
            int size = Integer.parseInt(request.queryParameter("size").firstValue());

            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, posts.size());

            List<Map<String, Object>> paginatedPosts = posts.subList(fromIndex, toIndex);

            return ResponseDefinitionBuilder
                    .like(responseDefinition)
                    .withBody(mapper.writeValueAsString(paginatedPosts))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDefinitionBuilder
                    .like(responseDefinition)
                    .withStatus(500)
                    .withBody("Error processing pagination")
                    .build();
        }
    }

    @Override
    public String getName() {
        return "pagination-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }
}