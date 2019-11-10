package io.gomk.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "d_zb_pkg")
@Entity
@Data
@Accessors(chain = true)
public class PackageDO {

    @Id
    private String PKG_CODE;

}
