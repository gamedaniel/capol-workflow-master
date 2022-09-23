package com.capol.workflow.server.common.utils;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import org.apache.el.ExpressionFactoryImpl;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Map;

/**
 * @author shidan1
 */
public class ExpressionUtil {
    public static ExpressionFactory expressionFactory = new ExpressionFactoryImpl();

    public static Boolean runBooleanExpression(String expression, Map<String, Object> variable) {
        SimpleContext simpleContext = new SimpleContext(new SimpleResolver());

        for (Map.Entry<String, Object> entry : variable.entrySet()) {
            simpleContext.setVariable(entry.getKey(), expressionFactory.createValueExpression(entry.getValue(), Object.class));
        }

        ValueExpression valueExpression = expressionFactory.createValueExpression(simpleContext, expression, Boolean.class);
        Object result = valueExpression.getValue(simpleContext);

        return (Boolean) result;
    }
}
