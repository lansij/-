package com.psychology.feignService;

import com.psychology.pojo.Comment;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-comment-service", path = "/api/comment")
public interface CommentFeignService {

    @PostMapping("/addComment")
    public boolean addComment(@RequestBody Comment comment);

    @PostMapping("/updateComment")
    public boolean updateComment(@RequestBody Comment comment);

    @PostMapping("/deleteCommentById/{id}")
    public boolean deleteCommentById(@PathVariable("id") Long id);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/getCommentById/{id}")
    public Comment getCommentById(@PathVariable("id") Long id);

    @PostMapping("/getCommentByUserId/{userId}")
    public List<Comment> getCommentByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/getCommentByTarget")
    public List<Comment> getCommentByTarget(@RequestBody Map<String, Object> map);

    @PostMapping("/getCommentByPage")
    public PageResult<Comment> getCommentByPage(@RequestBody Map<String, Object> map);
}
