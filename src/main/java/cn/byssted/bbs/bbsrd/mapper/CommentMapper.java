package cn.byssted.bbs.bbsrd.mapper;

import cn.byssted.bbs.bbsrd.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 评论数据访问层
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 根据帖子ID查询评论列表
     * @param postId 帖子ID
     * @return 评论列表
     */
    @Select("SELECT * FROM comments WHERE post_id = #{postId} AND deleted = 0 ORDER BY created_at ASC")
    List<Comment> findByPostId(Integer postId);
    
    /**
     * 根据父评论ID查询子评论列表
     * @param parentCommentId 父评论ID
     * @return 子评论列表
     */
    @Select("SELECT * FROM comments WHERE parent_comment_id = #{parentCommentId} AND deleted = 0 ORDER BY created_at ASC")
    List<Comment> findByParentCommentId(Integer parentCommentId);
    
    /**
     * 增加点赞数
     * @param commentId 评论ID
     */
    @Update("UPDATE comments SET upvotes = upvotes + 1 WHERE comment_id = #{commentId}")
    void incrementUpvotes(Integer commentId);
    
    /**
     * 减少点赞数
     * @param commentId 评论ID
     */
    @Update("UPDATE comments SET upvotes = upvotes - 1 WHERE comment_id = #{commentId} AND upvotes > 0")
    void decrementUpvotes(Integer commentId);
}