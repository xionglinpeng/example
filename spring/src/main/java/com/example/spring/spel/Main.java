package com.example.spring.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {

        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("#root.args[3]+'-hello'");


        ExpressionRootObject expressionRootObject = new ExpressionRootObject();
        expressionRootObject.setArgs(new Object[]{"arg1","arg2","arg3","arg4"});

        EvaluationContext evaluationContext = new StandardEvaluationContext(expressionRootObject);

        String value = expression.getValue(evaluationContext,String.class);

        System.out.println(value);

    }

    static class ExpressionRootObject {

        private Method method;

        private Object[] args;

        private Object target;

        private Class<?> targetClass;


        public ExpressionRootObject() {

        }

        public ExpressionRootObject( Method method, Object[] args, Object target, Class<?> targetClass) {

            this.method = method;
            this.target = target;
            this.targetClass = targetClass;
            this.args = args;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }
    }
}
