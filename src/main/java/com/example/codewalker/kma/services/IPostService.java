package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.dtos.UpdateDataDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.responses.CollectPostDataResponse;
import com.example.codewalker.kma.responses.PostResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostDTO postDTO) throws DataNotFoundException;

    StatusResponse deletePostById(Long id, String token) throws DataNotFoundException;

    Post findById(Long id) throws DataNotFoundException;

    StatusResponse updatePost(UpdateDataDTO postDTO, String token) throws DataNotFoundException;

    List<PostResponse> getPostByAuthorId(Long authorId) throws DataNotFoundException;

    CollectPostDataResponse getAllPosts(int page);

    CollectPostDataResponse getAllPostDecrease(int page);

    CollectPostDataResponse getAllPostPopular(int page);

    void increaseView(Long id);

    CollectPostDataResponse searchPosts(String searchTerm,int page);
    CollectPostDataResponse popularThisWeek(int page);

    CollectPostDataResponse popularAllTimes(int page);

    CollectPostDataResponse noRepliesYet(int page);

}