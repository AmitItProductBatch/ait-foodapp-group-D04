
package com.ait.app.ServiceImpl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.PaymentService;
import com.ait.app.controller.OrderController;
import com.ait.app.customExceptionHandler.PaymentException;
import com.ait.app.model.Order;
import com.ait.app.repository.OrderRepository;
import com.ait.app.requestBody.PaymentDto;
import com.ait.app.requestBody.PaymentVerifyDto;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderController orderController;

    @Autowired
    OrderRepository orderrepo;

    @Autowired
    RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    PaymentServiceImpl(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public ResponseEntity<String> createPayment(PaymentDto dto) {

        logger.info("Starting createPayment method for order ID: {}",
                dto.getOrderId());

        Order order = orderrepo.getById(dto.getOrderId());

        if (order == null) {
            logger.warn("Order not found with ID: {}", dto.getOrderId());

            throw new PaymentException(
                    "Order not found",
                    HttpStatus.NOT_FOUND);
        }

        if (order.getStatus().equals("Confirmed")) {

            logger.warn("Order {} is already confirmed", order.getId());

            throw new PaymentException(
                    "Order is already Confirmed ",
                    HttpStatus.BAD_REQUEST);
        }

        if (!"Placed".equalsIgnoreCase(order.getStatus())) {

            logger.warn("Order {} is not placed. Current status: {}",
                    order.getId(), order.getStatus());

            throw new PaymentException(
                    "Order is not placed",
                    HttpStatus.BAD_REQUEST);
        }

        // Take amount from Order
        dto.setAmount(order.getTotalAmount());

        if (dto.getAmount() <= 0) {

            logger.warn("Invalid payment amount for order ID: {}",
                    order.getId());

            throw new PaymentException(
                    "Invalid Payment amount",
                    HttpStatus.BAD_REQUEST);
        }

        int amount =
                (int) Math.round(dto.getAmount() * 100);

        if (amount < 100) {

            logger.warn("Payment amount is below the minimum for order ID: {}",
                    order.getId());

            throw new PaymentException(
                    "Amount must be at least 100",
                    HttpStatus.BAD_REQUEST);
        }

        try {

            logger.info("Creating Razorpay order for order ID: {}",
                    order.getId());

            JSONObject option = new JSONObject();

            option.put("amount", amount);
            option.put("currency", "INR");
            option.put(
                    "receipt",
                    "order" + order.getId());

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(option);

            String razorpayOrderId =
                    razorpayOrder.get("id").toString();

            logger.info("Razorpay order created successfully for order ID: {}",
                    order.getId());

            JSONObject response = new JSONObject();

            response.put("keyId", keyId);
            response.put("order_id", razorpayOrderId);
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("orderId", order.getId());

            return ResponseEntity.ok(
                    response.toString());

        } catch (Exception e) {

            logger.error("Failed to create Razorpay payment for order ID: {}",
                    order.getId(), e);

            throw new PaymentException(
                    "Unable to create RazorPay payment",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity verifyPayment(PaymentVerifyDto dto) {

        logger.info("Starting verifyPayment method for order ID: {}",
                dto.getOrderId());

        Order order = orderrepo.getById(dto.getOrderId());

        if (order == null) {

            logger.warn("Order not found with ID: {}", dto.getOrderId());

            throw new PaymentException(
                    "Order not found",
                    HttpStatus.NOT_FOUND);
        }

        if (!"Placed".equalsIgnoreCase(order.getStatus())) {

            logger.warn("Order {} is not available for payment. Current status: {}",
                    order.getId(), order.getStatus());

            throw new PaymentException(
                    "Order is not available for Payment",
                    HttpStatus.BAD_REQUEST);
        }

        if (dto.getRazorpayOrderId() == null) {

            logger.warn("Razorpay order ID is missing");

            throw new PaymentException(
                    "RazorpayOrderId details are missing",
                    HttpStatus.BAD_REQUEST);
        }

        if (dto.getRazorpaypaymentId() == null) {

            logger.warn("Razorpay payment ID is missing");

            throw new PaymentException(
                    "RazorpaypaymentId details are missing",
                    HttpStatus.BAD_REQUEST);
        }

        if (dto.getRazorpaySignature() == null) {

            logger.warn("Razorpay signature is missing");

            throw new PaymentException(
                    "RazorpaySignature details are missing",
                    HttpStatus.BAD_REQUEST);
        }

        try {

            String razorpayOrderId = dto.getRazorpayOrderId();
            String razorpayPaymentId = dto.getRazorpaypaymentId();
            String razorpaySignature = dto.getRazorpaySignature();

            logger.info("Received Razorpay payment details for order ID: {}",
                    order.getId());

            JSONObject payment = new JSONObject();

            payment.put("razorpay_order_id", dto.getRazorpayOrderId());
            payment.put("razorpay_payment_id", dto.getRazorpaypaymentId());
            payment.put("razorpay_signature", dto.getRazorpaySignature());

            logger.debug("Razorpay verification data prepared");

            boolean verified =
                    Utils.verifyPaymentSignature(payment, keySecret);

            if (!verified) {

                logger.warn("Payment verification failed for order ID: {}",
                        order.getId());

                order.setPaymentStatus("Failed");

                orderrepo.save(order);

                throw new PaymentException(
                        "PAyment verification failed 1",
                        HttpStatus.BAD_REQUEST);
            }

            logger.info("Payment signature verified successfully for order ID: {}",
                    order.getId());

            order.setPaymentStatus("Success");
            order.setStatus("Confirmed");

            orderrepo.save(order);

            logger.info("Payment successful. Order {} confirmed successfully",
                    order.getId());

            return ResponseEntity.ok("Payment Successful");

        } catch (Exception e) {

            logger.error("Error during payment verification for order ID: {}",
                    order.getId(), e);

            throw new PaymentException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
