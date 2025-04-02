package org.ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket_booking.Entities.Ticket;
import org.ticket_booking.Entities.Train;
import org.ticket_booking.Entities.User;
import org.ticket_booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> userList;
    private User user;
    private static final String USER_FILE_PATH = "C:\\Users\\shriy\\OneDrive\\Desktop\\train_booking\\app\\src\\main\\java\\org\\ticket_booking\\localDb\\users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        userList = objectMapper.readValue(new File(USER_FILE_PATH), new TypeReference<List<User>>() {});
    }

    @SuppressWarnings("unused")
    public Boolean loginUser() {
        return userList.stream()
                .anyMatch(user1 -> user1.getName().equals(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()));
    }

    @SuppressWarnings("unused")
    public Boolean signUp(User user1) {
        user1.setHashedPassword(UserServiceUtil.hashPassword(user1.getPassword())); // Hash password before saving
        userList.add(user1);
        saveUserListToFile();
        return true;
    }

    private void saveUserListToFile() {
        try {
            objectMapper.writeValue(new File(USER_FILE_PATH), userList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void fetchBookings() {
        userList.stream()
                .filter(user1 -> user1.getName().equals(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
                .findFirst()
                .ifPresent(User::printTickets);
    }

    @SuppressWarnings("unused")
    public Boolean cancelBooking(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return false;
        }

        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return true;
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public List<Train> getTrains(String source, String destination) {
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source, destination);
    }

    @SuppressWarnings("unused")
    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    @SuppressWarnings("unused")
    public Boolean bookTrainSeat(Train train, int row, int seat) {
        TrainService trainService = new TrainService();
        List<List<Integer>> seats = train.getSeats();

        if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
            if (seats.get(row).get(seat) == 0) {
                seats.get(row).set(seat, 1);
                train.setSeats(seats);
                trainService.addTrain(train);
                return true;
            }
        }
        return false;
    }
}
