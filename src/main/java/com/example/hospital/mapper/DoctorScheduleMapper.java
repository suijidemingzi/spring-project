package com.example.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.entity.DoctorSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {

    // 扣减号源（乐观锁）
    @Update("UPDATE doctor_schedule SET remain_num = remain_num - 1, version = version + 1 " +
            "WHERE id = #{scheduleId} AND version = #{version} AND remain_num > 0")
    int reduceRemainNum(@Param("scheduleId") Long scheduleId, @Param("version") Integer version);

    // 恢复号源
    @Update("UPDATE doctor_schedule SET remain_num = remain_num + 1 WHERE id = #{scheduleId}")
    int recoverRemainNum(@Param("scheduleId") Long scheduleId);
}