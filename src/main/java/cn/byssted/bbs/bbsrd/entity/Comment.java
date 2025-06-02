package cn.byssted.bbs.bbsrd.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@TableName("comments")
public class Comment {

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @TableField("post_id")
    private Integer postId;

    @TableField("user_id")
    private Integer userId;

    @TableField("parent_comment_id")
    private Integer parentCommentId;

    @TableField("content")
    private String content;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("upvotes")
    private Integer upvotes;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 构造函数
    public Comment() {}

    // Getter 和 Setter 方法
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", userId=" + userId +
                ", parentCommentId=" + parentCommentId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", upvotes=" + upvotes +
                ", deleted=" + deleted +
                '}';
    }
}