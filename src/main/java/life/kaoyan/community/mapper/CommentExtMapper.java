package life.kaoyan.community.mapper;

import life.kaoyan.community.model.Comment;
import life.kaoyan.community.model.CommentExample;
import life.kaoyan.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}