/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.offer;

import java.math.BigDecimal;
import java.util.Date;

public class OfferItem {

    private final Product product;

    private final Money cost;

    private final int quantity;

    private final String discountCause;

    private final BigDecimal discount;

    public OfferItem(String productId, BigDecimal productPrice, String productName, Date productSnapshotDate, String productType, int quantity) {
        this(productId, productPrice, productName, productSnapshotDate, productType, quantity, null, null);
    }

    public OfferItem(String productId, BigDecimal productPrice, String productName, Date productSnapshotDate,
            String productType, int quantity, BigDecimal discount, String discountCause) {

        product = new Product(productId, productPrice, productName, productSnapshotDate, productType);

        this.quantity = quantity;
        this.discount = discount;
        this.discountCause = discountCause;

        BigDecimal discountValue = new BigDecimal(0);
        if (discount != null) {
            discountValue = discountValue.subtract(discount);
        }

        cost = new Money(productPrice.multiply(new BigDecimal(quantity)).subtract(discountValue));
    }

    public String getProductId() {
        return product.getId();
    }

    public BigDecimal getProductPrice() {
        return product.getPrice();
    }

    public String getProductName() {
        return product.getName();
    }

    public Date getProductSnapshotDate() {
        return product.getSnapshotDate();
    }

    public String getProductType() {
        return product.getType();
    }

    public BigDecimal getTotalCost() {
        return cost.getAmount();
    }

    public String getTotalCostCurrency() {
        return cost.getCurrency();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getDiscountCause() {
        return discountCause;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (discount == null ? 0 : discount.hashCode());
        result = prime * result + (product.getName() == null ? 0 : product.getName().hashCode());
        result = prime * result + (product.getPrice() == null ? 0 : product.getPrice().hashCode());
        result = prime * result + (product.getId() == null ? 0 : product.getId().hashCode());
        result = prime * result + (product.getType() == null ? 0 : product.getType().hashCode());
        result = prime * result + quantity;
        result = prime * result + (cost.getAmount() == null ? 0 : cost.getAmount().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OfferItem other = (OfferItem) obj;
        if (discount == null) {
            if (other.discount != null) {
                return false;
            }
        } else if (!discount.equals(other.discount)) {
            return false;
        }

        if(!product.equals(other.product))
            return false;


        if (quantity != other.quantity) {
            return false;
        }

        if(!cost.equals(other.cost))
            return false;

        return true;
    }

    /**
     *
     * @param delta
     *            acceptable percentage difference
     */
    public boolean sameAs(OfferItem other, double delta) {
        if(!product.equals(other.product))
            return false;

        if (quantity != other.quantity) {
            return false;
        }

        BigDecimal max;
        BigDecimal min;
        if (cost.getAmount().compareTo(other.cost.getAmount()) > 0) {
            max = cost.getAmount();
            min = other.cost.getAmount();
        } else {
            max = other.cost.getAmount();
            min = cost.getAmount();
        }

        BigDecimal difference = max.subtract(min);
        BigDecimal acceptableDelta = max.multiply(BigDecimal.valueOf(delta / 100));

        return acceptableDelta.compareTo(difference) > 0;
    }

}
