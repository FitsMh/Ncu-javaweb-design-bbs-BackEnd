package cn.byssted.bbs.bbsrd.service;

import cn.byssted.bbs.bbsrd.entity.Comment;
import cn.byssted.bbs.bbsrd.entity.User;
import cn.byssted.bbs.bbsrd.mapper.CommentMapper;
import cn.byssted.bbs.bbsrd.mapper.PostMapper;
import cn.byssted.bbs.bbsrd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务类
 */
@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;
    
    /**
     * 根据帖子ID获取评论列表
     * @param postId 帖子ID
     * @return 评论列表
     */
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentMapper.findByPostId(postId);
    }
    
    /**
     * 根据父评论ID获取子评论列表
     * @param parentCommentId 父评论ID
     * @return 子评论列表
     */
    public List<Comment> getCommentsByParentId(Integer parentCommentId) {
        return commentMapper.findByParentCommentId(parentCommentId);
    }
    
    /**
     * 创建评论
     * @param postId 帖子ID
     * @param userId 用户ID
     * @param content 评论内容
     * @param parentCommentId 父评论ID（可选）
     * @return 创建的评论
     */
    public Comment createComment(Integer postId, Integer userId, String content, Integer parentCommentId) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentCommentId(parentCommentId);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpvotes(0);
        
        commentMapper.insert(comment);
        
        // 增加帖子的评论数
        postMapper.incrementCommentCount(postId.longValue());
        
        return comment;
    }
    
    /**
     * 更新评论
     * @param comment 评论信息
     * @return 更新结果
     */
    public boolean updateComment(Comment comment) {
        return commentMapper.updateById(comment) > 0;
    }
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 删除结果
     */
    public boolean deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentMapper.selectById(commentId);
        User user = userMapper.selectById(userId);
        if (comment == null) {
            return false;
        }
        if (!comment.getUserId().equals(userId) && !user.getIsAdmin().equals("1")) {
            return false;
        }
        boolean success = commentMapper.deleteById(commentId) > 0;
        if (success) {
            // 减少帖子的评论数
            postMapper.decrementCommentCount(comment.getPostId().longValue());
        }
        
        return success;
    }
    
    /**
     * 点赞评论
     * @param commentId 评论ID
     * @return 点赞结果
     */
    public boolean likeComment(Integer commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }
        commentMapper.incrementUpvotes(commentId);
        return true;
    }
    
    /**
     * 取消点赞评论
     * @param commentId 评论ID
     * @return 取消点赞结果
     */
    public boolean unlikeComment(Integer commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }
        commentMapper.decrementUpvotes(commentId);
        return true;
    }
    
    /**
     * 根据ID获取评论
     * @param commentId 评论ID
     * @return 评论信息
     */
    public Comment getCommentById(Integer commentId) {
        return commentMapper.selectById(commentId);
    }
}
