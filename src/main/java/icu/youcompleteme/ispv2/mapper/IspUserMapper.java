package icu.youcompleteme.ispv2.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import icu.youcompleteme.ispv2.entity.IspUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录用户(IspUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-11 16:24:24
 */
@Mapper
public interface IspUserMapper extends BaseMapper<IspUser> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param list List<IspUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<IspUser> list);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param list List<IspUser> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("list") List<IspUser> list);

}

