package com.luoying.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 落樱的悔恨
 */
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 9110567632735853321L;
    /**
     * 当前是第几页
     */
    protected Long page = 1l;
    /**
     * 页面大小
     */
    protected Long pageSize = 10l;
}
