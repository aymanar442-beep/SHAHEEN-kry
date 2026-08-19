import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "SHAHEEN APEX AI | Sovereign Intelligence & Cybernetic Advancements",
  description: "Global Sovereign Financial Defense & Autonomous Biosensor Ecosystem. Prevent before panic.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="scroll-smooth">
      <body className="bg-[#020202] text-white min-h-screen">
        {children}
      </body>
    </html>
  );
}
