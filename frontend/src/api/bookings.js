import apiClient from "./client";

export const getBookings = async () => {
  const response = await apiClient.get("/bookings");
  return response.data;
};

export const getBookingById = async (bookingId) => {
  const response = await apiClient.get(`/bookings/${bookingId}`);
  return response.data;
};

export const getBookingsByHotel = async (hotelId) => {
  const response = await apiClient.get(`/bookings/hotel/${hotelId}`);
  return response.data;
};

export const getBookingsByRoom = async (roomId) => {
  const response = await apiClient.get(`/bookings/room/${roomId}`);
  return response.data;
};

export const createBooking = async (bookingData) => {
  const response = await apiClient.post("/bookings", bookingData);
  return response.data;
};

export const cancelBooking = async (bookingId) => {
  const response = await apiClient.patch(
    `/bookings/${bookingId}/cancel`
  );

  return response.data;
};

export const deleteBooking = async (bookingId) => {
  await apiClient.delete(`/bookings/${bookingId}`);
};