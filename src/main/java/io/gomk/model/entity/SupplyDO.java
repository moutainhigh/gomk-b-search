package io.gomk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "i_zb_line_prj_suppl")
@Entity
@Data
@Accessors(chain = true)
public class SupplyDO {

    @Id
    private String GUID;

    @JsonProperty("mate_name")
    private String mateName;

    @JsonProperty("mate_code")
    private String mateCode;


}
