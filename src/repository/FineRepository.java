package repository;

import model.Fine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FineRepository.java
 * In-memory storage for fines.
 */
public class FineRepository {

    private Map<String, Fine> fineStore;

    public FineRepository() {
        this.fineStore = new HashMap<>();
    }

    public boolean save(Fine fine) {
        if (fine != null && fine.getFineId() != null) {
            fineStore.put(fine.getFineId(), fine);
            return true;
        }
        return false;
    }

    public Fine findById(String fineId) {
        return fineStore.get(fineId);
    }

    public boolean update(Fine fine) {
        if (fine != null && fine.getFineId() != null && fineStore.containsKey(fine.getFineId())) {
            fineStore.put(fine.getFineId(), fine);
            return true;
        }
        return false;
    }

    public boolean delete(String fineId) {
        return fineStore.remove(fineId) != null;
    }

    public List<Fine> findAll() {
        return new ArrayList<>(fineStore.values());
    }

    public List<Fine> findByUserId(String userId) {
        List<Fine> matches = new ArrayList<>();
        for (Fine fine : fineStore.values()) {
            if (fine.getUserId().equals(userId)) {
                matches.add(fine);
            }
        }
        return matches;
    }

    public Fine findByLoanId(String loanId) {
        for (Fine fine : fineStore.values()) {
            if (fine.getLoanId().equals(loanId)) {
                return fine;
            }
        }
        return null;
    }

    public List<Fine> findUnpaid() {
        List<Fine> matches = new ArrayList<>();
        for (Fine fine : fineStore.values()) {
            if (!fine.isPaid()) {
                matches.add(fine);
            }
        }
        return matches;
    }
}
