package com.phoenix.devops.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2025-02-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentMenu implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long value;
    private String label;
    private ParentMenu[] children;
}
