package com.siemens.internship;

import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InternshipApplicationTests {

	private ItemService itemService;
	private Validator validator;

	@BeforeEach
	void setUp() {
		itemService = new ItemService();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void findById_NegativeId_ThrowsIllegalArgumentException() {
		try {
			itemService.findById(-1L);
			fail("Expected IllegalArgumentException for negative id");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid id, the id must be a positive integer", e.getMessage());
		}
	}

	@Test
	void deleteById_NegativeId_ThrowsIllegalArgumentException() {
		try {
			itemService.deleteById(-5L);
			fail("Expected IllegalArgumentException for negative id");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid id, the id must be a positive integer", e.getMessage());
		}
	}

	@Test
	void entityValidation_BlankFields_ShouldHaveViolations() {
		// id is irrelevant for validation here
		Item bad = new Item(null, "", "", "", "not-an-email");
		Set<ConstraintViolation<Item>> violations = validator.validate(bad);
		assertFalse(violations.isEmpty(), "Expected violations for blank/invalid fields");
	}

	@Test
	void entityValidation_ValidItem_NoViolations() {
		Item good = new Item(null, "Name", "Desc", "NEW", "test@example.com");
		Set<ConstraintViolation<Item>> violations = validator.validate(good);
		assertTrue(violations.isEmpty(), "Expected no violations for a valid Item");
	}

	@Test
	void contextLoads() {
	}

}
