package com.lbeen.spring.sys.web;

import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.common.cache.DicCache;
import com.lbeen.spring.sys.bean.Dic;
import com.lbeen.spring.sys.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/dic/")
public class DicController {
    @Autowired
    private DicService dicService;

    @RequestMapping("saveDic")
    public Result saveDic(Dic dic) {
        return dicService.saveDic(dic);
    }

    @RequestMapping("getDicById")
    public Dic getDicById(String id) {
        return dicService.selectById(id);
    }

    @RequestMapping("getDics")
    public List<Dic> getDics(String parentId) {
        return dicService.selectDicByParentId(parentId);
    }

    @RequestMapping("getUsedDics")
    public Collection<Dic> getUsedDics(String type) {
        return DicCache.getDics(type);
    }

    @RequestMapping("getUsedDic")
    public Dic getUsedDic(String type, String code) {
        return DicCache.getDic(type, code);
    }

    @RequestMapping("refreshDicCache")
    public Result refreshDicCache() {
        DicCache.loadCache();
        return Result.successMsg("刷新成功");
    }
}
