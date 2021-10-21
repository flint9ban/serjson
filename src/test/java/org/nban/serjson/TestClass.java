package org.nban.serjson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author liyi
 * @date 2021/10/17
 */
@Getter
@Setter
public class TestClass {

    private String a;

    private List<String> bList;

    private TestInnerClass testInnerClass;


    private List<TestInnerClass> innerClassList;

}
