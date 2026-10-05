import { BrowserRouter, Route, Routes } from "react-router-dom";

import Layout from "./components/layout/Layout";

import Dashboard from "./pages/Dashboard";
import Hotels from "./pages/Hotels";
import Rooms from "./pages/Rooms";
import Guests from "./pages/Guests";
import Bookings from "./pages/Bookings";
import PricingRules from "./pages/PricingRules";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/hotels" element={<Hotels />} />
          <Route path="/rooms" element={<Rooms />} />
          <Route path="/guests" element={<Guests />} />
          <Route path="/bookings" element={<Bookings />} />
          <Route
            path="/pricing-rules"
            element={<PricingRules />}
          />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;