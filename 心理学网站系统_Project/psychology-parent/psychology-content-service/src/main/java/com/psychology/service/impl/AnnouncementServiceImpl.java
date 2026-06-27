package com.psychology.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psychology.dao.AnnouncementMapper;
import com.psychology.pojo.Announcement;
import com.psychology.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

}
