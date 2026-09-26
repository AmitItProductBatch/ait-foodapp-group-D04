package com.ait.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

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
	
	@Override
	public ResponseEntity updateDeliveryRules(DeliveryRuleDto dto) {
		
		if (dto.getBaseFee() <= 0 ||
	            dto.getPerKmRate() <= 0 ||
	            dto.getMaxDeliveryRadius() <= 0 ||
	            dto.getFreeDeliveryThreshold() <= 0) {
			throw new DeliveryRuleException("All delivery pricing values must be positive",HttpStatus.BAD_REQUEST);
		}
		
		List<DeliveryRules> rules = deliveryrulerepo.findByActive(true);
		
		DeliveryRules rule;
		
		if(rules.isEmpty()) {
			
			rule = new DeliveryRules();
			
			rule.setCreatedAt(LocalDateTime.now());
		}
		else {
			rule = rules.get(0);
		}
		
		 rule.setBaseFee(dto.getBaseFee());
	        rule.setPerKmRate(dto.getPerKmRate());
	        rule.setMaxDeliveryRadius(dto.getMaxDeliveryRadius());
	        rule.setFreeDeliveryThreshold(
	                dto.getFreeDeliveryThreshold());
	        
	        rule.setActive(true);
	        
	        rule.setUpdatedAt(LocalDateTime.now());
	        
	        deliveryrulerepo.save(rule);
	        return new ResponseEntity(dto, HttpStatus.OK);
		
	}

}
