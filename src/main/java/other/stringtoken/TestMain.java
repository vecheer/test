package other.stringtoken;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TestMain {
    public static void main(String[] args) {
        String[] strings = tokenizeToStringArray("a;b;c,d", ",; \t\n", true, false);
        System.out.println(Arrays.toString(strings));

    }

    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return new String[0];
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return (tokens != null ? tokens.toArray(new String[0]) : new String[0]);
    }
}
