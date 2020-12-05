package com.initializers.services.apiservices.others;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils; 

public class EvaluateString 
{ 
    public static float evaluateExpression(String expression) 
    { 
        char[] tokens = expression.toCharArray(); 
  
         // Stack for numbers: 'values' 
        Stack<Float> values = new Stack<Float>(); 
  
        // Stack for Operators: 'ops' 
        Stack<Character> ops = new Stack<Character>(); 
  
        for (int i = 0; i < tokens.length; i++) 
        { 
             // Current token is a whitespace, skip it 
            if (tokens[i] == ' ') 
                continue; 
  
            // Current token is a number, push it to stack for numbers 
            if (tokens[i] >= '0' && tokens[i] <= '9') 
            { 
                StringBuffer sbuf = new StringBuffer(); 
                // There may be more than one digits in number 
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.' )) 
                    sbuf.append(tokens[i++]); 
                values.push(Float.parseFloat(sbuf.toString())); 
                i--;
            } 
  
            // Current token is an opening brace, push it to 'ops' 
            else if (tokens[i] == '(') 
                ops.push(tokens[i]); 
  
            // Closing brace encountered, solve entire brace 
            else if (tokens[i] == ')') 
            { 
                while (ops.peek() != '(') 
                  values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
                ops.pop(); 
            } 
  
            // Current token is an operator. 
            else if (tokens[i] == '+' || tokens[i] == '-' || 
                     tokens[i] == '*' || tokens[i] == '/') 
            { 
                // While top of 'ops' has same or greater precedence to current 
                // token, which is an operator. Apply operator on top of 'ops' 
                // to top two elements in values stack 
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) 
                  values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
  
                // Push current token to 'ops'. 
                ops.push(tokens[i]); 
            } 
        } 
  
        // Entire expression has been parsed at this point, apply remaining 
        // ops to remaining values 
        while (!ops.empty()) 
            values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
  
        // Top of 'values' contains result, return it 
        return values.pop(); 
    } 
  
    // Returns true if 'op2' has higher or same precedence as 'op1', 
    // otherwise returns false. 
    public static boolean hasPrecedence(char op1, char op2) 
    { 
        if (op2 == '(' || op2 == ')') 
            return false; 
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) 
            return false; 
        else
            return true; 
    } 
  
    // A utility method to apply an operator 'op' on operands 'a'  
    // and 'b'. Return the result. 
    public static float applyOp(char op, float a, float b) 
    { 
        switch (op) 
        { 
        case '+': 
            return a + b; 
        case '-': 
            return a - b; 
        case '*': 
            return a * b; 
        case '/': 
            if (b == 0) 
                throw new
                UnsupportedOperationException("Cannot divide by zero"); 
            return a / b; 
        } 
        return 0; 
    }
    public static Date applyOp(char op, Date b, float a) {
    	if(op == '+') {
    		return addDays(b, (int)a);
    	}
    	return null;
    }
    public static boolean evaluateCondition(String condition) {
    	Pattern pattern = Pattern.compile("(&&)|(\\|\\|)");
		Matcher matcher = pattern.matcher(condition);
		int end = 0;
		Stack<String> stack = new Stack<String>();
		while(matcher.find()) {
			stack.push(condition.substring(end, matcher.start()));
			stack.push(matcher.group());
			end = matcher.end();
		}
		stack.push(condition.substring(end, condition.length()));
		return evaluateSubExpression(stack);
    }
	public static boolean evaluateSubExpression(Stack<String> formula) {
		if(formula.size() == 1) {
			return solveSubExpression(formula.pop());
		}else {
			boolean val1 = solveSubExpression(formula.pop());
			String operand = formula.pop();
			boolean val2 = formula.contains("(&&)|(\\|\\|)")?solveSubExpression(formula.pop()):evaluateSubExpression(formula);
			if(operand.equals("&&")) {
				return val1 && val2;
			}else if(operand.equals("||")){
				return val1 || val2;
			}else return false;
		}
	}
	
	public static boolean solveSubExpression(String formula) {
		Pattern pattern = Pattern.compile("(LT)|(GT)|(EQ)|(LE)|(GE)");
		Matcher matcher = pattern.matcher(formula);
		while(matcher.find()) {
			String operator1 = formula.substring(0, matcher.start());
			String operand = matcher.group();
			String operator2 = formula.substring(matcher.end(), formula.length());
			
			switch (operand) {
			case "LT":
				if(NumberUtils.isParsable(operator1) && NumberUtils.isParsable(operator2)) {
					return Float.parseFloat(operator1) < Float.parseFloat(operator2);
				}
				break;
			case "GT":
				if(NumberUtils.isParsable(operator1) && NumberUtils.isParsable(operator2)) {
					return Float.parseFloat(operator1) > Float.parseFloat(operator2);
				}
				break;
			case "GE":
				if(NumberUtils.isParsable(operator1) && NumberUtils.isParsable(operator2)) {
					return Float.parseFloat(operator1) >= Float.parseFloat(operator2);
				}
				break;
			case "LE":
				if(NumberUtils.isParsable(operator1) && NumberUtils.isParsable(operator2)) {
					return Float.parseFloat(operator1) <= Float.parseFloat(operator2);
				}
				break;
			case "EQ":
				if(NumberUtils.isParsable(operator1) && NumberUtils.isParsable(operator2)) {
					return Float.parseFloat(operator1) == Float.parseFloat(operator2);
				}else {
					return operator1.equals(operator2);
				}
			default:
				break;
			}
		}
		return false;
	}
	public static Date addDays(Date date, int days)
	{
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, days); //minus number would decrement the days
	    return cal.getTime();
	}
	
	
}