package life.kaoyan.community.cache;

import life.kaoyan.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
       public static List<TagDTO> get(){
           List<TagDTO> tagDTOS = new ArrayList<>();
           TagDTO math = new TagDTO();
           math.setCategoryName("数学");
           math.setTags(Arrays.asList("函数与极限","导数与微分","导数的应用","不定积分",
                   "定积分","微分方程","多元函数","重积分","曲线积分与曲面积分",
                   "无穷级数","行列式","矩阵","线性方程组","特征值与特征向量","二次型","随机事件及概率",
                   "随机变量及其分布","多维随机变量及其分布","数字特征","大数定律","参数估计"));
           tagDTOS.add(math);
           TagDTO English = new TagDTO();
           English.setCategoryName("英语");
           English.setTags(Arrays.asList("完形填空","阅读理解","7选5","排序题","标题匹配","翻译","写作"));
           tagDTOS.add(English);
           TagDTO politics = new TagDTO();
           politics.setCategoryName("英语");
           politics.setTags(Arrays.asList("反对外国侵略战争","早期探索","辛亥革命","开天辟地大事变","革命新道路",
                   "抗日战争","为建设新中国而奋斗","社会主义基本制度全面建立","社会主义建设在探索中曲折发展","改革开放",
                   "物质世界和实践","事物的普遍联系和发展","客观规律与主观能动性","真理与价值"));
           tagDTOS.add(politics);
           return tagDTOS;
       }
       public static String filterInvalid(String tags){
           String[] split = StringUtils.split(tags, "，");
           List<TagDTO> tagDTOS = get();
           List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
           String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining("，"));

           return invalid;
       }
}
