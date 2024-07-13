package com.example.codewalker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/block_detail_ranking")
@CrossOrigin(origins = "http://localhost:4200")
public class BlockDetailRankingController {
}
