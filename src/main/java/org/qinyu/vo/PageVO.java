package org.qinyu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageVO<T> {

    private Long total;

    private List<T> data;

    public PageVO() {

    }
}
