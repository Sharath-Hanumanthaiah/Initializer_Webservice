package com.initializers.services.apiservices.service.impl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.UserOrderDeliveryDateException;
import com.initializers.services.apiservices.exception.UserOrderItemDeliveredException;
import com.initializers.services.apiservices.exception.UserOrderSetNotConfirmed;
import com.initializers.services.apiservices.model.Address;
import com.initializers.services.apiservices.model.OrderStatus;
import com.initializers.services.apiservices.model.UserOrder;
import com.initializers.services.apiservices.model.UserOrderSet;
import com.initializers.services.apiservices.model.UserOrderSetList;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.others.FilterAction;
import com.initializers.services.apiservices.others.FilterValue;
import com.initializers.services.apiservices.repo.UserOrderSetRepo;
import com.initializers.services.apiservices.service.AddressService;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.UserOrderService;

@Service
public class UserOrderServiceImpl implements UserOrderService {

	@Autowired
	private UserOrderSetRepo userOrderSetRepo;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private MongoTemplate mongoTemplate;

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Object getUserOrderList(Long userId, Pageable pageable) {
		List<UserOrderSet> userOrderSetList = userOrderSetRepo.findByOrderListUserId(userId, pageable);
		List<Object> returnVal = new ArrayList<Object>();

		for (UserOrderSet userOrderSet : userOrderSetList) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			String itemName = null;
			returnMap.put("id", userOrderSet.getId());
			for (UserOrder userOrder : userOrderSet.getOrderList()) {
				String getName = StringUtils.capitalize(itemDetailsService.getItemNameById(userOrder.getItemId()));
				if (itemName == null) {
					itemName = getName;
				} else {
					itemName = itemName + ", " + getName;
				}
			}
			returnMap.put("address", addressService.getAddressById(userOrderSet.getAddressId()));
			returnMap.put("ItemDetails", itemName);
			returnMap.put("status", userOrderSet.getStatus());
			returnMap.put("orderAt", userOrderSet.getOrderAt());
			returnMap.put("deliveredBy", userOrderSet.getDeliveredBy());
			returnMap.put("deliveryCharge", userOrderSet.getDeliveryCharge());
			returnMap.put("totalAmount", userOrderSet.getTotalAmount());
			returnVal.add(returnMap);
		}
		return returnVal;
	}

	@Override
	public Object getUserOrder(Long id) {
		Map<String, Object> returnVal = new HashMap<>();
		List<Object> userOrderList = new ArrayList<>();
		UserOrderSet userOrderSet = userOrderSetRepo.findFirstById(id);
		if (userOrderSet != null) {
			returnVal.put("id", userOrderSet.getId());
			for (UserOrder userOrder : userOrderSet.getOrderList()) {
				Map<String, Object> userOrderMap = new HashMap<>();
				userOrderMap.put("amount", userOrder.getAmount());
				userOrderMap.put("quantity", userOrder.getQuantity());
				// add new value to availablity ID to get data irrespective of availability
				Long[] availablityId = { userOrder.getAvailabilityId(), 1L };

				ItemListTemp itemListTemp = itemDetailsService.getItemListById(userOrder.getItemId(), availablityId);
				if (itemListTemp != null) {
					userOrderMap.put("itemDetails", itemListTemp);
				}
				userOrderList.add(userOrderMap);
			}
			returnVal.put("item", userOrderList);
			returnVal.put("totalAmount", userOrderSet.getTotalAmount());
			returnVal.put("status", userOrderSet.getStatus());
			returnVal.put("orderAt", userOrderSet.getOrderAt());
			Float deliveryCharge = userOrderSet.getDeliveryCharge();
			if (deliveryCharge != null) {
				returnVal.put("deliveryCharge", "+ " + deliveryCharge);
			}
			returnVal.put("deliveredBy", userOrderSet.getDeliveredBy());
			returnVal.put("coupenCode", userOrderSet.getCoupenCode());
			Float coupenDiscount = userOrderSet.getCoupenDiscount();
			if (coupenDiscount != null) {
				returnVal.put("coupenDiscount", "- " + coupenDiscount);
			}
		}
		return returnVal;
	}

	@Override
	public Object getAllUserOrderAdmin(String[] filter) {
		List<Object> userOrderList = new ArrayList<Object>();

		List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		list.add(LookupOperation.newLookup().from("user_address").localField("addressId").foreignField("_id")
				.as("join_address"));

		list.add(Aggregation.project(Fields.fields("_id", "orderAt", "deliveredBy", "totalAmount", "status")
				.and(Fields.field("name", "join_address.name")).and(Fields.field("firstLine", "join_address.firstLine"))
				.and(Fields.field("secondLine", "join_address.secondLine"))
				.and(Fields.field("pinCode", "join_address.pinCode"))
				.and(Fields.field("phoneNumber", "join_address.phoneNumber"))));
		if (filter != null) {
			List<FilterValue> filterValues = new FilterAction().getFilterValue(filter);

			for (FilterValue filterValue : filterValues) {
				String addnlFilterName = null;
				List<Object> addnlFilterValue = new ArrayList<>();
				List<Object> alteredFilterValue = new ArrayList<>();
				Object alteredFilterVariableValue = new Object();
				if (filterValue.getName() != null && filterValue.getOperator() != null
						&& filterValue.getValue() != null) {
					switch (filterValue.getName()) {
					case "_id":
						alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong)
								.collect(Collectors.toList());
						break;
					case "orderAt":
						if (filterValue.getValue().size() > 0) {
							String dateValue = filterValue.getValue().get(0);
							try {
								alteredFilterVariableValue = simpleDateFormat.parse(dateValue);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					case "deliveredBy":
						if (filterValue.getValue().size() > 0) {
							String dateValue = filterValue.getValue().get(0);
							try {
								alteredFilterVariableValue = simpleDateFormat.parse(dateValue);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					case "overallStatus":
						// type !confirmed => pending, confirmed => confirmed, delivered => completed
						if (filterValue.getValue().size() > 0) {
							String value = filterValue.getValue().get(0);
							if (value != null) {
								if (value.equals("pending")) {
									filterValue.setName("status.confirmed");
									alteredFilterValue.add(false);
								} else if (value.equals("confirmed")) {
									filterValue.setName("status.confirmed");
									alteredFilterValue.add(true);
									addnlFilterName = "status.delivered";
									addnlFilterValue.add(false);
								} else if (value.equals("completed")) {
									filterValue.setName("status.delivered");
									alteredFilterValue.add(true);
								}
							}
						}
						break;
					default:
						alteredFilterValue = filterValue.getValue().stream().collect(Collectors.toList());
						break;
					}
					if (filterValue.getOperator().equals("ge")) {
						list.add(Aggregation
								.match(Criteria.where(filterValue.getName()).gte(alteredFilterVariableValue)));
					} else if (filterValue.getOperator().equals("le")) {
						list.add(Aggregation
								.match(Criteria.where(filterValue.getName()).lte(alteredFilterVariableValue)));
					} else {
						list.add(Aggregation.match(Criteria.where(filterValue.getName()).in(alteredFilterValue)));
					}
					if (addnlFilterName != null && addnlFilterValue.size() > 0) {
						list.add(Aggregation.match(Criteria.where(addnlFilterName).in(addnlFilterValue)));
					}
				}
			}
		}
		Aggregation aggregation = Aggregation.newAggregation(list);
		List<UserOrderSetList> results = mongoTemplate.aggregate(aggregation, "user-order", UserOrderSetList.class)
				.getMappedResults();
		for (UserOrderSetList userOrderSet : results) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("id", userOrderSet.getId());

			// calculate address string
			StringBuilder addressString = new StringBuilder();
			addressString.append(userOrderSet.getName());
			addressString.append(", ");
			addressString.append(userOrderSet.getFirstLine());
			addressString.append(" ");
			addressString.append(userOrderSet.getSecondLine());
			addressString.append(", ");
			addressString.append(userOrderSet.getPinCode());
			returnMap.put("address", addressString);

			returnMap.put("phoneNumber", userOrderSet.getPhoneNumber());
			String formatedOrderAt = convertMongoDate(userOrderSet.getOrderAt());
			returnMap.put("orderAt", formatedOrderAt == null ? userOrderSet.getOrderAt() : formatedOrderAt);

			String formateddeliveredBy = convertMongoDate(userOrderSet.getDeliveredBy());
			returnMap.put("deliveredBy",
					formateddeliveredBy == null ? userOrderSet.getDeliveredBy() : formateddeliveredBy);

			NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
			String currency = format.format(userOrderSet.getTotalAmount());
			returnMap.put("totalAmount", currency);
			if (!userOrderSet.getStatus().confirmed) {
				returnMap.put("overallStatus", "Pending");
				returnMap.put("state", "Error");
			} else if (userOrderSet.getStatus().confirmed && !userOrderSet.getStatus().delivered) {
				returnMap.put("overallStatus", "Confirmed");
				returnMap.put("state", "Warning");
			} else if (userOrderSet.getStatus().delivered) {
				returnMap.put("overallStatus", "Completed");
				returnMap.put("state", "Success");
			}
			userOrderList.add(returnMap);
		}
		return userOrderList;
	}

	private String convertMongoDate(Date date) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
		try {
			if (date != null) {
				String finalStr = outputFormat.format(date);
				return finalStr;
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getUserOrderAdmin(Long id) {
		UserOrderSet userOrderSet = userOrderSetRepo.findFirstById(id);
		
		return adminOrderReturnValue(userOrderSet);
	}

	@Override
	public Map<String, Object> updateUserOrderSet(UserOrderSet userOrderSet) {
		UserOrderSet dbUserOrderSet = userOrderSetRepo.findFirstById(userOrderSet.getId());
		// throw exception if delivered is set without confirmed
		if (userOrderSet.getDeliveredBy() != null) {
			if(dbUserOrderSet.getStatus().delivered ) {
				throw new UserOrderItemDeliveredException();
			}
			LocalDate currentDate = LocalDate.now();
			LocalDate deliveryDate = userOrderSet.getDeliveredBy().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (deliveryDate.isBefore(currentDate)) {
				throw new UserOrderDeliveryDateException();
			}
			dbUserOrderSet.setDeliveredBy(userOrderSet.getDeliveredBy());

		}
		OrderStatus orderStatus = dbUserOrderSet.getStatus();
		if (dbUserOrderSet.getStatus().delivered && !dbUserOrderSet.getStatus().confirmed) {
			throw new UserOrderSetNotConfirmed();
		}
		if (!dbUserOrderSet.getStatus().confirmed && userOrderSet.getStatus().confirmed) {
			orderStatus.setConfirmed(true);
		}
		if (!dbUserOrderSet.getStatus().delivered && userOrderSet.getStatus().delivered) {
			orderStatus.setDelivered(true);
		}
		dbUserOrderSet.setStatus(orderStatus);

		userOrderSetRepo.save(dbUserOrderSet);

		return adminOrderReturnValue(dbUserOrderSet);
	}
	
	Map<String, Object> adminOrderReturnValue(UserOrderSet userOrderSet) {
		Map<String, Object> returnVal = new HashMap<>();
		List<Object> userOrderList = new ArrayList<>();
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		
		returnVal.put("id", userOrderSet.getId());
		returnVal.put("orderAt", convertMongoDate(userOrderSet.getOrderAt()));
		returnVal.put("deliveredBy", convertMongoDate(userOrderSet.getDeliveredBy()));
		returnVal.put("coupenCode", userOrderSet.getCoupenCode());
		returnVal.put("coupenDiscount",
				userOrderSet.getCoupenDiscount() != null ? format.format(userOrderSet.getCoupenDiscount()) : "");
		returnVal.put("deliveryCharge",
				userOrderSet.getDeliveryCharge() != null ? format.format(userOrderSet.getDeliveryCharge()) : "");
		returnVal.put("totalAmount",
				userOrderSet.getTotalAmount() != null ? format.format(userOrderSet.getTotalAmount()) : "");
		// Status Info
		Map<String, Object> userStatusMap = new HashMap<>();
		userStatusMap.put("confirmed", new Boolean(userOrderSet.getStatus().getConfirmed()).toString());
		userStatusMap.put("delivered", new Boolean(userOrderSet.getStatus().getDelivered()).toString());
		returnVal.put("status", userStatusMap);

		// Customer Info
		Address address = addressService.getAddressById(userOrderSet.getAddressId());
		if (address != null) {
			returnVal.put("customerName", address.getName());
			StringBuilder firstandsecondline = new StringBuilder(address.getFirstLine());
			firstandsecondline.append(" ");
			firstandsecondline.append(address.getSecondLine());
			returnVal.put("address", firstandsecondline);
			returnVal.put("landmark", address.getLandMark());
			returnVal.put("pincode", address.getPinCode());
			returnVal.put("phoneNumber", address.getPhoneNumber());
		}

		// Item Details
		if (userOrderSet != null) {
			for (UserOrder userOrder : userOrderSet.getOrderList()) {
				Map<String, Object> userOrderMap = new HashMap<>();
				userOrderMap.put("id", userOrder.getItemId());
				userOrderMap.put("name", itemDetailsService.getItemNameById(userOrder.getItemId()));
				userOrderMap.put("availability",
						itemAvailabilityService.getValueUnitByAvailabilityId(userOrder.getAvailabilityId()));
				userOrderMap.put("amount", userOrder.getAmount() != null ? format.format(userOrder.getAmount()) : "");
				userOrderMap.put("quantity", userOrder.getQuantity());
				userOrderList.add(userOrderMap);
			}
			returnVal.put("items", userOrderList);
		}
		return returnVal;
	}

}
