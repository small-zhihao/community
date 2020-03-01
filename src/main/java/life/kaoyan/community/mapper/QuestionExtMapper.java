package life.kaoyan.community.mapper;

import life.kaoyan.community.model.Question;

public interface QuestionExtMapper {
   int incView(Question record);
   int incCommentCount(Question record);
}