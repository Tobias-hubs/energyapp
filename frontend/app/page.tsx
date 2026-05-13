"use client";

import { useEffect, useState } from "react";

export default function Home() {
  const [prices, setPrices] = useState<any[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/prices?zone=SE3")
      .then((res) => res.json())
      .then((data) => setPrices(data))
      .catch((err) => console.error(err));
  }, []);

  const cheapest = prices.reduce((min, p) =>
  p.SEK < min.SEK ? p : min, prices[0] || {}
);

  return (
    <div style={{ display: "grid", gap: 10, marginTop: 20 }}>
  {prices.map((p, i) => (
    <div
      key={i}
      style={{
  padding: 12,
  border: "1px solid #ddd",
  borderRadius: 10,
  background: cheapest?.timeStart === p.timeStart ? "#040404" : "#312b2b",
}}
    >
      <div>
        ⏰ {new Date(p.timeStart).toLocaleString()}
      </div>
      <div style={{ fontWeight: "bold" }}>
        ⚡ {p.SEK.toFixed(2)} kr/kWh
      </div>
    </div>
  ))}
</div>
  );
}