package com.capol.workflow.server.algorithm;

import org.springframework.stereotype.Service;

@Service
public class GeneralService {
    public static String lcsUseDynamicProgram(String str1,String str2) {
        // 万法入口惯例校验入参
        if (str1 == null || "".equals(str1) || str2 == null || "".equals(str2)) {
            return "-1";
        }

        char[] charArr1 = str1.toCharArray();
        char[] charArr2 = str2.toCharArray();
        int size1 = charArr1.length;
        int size2 = charArr2.length;

        // 先将第一列和第二列置为0
        int[][] dp = new int[size1 + 1][size2 + 1];
        for (int i = 0; i < size1; i++) {
            dp[i][0] = 0;
            dp[0][i] = 0;
        }

        // maxLength保存数组中出现过的最大子串长度，maxIndexInChar1标识最大子串的末尾字符在str1中的index
        int maxLength = 0;
        int maxIndexInChar1 = 0;
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                //dp[i+1][j+1] 如果charArr1[i]与charArr2[j]不同则为0，相同则为dp[i][j]+1if(charArr1[i]=charArr2[j]){dp[i+1][j+1]=0;helse
                if (charArr1[i] != charArr2[j]) {
                    dp[i+1][j+1]=0;
                } else {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                    if (dp[i + 1][j + 1] > maxLength) {
                        maxLength = dp[i + 1][j + 1];
                        maxIndexInChar1 = i;
                    }
                }
            }
        }

        if (maxLength == 0) {
            return "-1";
        } else {
            //根据长度和在字符串str1中下标截取最大公共子串
            return str1.substring(maxIndexInChar1 + 1 - maxLength, maxIndexInChar1 + 1);
        }
    }

    public String longCommonSubsequence(String source, String target) {
        // 万法入口惯例校验入参
        if (source == null || "".equals(source) || target == null || "".equals(target)) {
            return "-1";
        }

        char[] sourceCharArray = source.toCharArray();
        char[] targetCharArray = target.toCharArray();
        int sourceSize = sourceCharArray.length;
        int targetSize = targetCharArray.length;

        int maxLength = 0;
        int maxIndexInChar = 0;

        for (int i = 0; i < sourceSize; i++) {
            int maxLengthLoop = 0;
            for (int j = 0; j < targetSize; j++) {
                maxLengthLoop = maxLength(sourceCharArray, targetCharArray, i, j);
                if (maxLengthLoop > maxLength) {
                    maxLength = maxLengthLoop;
                    maxIndexInChar = j;
                }
            }
        }

        if (maxLength == 0) {
            return "-1";
        } else {
            //根据长度和在字符串str1中下标截取最大公共子串
            return target.substring(maxIndexInChar, maxIndexInChar + maxLength);
        }
    }

    public int maxLength(char[] sourceCharArray, char[] targetCharArray, int sourceIndex, int targetIndex) {
        int maxLength = 0;
        int sourceSize = sourceCharArray.length;
        int targetSize = targetCharArray.length;
        while (true) {
            if (sourceIndex < sourceSize && targetIndex < targetSize) {
                if (sourceCharArray[sourceIndex] == targetCharArray[targetIndex]) {
                    sourceIndex ++;
                    targetIndex ++;
                    maxLength += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return maxLength;
    }
}
