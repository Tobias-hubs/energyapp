"use client";

import { useEffect, useState } from "react";

type Price = {
  timeStart: string;
  timeEnd: string;
  SEK: number;
};

export default function Home() {
  const [prices, setPrices] = useState<Price[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/prices?zone=SE3")
      .then((res) => res.json())
      .then((data) => {
        setPrices(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Hämtar elpriser...</p>;

  // hitta billigaste timmen
  const cheapest = prices.reduce((min, p) =>
    p.SEK < min.SEK ? p : min
  , prices[0]);

  function getColor(price: number) {
    if (price < 1.0) return "#22c55e"; // grön
    if (price < 1.3) return "#eab308"; // gul
    return "#ef4444"; // röd
  }

  return (
    <main style={styles.page}>
    
    <div style={styles.header}>
      <h1>⚡ Elpriser idag</h1>
      <p style={styles.sub}>
        Realtidsanalys av elpriser i Sverige
      </p>

      <div style={styles.highlight}>
        🟢 Billigaste timmen:{" "}
        <b>
          {new Date(cheapest.timeStart).getHours()}:00 –{" "}
          {cheapest.SEK.toFixed(2)} kr/kWh
        </b>
      </div>
    </div>

    <div style={styles.grid}>
      {prices.map((p, i) => {
        const isCheapest = p === cheapest;

        return (
          <div
            key={i}
            style={{
              ...styles.card,
              borderColor: getColor(p.SEK),
              transform: isCheapest ? "scale(1.03)" : "scale(1)",
              background: isCheapest ? "#30b577" : "white",
            }}
          >
            <div style={styles.time}>
              ⏰ {new Date(p.timeStart).getHours()}:00 -{" "}
              {new Date(p.timeEnd).getHours()}:00
            </div>

            <div style={styles.price}>
              {p.SEK.toFixed(2)} kr/kWh
            </div>

            {isCheapest && <div style={styles.badge}>🔥 Billigast</div>}
          </div>
        );
      })}
    </div>
  </main>
  );
  
}
const styles: any = {
  page: {
    padding: "40px",
    fontFamily: "system-ui",
    background: "#121213",
    minHeight: "100vh",
  },

  header: {
    marginBottom: "30px",
  },

  sub: {
    color: "#2c2e2f",
    marginTop: "-10px",
  },

  highlight: {
    marginTop: "15px",
    padding: "12px 16px",
    background: "#028bd0",
    borderRadius: "10px",
    display: "inline-block",
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))",
    gap: "12px",
  },

  card: {
    padding: "14px",
    borderRadius: "12px",
    border: "2px solid #d32121",
    background: "white",
    transition: "0.2s",
  },

  time: {
    fontSize: "12px",
    color: "#70360a",
  },

  price: {
    fontSize: "20px",
    fontWeight: "bold",
    marginTop: "6px",
    color: "black"
  },

  badge: {
    marginTop: "8px",
    fontSize: "12px",
    color: "#16a34a",
  },
};