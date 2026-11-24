package tn.esprit.autoloc.reservation.service;

import tn.esprit.autoloc.reservation.entity.*;
import tn.esprit.autoloc.reservation.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository repository;
    private final IndexTrajetRepository trajetRepository;
    private final HistoriqueStatutRepository historiqueRepository;

    public ReservationService(ReservationRepository repository,
                              IndexTrajetRepository trajetRepository,
                              HistoriqueStatutRepository historiqueRepository) {
        this.repository = repository;
        this.trajetRepository = trajetRepository;
        this.historiqueRepository = historiqueRepository;
    }

    public List<Reservation> findAll() { return repository.findAll(); }

    public Reservation findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable : " + id));
    }

    public List<Reservation> findByPassenger(Long passengerId) {
        return repository.findByPassengerId(passengerId);
    }

    public List<HistoriqueStatut> historique(Long id) {
        return historiqueRepository.findByBookingIdOrderByChangedAtAsc(id);
    }

    @Transactional
    public Reservation create(Long rideId, Long passengerId, int seats) {
        IndexTrajet ride = trajetRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Trajet introuvable : " + rideId));
        if (ride.getSeatsLeft() < seats) {
            throw new RuntimeException("Pas assez de places disponibles");
        }
        ride.setSeatsLeft(ride.getSeatsLeft() - seats);
        trajetRepository.save(ride);

        Reservation r = new Reservation();
        r.setRide(ride);
        r.setPassengerId(passengerId);
        r.setSeats(seats);
        r.setStatus(Status.EN_ATTENTE);
        r = repository.save(r);

        trace(r, null, Status.EN_ATTENTE);
        return r;
    }

    @Transactional
    public Reservation changeStatus(Long id, Status newStatus) {
        Reservation r = findById(id);
        Status old = r.getStatus();
        if (old == newStatus) return r;

        // annulation : on rend les places au trajet
        if (newStatus == Status.ANNULEE && old != Status.ANNULEE) {
            IndexTrajet ride = r.getRide();
            ride.setSeatsLeft(ride.getSeatsLeft() + r.getSeats());
            trajetRepository.save(ride);
        }
        r.setStatus(newStatus);
        r = repository.save(r);
        trace(r, old, newStatus);
        return r;
    }

    @Transactional
    public void delete(Long id) {
        Reservation r = findById(id);
        if (r.getStatus() != Status.ANNULEE) {
            IndexTrajet ride = r.getRide();
            ride.setSeatsLeft(ride.getSeatsLeft() + r.getSeats());
            trajetRepository.save(ride);
        }
        repository.delete(r);   // l'historique est supprimé en cascade
    }

    private void trace(Reservation r, Status oldS, Status newS) {
        HistoriqueStatut h = new HistoriqueStatut();
        h.setBooking(r);
        h.setOldStatus(oldS);
        h.setNewStatus(newS);
        historiqueRepository.save(h);
    }
}