package com.psychology.controller;

import com.psychology.feignService.FavoriteFeignService;
import com.psychology.feignService.ArticleFeignService;
import com.psychology.feignService.BookFeignService;
import com.psychology.feignService.AnnouncementFeignService;
import com.psychology.feignService.UserFeignService;
import com.psychology.pojo.Favorite;
import com.psychology.pojo.Article;
import com.psychology.pojo.Book;
import com.psychology.pojo.Announcement;
import com.psychology.pojo.User;
import com.psychology.untils.R;
import com.psychology.utils.PageResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteFeignService favoriteFeignService;
    @Autowired
    private ArticleFeignService articleFeignService;
    @Autowired
    private BookFeignService bookFeignService;
    @Autowired
    private AnnouncementFeignService announcementFeignService;
    @Autowired
    private UserFeignService userFeignService;

    //管理员收藏列表
    @GetMapping("/toList")
    public String toList() {
        return "favorite/list";
    }

    //用户我的收藏
    @RequestMapping("/toMyList")
    public String toMyList() {
        return "favorite/myList";
    }

    //用户我的文章收藏
    @RequestMapping("/toMyArticleList")
    public String toMyArticleList() {
        return "favorite/myArticleList";
    }

    //用户我的书籍收藏
    @RequestMapping("/toMyBookList")
    public String toMyBookList() {
        return "favorite/myBookList";
    }

    //分页查询（带用户昵称）
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Map<String, Object>> getPage(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageSize", limit);
        PageResult<Favorite> favPage = favoriteFeignService.getFavoriteByPage(map);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Favorite f : favPage.getData()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", f.getId());
            item.put("userId", f.getUserId());
            item.put("targetType", f.getTargetType());
            item.put("targetId", f.getTargetId());
            item.put("createTime", f.getCreateTime());
            User user = userFeignService.getUserById(f.getUserId());
            item.put("nickname", user != null && user.getNickname() != null ? user.getNickname() : "未知用户");
            list.add(item);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(favPage.getCount());
        result.setData(list);
        return result;
    }

    //查询用户自己的收藏
    @PostMapping("/getMyFavorites")
    @ResponseBody
    public PageResult<Favorite> getMyFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("targetType", "ARTICLE");
        List<Favorite> articleFavorites = favoriteFeignService.getFavoriteByUser(map);
        map.put("targetType", "BOOK");
        List<Favorite> bookFavorites = favoriteFeignService.getFavoriteByUser(map);
        articleFavorites.addAll(bookFavorites);
        PageResult<Favorite> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(articleFavorites.size());
        result.setData(articleFavorites);
        return result;
    }

    //查询用户收藏的文章（带标题）
    @PostMapping("/getMyArticleFavorites")
    @ResponseBody
    public PageResult<Map<String, Object>> getMyArticleFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("targetType", "ARTICLE");
        List<Favorite> favorites = favoriteFeignService.getFavoriteByUser(map);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Favorite f : favorites) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", f.getId());
            item.put("targetId", f.getTargetId());
            item.put("createTime", f.getCreateTime());
            Article article = articleFeignService.getArticleById(f.getTargetId());
            item.put("title", article != null ? article.getTitle() : "已删除");
            item.put("author", article != null ? article.getAuthor() : "");
            list.add(item);
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(list.size());
        result.setData(list);
        return result;
    }

    //查询用户收藏的书籍（带标题）
    @PostMapping("/getMyBookFavorites")
    @ResponseBody
    public PageResult<Map<String, Object>> getMyBookFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("targetType", "BOOK");
        List<Favorite> favorites = favoriteFeignService.getFavoriteByUser(map);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Favorite f : favorites) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", f.getId());
            item.put("targetId", f.getTargetId());
            item.put("createTime", f.getCreateTime());
            Book book = bookFeignService.getBookById(f.getTargetId());
            item.put("title", book != null ? book.getTitle() : "已删除");
            item.put("author", book != null && book.getBookAuthor() != null ? book.getBookAuthor() : "");
            list.add(item);
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(list.size());
        result.setData(list);
        return result;
    }

    //查询用户收藏的活动（带标题）
    @PostMapping("/getMyAnnouncementFavorites")
    @ResponseBody
    public PageResult<Map<String, Object>> getMyAnnouncementFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("targetType", "ANNOUNCEMENT");
        List<Favorite> favorites = favoriteFeignService.getFavoriteByUser(map);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Favorite f : favorites) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", f.getId());
            item.put("targetId", f.getTargetId());
            item.put("createTime", f.getCreateTime());
            Announcement announcement = announcementFeignService.getAnnouncementById(f.getTargetId());
            item.put("title", announcement != null ? announcement.getTitle() : "已删除");
            item.put("author", announcement != null ? announcement.getAuthor() : "");
            list.add(item);
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(list.size());
        result.setData(list);
        return result;
    }

    //删除收藏
    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        boolean flag = favoriteFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }

    //批量删除
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = favoriteFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }

    //检查是否已收藏
    @PostMapping("/check")
    @ResponseBody
    public boolean check(@RequestParam String targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return false;
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("targetType", targetType);
        map.put("targetId", targetId);
        return favoriteFeignService.checkFavorite(map);
    }

    //添加收藏
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestParam String targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return R.error("请先登录");
        Favorite favorite = new Favorite();
        favorite.setUserId(user.getId());
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        boolean flag = favoriteFeignService.addFavorite(favorite);
        return flag ? R.right() : R.error();
    }

    //取消收藏
    @PostMapping("/remove")
    @ResponseBody
    public R remove(@RequestParam String targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return R.error("请先登录");
        Favorite favorite = new Favorite();
        favorite.setUserId(user.getId());
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        boolean flag = favoriteFeignService.removeFavorite(favorite);
        return flag ? R.right() : R.error();
    }
}
