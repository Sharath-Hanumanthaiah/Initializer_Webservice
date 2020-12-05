package com.initializers.services.apiservices.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such item")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // Nothing to do
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item doesn't exist")
    @ExceptionHandler(ItemNotFoundException.class)
    public void itemNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Category doesn't exist")
    @ExceptionHandler(CategoryNotFoundException.class)
    public void categoryNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Sub Category doesn't exist")
    @ExceptionHandler(SubCategoryNotFoundException.class)
    public void subCategoryNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User doesn't exist")
    @ExceptionHandler(UserNotFoundException.class)
    public void userNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Mandatory value is missing")
    @ExceptionHandler(RequiredValueMissingException.class)
    public void requiredValueMissing() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Availabilty doesn't exist or unavailable")
    @ExceptionHandler(ItemAvailabilityException.class)
    public void itemAvailabilityNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Availabilty exist with same value and unit")
    @ExceptionHandler(ItemAvailabilityExistException.class)
    public void itemAvailabilityExist() {
    	
    }
    
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Address not found")
    @ExceptionHandler(AddressNotFoundException.class)
    public void addressNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error in Order Configuration database")
    @ExceptionHandler(OrderConfigurationException.class)
    public void orderConfiguration() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "cart is empty")
    @ExceptionHandler(CartItemNotFoundException.class)
    public void CartItemNotFound() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email already exist")
    @ExceptionHandler(EmailExistException.class)
    public void emailExist() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Faulty EmailID")
    @ExceptionHandler(EmailIDException.class)
    public void emailIdFault() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.GONE, reason = "OTP expired")
    @ExceptionHandler(OTPExpiredException.class)
    public void otpExpired() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_GATEWAY, reason = "internal issue")
    @ExceptionHandler(InternalIssueException.class)
    public void internalIssue() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "otp is not valid")
    @ExceptionHandler(OTPNotValidException.class)
    public void otpNotValid() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to upload Image")
    @ExceptionHandler(ImageUploadException.class)
    public void imageUploadException() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Please confirm order before marking delivered")
    @ExceptionHandler(UserOrderSetNotConfirmed.class)
    public void userOrderSetNotConfirmed() {
    	
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Delivery Date should greater that or equal to todays date")
    @ExceptionHandler(UserOrderDeliveryDateException.class)
    public void userOrderDeliveryDateException() {
    	
    }
    	
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Item is already delivered, you cannot change delivery date")
    @ExceptionHandler(UserOrderItemDeliveredException.class)
    public void userOrderItemDeliveredException() {
    	
    }
}
