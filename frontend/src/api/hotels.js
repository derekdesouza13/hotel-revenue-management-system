import apiClient from "./client";

export const getHotels = async () => {
  const response = await apiClient.get("/hotels");
  return response.data;
};

export const getHotelById = async (hotelId) => {
  const response = await apiClient.get(`/hotels/${hotelId}`);
  return response.data;
};

export const createHotel = async (hotelData) => {
  const response = await apiClient.post("/hotels", hotelData);
  return response.data;
};

export const updateHotel = async (hotelId, hotelData) => {
  const response = await apiClient.put(`/hotels/${hotelId}`, hotelData);
  return response.data;
};

export const deleteHotel = async (hotelId) => {
  await apiClient.delete(`/hotels/${hotelId}`);
};