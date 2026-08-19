"use client";
import React, { useState, useEffect } from 'react';
import { Shield, Activity, Lock, Globe, ChevronRight, BarChart3, Zap, Terminal, Database, Server, Fingerprint, Eye, ArrowRight, HeartPulse, Cpu, CheckCircle, ChevronDown, BookOpen, Layers, Megaphone } from 'lucide-react';
import { motion } from 'framer-motion';

export default function Home() {
  const [mounted, setMounted] = useState(false);
  const [ticker, setTicker] = useState(40);
  const [lang, setLang] = useState('EN');

  useEffect(() => {
    setMounted(true);
    const interval = setInterval(() => setTicker(prev => (prev + Math.random() * 5 - 2.5)), 2000);
    return () => clearInterval(interval);
  }, []);

  if (!mounted) return null;

  return (
    <main className="flex min-h-screen flex-col items-center p-8 md:p-12 lg:p-24 relative overflow-hidden bg-[#020202] text-gray-300 selection:bg-[#00E5FF] selection:text-black font-sans">
      {/* Immersive Background Matrix */}
      <div className="fixed inset-0 z-0 pointer-events-none opacity-[0.03]" 
           style={{ backgroundImage: 'linear-gradient(#fff 1px, transparent 1px), linear-gradient(90deg, #fff 1px, transparent 1px)', backgroundSize: '64px 64px' }}>
      </div>
      
      {/* Deep Ambient Glows */}
      <div className="fixed top-[-20%] left-[-10%] w-[800px] h-[800px] bg-[#00E5FF] opacity-[0.02] blur-[150px] rounded-full pointer-events-none"></div>
      <div className="fixed bottom-[-20%] right-[-10%] w-[600px] h-[600px] bg-[#D4AF37] opacity-[0.02] blur-[120px] rounded-full pointer-events-none"></div>

      {/* Premium Header */}
      <motion.header 
        initial={{ y: -20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.8, ease: "easeOut" }}
        className="z-10 w-full max-w-7xl flex justify-between items-center mb-16 md:mb-24 border-b border-white/5 pb-6 backdrop-blur-md sticky top-0 pt-6"
      >
        <div className="flex items-center gap-4 cursor-pointer group">
          <div className="relative">
             <Shield className="w-8 h-8 text-[#00E5FF] relative z-10 transition-transform duration-700 group-hover:rotate-180" />
             <div className="absolute inset-0 bg-[#00E5FF] blur-lg opacity-20 group-hover:opacity-50 transition-opacity"></div>
          </div>
          <h1 className="text-xl md:text-2xl font-black tracking-[0.2em] md:tracking-[0.3em] text-white">SHAHEEN<span className="text-[#00E5FF] font-light ml-2 animate-pulse">APEX AI</span></h1>
        </div>
        <nav className="hidden lg:flex gap-12 text-xs font-mono tracking-[0.2em] text-gray-500 items-center">
          {['BIOMETRIC SAFETY', 'FINANCIAL ENGINE', 'VISION', 'ECOSYSTEM'].map((item) => (
             <a key={item} href={`#${item.split(' ')[0].toLowerCase()}`} className="hover:text-white transition-colors relative group py-2">
                {item}
                <span className="absolute bottom-0 left-0 w-0 h-[1px] bg-[#00E5FF] transition-all duration-300 group-hover:w-full"></span>
             </a>
          ))}
        </nav>
        <div className="hidden md:flex items-center gap-6">
          {/* Language Switcher */}
          <div className="flex items-center gap-2 cursor-pointer text-xs font-mono tracking-widest text-gray-400 hover:text-white transition-colors group relative">
            <Globe className="w-4 h-4 text-[#00E5FF]" />
            <span>{lang}</span>
            <ChevronDown className="w-3 h-3 group-hover:rotate-180 transition-transform" />
            
            {/* Dropdown (Hidden by default, shown on hover) */}
            <div className="absolute top-full right-0 mt-2 w-32 bg-[#0A0A0A] border border-white/10 rounded-md shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all flex flex-col overflow-hidden">
               {['EN (English)', 'AR (العربية)', 'FR (Français)', 'ZH (中文)'].map(l => (
                 <button key={l} onClick={() => setLang(l.substring(0,2))} className="text-left px-4 py-3 text-xs hover:bg-white/5 transition-colors border-b border-white/5 last:border-0 text-gray-400 hover:text-white">
                   {l}
                 </button>
               ))}
            </div>
          </div>

          <button className="px-6 py-2.5 border border-white/10 hover:border-[#00E5FF] text-white hover:text-[#00E5FF] hover:bg-[#00E5FF]/5 transition-all text-xs tracking-widest font-mono flex items-center gap-2 group">
            <Fingerprint className="w-4 h-4 group-hover:scale-110 transition-transform" /> 
            REQUEST ACCESS
          </button>
        </div>
      </motion.header>

      {/* Cinematic Hero Section */}
      <section className="z-10 w-full max-w-7xl flex flex-col lg:flex-row items-center gap-16 mt-4 md:mt-12 mb-32">
        <div className="flex-1 flex flex-col items-center lg:items-start text-center lg:text-left">
           <motion.div 
             initial={{ scale: 0.9, opacity: 0 }}
             animate={{ scale: 1, opacity: 1 }}
             transition={{ duration: 1, delay: 0.2 }}
             className="inline-flex items-center gap-3 px-4 py-1.5 rounded-full border border-[#00E5FF]/30 bg-[#00E5FF]/5 backdrop-blur-sm mb-10 shadow-[0_0_30px_rgba(0,229,255,0.05)]"
           >
             <div className="relative flex h-2 w-2">
               <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-[#00E5FF] opacity-75"></span>
               <span className="relative inline-flex rounded-full h-2 w-2 bg-[#00E5FF] shadow-[0_0_8px_#00E5FF]"></span>
             </div>
             <span className="text-[10px] font-mono tracking-[0.3em] text-[#00E5FF]">GLOBAL APEX NETWORK ONLINE</span>
           </motion.div>
           
           <motion.h2 
             initial={{ y: 20, opacity: 0 }}
             animate={{ y: 0, opacity: 1 }}
             transition={{ duration: 0.8, delay: 0.4 }}
             className="text-5xl md:text-7xl lg:text-8xl font-black tracking-tighter mb-8 leading-[1.05] text-white"
           >
             Cybernetic <br className="hidden lg:block"/>
             <span className="text-transparent bg-clip-text bg-gradient-to-r from-gray-100 via-[#00E5FF] to-blue-500 drop-shadow-sm">
               Advancements.
             </span>
           </motion.h2>
           
           <motion.p 
             initial={{ y: 20, opacity: 0 }}
             animate={{ y: 0, opacity: 1 }}
             transition={{ duration: 0.8, delay: 0.6 }}
             className="text-lg md:text-xl text-gray-400 max-w-2xl mb-12 leading-relaxed font-light"
           >
             SHAHEEN APEX AI is a global deep-tech holding entity. We build the future of intelligent safety through autonomous biosensors, and secure sovereign assets via our Tier-1 algorithmic trading engine. <br/><br/><span className="text-white font-medium italic">"Prevent before panic."</span>
           </motion.p>
           
           <motion.div 
             initial={{ y: 20, opacity: 0 }}
             animate={{ y: 0, opacity: 1 }}
             transition={{ duration: 0.8, delay: 0.8 }}
             className="flex flex-col sm:flex-row gap-6 w-full max-w-lg"
           >
             <a href="#biometric" className="flex-1 flex items-center justify-center gap-3 px-8 py-4 bg-white text-black hover:bg-gray-200 transition-all font-bold tracking-[0.2em] text-xs md:text-sm shadow-[0_0_40px_rgba(255,255,255,0.15)] group">
               SAFETY DIV <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
             </a>
             <a href="#financial" className="flex-1 flex items-center justify-center gap-3 px-8 py-4 border border-white/10 hover:border-white/40 bg-white/5 backdrop-blur-md transition-all font-bold tracking-[0.2em] text-white text-xs md:text-sm">
               <Activity className="w-4 h-4" /> TRADING DIV
             </a>
           </motion.div>
        </div>
        
        {/* Hero Image - Cybernetic Eagle Logo */}
        <motion.div 
          initial={{ opacity: 0, x: 50 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 1.2, delay: 0.5 }}
          className="flex-1 relative w-full max-w-lg aspect-square"
        >
           <div className="absolute inset-0 bg-[#00E5FF] blur-[100px] opacity-10 rounded-full"></div>
           <img src="/assets/shaheen_logo.jpg" alt="Shaheen Apex AI Cybernetic Eagle" className="w-full h-full object-contain relative z-10 drop-shadow-[0_0_30px_rgba(0,229,255,0.3)] rounded-2xl" />
        </motion.div>
      </section>

      {/* Division 1: Biometric Safety (Intelligent Kids Tracker) */}
      <section id="biometric" className="z-10 w-full max-w-7xl mb-40 pt-20 border-t border-white/10">
         <div className="flex flex-col lg:flex-row gap-16 items-center">
            <div className="flex-1 order-2 lg:order-1">
               <div className="grid grid-cols-2 gap-4">
                  <img src="/assets/60061.jpg" alt="Shaheen Smart Tracker AI" className="w-full h-auto rounded-xl border border-white/10 shadow-2xl hover:scale-105 transition-transform duration-500" />
                  <img src="/assets/60120.jpg" alt="Shaheen App Interface" className="w-full h-auto rounded-xl border border-white/10 shadow-2xl hover:scale-105 transition-transform duration-500" />
               </div>
            </div>
            
            <div className="flex-1 order-1 lg:order-2">
               <span className="text-[#00E5FF] text-[10px] font-mono tracking-[0.3em] mb-4 border border-[#00E5FF]/30 px-3 py-1 rounded-full inline-block">DIV 01 // AMBIENT CARE</span>
               <h3 className="text-4xl md:text-5xl font-black mt-6 mb-6 leading-tight">Intelligent Safety <br/>for Kids & Pets.</h3>
               <p className="text-gray-400 text-lg font-light mb-8 leading-relaxed">
                  Technology should not only respond to danger. It should help prevent it. The <span className="text-white">Shaheen A1</span> is an advanced, autonomous safety and biosensor ecosystem.
               </p>
               
               <div className="space-y-6">
                  {[
                     { icon: HeartPulse, title: "Psycho-Bio Engine", desc: "Fuses PPG (heart rate) and EDA (electrodermal activity) to detect panic and neurological distress silently." },
                     { icon: Cpu, title: "Offline Edge Processing", desc: "Computes signals locally and saves payloads securely even when internet connectivity is lost." },
                     { icon: Zap, title: "Stability Before Severity", desc: "Filters out false alarms caused by physical movement using a 3-axis IMU sensor correlation matrix." }
                  ].map((f, i) => (
                     <div key={i} className="flex gap-4">
                        <div className="w-12 h-12 rounded-lg bg-white/5 border border-white/10 flex items-center justify-center shrink-0">
                           <f.icon className="w-5 h-5 text-[#00E5FF]" />
                        </div>
                        <div>
                           <h4 className="text-white font-bold tracking-wide">{f.title}</h4>
                           <p className="text-gray-500 text-sm mt-1 leading-relaxed">{f.desc}</p>
                        </div>
                     </div>
                  ))}
               </div>
            </div>
         </div>
      </section>

      {/* Division 2: Financial Defense (Trading Weapon) */}
      <section id="financial" className="z-10 w-full max-w-7xl mb-40 pt-20 border-t border-white/10">
         <div className="flex flex-col lg:flex-row gap-16 items-center">
            <div className="flex-1">
               <span className="text-[#D4AF37] text-[10px] font-mono tracking-[0.3em] mb-4 border border-[#D4AF37]/30 px-3 py-1 rounded-full inline-block">DIV 02 // ALGORITHMIC SOVEREIGNTY</span>
               <h3 className="text-4xl md:text-5xl font-black mt-6 mb-6 leading-tight">High-Frequency <br/>Arbitrage Engine.</h3>
               <p className="text-gray-400 text-lg font-light mb-8 leading-relaxed">
                  Shaheen extends its predictive capabilities to global financial markets. Our proprietary Tier-1 algorithmic trading weapon decodes market panic and maps global greed indices.
               </p>
               
               <div className="space-y-6">
                  {[
                     { icon: BarChart3, title: "Momentum Heuristics", desc: "Maps extreme market fear/greed levels to execute entry/exit strategies during extreme volatility." },
                     { icon: Globe, title: "Auto-Swap Protocol", desc: "Instantaneous cross-exchange arbitrage executing at nanosecond latency to capture absolute yield." },
                     { icon: Lock, title: "Hardware Isolation", desc: "Encrypted device hardware locking protocols ensure zero unauthorized breaches." }
                  ].map((f, i) => (
                     <div key={i} className="flex gap-4">
                        <div className="w-12 h-12 rounded-lg bg-white/5 border border-white/10 flex items-center justify-center shrink-0">
                           <f.icon className="w-5 h-5 text-[#D4AF37]" />
                        </div>
                        <div>
                           <h4 className="text-white font-bold tracking-wide">{f.title}</h4>
                           <p className="text-gray-500 text-sm mt-1 leading-relaxed">{f.desc}</p>
                        </div>
                     </div>
                  ))}
               </div>
            </div>
            
            <div className="flex-1">
               <img src="/assets/shaheen_cinematic_3d_promo_1786884400292.jpg" alt="Shaheen High Frequency Trading Engine" className="w-full h-auto rounded-2xl border border-[#D4AF37]/20 shadow-[0_0_50px_rgba(212,175,55,0.15)]" />
            </div>
         </div>
      </section>

      {/* The Apex Ecosystem (Massive Holding Company Vision) */}
      <section id="ecosystem" className="z-10 w-full max-w-7xl mb-32 pt-20 border-t border-white/10">
        <div className="flex flex-col mb-20 text-center items-center">
          <span className="text-[#00E5FF] text-[10px] font-mono tracking-[0.3em] mb-4 border border-[#00E5FF]/30 px-3 py-1 rounded-full">GLOBAL HOLDING ENTITY</span>
          <h3 className="text-3xl md:text-5xl font-black tracking-tight text-white max-w-3xl leading-tight">
            The Apex Ecosystem.
          </h3>
          <p className="text-gray-400 mt-6 max-w-2xl text-lg font-light leading-relaxed">
            SHAHEEN APEX AI is not just a platform; it is a borderless digital ecosystem. From sovereign financial exchanges to global ad networks, we govern the future of integrated technology.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[
            { icon: Activity, title: "SHAHEEN Care Core", desc: "The foundation of our ecosystem. Autonomous biosensors and intelligent safety networks protecting families globally.", color: "text-[#00E5FF]" },
            { icon: Globe, title: "SHAHEEN Exchange (X)", desc: "A Tier-1 Centralized/Decentralized crypto trading exchange built for ultra-low latency, designed to rival and surpass Binance.", color: "text-[#D4AF37]" },
            { icon: BookOpen, title: "SHAHEEN Press & Books", desc: "A decentralized publishing platform empowering authors to publish, distribute, and monetize books and articles globally.", color: "text-white" },
            { icon: Megaphone, title: "SHAHEEN Ads Network", desc: "A high-conversion global advertising engine utilizing predictive AI to map consumer intent safely and privately.", color: "text-purple-400" },
            { icon: Layers, title: "SHAHEEN App Labs", desc: "Our developer arm. Expanding the ecosystem with external app integrations, SaaS tools, and 3rd-party API ecosystems.", color: "text-green-400" },
            { icon: Database, title: "Quantum Data Hub", desc: "(In Development) A multi-layered data lake utilizing predictive models to fuel all Shaheen divisions with unparalleled intelligence.", color: "text-blue-500" }
          ].map((feature, i) => (
            <motion.div 
              key={i}
              initial={{ y: 20, opacity: 0 }}
              whileInView={{ y: 0, opacity: 1 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.1, duration: 0.5 }}
              className="p-8 border border-white/5 bg-[#050505] hover:bg-[#0A0A0A] hover:border-white/10 transition-all duration-300 group rounded-xl shadow-lg relative overflow-hidden"
            >
              <div className={`absolute top-0 right-0 w-32 h-32 bg-current opacity-[0.03] blur-3xl rounded-full ${feature.color}`}></div>
              <feature.icon className={`w-8 h-8 ${feature.color} mb-6 opacity-80 group-hover:opacity-100 group-hover:scale-110 transition-all duration-500`} />
              <h4 className="text-xl font-bold mb-3 tracking-wide text-white">{feature.title}</h4>
              <p className="text-gray-500 text-sm leading-relaxed font-light">{feature.desc}</p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Vision & Origin Story */}
      <section id="vision" className="z-10 w-full max-w-5xl mb-40 pt-20 text-center">
         <Shield className="w-12 h-12 text-white/20 mx-auto mb-8" />
         <h3 className="text-3xl md:text-4xl font-black tracking-tight text-white mb-10">
            From a 6-Inch Screen in Damascus <br className="hidden md:block"/>to Y Combinator.
         </h3>
         
         <div className="bg-[#050505] border border-white/10 p-8 md:p-12 rounded-xl text-left relative overflow-hidden">
            <div className="absolute top-0 right-0 p-8 opacity-20 pointer-events-none">
               <img src="/assets/68869.jpg" className="w-48 blur-sm grayscale transition-all duration-500" alt="Credentials" />
            </div>
            <p className="text-gray-400 leading-relaxed font-light text-lg mb-6 relative z-10">
               "What does it take to build the infrastructure that secures life on Planet Earth? For many, it takes venture capital, state-of-the-art labs, and elite university degrees. For me, it took surviving war, enduring wrongful imprisonment, fighting off a fatal systemic infection, and coding an entire embedded hardware-software ecosystem on a 6-inch mobile phone screen from the heart of Damascus, Syria."
            </p>
            <p className="text-gray-400 leading-relaxed font-light text-lg mb-8 relative z-10">
               "I have proven to the world that pure willpower, raw intelligence, and a living conscience can rise from the deepest levels of suffering to invent solutions that serve all of humanity. We are not building another tracking device. We are building the future of intelligent safety."
            </p>
            
            <div className="flex items-center gap-4 relative z-10 pt-6 border-t border-white/10">
               <div className="w-12 h-12 rounded-full bg-[#00E5FF]/20 border border-[#00E5FF]/50 flex items-center justify-center text-white font-bold tracking-widest text-xs">AA</div>
               <div>
                  <p className="text-white font-bold tracking-widest text-sm">AYMAN AL-ARAISHI</p>
                  <p className="text-[#00E5FF] text-[10px] font-mono tracking-[0.2em] mt-1">FOUNDER & SOLE INVENTOR</p>
               </div>
            </div>
         </div>
      </section>

      {/* Legal & Compliance Footer */}
      <footer id="legal" className="z-10 w-full max-w-7xl pt-12 border-t border-white/10 flex flex-col gap-10">
         <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-xs text-gray-500 font-mono leading-relaxed">
            <div>
               <h5 className="text-white tracking-widest mb-4">REGULATORY STATUS</h5>
               <p>SHAHEEN Shield Core is configured as a Behavioral Monitor (GENERAL_VIGILANCE). It is NOT a medical diagnostic tool. FDA Compliance Mode active.</p>
            </div>
            <div>
               <h5 className="text-white tracking-widest mb-4">FINANCIAL DISCLAIMER</h5>
               <p>The High-Frequency Arbitrage system is an automated Beta System. It relies on complex heuristics but does not guarantee profit. Crypto markets involve extreme risk (Slippage, Exchange Latency).</p>
            </div>
            <div>
               <h5 className="text-white tracking-widest mb-4">INTELLECTUAL PROPERTY</h5>
               <p>All source codes, algorithms, TinyML micro-kernels, and schematics are 100% owned by Ayman Al Araishi. Protected under strict International NDA and Commercial Law.</p>
            </div>
         </div>
         
         <div className="pt-8 border-t border-white/5 flex flex-col md:flex-row justify-between items-center gap-6 text-[10px] text-gray-600 font-mono tracking-widest">
            <p>© 2026 SHAHEEN APEX AI INC. // GLOBAL CORPORATE ENTITY</p>
            <div className="flex gap-6 items-center">
               <span className="flex items-center gap-2"><Server className="w-3 h-3 text-[#00E5FF]" /> GLOBAL NETWORK SECURED</span>
               <span className="text-white px-2 py-1 border border-white/20 bg-white/5">ALL RIGHTS RESERVED</span>
            </div>
         </div>
      </footer>
    </main>
  );
}
