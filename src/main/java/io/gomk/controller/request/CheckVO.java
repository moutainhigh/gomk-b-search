package io.gomk.controller.request;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/26
 */
@ToString
public class CheckVO {
    String name;
    String type;
    String calculate;
    String result;
    String scope;
    String tablefleld;
    String subcontext;
    List<String> patterns = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCalculate() {
        return calculate;
    }

    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public String getTablefleld() {
        return tablefleld;
    }

    public void setTablefleld(String tablefleld) {
        this.tablefleld = tablefleld;
    }

    public String getSubcontext() {
        return subcontext;
    }

    public void setSubcontext(String subcontext) {
        this.subcontext = subcontext;
    }


}
