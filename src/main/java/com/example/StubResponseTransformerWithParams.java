package com.example;

import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.serveevent.ServeEvent;
import com.github.tomakehurst.wiremock.extension.Parameters;


public static class StubResponseTransformerWithParams implements ResponseTransformerV2 {

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {
        Parameters parameters = serveEvent.getTransformerParameters();
        return Response.Builder.like(response)
                .but().body(parameters.getString("name") + ", "
                        + parameters.getInt("number") + ", "
                        + parameters.getBoolean("flag"))
                .build();
    }

    @Override
    public String getName() {
        return "stub-transformer-with-params";
    }
}