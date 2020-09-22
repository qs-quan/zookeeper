package com.wayzim.zookeeper.lock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-21 15:20
 */
@Data
@FieldNameConstants
@TableName("lock_record")
public class LockRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String lockName;


}
