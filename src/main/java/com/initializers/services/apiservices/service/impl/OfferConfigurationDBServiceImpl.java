package com.initializers.services.apiservices.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.initializers.services.apiservices.exception.OrderConfigurationException;
import com.initializers.services.apiservices.model.OfferConfigurationDB;
import com.initializers.services.apiservices.others.ApplicationProperties;
import com.initializers.services.apiservices.others.EvaluateString;
import com.initializers.services.apiservices.repo.OfferConfigurationDBRepo;
import com.initializers.services.apiservices.service.OfferConfigurationDBService;

@Service
public class OfferConfigurationDBServiceImpl implements OfferConfigurationDBService {

	@Autowired
	private OfferConfigurationDBRepo offerConfigurationDBRepo;
	@Autowired
	private ApplicationContext applicationContext;
	
	Logger logger = LoggerFactory.getLogger(OfferConfigurationDBServiceImpl.class);
	String currentObject = "UserOrderSet";

	@Override
	public Object configureOrderBeforeSend(Object object, String type) {
		List<OfferConfigurationDB> offerConfigurationDBList = offerConfigurationDBRepo.findByType(type);
		Map<Object, Object> resolvedFieldValue = new HashMap<>();
		for (OfferConfigurationDB offerConfigurationDB : offerConfigurationDBList) {
			if (offerConfigurationDB.getFieldIn() != null) {
				resolvedFieldValue = resolveFieldValue(object, offerConfigurationDB.getFieldIn());
			}
			if (resolvedFieldValue == null)
				continue;
			offerConfigurationDB
					.setFormula(replaceFieldsWithValueInFormula(offerConfigurationDB.getFormula(), resolvedFieldValue));
			Number formulaValue = evaluateFormula(offerConfigurationDB.getFormula(), offerConfigurationDB.getRound());
			if (formulaValue != null) {
				updateFieldOutValue(object, offerConfigurationDB.getFieldOut(), formulaValue,
						offerConfigurationDB.getSymbol());
				return formulaValue;
			}

		}
		return null;
	}

