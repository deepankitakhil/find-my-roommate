package util;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhil on 1/9/2017.
 */

public class ObjectUtil {
    private static final String GET = "get";
    private static final String DEFAULT_SELECTION_OPTION = "Doesn\'t Matter";

    public static boolean areEqualObjects(Object base, Object compareTo, List<String> ignoreFields) {

        for (Method method : getGetters(base.getClass(), ignoreFields)) {
            try {
                Method compareMethod = compareTo.getClass().getMethod(method.getName());

                Object baseResult = method.invoke(base);
                Object compareResult = compareMethod.invoke(compareTo);

                if (!baseResult.equals(DEFAULT_SELECTION_OPTION) &&
                        !new EqualsBuilder().append(baseResult, compareResult).isEquals())
                    return false;

            } catch (NullPointerException e) {
                throw e;
            } catch (Exception e) {
                // do nothing.
            }
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    private static List<Method> getGetters(Class clazz, List<String> ignoreFields) {

        if (ignoreFields == null)
            ignoreFields = new ArrayList<>();

        ignoreFields.add("class");

        List<Method> getters = new ArrayList<>();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (isGetter(method) && !listContainsString(ignoreFields, method.getName()))
                getters.add(method);
        }
        return getters;
    }

    private static boolean listContainsString(List<String> list, String string) {
        boolean contains = false;
        for (String item : list) {
            if (string.toLowerCase().contains(item.toLowerCase())) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    private static boolean isGetter(Method method) {
        return method.getName().startsWith(GET) && method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType());
    }
}
