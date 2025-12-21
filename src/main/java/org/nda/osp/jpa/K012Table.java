package org.nda.osp.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


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
