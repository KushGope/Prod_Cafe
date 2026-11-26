package com.floras.cafe.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleVerifierService(
            @Value("${application.security.oauth2.client.registration.google.client-id}") String clientId) {

        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verify(String idToken) {

        try {
            GoogleIdToken token = verifier.verify(idToken);

            if (token == null) {
                throw new IllegalArgumentException(
                        "Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = token.getPayload();

            if (!"accounts.google.com".equals(payload.getIssuer())
                    && !"https://accounts.google.com".equals(payload.getIssuer())) {
                throw new IllegalArgumentException("Invalid token issuer");
            }

            if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
                throw new IllegalArgumentException(
                        "Google email is not verified");
            }

            return payload;

        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException(
                    "Google token verification failed", e);
        }
    }
}
