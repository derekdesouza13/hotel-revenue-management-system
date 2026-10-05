import apiClient from "./client";

export const getPricingRules = async () => {
  const response = await apiClient.get("/pricing-rules");
  return response.data;
};

export const getPricingRuleById = async (ruleId) => {
  const response = await apiClient.get(`/pricing-rules/${ruleId}`);
  return response.data;
};

export const getPricingRulesByHotel = async (hotelId) => {
  const response = await apiClient.get(
    `/pricing-rules/hotel/${hotelId}`
  );

  return response.data;
};

export const createPricingRule = async (ruleData) => {
  const response = await apiClient.post("/pricing-rules", ruleData);
  return response.data;
};

export const updatePricingRule = async (ruleId, ruleData) => {
  const response = await apiClient.put(
    `/pricing-rules/${ruleId}`,
    ruleData
  );

  return response.data;
};

export const deletePricingRule = async (ruleId) => {
  await apiClient.delete(`/pricing-rules/${ruleId}`);
};