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

    public PaymentMessages(){
        // prevent instantiation
    }
}
