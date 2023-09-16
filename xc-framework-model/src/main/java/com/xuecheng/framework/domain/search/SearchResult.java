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
@Table(name="search_result")
//@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SearchResult implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;
    @Id
//    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    private String fid;
    private String searchname;
    private String scope;
    private String resultname;

}
