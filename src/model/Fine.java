package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Fine.java
 * Represents a fine applied to a loan or user account.
 */
public class Fine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fineId;
    private String loanId;
    private String userId;
    private double amount;
    private LocalDateTime issuedAt;
    private boolean paid;
    private LocalDateTime paidAt;
    private String reason;
    private String paymentMethod;

    public Fine(String fineId, String loanId, String userId, double amount, String reason) {
        this.fineId = fineId;
        this.loanId = loanId;
        this.userId = userId;
        this.amount = amount;
        this.reason = reason;
        this.issuedAt = LocalDateTime.now();
        this.paid = false;
        this.paidAt = null;
        this.paymentMethod = "UNPAID";
    }

    public String getFineId() {
        return fineId;
    }

    public void setFineId(String fineId) {
        this.fineId = fineId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void markPaid(String method) {
        this.paid = true;
        this.paymentMethod = method == null ? "UNKNOWN" : method;
        this.paidAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Fine{id='" + fineId + "', userId='" + userId + "', amount=" + amount + ", paid=" + paid + "}";
    }
}
