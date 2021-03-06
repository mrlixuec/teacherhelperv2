package com.spm.teacherhelperv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spm.teacherhelperv2.entity.MenuDO;
import com.spm.teacherhelperv2.entity.SpecDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * SpecDOMapper 接口
 * @author zhangjr
 * @since 2020-05-15 22:42:58
 */

public interface SpecMapper extends BaseMapper<SpecDO> {
}