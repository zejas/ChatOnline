package com.zejas.authsvr.model.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 *  <p>  </p>
 *
 * @author ZShuo
 * @description 
 * @date 2025/7/15 17:20
 */
@Data
public class UserRole implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long roleId;
    private Date createTime;
    private String roleName;
    private Integer roleCode;
}
