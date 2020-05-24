package com.lbeen.spring.sys.service.impl;

import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.sys.bean.Dic;
import com.lbeen.spring.sys.mapper.DicMapper;
import com.lbeen.spring.sys.service.DicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicMapper dicMapper;

    @Override
    public Result saveDic(Dic dic) {
        if (StringUtils.isBlank(dic.getParentId())) {
            dic.setParentId(null);
        }
        Dic exist = dicMapper.selectOne(dic);
        if (StringUtils.isBlank(dic.getId())) {
            if (exist != null) {
                return Result.error(dic.getCode() + "已存在");
            }
            dic.setId(CommonUtil.uuid());
            dicMapper.insert(dic);
        } else {
            if (exist != null && !dic.getId().equals(exist.getId())) {
                return Result.error(dic.getCode() + "已存在");
            }
            dicMapper.update(dic);
        }

        return Result.saveSuccess();
    }

    @Override
    public Dic selectById(String id) {
        return dicMapper.selectById(id);
    }

    @Override
    public List<Dic> selectDicByParentId(String parentId) {
        return dicMapper.selectDicByParentId(parentId);
    }

    @Override
    public List<Dic> selectAllUsedDic() {
        return dicMapper.selectAllUsedDic();
    }
}
