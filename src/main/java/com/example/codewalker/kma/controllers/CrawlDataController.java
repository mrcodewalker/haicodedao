package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.services.CrawlDataService;
import jdk.management.jfr.RemoteRecordingStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/crawl")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class CrawlDataController {
    private final CrawlDataService crawlDataService;
    @PostMapping("/data")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws IOException {
        return this.crawlDataService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }
}
