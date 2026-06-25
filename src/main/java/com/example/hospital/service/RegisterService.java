package com.example.hospital.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.hospital.common.Result;
import com.example.hospital.entity.*;
import com.example.hospital.mapper.DoctorScheduleMapper;
import com.example.hospital.mapper.RegisterOrderMapper;
import com.example.hospital.mapper.SysDepartmentMapper;
import com.example.hospital.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RegisterService {

    @Resource
    private DoctorScheduleMapper scheduleMapper;

    @Resource
    private RegisterOrderMapper orderMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysDepartmentMapper departmentMapper;

    private final ReentrantLock lock = new ReentrantLock();

    // ===================== 挂号 =====================
    @Transactional(rollbackFor = Exception.class)
    public Result<String> submitRegister(Long patientId, Long scheduleId) {
        if (patientId == null || scheduleId == null || patientId <= 0 || scheduleId <= 0) {
            return Result.fail("参数错误");
        }

        lock.lock();
        try {
            SysUser patient = userMapper.selectById(patientId);
            if (patient == null) {
                return Result.fail("患者不存在");
            }

            DoctorSchedule schedule = scheduleMapper.selectById(scheduleId);
            if (schedule == null) {
                return Result.fail("号源不存在");
            }

            if (schedule.getRemainNum() <= 0) {
                return Result.fail("号源已售罄，剩余：" + schedule.getRemainNum());
            }

            // 扣减号源
            int rows = scheduleMapper.reduceRemainNum(scheduleId, schedule.getVersion());
            if (rows == 0) {
                return Result.fail("抢号冲突，请重试");
            }

            // 创建订单
            RegisterOrder order = new RegisterOrder();
            order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
            order.setPatientId(patientId);
            order.setDeptId(schedule.getDeptId());
            order.setDoctorId(schedule.getDoctorId());
            order.setScheduleId(scheduleId);
            order.setScheduleDate(schedule.getScheduleDate());
            order.setTimeType(schedule.getTimeType());
            order.setRegisterFee(schedule.getRegisterFee());
            order.setPayStatus(1);
            order.setPayTime(LocalDateTime.now());
            order.setOrderStatus(1);
            order.setIdCard(patient.getIdCard());
            order.setPatientName(patient.getRealName());
            order.setPhone(patient.getPhone());

            // 关键修复：时间不能为空
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());

            orderMapper.insert(order);

            // 必须返回 orderId 给前端！
            return Result.success("挂号成功！订单ID：" + order.getId());

        } finally {
            lock.unlock();
        }
    }

    // ===================== 查询我的挂号 =====================
    public Result<List<RegisterOrder>> getMyOrder(Long patientId) {
        if (patientId == null || patientId <= 0) {
            return Result.success(List.of());
        }

        LambdaQueryWrapper<RegisterOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegisterOrder::getPatientId, patientId)
                .orderByDesc(RegisterOrder::getCreateTime);

        List<RegisterOrder> list = orderMapper.selectList(wrapper);
        return Result.success(list);
    }

    // ===================== 取消挂号（修复版！） =====================
    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancelRegister(Long orderId) {
        if (orderId == null || orderId <= 0) {
            System.err.println("【取消挂号】订单ID无效：" + orderId);
            return Result.fail("订单ID无效");
        }

        lock.lock();
        try {
            // 1. 查询订单
            RegisterOrder order = orderMapper.selectById(orderId);
            if (order == null) {
                System.err.println("【取消挂号】订单不存在：" + orderId);
                return Result.fail("订单不存在");
            }

            // ====================== 要求：打印订单号到控制台 ======================
            System.out.println("==============================================");
            System.out.println("【取消挂号】订单ID：" + orderId);
            System.out.println("【取消挂号】订单号：" + order.getOrderNo());
            System.out.println("==============================================");

            // 2. 状态校验
            if (order.getOrderStatus() == 3) {
                return Result.fail("已取消，无需重复操作");
            }
            if (order.getOrderStatus() == 2) {
                return Result.fail("已就诊，无法取消");
            }

            // 3. 恢复号源（关键！剩余号源显示全靠这行）
            DoctorSchedule schedule = scheduleMapper.selectById(order.getScheduleId());
            if (schedule != null) {
                scheduleMapper.recoverRemainNum(order.getScheduleId());
            }

            // 4. 更新订单状态
            order.setOrderStatus(3);
            order.setPayStatus(2);
            order.setCancelTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            orderMapper.updateById(order);

            return Result.success("取消成功");

        } finally {
            lock.unlock();
        }
    }

    // ===================== 新增：候诊排队状态查询 =====================
    public Result<QueueStatus> getQueueStatus(Long patientId) {
        if (patientId == null || patientId <= 0) {
            return Result.fail("参数错误");
        }

        // 1. 查询患者今天正处于候诊状态的挂号订单
        // 优化点：使用 MySQL 原生的 CURDATE() 替代 Java LocalDate.now()，彻底解决时区偏差问题
        LambdaQueryWrapper<RegisterOrder> myOrderWrapper = new LambdaQueryWrapper<>();
        myOrderWrapper.eq(RegisterOrder::getPatientId, patientId)
                .apply("schedule_date = CURDATE()") // 保证与数据库当天日期绝对一致
                .eq(RegisterOrder::getOrderStatus, 1) // 1-预约成功
                .orderByDesc(RegisterOrder::getCreateTime)
                .last("LIMIT 1");

        RegisterOrder myOrder = orderMapper.selectOne(myOrderWrapper);
        if (myOrder == null) {
            return Result.fail("您今天暂无候诊排队信息");
        }

        // 2. 根据实际关联的 dept_id 获取科室诊室名称
        String deptName = "未知诊室";
        if (myOrder.getDeptId() != null) {
            SysDepartment dept = departmentMapper.selectById(myOrder.getDeptId());
            if (dept != null) {
                deptName = dept.getDeptName() + "诊室";
            }
        }

        // 3. 查询当前正在叫号的就诊序号
        // 优化点：同样使用 CURDATE()，且限定相同的就诊时段（AM/PM）
        LambdaQueryWrapper<RegisterOrder> callingWrapper = new LambdaQueryWrapper<>();
        callingWrapper.eq(RegisterOrder::getDeptId, myOrder.getDeptId())
                .apply("schedule_date = CURDATE()")
                .eq(RegisterOrder::getTimeType, myOrder.getTimeType())
                .eq(RegisterOrder::getOrderStatus, 2) // 2-已就诊 (代表已叫过号的最新状态)
                .orderByDesc(RegisterOrder::getVisitNum)
                .last("LIMIT 1");

        RegisterOrder callingOrder = orderMapper.selectOne(callingWrapper);
        int currentCalling = 0;
        if (callingOrder != null && callingOrder.getVisitNum() != null) {
            currentCalling = callingOrder.getVisitNum();
        }

        // 4. 获取用户真实的排队序号
        int myQueueNum = (myOrder.getVisitNum() != null) ? myOrder.getVisitNum() : 0;

        // 5. 计算当前等待人数
        int waitingCount = myQueueNum - currentCalling - 1;
        if (waitingCount < 0) {
            waitingCount = 0;
        }

        // 6. 构造返回对象
        QueueStatus status = new QueueStatus();
        status.setDeptName(deptName);
        status.setCurrentCallingNumber(currentCalling);
        status.setUserQueueNumber(myQueueNum);
        status.setWaitingCount(waitingCount);

        return Result.success(status);
    }
}