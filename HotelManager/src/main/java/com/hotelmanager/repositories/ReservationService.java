package com.hotelmanager.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hotelmanager.dtos.reservation.CreateReservationDTO;
import com.hotelmanager.dtos.reservation.ReservationDTO;
import com.hotelmanager.exception.reservation.ReservationCustomException;
import com.hotelmanager.mappers.ReservationMapper;
import com.hotelmanager.models.Reservation;
import com.hotelmanager.models.User;
import com.hotelmanager.repositories.ReservationRepository;
import com.hotelmanager.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReservationDTO createReservation(CreateReservationDTO createReservationDTO) {
        try {
            if (createReservationDTO == null) {
                throw new ReservationCustomException.InvalidReservationDataException();
            }

            if (createReservationDTO.getCheckInDate() == null || createReservationDTO.getCheckOutDate() == null) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            if (createReservationDTO.getCheckInDate().isAfter(createReservationDTO.getCheckOutDate())) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            UUID userId = createReservationDTO.getUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ReservationCustomException.UserNotFoundException(userId));

            if (reservationRepository.existsByRoomIdAndDateRange(createReservationDTO.getRoomId(),
                    createReservationDTO.getCheckInDate(), createReservationDTO.getCheckOutDate())) {
                throw new ReservationCustomException.ReservationConflictException();
            }

            Reservation reservation = reservationMapper.toEntity(createReservationDTO);
            reservation.setUser(user);
            reservationRepository.save(reservation);

            return mapToDto(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReservationDTO updateReservation(UUID id, CreateReservationDTO updateReservationDTO) {
        try {
            if (updateReservationDTO == null) {
                throw new ReservationCustomException.InvalidReservationDataException();
            }

            Reservation existingReservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException(id));

            if (updateReservationDTO.getCheckInDate().isAfter(updateReservationDTO.getCheckOutDate())) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            existingReservation.setCheckInDate(updateReservationDTO.getCheckInDate());
            existingReservation.setCheckOutDate(updateReservationDTO.getCheckOutDate());

            reservationRepository.save(existingReservation);
            return mapToDto(existingReservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReservation(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException(id));
            reservationRepository.delete(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReservationDTO getReservationById(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException(id));
            return mapToDto(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReservationDTO> getAllReservations() {
        try {
            return reservationRepository.findAll()
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReservationDTO mapToDto(Reservation reservation) {
        try {
            if (reservation == null) {
                throw new ReservationCustomException.ReservationNotFoundException(reservation.getId());
            }
            return modelMapper.map(reservation, ReservationDTO.class);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReservationById(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException(id));
            reservationRepository.delete(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReservationDTO updateReservation(UUID id, ReservationDTO updateReservationDTO) {
        try {
            if (updateReservationDTO == null) {
                throw new ReservationCustomException.InvalidReservationDataException();
            }

            Reservation existingReservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException(id));

            if (updateReservationDTO.getCheckInDate().isAfter(updateReservationDTO.getCheckOutDate())) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            existingReservation.setCheckInDate(updateReservationDTO.getCheckInDate());
            existingReservation.setCheckOutDate(updateReservationDTO.getCheckOutDate());

            reservationRepository.save(existingReservation);
            return mapToDto(existingReservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
