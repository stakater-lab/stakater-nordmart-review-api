package com.stakater.nordmart.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review implements Serializable {
    static final long serialVersionUID = -7304814269819778382L;

    @Id
    String id;
    @NotBlank
    String productId;
    String customerName = "";
    int rating = 3;
    String reviewText = "";
    Date dateTime;

    public Review(
            final String productId,
            final String customerName,
            final String rating,
            final String text) {
        super();
        setRating(NumberUtils.toInt(rating, 3));
        if (StringUtils.isNotBlank(text)) {
            this.reviewText = text;
        }
        this.productId = productId;
        this.customerName = customerName;
        this.dateTime = new Date();

    }

    public void setRating(final int rating) {
        this.rating = getRangedRating(rating);
    }

    public static int getRangedRating(final int rating) {
        if (rating < 1) {
            return 1;
        }
        return Math.min(rating, 5);
    }

    @Override
    public String toString() {
        return String.format(
                "Review[id=%s, productId='%s', customerName='%s', rating='%d', reviewText='%s']",
                this.id, this.productId, this.customerName, this.rating, this.reviewText);
    }
}
