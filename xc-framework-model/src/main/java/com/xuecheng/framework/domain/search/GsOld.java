package com.xuecheng.framework.domain.search;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by admin on 2018/2/10.
 */
@Data
@ToString
@Entity
@Table(name="gs_old")
//@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class GsOld implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String old_FID;
    private String old_PRNM;
    private String old_DPOZ;
    private String old_PRTYPE;
    private String old_SEAA;
    private String old_SANUM;
    private String old_SADT;
    private String old_PART;
    private String old_DAREA;

}
