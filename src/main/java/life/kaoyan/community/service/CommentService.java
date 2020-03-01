package life.kaoyan.community.service;

import life.kaoyan.community.enums.CommentTypeEnum;
import life.kaoyan.community.exception.CustomizeErrorCode;
import life.kaoyan.community.exception.CustomizeException;
import life.kaoyan.community.mapper.CommentMapper;
import life.kaoyan.community.mapper.QuestionExtMapper;
import life.kaoyan.community.mapper.QuestionMapper;
import life.kaoyan.community.model.Comment;
import life.kaoyan.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Transactional
    public void insert(Comment comment) {
         if (comment.getParentId()==null||comment.getParentId()==0){
             throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
         }
         if (comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
             throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);

         }
         if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
             //回复评论
             Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
             if (dbComment==null){
                 throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
             }
             commentMapper.insert(comment);
         }else {
             //回复问题
             Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
             if (question==null){
                 throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
             }
             commentMapper.insert(comment);
             question.setCommentCount(1);
             questionExtMapper.incCommentCount(question);
         }
    }
}
