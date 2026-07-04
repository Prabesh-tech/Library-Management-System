package repository;

import model.Reservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ReservationRepository.java
 * In-memory storage for reservations.
 */
public class ReservationRepository {

    private Map<String, Reservation> reservationStore;

    public ReservationRepository() {
        this.reservationStore = new HashMap<>();
    }

    public boolean save(Reservation reservation) {
        if (reservation != null && reservation.getReservationId() != null) {
            reservationStore.put(reservation.getReservationId(), reservation);
            return true;
        }
        return false;
    }

    public Reservation findById(String reservationId) {
        return reservationStore.get(reservationId);
    }

    public boolean update(Reservation reservation) {
        if (reservation != null && reservation.getReservationId() != null && reservationStore.containsKey(reservation.getReservationId())) {
            reservationStore.put(reservation.getReservationId(), reservation);
            return true;
        }
        return false;
    }

    public boolean delete(String reservationId) {
        return reservationStore.remove(reservationId) != null;
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservationStore.values());
    }

    public List<Reservation> findByBookId(String bookId) {
        List<Reservation> matches = new ArrayList<>();
        for (Reservation reservation : reservationStore.values()) {
            if (reservation.getBookId().equals(bookId)) {
                matches.add(reservation);
            }
        }
        return matches;
    }

    public List<Reservation> findByUserId(String userId) {
        List<Reservation> matches = new ArrayList<>();
        for (Reservation reservation : reservationStore.values()) {
            if (reservation.getUserId().equals(userId)) {
                matches.add(reservation);
            }
        }
        return matches;
    }

    public List<Reservation> findActive() {
        List<Reservation> matches = new ArrayList<>();
        for (Reservation reservation : reservationStore.values()) {
            if (Reservation.STATUS_ACTIVE.equals(reservation.getStatus())) {
                matches.add(reservation);
            }
        }
        return matches;
    }
}
