package com.psychology.feignService;

import com.psychology.pojo.Favorite;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-favorite-service", path = "/api/favorite")
public interface FavoriteFeignService {

    @PostMapping("/addFavorite")
    public boolean addFavorite(@RequestBody Favorite favorite);

    @PostMapping("/removeFavorite")
    public boolean removeFavorite(@RequestBody Favorite favorite);

    @PostMapping("/checkFavorite")
    public boolean checkFavorite(@RequestBody Map<String, Object> map);

    @PostMapping("/getFavoriteByUser")
    public List<Favorite> getFavoriteByUser(@RequestBody Map<String, Object> map);

    @PostMapping("/getFavoriteByPage")
    public PageResult<Favorite> getFavoriteByPage(@RequestBody Map<String, Object> map);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);
}
