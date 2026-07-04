package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Reservation.java
 * Represents a book reservation request with queue position and expiration.
 */
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String bookId;
    private String userId;
    private LocalDateTime reservedAt;
    private String status;
    private int queuePosition;
    private LocalDateTime expiresAt;

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_FULFILLED = "FULFILLED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    public Reservation(String reservationId, String bookId, String userId, int queuePosition, LocalDateTime expiresAt) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservedAt = LocalDateTime.now();
        this.status = STATUS_ACTIVE;
        this.queuePosition = queuePosition;
        this.expiresAt = expiresAt;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return status.equals(STATUS_ACTIVE) && expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    @Override
    public String toString() {
        return "Reservation{id='" + reservationId + "', bookId='" + bookId + "', userId='" + userId + "', status='" + status + "'}";
    }
}
