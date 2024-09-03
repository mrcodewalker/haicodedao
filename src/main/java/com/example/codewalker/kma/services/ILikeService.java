package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.LikeDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Like;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.responses.LikeResponse;
import com.example.codewalker.kma.responses.PostResponse;

import javax.xml.crypto.Data;
import java.util.List;

public interface ILikeService {
    LikeResponse createLike(LikeDTO likeDTO) throws DataNotFoundException;
    void deleteLikeById(Long id);
    Like findById(Long id) throws DataNotFoundException;
    LikeResponse updateLike(PostDTO postDTO);
    List<LikeResponse> getListLikeByPostId(Long postId) throws DataNotFoundException;
}
