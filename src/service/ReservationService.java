package service;

import model.Reservation;
import repository.ReservationRepository;
import util.DataManager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * ReservationService.java
 * Business logic for managing reservations.
 */
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService() {
        this.reservationRepository = DataManager.getInstance().getReservationRepository();
    }

    public boolean createReservation(Reservation reservation) {
        List<Reservation> bookQueue = reservationRepository.findByBookId(reservation.getBookId());
        int nextPosition = bookQueue.stream().map(Reservation::getQueuePosition).max(Comparator.naturalOrder()).orElse(0) + 1;
        reservation.setQueuePosition(nextPosition);
        if (reservation.getExpiresAt() == null) {
            reservation.setExpiresAt(LocalDateTime.now().plusDays(7));
        }
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(String reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public boolean updateReservation(Reservation reservation) {
        return reservationRepository.update(reservation);
    }

    public boolean cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) return false;
        reservation.setStatus(Reservation.STATUS_CANCELLED);
        return reservationRepository.update(reservation);
    }

    public List<Reservation> getBookReservations(String bookId) {
        return reservationRepository.findByBookId(bookId);
    }

    public List<Reservation> getUserReservations(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getActiveReservations() {
        return reservationRepository.findActive();
    }
}
