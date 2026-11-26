package tn.esprit.autoloc.trajets.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.trajets.entity.Ride;
import tn.esprit.autoloc.trajets.entity.RideRecurrence;
import tn.esprit.autoloc.trajets.entity.RideStop;
import tn.esprit.autoloc.trajets.entity.Vehicle;
import tn.esprit.autoloc.trajets.repository.RideRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RideService {

    private final RideRepository rideRepo;
    private final VehicleService vehicleService;

    public Ride create(Ride ride) {
        ride.setId(null);
        ride.setVehicle(resolveVehicle(ride.getVehicle()));
        linkChildren(ride);
        return rideRepo.save(ride);
    }

    @Transactional(readOnly = true)
    public List<Ride> findAll() {
        return rideRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Ride> search(String from, String to) {
        if (from != null && to != null) {
            return rideRepo.findByFromCityIgnoreCaseAndToCityIgnoreCase(from, to);
        }
        return rideRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Ride> findByDriver(UUID driverId) {
        return rideRepo.findByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public Ride findById(Long id) {
        return rideRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found: " + id));
    }

    public Ride update(Long id, Ride data) {
        Ride r = findById(id);
        r.setFromCity(data.getFromCity());
        r.setToCity(data.getToCity());
        r.setDepartureDatetime(data.getDepartureDatetime());
        r.setSeatsTotal(data.getSeatsTotal());
        r.setPricePerSeat(data.getPricePerSeat());
        if (data.getStatus() != null) {
            r.setStatus(data.getStatus());
        }
        if (data.getVehicle() != null) {
            r.setVehicle(resolveVehicle(data.getVehicle()));
        }

        // Remplacement des arrêts (orphanRemoval supprime les anciens)
        r.getStops().clear();
        if (data.getStops() != null) {
            for (RideStop s : data.getStops()) {
                s.setId(null);
                s.setRide(r);
                r.getStops().add(s);
            }
        }

        // Récurrence
        if (data.getRecurrence() == null) {
            r.setRecurrence(null);
        } else if (r.getRecurrence() == null) {
            RideRecurrence rec = data.getRecurrence();
            rec.setId(null);
            rec.setRide(r);
            r.setRecurrence(rec);
        } else {
            RideRecurrence rec = r.getRecurrence();
            rec.setDaysOfWeek(data.getRecurrence().getDaysOfWeek());
            rec.setStartDate(data.getRecurrence().getStartDate());
            rec.setEndDate(data.getRecurrence().getEndDate());
        }
        return rideRepo.save(r);
    }

    public Ride updateStatus(Long id, String status) {
        Ride r = findById(id);
        r.setStatus(status);
        return rideRepo.save(r);
    }

    public void delete(Long id) {
        rideRepo.delete(findById(id));
    }

    // ---------- helpers ----------

    private Vehicle resolveVehicle(Vehicle v) {
        if (v == null || v.getId() == null) {
            return null;
        }
        return vehicleService.findById(v.getId());
    }

    private void linkChildren(Ride ride) {
        if (ride.getStops() == null) {
            ride.setStops(new ArrayList<>());
        }
        for (RideStop s : ride.getStops()) {
            s.setId(null);
            s.setRide(ride);
        }
        if (ride.getRecurrence() != null) {
            ride.getRecurrence().setId(null);
            ride.getRecurrence().setRide(ride);
        }
    }
}