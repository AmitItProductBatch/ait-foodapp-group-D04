package com.ait.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryRuleService;
import com.ait.app.customExceptionHandler.DeliveryRuleException;
import com.ait.app.model.DeliveryRules;
import com.ait.app.repository.DeliveryRuleRepo;
import com.ait.app.requestBody.DeliveryRuleDto;

@Service
public class DeliveryRuleServiceImpl implements DeliveryRuleService{

	@Autowired
	DeliveryRuleRepo deliveryrulerepo;
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryRuleServiceImpl.class);
	

	@Override
	public ResponseEntity updateDeliveryRules(DeliveryRuleDto dto) {

		logger.info("Starting updateDeliveryRules method");

		if (dto.getBaseFee() <= 0 || dto.getPerKmRate() <= 0 || dto.getMaxDeliveryRadius() <= 0
				|| dto.getFreeDeliveryThreshold() <= 0) {

			logger.warn("Invalid delivery pricing values provided");
			throw new DeliveryRuleException("All delivery pricing values must be positive", HttpStatus.BAD_REQUEST);
		}

		logger.info("Fetching active delivery rules");

		List<DeliveryRules> rules = deliveryrulerepo.findByActive(true);

		DeliveryRules rule;

		if (rules.isEmpty()) {

			logger.info("No active delivery rules found. Creating new rules");

			rule = new DeliveryRules();

			rule.setCreatedAt(LocalDateTime.now());
		} else {
			logger.info("Existing active delivery rules found. Updating the first rule");

			rule = rules.get(0);
		}

		rule.setBaseFee(dto.getBaseFee());
		rule.setPerKmRate(dto.getPerKmRate());
		rule.setMaxDeliveryRadius(dto.getMaxDeliveryRadius());
		rule.setFreeDeliveryThreshold(dto.getFreeDeliveryThreshold());

		rule.setActive(true);

		rule.setUpdatedAt(LocalDateTime.now());

		deliveryrulerepo.save(rule);

		logger.info("Delivery rules saved successfully");

		return new ResponseEntity(dto, HttpStatus.OK);
	}

}
