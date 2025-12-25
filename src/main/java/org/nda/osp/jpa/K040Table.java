package org.nda.osp.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "K040_ECT_CABINET")
public class K040Table {

    @Column(name = "K001_ID")
    private String k001Id;

    @Id
    @Column(name = "K040_ID")
    private String k040Id;

    @Column(name = "K040_PARENT_ID")
    private String k040ParentId;

    @Column(name = "K040_TITLE")
    private String k040Title;

}