	public Map<Object, Object> resolveFieldValue(Object object, List<String> fieldInList) {
		Map<Object, Object> fieldInMap = new HashMap<Object, Object>();
		int counter = 0;

		for (String fieldInInst : fieldInList) {
			String[] fieldIn = fieldInInst.split("[.]", 2);
			if (fieldIn.length < 2) {
				logger.error(Arrays.toString(fieldIn) + "is invalid in orderconfigurationDB");
				throw new OrderConfigurationException();
			}
			if (fieldIn[0].equals(currentObject)) {
				try {
					Object obj = resolveValue(object, fieldIn[1]);
					if (obj == null)
						return null;
					fieldInMap.put("${" + counter + "}", obj);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException  e1) {
					logger.error(Arrays.toString(fieldIn) + "is invalid in orderconfigurationDB");
					throw new OrderConfigurationException();
				}
//				counter++;
			}else {
				List<String> tempFieldInList = new ArrayList<>();
				tempFieldInList.add(ApplicationProperties.orderFieldMap.get(fieldIn[0]));
				Map<Object,Object> tempResult = resolveFieldValue(object,tempFieldInList);
				Object tempResultValue = tempResult.get("${0}");
				String repoString = "com.initializers.services.apiservices.repo."+fieldIn[0]+"Repo";
				try {
					Class repoClass = Class.forName(repoString);
					System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
					
					Object tempObject = applicationContext.getBean(fieldIn[0].substring(0,1).toLowerCase()
							+ fieldIn[0].substring(1)+"Repo");
					String methodName = "findFirstById";
					Method methodDeclaration = repoClass.getDeclaredMethod(methodName, Long.class);
					Object mtdvalue = null; 
					Object obj = null;
					if(tempResultValue.getClass().toString().equals("class java.util.ArrayList")) {
						Set<Object> objl = new HashSet<Object>();
						List<Object> objList = new ArrayList<>();
						for(Object tempFieldIn : (ArrayList) tempResultValue) {
							try {
								mtdvalue = methodDeclaration.invoke(tempObject, 
										Long.parseLong(tempFieldIn.toString()));
								objList.add(resolveValue(mtdvalue, fieldIn[1]));
								objl.add(resolveValue(mtdvalue, fieldIn[1]));
							} catch (Exception e) {
								logger.error(Arrays.toString(fieldIn) + "is invalid in orderconfigurationDB");
								throw new OrderConfigurationException();
							}							
						}
						obj = objList.stream().distinct().collect(Collectors.toList());
					}else {
						mtdvalue = methodDeclaration.invoke(tempObject,
								Long.parseLong(tempResultValue.toString()));
						obj = resolveValue(mtdvalue, fieldIn[0]);
					}
					fieldInMap.put("${" + counter + "}", obj);
				} catch (ClassNotFoundException |NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			counter++;
		}

		return fieldInMap;
	}

	@SuppressWarnings("unchecked")
	public Object resolveValue(Object object, String field) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String[] fieldIn = field.split("[.]", 2);
		System.out.println(object.getClass().toGenericString());
		if (object.getClass().toString().equals("class java.util.ArrayList")) {
			ArrayList<Object> objectArray = (ArrayList<Object>) object;
			List<Object> resultArray = new ArrayList<>();
//			Object[] objectArray = (Object[]) object;
			if (objectArray.size() > 0) {
				for (Object objectLocal : objectArray) {
					Object resolvedValue = resolveValue(objectLocal, fieldIn[0]);
					if (resolvedValue.getClass().toString().equals("class java.util.ArrayList")) {
						resultArray = (ArrayList<Object>) object;
					} else {
						resultArray.add(resolvedValue);
					}
				}
				return resultArray;
			}

		}

		Class<? extends Object> cls = object.getClass();
		String methodName = "get" + StringUtils.capitalize(fieldIn[0]);
		Method mtd = cls.getDeclaredMethod(methodName);
		Object mtdvalue = mtd.invoke(object);

		if (fieldIn.length > 1) {
			return resolveValue(mtdvalue, fieldIn[1]);
		} else {
			return mtdvalue;
		}
	}

	public String replaceFieldsWithValueInFormula(String formula, Map<Object, Object> fieldValue) {
		formula = formula.trim().replaceAll("\\s+", "");
		for (Map.Entry<Object, Object> entry : fieldValue.entrySet()) {
			if (entry.getValue() != null) {
				if (entry.getValue().getClass().toString().equals("class java.util.ArrayList")) {
					String[] formulaArray = formula.split("=");
					if(formulaArray.length > 1) {
						String condition = formulaArray[0];
						String tempFormula = null;
						for (Object object : (ArrayList) entry.getValue()) {
							if(tempFormula == null) {
								tempFormula = condition.replace(entry.getKey().toString(), object.toString());
							}else {
								tempFormula = tempFormula + "||" +
										condition.replace(entry.getKey().toString(), object.toString());
							}
						}
						formula = tempFormula +"="+ formulaArray[1];
					}
				} else {
					formula = formula.replace(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		return formula;
	}

	public Number evaluateFormula(String formula, Boolean round) {
		Number value = null;
		String[] formulaComponent = formula.split("=");
		if (formulaComponent.length < 2) {
			logger.error(formula + "is invalid in orderconfigurationDB");
			throw new OrderConfigurationException();
		}
		String condition = formulaComponent[0];
		String expression = formulaComponent[1];
		if (condition.equals("")) {
			value = EvaluateString.evaluateExpression(expression);
		} else {
			if (EvaluateString.evaluateCondition(condition)) {
				value = EvaluateString.evaluateExpression(expression);
			}
		}
		if (round && value != null) {
			value = Math.round(value.floatValue());
		}

		return value;
	}

	public void updateFieldOutValue(Object object, String fieldOut, Number formulaValue, String symbol) {
		Object globalObject = object;
		String globalFieldOut = fieldOut;
		String methodName = "";
		String[] fieldOutAry = globalFieldOut.split("[.]", 2);
		Class<? extends Object> cls = null;
		String returnType = null;
		Method mtd = null;
		Object resultObject = null;

		// check for min 2 entry

		if (fieldOutAry[0].equals(currentObject)) {
			String[] fieldVariable = fieldOutAry[1].split("[.]", 2);
			while (fieldVariable.length > 1) {
				cls = globalObject.getClass();
				methodName = "get" + StringUtils.capitalize(fieldVariable[0]);
				try {
					mtd = cls.getDeclaredMethod(methodName);
					globalObject = mtd.invoke(object);
					globalFieldOut = fieldVariable[1];
					fieldVariable = fieldVariable[1].split("[.]", 2);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					logger.error(fieldOut + "is invalid in orderconfigurationDB");
					throw new OrderConfigurationException();
				}

			}
			methodName = "get" + StringUtils.capitalize(fieldVariable[0]);
			cls = globalObject.getClass();
			try {
				mtd = cls.getDeclaredMethod(methodName);
				returnType = mtd.getReturnType().getName();
				resultObject = mtd.invoke(globalObject);
				// set value

				methodName = "set" + StringUtils.capitalize(fieldVariable[0]);
				mtd = cls.getDeclaredMethod(methodName, mtd.getReturnType());
				switch (returnType) {
				case "java.lang.Float":
					Float resultF = Float.parseFloat(resultObject.toString());
					Float evaluatedValF = EvaluateString.applyOp(symbol.charAt(0), resultF, formulaValue.floatValue());
					mtd.invoke(globalObject, evaluatedValF);
					break;
				case "java.util.Date":
					Date resultD = (Date) resultObject;
					if (resultD == null)
						resultD = new Date();
					Date evaluatedValD = EvaluateString.applyOp(symbol.charAt(0), resultD, formulaValue.floatValue());
					mtd.invoke(globalObject, evaluatedValD);
					break;
				case "boolean":
					Boolean evaluatedValB = false;
					if (formulaValue.intValue() == 1)
						evaluatedValB = true;
					mtd.invoke(globalObject, evaluatedValB);
				default:
					break;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				logger.error(fieldOut + "is invalid in orderconfigurationDB");
				throw new OrderConfigurationException();
			}

		}

//		try {
//			
//			
//			String[] fieldOutAry = globalFieldOut.split("[.]", 2);
//			while(fieldOutAry.length > 1) {
//				Class<? extends Object> cls = object.getClass();
//				String methodName = "get" + StringUtils.capitalize(fieldOutAry[0]);
//				Method mtd = cls.getDeclaredMethod(methodName);
//				globalObject = mtd.invoke(object);
//				globalFieldOut = fieldOutAry[1];
//				fieldOutAry = globalFieldOut.split("[.]", 2);
//			}
//			String methodName = "get" + StringUtils.capitalize(fieldOutAry[0]);
//
//			
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
//				| SecurityException e) {
//			logger.error(fieldOut + "is invalid in orderconfigurationDB");
//			throw new OrderConfigurationException();
//		}

	}
}
