package com.marcomarchionni.ibportfolio.validators;

import com.marcomarchionni.ibportfolio.domain.Position;
import com.marcomarchionni.ibportfolio.dtos.update.UpdateDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.marcomarchionni.ibportfolio.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateDtoValidatorTest {

    UpdateDtoValidator dtoValidator;
    UpdateDto dto;


    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            dtoValidator = new UpdateDtoValidator(validator);
        }

        dto = UpdateDto.builder()
                .flexStatement(getSampleFlexStatement())
                .positions(getSamplePositions())
                .trades(getSampleTrades())
                .dividends(getSampleDividends())
                .build();
    }

    @Test
    void isValid() {
        assertTrue(dtoValidator.isValid(dto));
    }

    @Test
    void invalidData() {
        dto.setPositions(null);
        // Act
        assertThrows(ConstraintViolationException.class, () -> dtoValidator.isValid(dto));
    }

    @Test
    void invalidAccountId() {
        Position position = getSamplePosition();
        position.setAccountId("U22222");
        dto.getPositions().add(position);
        // Act
        assertThrows(ConstraintViolationException.class, () -> dtoValidator.isValid(dto));
    }
}