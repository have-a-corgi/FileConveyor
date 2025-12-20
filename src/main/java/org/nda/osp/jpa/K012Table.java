package org.nda.osp.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "K012_ECT_CABINER")
public class K012Table {

    @Column(name = "K001_ID")
    private String k001Id;

    @Id
    @Column(name = "K012_ID")
    private String k012Id;

    @Column(name = "K012_PARENT_ID")
    private String k012ParentId;

    @Column(name = "K012_TITLE")
    private String k012Title;

}
