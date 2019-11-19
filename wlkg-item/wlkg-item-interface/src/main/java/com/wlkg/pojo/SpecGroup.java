package com.wlkg.pojo;

import lombok.Data;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.util.List;

@Table(name = "tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> params;
}