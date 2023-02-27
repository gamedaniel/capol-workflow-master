package com.capol.workflow.server.algorithm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class GeneralServiceTest {
    @Autowired
    private GeneralService modelerService = new GeneralService();

    @Test
    public void longCommonSubsequence() {
        String result = modelerService.longCommonSubsequence("1", "2");
        System.out.println(result);
        assert result.equals("-1");

        result = modelerService.longCommonSubsequence("abc", "abc");
        System.out.println(result);
        assert result.equals("abc");

        result = modelerService.longCommonSubsequence("abc", "aababc");
        System.out.println(result);
        assert result.equals("abc");

        result = modelerService.longCommonSubsequence("aababc", "abc");
        System.out.println(result);
        assert result.equals("abc");
    }

    @Test
    public void longCommonSubsequence1() {
        String result = modelerService.lcsUseDynamicProgram("1", "2");
        System.out.println(result);
        assert result.equals("-1");

        result = modelerService.lcsUseDynamicProgram("abc", "abc");
        System.out.println(result);
        assert result.equals("abc");

        result = modelerService.lcsUseDynamicProgram("abc", "adeggfgfabsdsdeabc");
        System.out.println(result);
        assert result.equals("abc");
    }
}