
package com.ait.app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.ait.app.model.Feedback;

public class FeedbackControllerTest {

    @Test
    public void testFeedback() {

        Feedback feedback = new Feedback();

        feedback.setId(1);
        feedback.setUserId(1);
        feedback.setRestaurantId(1);
        feedback.setOrderId(1);
        feedback.setRating(5);
        feedback.setComment("Good food");

        assertEquals(1, feedback.getId());
        assertEquals(1, feedback.getUserId());
        assertEquals(1, feedback.getRestaurantId());
        assertEquals(1, feedback.getOrderId());
        assertEquals(5, feedback.getRating());
        assertEquals("Good food", feedback.getComment());
    }
}
