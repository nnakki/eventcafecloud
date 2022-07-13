package com.eventcafecloud.post.service;

import com.eventcafecloud.post.domain.Post;
import com.eventcafecloud.post.domain.type.PostType;
import com.eventcafecloud.post.dto.PostCreateRequestDto;
import com.eventcafecloud.post.dto.PostReadResponseDto;
import com.eventcafecloud.post.dto.PostUpdateRequestDto;
import com.eventcafecloud.post.repository.PostRepository;
import com.eventcafecloud.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static com.eventcafecloud.exception.ExceptionStatus.POST_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostCreateRequestDto requestDto, User user, PostType postType) {
        Post post = new Post(requestDto, user, postType);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostReadResponseDto> getPostList() {
        List<Post> posts = postRepository.findPostsByPostTypeOrderByIdDesc(PostType.USERPOST);
        List<PostReadResponseDto> output = new ArrayList<>();

        for (Post post : posts ) {
            PostReadResponseDto postReadResponseDto = new PostReadResponseDto(post);
            output.add(postReadResponseDto);
        }
        return output;
    }

    public void updatePost(@PathVariable Long id, PostUpdateRequestDto requestDto){
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(POST_NOT_FOUND.getMessage()));
        post.updatePost(requestDto);
    }

    public void deletePost(Long id) {
        try {
            postRepository.deleteById(id);
        }catch (Exception e) {
           throw new IllegalArgumentException(POST_NOT_FOUND.getMessage());
        }
    }

    //페이징 처리된 게시글 리스트 반환
//    public Page<Post> findPostList(Pageable pageable){
//        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0: pageable.getPageNumber() -1, pageable.getPageSize());
//        return postRepository.findAll(pageable);
//    }

    // 게시글 ID로 조회
    public PostUpdateRequestDto findPostByIdForUpdate(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(POST_NOT_FOUND.getMessage()));
        return PostUpdateRequestDto.toDto(post);
    }

    //게시글 조회 및 조회수 증가
    public PostReadResponseDto getPostUpdatedCount(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(POST_NOT_FOUND.getMessage()));
        post.updateCount();
        return new PostReadResponseDto(post);
    }

    //공지게시판 게시글 조회
    @Transactional(readOnly = true)
    public List<PostReadResponseDto> getNoticePostList() {
        List<Post> posts = postRepository.findPostsByPostTypeOrderByIdDesc(PostType.NOTICE);
        List<PostReadResponseDto> output = new ArrayList<>();

        for (Post post : posts) {
            PostReadResponseDto postReadResponseDto = new PostReadResponseDto(post);
            output.add(postReadResponseDto);
        }
        return output;
    }
    //사용자에 따른 게시글 가져오기
    @Transactional(readOnly = true)
    public Page<Post> getPostListByUser(Long userId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return postRepository.findAllByUserId(userId, pageable);
    }

}
