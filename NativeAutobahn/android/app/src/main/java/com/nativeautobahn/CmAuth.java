package com.nativeautobahn;

import ws.wamp.jawampa.WampMessages.AuthenticateMessage;
import ws.wamp.jawampa.WampMessages.ChallengeMessage;
import ws.wamp.jawampa.auth.client.ClientSideAuthentication;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CmAuth implements ClientSideAuthentication {
    public static final String AUTH_METHOD = "CmAuth";

    private final String ticket;

    public CmAuth(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String getAuthMethod() {
        return AUTH_METHOD;
    }

    @Override
    public AuthenticateMessage handleChallenge(ChallengeMessage message,
                                               ObjectMapper objectMapper) {
        return new AuthenticateMessage(ticket, objectMapper.createObjectNode());
    }
}