package cn.byssted.bbs.bbsrd.controller;

import cn.byssted.bbs.bbsrd.annotation.AuthRequired;
import cn.byssted.bbs.bbsrd.common.Result;
import cn.byssted.bbs.bbsrd.entity.Comment;
import cn.byssted.bbs.bbsrd.service.CommentService;
import cn.byssted.bbs.bbsrd.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@Tag(name = "评论管理", description = "评论相关接口")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 根据帖子ID获取评论列表
     */
    @Operation(summary = "获取帖子评论", description = "根据帖子ID获取该帖子的所有评论")
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getCommentsByPostId(@Parameter(description = "帖子ID") @PathVariable Integer postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error("获取评论列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据父评论ID获取子评论列表
     */
    @Operation(summary = "获取子评论", description = "根据父评论ID获取子评论列表")
    @GetMapping("/parent/{parentCommentId}")
    public Result<List<Comment>> getCommentsByParentId(@Parameter(description = "父评论ID") @PathVariable Integer parentCommentId) {
        try {
            List<Comment> comments = commentService.getCommentsByParentId(parentCommentId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error("获取子评论列表失败：" + e.getMessage());
        }
    }

    /**
     * 发布评论
     */
    @Operation(summary = "发布评论", description = "发布新评论（需要登录）")
    @AuthRequired
    @PostMapping
    public Result<Comment> createComment(HttpServletRequest httpRequest, @Parameter(description = "评论信息") @RequestBody Map<String, Object> request) {
        try {
            Long userId = (Long) httpRequest.getAttribute("userId");

            Integer postId = (Integer) request.get("postId");
            String content = (String) request.get("content");
            Integer parentCommentId = (Integer) request.get("parentCommentId");

            // 参数验证
            if (postId == null) {
                return Result.badRequest("帖子ID不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                return Result.badRequest("评论内容不能为空");
            }

            Comment comment = commentService.createComment(postId, userId.intValue(), content, parentCommentId);
            return Result.success("评论成功", comment);
        } catch (Exception e) {
            return Result.error("发布评论失败：" + e.getMessage());
        }
    }

    /**
     * 更新评论
     */
//    @Operation(summary = "更新评论", description = "更新评论内容（仅作者可操作）")
//    @AuthRequired
//    @PutMapping("/{id}")
//    public Result<String> updateComment(HttpServletRequest httpRequest, @Parameter(description = "评论ID") @PathVariable Integer id, @Parameter(description = "评论信息") @RequestBody Map<String, String> request) {
//        try {
//            Long userId = (Long) httpRequest.getAttribute("userId");
//
//            Comment comment = commentService.getCommentById(id);
//            if (comment == null) {
//                return Result.notFound("评论不存在");
//            }
//
//            // 只有作者可以修改评论
//            if (!comment.getUserId().equals(userId.intValue())) {
//                return Result.forbidden("无权限修改此评论");
//            }
//
//            String content = request.get("content");
//            if (content != null && !content.trim().isEmpty()) {
//                comment.setContent(content);
//            }
//
//            boolean success = commentService.updateComment(comment);
//            if (success) {
//                return Result.success("更新成功");
//            } else {
//                return Result.error("更新失败");
//            }
//        } catch (Exception e) {
//            return Result.error("更新评论失败：" + e.getMessage());
//        }
//    }

    /**
     * 删除评论
     */
    @Operation(summary = "删除评论", description = "删除评论（仅作者和管理员可操作）")
    @AuthRequired
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(HttpServletRequest httpRequest, @Parameter(description = "评论ID") @PathVariable Integer id) {
        try {
            Long userId = (Long) httpRequest.getAttribute("userId");

            boolean success = commentService.deleteComment(id, userId.intValue());
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败或无权限");
            }
        } catch (Exception e) {
            return Result.error("删除评论失败：" + e.getMessage());
        }
    }

    /**
     * 点赞评论
     */
    @Operation(summary = "点赞评论", description = "为评论点赞（需要登录）")
    @AuthRequired
    @PostMapping("/{id}/like")
    public Result<String> likeComment(@Parameter(description = "评论ID") @PathVariable Integer id) {
        try {
            boolean success = commentService.likeComment(id);
            if (success) {
                return Result.success("点赞成功");
            } else {
                return Result.error("点赞失败");
            }
        } catch (Exception e) {
            return Result.error("点赞失败：" + e.getMessage());
        }
    }
}
