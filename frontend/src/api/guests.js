import apiClient from "./client";

export const getGuests = async () => {
  const response = await apiClient.get("/guests");
  return response.data;
};

export const getGuestById = async (guestId) => {
  const response = await apiClient.get(`/guests/${guestId}`);
  return response.data;
};

export const createGuest = async (guestData) => {
  const response = await apiClient.post("/guests", guestData);
  return response.data;
};

export const updateGuest = async (guestId, guestData) => {
  const response = await apiClient.put(`/guests/${guestId}`, guestData);
  return response.data;
};

export const deleteGuest = async (guestId) => {
  await apiClient.delete(`/guests/${guestId}`);
};