import apiClient from "./client";

export const getRooms = async () => {
  const response = await apiClient.get("/rooms");
  return response.data;
};

export const getRoomById = async (roomId) => {
  const response = await apiClient.get(`/rooms/${roomId}`);
  return response.data;
};

export const getRoomsByHotel = async (hotelId) => {
  const response = await apiClient.get(`/rooms/hotel/${hotelId}`);
  return response.data;
};

export const createRoom = async (roomData) => {
  const response = await apiClient.post("/rooms", roomData);
  return response.data;
};

export const updateRoom = async (roomId, roomData) => {
  const response = await apiClient.put(`/rooms/${roomId}`, roomData);
  return response.data;
};

export const deleteRoom = async (roomId) => {
  await apiClient.delete(`/rooms/${roomId}`);
};