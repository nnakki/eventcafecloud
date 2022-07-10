package com.eventcafecloud.comment.controller;

import com.eventcafecloud.comment.dto.*;
import com.eventcafecloud.comment.service.CommentService;
import com.eventcafecloud.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Transactional
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comment/registration")
    public String createComment(@PathVariable Long postId, User loginUser,
                                @Validated @ModelAttribute CommentCreateRequestDto requestDto,
                                BindingResult bindingResult){
        if (loginUser != null) {
            commentService.createComment(requestDto, postId, loginUser.getUserEmail());
            if (bindingResult.hasErrors()) {
                return "post/postsDetail";
            } else {
                return "redirect:/posts/" + postId;
            }
        }
        return "redirect:/posts/" + postId;
    }

    @PutMapping("/comment/{id}")
    @ResponseBody
    public Long updateComment(@PathVariable Long id, @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.updateComment(id, requestDto);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @PathVariable Long postId){
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
