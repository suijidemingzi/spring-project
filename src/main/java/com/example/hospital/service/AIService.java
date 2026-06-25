package com.example.hospital.service;

import com.example.hospital.common.Result;
import com.example.hospital.entity.SysDepartment;
import com.example.hospital.mapper.SysDepartmentMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIService {

    @Resource
    private SysDepartmentMapper departmentMapper;

    private final Map<String, List<String>> symptomDepartmentMap = new HashMap<>();
    private final Map<String, String> faqMap = new HashMap<>();

    public AIService() {
        initSymptomDepartmentMap();
        initFaqMap();
    }

    private void initSymptomDepartmentMap() {
        symptomDepartmentMap.put("头痛", Arrays.asList("神经内科", "急诊科"));
        symptomDepartmentMap.put("头晕", Arrays.asList("神经内科", "心血管内科"));
        symptomDepartmentMap.put("发烧", Arrays.asList("急诊科", "呼吸内科"));
        symptomDepartmentMap.put("咳嗽", Arrays.asList("呼吸内科", "急诊科"));
        symptomDepartmentMap.put("感冒", Arrays.asList("呼吸内科", "急诊科"));
        symptomDepartmentMap.put("发烧", Arrays.asList("急诊科", "呼吸内科"));
        symptomDepartmentMap.put("胃痛", Arrays.asList("消化内科", "急诊科"));
        symptomDepartmentMap.put("腹痛", Arrays.asList("消化内科", "急诊科", "外科"));
        symptomDepartmentMap.put("腹泻", Arrays.asList("消化内科", "急诊科"));
        symptomDepartmentMap.put("呕吐", Arrays.asList("消化内科", "急诊科"));
        symptomDepartmentMap.put("胸闷", Arrays.asList("心血管内科", "呼吸内科"));
        symptomDepartmentMap.put("胸痛", Arrays.asList("心血管内科", "急诊科"));
        symptomDepartmentMap.put("心悸", Arrays.asList("心血管内科"));
        symptomDepartmentMap.put("关节痛", Arrays.asList("骨科", "风湿免疫科"));
        symptomDepartmentMap.put("腰痛", Arrays.asList("骨科", "康复科"));
        symptomDepartmentMap.put("腿痛", Arrays.asList("骨科"));
        symptomDepartmentMap.put("背痛", Arrays.asList("骨科", "康复科"));
        symptomDepartmentMap.put("皮肤瘙痒", Arrays.asList("皮肤科"));
        symptomDepartmentMap.put("皮疹", Arrays.asList("皮肤科"));
        symptomDepartmentMap.put("过敏", Arrays.asList("皮肤科", "急诊科"));
        symptomDepartmentMap.put("视力模糊", Arrays.asList("眼科"));
        symptomDepartmentMap.put("眼睛红肿", Arrays.asList("眼科"));
        symptomDepartmentMap.put("耳鸣", Arrays.asList("耳鼻喉科"));
        symptomDepartmentMap.put("喉咙痛", Arrays.asList("耳鼻喉科", "呼吸内科"));
        symptomDepartmentMap.put("牙痛", Arrays.asList("口腔科"));
        symptomDepartmentMap.put("牙龈出血", Arrays.asList("口腔科"));
        symptomDepartmentMap.put("妇科", Arrays.asList("妇产科"));
        symptomDepartmentMap.put("月经", Arrays.asList("妇产科"));
        symptomDepartmentMap.put("怀孕", Arrays.asList("妇产科"));
        symptomDepartmentMap.put("小儿", Arrays.asList("儿科"));
        symptomDepartmentMap.put("儿童", Arrays.asList("儿科"));
        symptomDepartmentMap.put("孩子", Arrays.asList("儿科"));
        symptomDepartmentMap.put("发烧", Arrays.asList("急诊科", "呼吸内科"));
        symptomDepartmentMap.put("高血压", Arrays.asList("心血管内科"));
        symptomDepartmentMap.put("糖尿病", Arrays.asList("内分泌科"));
        symptomDepartmentMap.put("血糖高", Arrays.asList("内分泌科"));
        symptomDepartmentMap.put("甲状腺", Arrays.asList("内分泌科"));
        symptomDepartmentMap.put("失眠", Arrays.asList("神经内科", "精神科"));
        symptomDepartmentMap.put("抑郁", Arrays.asList("精神科", "心理咨询"));
        symptomDepartmentMap.put("焦虑", Arrays.asList("精神科", "心理咨询"));
        symptomDepartmentMap.put("便秘", Arrays.asList("消化内科"));
        symptomDepartmentMap.put("便血", Arrays.asList("消化内科", "肛肠科"));
        symptomDepartmentMap.put("尿血", Arrays.asList("泌尿外科", "肾内科"));
        symptomDepartmentMap.put("尿频", Arrays.asList("泌尿外科"));
        symptomDepartmentMap.put("尿急", Arrays.asList("泌尿外科"));
        symptomDepartmentMap.put("尿痛", Arrays.asList("泌尿外科"));
        symptomDepartmentMap.put("肝炎", Arrays.asList("感染科", "消化内科"));
        symptomDepartmentMap.put("肺炎", Arrays.asList("呼吸内科", "急诊科"));
        symptomDepartmentMap.put("骨折", Arrays.asList("骨科", "急诊科"));
        symptomDepartmentMap.put("扭伤", Arrays.asList("骨科", "急诊科"));
        symptomDepartmentMap.put("烧伤", Arrays.asList("烧伤科", "急诊科"));
        symptomDepartmentMap.put("烫伤", Arrays.asList("烧伤科", "急诊科"));
        symptomDepartmentMap.put("中风", Arrays.asList("神经内科", "急诊科"));
        symptomDepartmentMap.put("脑梗", Arrays.asList("神经内科", "急诊科"));
        symptomDepartmentMap.put("心梗", Arrays.asList("心血管内科", "急诊科"));
    }

    private void initFaqMap() {
        faqMap.put("医院地址", "我院位于市中心繁华地段，具体地址为：XX市XX区XX路XX号。");
        faqMap.put("医院位置", "我院位于市中心繁华地段，具体地址为：XX市XX区XX路XX号。");
        faqMap.put("联系电话", "我院服务热线：0XX-XXXXXXXX，24小时为您服务。");
        faqMap.put("咨询电话", "我院服务热线：0XX-XXXXXXXX，24小时为您服务。");
        faqMap.put("上班时间", "门诊时间：周一至周五 8:00-17:30，周六至周日 9:00-16:00。急诊24小时开放。");
        faqMap.put("挂号时间", "现场挂号：7:30-17:00；网上预约：全天24小时开放。");
        faqMap.put("如何挂号", "您可以通过以下方式挂号：1. 医院官网预约；2. 微信公众号预约；3. 现场窗口挂号；4. 电话预约。");
        faqMap.put("预约挂号", "您可以通过医院官网、微信公众号或拨打服务热线进行预约挂号。");
        faqMap.put("取消挂号", "已预约的挂号可以在就诊前一天24:00前通过原预约渠道取消。");
        faqMap.put("退号", "已预约的挂号可以在就诊前一天24:00前通过原预约渠道取消并退款。");
        faqMap.put("医保", "我院支持医保结算，请携带医保卡和有效身份证件就诊。");
        faqMap.put("社保卡", "我院支持社保卡结算，请携带社保卡和有效身份证件就诊。");
        faqMap.put("停车", "我院设有地下停车场，就诊患者可享受2小时免费停车。");
        faqMap.put("病历", "就诊时请携带本人身份证、医保卡及既往病历资料。");
        faqMap.put("就诊流程", "1. 挂号；2. 到相应科室候诊；3. 医生诊断；4. 缴费取药。");
        faqMap.put("科室介绍", "我院设有内科、外科、妇产科、儿科、骨科、急诊科等20多个临床科室。");
        faqMap.put("专家门诊", "专家门诊时间为周一至周五，可提前7天预约。");
        faqMap.put("体检", "我院体检中心提供多种体检套餐，可通过官网或电话预约。");
        faqMap.put("住院", "住院需由主治医生开具住院证，携带身份证、医保卡到住院部办理手续。");
        faqMap.put("出院", "出院时请到住院部结算窗口办理出院手续。");
        faqMap.put("药房", "门诊药房位于门诊楼一层，住院药房位于住院楼一层。");
        faqMap.put("检验科", "检验科位于门诊楼二层，检查报告可在自助机打印。");
        faqMap.put("放射科", "放射科位于门诊楼地下一层，检查需提前预约。");
        faqMap.put("B超", "B超检查位于门诊楼三层，需提前预约。");
        faqMap.put("CT", "CT检查位于门诊楼地下一层，需提前预约。");
        faqMap.put("MRI", "MRI检查位于门诊楼地下一层，需提前预约。");
        faqMap.put("核酸检测", "核酸检测点位于医院西侧，检测时间：8:00-17:00。");
        faqMap.put("疫苗接种", "疫苗接种门诊位于门诊楼四层，周一至周五开放。");
        faqMap.put("发热门诊", "发热门诊位于门诊楼东侧，24小时开放。");
        faqMap.put("急诊", "急诊科位于门诊楼北侧，24小时开放。");
        faqMap.put("急诊电话", "急诊热线：0XX-XXXXXXX。");
        faqMap.put("投诉", "如有投诉建议，请拨打服务监督电话：0XX-XXXXXXXX。");
        faqMap.put("建议", "如有投诉建议，请拨打服务监督电话：0XX-XXXXXXXX。");
    }

    public Result<String> askQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return Result.fail("请输入您的问题");
        }

        String trimmedQuestion = question.trim();

        if (trimmedQuestion.contains("挂什么科") || trimmedQuestion.contains("该挂") || 
            trimmedQuestion.contains("哪个科") || trimmedQuestion.contains("什么科室")) {
            return analyzeSymptom(trimmedQuestion);
        }

        for (Map.Entry<String, String> entry : faqMap.entrySet()) {
            if (trimmedQuestion.contains(entry.getKey())) {
                return Result.success(entry.getValue());
            }
        }

        return Result.success(generalResponse(trimmedQuestion));
    }

    private Result<String> analyzeSymptom(String question) {
        List<String> matchedDepartments = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : symptomDepartmentMap.entrySet()) {
            if (question.contains(entry.getKey())) {
                matchedDepartments.addAll(entry.getValue());
            }
        }

        if (matchedDepartments.isEmpty()) {
            return Result.success("根据您的症状描述，建议您先挂【全科】或【急诊科】进行初步诊断，由医生为您进一步转诊。\n\n温馨提示：如果症状紧急，请立即前往急诊科。");
        }

        Set<String> uniqueDepts = new LinkedHashSet<>(matchedDepartments);
        StringBuilder response = new StringBuilder("根据您的症状，建议您挂以下科室：\n\n");
        
        int index = 1;
        for (String dept : uniqueDepts) {
            response.append(index++).append(". ").append(dept).append("\n");
        }
        
        response.append("\n温馨提示：如果症状紧急或不确定，建议先挂急诊科进行初步诊断。");
        
        return Result.success(response.toString());
    }

    private String generalResponse(String question) {
        String[] responses = {
            "您好！感谢您的咨询。关于您的问题，建议您拨打我院服务热线：0XX-XXXXXXXX，我们的工作人员会为您提供更详细的解答。",
            "您好！如果您有就诊需求，可以通过我院官网或微信公众号进行预约挂号。",
            "感谢您的提问！为了给您更准确的答复，建议您直接到我院相关科室咨询专业医生。",
            "您好！您可以通过我院官方渠道获取更多详细信息，如有需要请随时联系我们。"
        };
        
        Random random = new Random();
        return responses[random.nextInt(responses.length)];
    }

    public Result<List<Map<String, Object>>> getAllDepartments() {
        List<SysDepartment> departments = departmentMapper.selectList(null);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (SysDepartment dept : departments) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", dept.getId());
            map.put("name", dept.getDeptName());
            map.put("description", dept.getDeptDesc());
            result.add(map);
        }
        
        return Result.success(result);
    }
}