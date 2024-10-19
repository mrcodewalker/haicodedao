package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.responses.GoogleLoginResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/oauth2")
public class OAuth2Controller {
    @GetMapping("/login/google")
    @PermitAll
    public ResponseEntity<?> currentUserGoogle(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        Map<String, Object> map = oAuth2AuthenticationToken.getPrincipal().getAttributes();

        ModelMapper modelMapper = new ModelMapper();

        return ResponseEntity.ok(GoogleLoginResponse.builder()
                        .email((String) map.get("email"))
                        .name((String) map.get("name"))
                        .picture((String) map.get("picture"))
                .build());
    }
    @GetMapping("/login/facebook")
    public Map<String, Object> currentUserFacebook(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }
    public ResponseEntity<?> toPerson(Map<String, Object> map){
        if (map==null){
            return ResponseEntity.ok("null");
        }

        return ResponseEntity.ok(null);
    }
    @GetMapping("/get/info/google")
    @PermitAll
    public ResponseEntity<?> getInformation(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        Map<String, Object> map = oAuth2AuthenticationToken.getPrincipal().getAttributes();


        return ResponseEntity.ok(null);
    }
}
