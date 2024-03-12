// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2023 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.importer.handler.condition.impl;

import static com.norconex.commons.lang.Operator.EQUALS;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import java.util.function.Predicate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.norconex.commons.lang.Operator;

public class NumericValueMatcher implements  //NOSONAR we want to support null
Predicate<Double> {
    private final Operator operator;
    private final double number;

    @JsonCreator
    public NumericValueMatcher(@JsonProperty("operator") Operator operator, @JsonProperty("number") double number) {
        this.operator = operator;
        this.number = number;
    }

    @Override
    public boolean test(Double number) {
        if (number == null) {
            return false;
        }
        var op = defaultIfNull(operator, EQUALS);
        return op.evaluate(number, this.number);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Operator getOperator() {
        return this.operator;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public double getNumber() {
        return this.number;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof NumericValueMatcher)) return false;
        final NumericValueMatcher other = (NumericValueMatcher) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (java.lang.Double.compare(this.getNumber(), other.getNumber()) != 0) return false;
        final java.lang.Object this$operator = this.getOperator();
        final java.lang.Object other$operator = other.getOperator();
        if (this$operator == null ? other$operator != null : !this$operator.equals(other$operator)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof NumericValueMatcher;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $number = java.lang.Double.doubleToLongBits(this.getNumber());
        result = result * PRIME + (int) ($number >>> 32 ^ $number);
        final java.lang.Object $operator = this.getOperator();
        result = result * PRIME + ($operator == null ? 43 : $operator.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "NumericValueMatcher(operator=" + this.getOperator() + ", number=" + this.getNumber() + ")";
    }
}
