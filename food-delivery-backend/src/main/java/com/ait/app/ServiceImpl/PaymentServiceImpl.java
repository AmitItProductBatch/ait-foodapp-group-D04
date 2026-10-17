package com.ait.app.ServiceImpl;

import org.json.JSONObject;
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

        System.out.println("payment service call");

        Order order = orderrepo.getById(dto.getOrderId());

        if (order == null) {
            throw new PaymentException(
                    "Order not found",
                    HttpStatus.NOT_FOUND);
        }
        
        if(order.getStatus().equals("Confirmed")) {
        	
        	throw new PaymentException("Order is already Confirmed ", HttpStatus.BAD_REQUEST);
        }

//        String stat="Placed";
        if (!"Placed".equalsIgnoreCase(order.getStatus())) {
            throw new PaymentException(
                    "Order is not placed",
                    HttpStatus.BAD_REQUEST);
        }

        // Take amount from Order
        dto.setAmount(order.getTotalAmount());

        if (dto.getAmount() <= 0) {
            throw new PaymentException(
                    "Invalid Payment amount",
                    HttpStatus.BAD_REQUEST);
        }

        int amount =
                (int) Math.round(dto.getAmount() * 100);

        if (amount < 100) {
            throw new PaymentException(
                    "Amount must be at least 100",
                    HttpStatus.BAD_REQUEST);
        }

        try {

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

            JSONObject response = new JSONObject();

            response.put("keyId", keyId);
            response.put("order_id", razorpayOrderId);
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("orderId", order.getId());

            return ResponseEntity.ok(
                    response.toString());

        } catch (Exception e) {

            throw new PaymentException(
                    "Unable to create RazorPay payment",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Override
	public ResponseEntity verifyPayment(PaymentVerifyDto dto) {

		Order order = orderrepo.getById(dto.getOrderId());
		System.out.println(dto.getRazorpaypaymentId());

		if (order == null) {
			throw new PaymentException("Order not found", HttpStatus.NOT_FOUND);
		}

		if (!"Placed".equalsIgnoreCase(order.getStatus())) {
			throw new PaymentException("Order is not available for Payment", HttpStatus.BAD_REQUEST);

		}

		if (dto.getRazorpayOrderId() == null ) {

			throw new PaymentException("RazorpayOrderId details are missing", HttpStatus.BAD_REQUEST);

		}
		if (dto.getRazorpaypaymentId() == null) {

			throw new PaymentException("RazorpaypaymentId details are missing", HttpStatus.BAD_REQUEST);

		}
		if (dto.getRazorpaySignature() == null) {

			throw new PaymentException("RazorpaySignature details are missing", HttpStatus.BAD_REQUEST);

		}

		try {
			
			    String razorpayOrderId = dto.getRazorpayOrderId();
			    String razorpayPaymentId = dto.getRazorpaypaymentId();
			    String razorpaySignature = dto.getRazorpaySignature();

			    System.out.println("Order ID = " + razorpayOrderId);
			    System.out.println("Payment ID = " + razorpayPaymentId);
			    System.out.println("Signature = " + razorpaySignature);


			    JSONObject payment = new JSONObject();

			    payment.put("razorpay_order_id", dto.getRazorpayOrderId());
			    payment.put("razorpay_payment_id", dto.getRazorpaypaymentId());
			    payment.put("razorpay_signature", dto.getRazorpaySignature());

			    System.out.println("JSON OBJECT = " + payment.toString());
			    System.out.println("HAS ORDER ID = " + payment.has("razorpay_order_id"));

			    boolean verified =
			            Utils.verifyPaymentSignature(payment, keySecret);

			if (!verified) {
				order.setPaymentStatus("Failed");

				orderrepo.save(order);

				throw new PaymentException("PAyment verification failed 1", HttpStatus.BAD_REQUEST);
			}

			order.setPaymentStatus("Success");
			order.setStatus("Confirmed");

			orderrepo.save(order);

			return ResponseEntity.ok("Payment Successful");

		} catch (Exception e) {
//			throw new PaymentException("PAyment verification failed 2", HttpStatus.INTERNAL_SERVER_ERROR);
			
			   e.printStackTrace();

			    throw new PaymentException(
			            e.getMessage(),
			            HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

}
