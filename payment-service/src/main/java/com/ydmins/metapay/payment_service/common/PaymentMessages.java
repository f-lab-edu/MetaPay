package com.ydmins.metapay.payment_service.common;

public class PaymentMessages {
    // COMMON
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    // PAYMENT PROCESSING
    public static final String PAYMENT_SUCCESS = "Payment processed successfully";
    public static final String PAYMENT_FAILURE_INSUFFICIENT_FUNDS = "Payment processing failed due to insufficient " +
            "funds.";
    public static final String PAYMENT_PROCESSING_ERROR = "Payment processing error: Please try again later";

    // PAYMENT RETRIEVING
    public static final String PAYMENT_DETAIL_RETRIEVED = "Payment details successfully retrieved";
    public static final String PAYMENT_NOT_FOUND = "Payment not found for the given ID : ";
    public static final String PAYMENT_RETRIEVE_ERROR = "Error retrieving payment details";

    // PAYMENT LIST RETRIEVING
    public static final String PAYMENT_LIST_RETRIEVED = "Payment list retrieved successfully";
    public static final String PAYMENT_LIST_EMPTY = "No payments found for the specified criteria";
    public static final String PAYMENT_LIST_RETRIEVE_ERROR = "Error retrieving payment list";

    // PAYMENT CANCELLATION
    public static final String PAYMENT_CANCEL_REQUEST_SUCCESS = "Payment cancellation request submitted successfully";
    public static final String PAYMENT_CANCEL_REQUEST_FAILURE = "Payment cancellation failed: Payment already processed";
    public static final String PAYMENT_CANCEL_REQUEST_ERROR = "Error processing payment cancellation request";

    // PAYMENT REFUND PROCESSING
    public static final String PREFUND_SUCCESS = "Refund processed successfully";
    public static final String REFUND_FAILURE_PAYMENT_NOT_FOUND = "Refund processing failed: Original payment not found";
    public static final String REFUND_PROCESSING_ERROR = "Refund processing error: Please try again later";

    // PAYMENT METHOD REGISTRATION
    public static final String PAYMENT_METHOD_REGISTRATION_SUCCESS = "Payment method registered successfully";
    public static final String PAYMENT_METHOD_REGISTRATION_FAILURE = "Failed to register payment method";
    public static final String PAYMENT_METHOD_ALREADY_EXISTS = "Payment method already exists";

    // PAYMENT METHOD RETRIEVAL
    public static final String PAYMENT_METHOD_RETRIEVED = "Payment method retrieved successfully";
    public static final String PAYMENT_METHOD_NOT_FOUND = "Payment method not found";
    public static final String PAYMENT_METHOD_LIST_RETRIEVED = "Payment method list retrieved successfully";
    public static final String PAYMENT_METHOD_LIST_EMPTY = "No payment methods found";

    // PAYMENT METHOD DELETION
    public static final String PAYMENT_METHOD_DELETION_SUCCESS = "Payment method deleted successfully";
    public static final String PAYMENT_METHOD_DELETION_FAILURE = "Failed to delete payment method";

    public PaymentMessages(){
        // prevent instantiation
    }
}
