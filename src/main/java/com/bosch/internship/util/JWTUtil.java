package com.bosch.internship.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {

    private static final RSAKey rsaKeyInstance = generateRSAJWK();

    public RSAPrivateKey getPrivateKey(RSAKey key) throws JOSEException {
        return key.toRSAPrivateKey();
    }

    private static RSAKey generateRSAJWK() {
        try {
            return new RSAKeyGenerator(4096)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID("internship-key")
                    .generate();
        } catch (Exception e){
            log.error("Error generating key", e);
            return null;
        }
    }


    private String generateToken(JWTClaimsSet claimsSet) throws JOSEException {

        assert rsaKeyInstance != null;
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKeyInstance.getKeyID())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        assert rsaKeyInstance != null;
        JWSSigner signer = new RSASSASigner(getPrivateKey(rsaKeyInstance));

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String generateAccessToken(UserDetails userDetails) throws JOSEException, ParseException {

        String role = "USER";

        if(userDetails.getUsername().equals("admin")){
            role = "ADMIN";
        }

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issuer("internshipBosch.com")
                .issueTime(new java.util.Date())
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .claim("username", userDetails.getUsername())
                .claim("role", role)
                .build();

        return generateToken(claimsSet);
    }

    public boolean validateToken(SignedJWT signedJWT) throws JOSEException, ParseException {


        JWSHeader header = signedJWT.getHeader();

        RSAPublicKey publicKey = rsaKeyInstance.toRSAPublicKey();

        JWSVerifier verifier = new RSASSAVerifier(publicKey);

        boolean signatureValid = signedJWT.verify(verifier);
        boolean expired = isTokenExpired(signedJWT);

        log.info("Validating token: signature={}, header={}", signatureValid, header);

        return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
    }

    public boolean validateAccessToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        return validateToken(signedJWT);
    }


    public String extractUsername(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public String extractRole(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim("role");
    }

    public boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        return new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime());
    }

}
