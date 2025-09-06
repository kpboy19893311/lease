package com.lyc.lease.web.app.service.impl;

import com.lyc.lease.model.entity.ApartmentInfo;
import com.lyc.lease.model.entity.FacilityInfo;
import com.lyc.lease.model.entity.LabelInfo;
import com.lyc.lease.model.enums.ItemType;
import com.lyc.lease.web.app.mapper.*;
import com.lyc.lease.web.app.service.ApartmentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.lyc.lease.web.app.vo.apartment.ApartmentItemVo;
import com.lyc.lease.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.lyc.lease.common.constant.RedisConstant.APP_APARTMENT_PREFIX;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2025-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    LabelInfoMapper labelInfoMapper;

    @Autowired
    GraphInfoMapper graphInfoMapper;

    @Autowired
    RoomInfoMapper roomInfoMapper;

    @Autowired
    FacilityInfoMapper facilityInfoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public ApartmentItemVo selectApartmentItemVoById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);

        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);

        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);

        BigDecimal minRent = roomInfoMapper.selectMinRentByApartmentId(id);

        ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);

        apartmentItemVo.setGraphVoList(graphVoList);
        apartmentItemVo.setLabelInfoList(labelInfoList);
        apartmentItemVo.setMinRent(minRent);
        return apartmentItemVo;
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //改造一下这个接口，获取公寓详细信息放到缓存里，加快接口响应。
        String key = APP_APARTMENT_PREFIX + id;
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            return (ApartmentDetailVo) object;
        } else {

            //1.查询公寓信息
            ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
            //2.查询图片信息
            List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
            //3.查询标签信息
            List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
            //4.查询配套信息
            List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
            //5.查询最小租金
            BigDecimal minRent = roomInfoMapper.selectMinRentByApartmentId(id);

            ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();

            BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
            apartmentDetailVo.setGraphVoList(graphVoList);
            apartmentDetailVo.setLabelInfoList(labelInfoList);
            apartmentDetailVo.setFacilityInfoList(facilityInfoList);
            apartmentDetailVo.setMinRent(minRent);

            redisTemplate.opsForValue().set(key, apartmentDetailVo);
            return apartmentDetailVo;
        }


    }

    //双因子认证
    //登录都要要满足这个要求。

    //除了用户名和密码以外，还有有一个其他的认证方式，手机验证码或者 动态令牌


    //信创、国产化

    //Ukey登录、达梦数据库、
    //东方通、金蝶这种中间件去部署

    //算法用国密算法，SM2、SM3、SM4

}




