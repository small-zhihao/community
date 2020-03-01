package life.kaoyan.community.service;

import life.kaoyan.community.dto.CommentDTO;
import life.kaoyan.community.enums.CommentTypeEnum;
import life.kaoyan.community.exception.CustomizeErrorCode;
import life.kaoyan.community.exception.CustomizeException;
import life.kaoyan.community.mapper.CommentMapper;
import life.kaoyan.community.mapper.QuestionExtMapper;
import life.kaoyan.community.mapper.QuestionMapper;
import life.kaoyan.community.mapper.UserMapper;
import life.kaoyan.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
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

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments=commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIdS =new ArrayList();
        userIdS.addAll(commentators);
        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIdS);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
