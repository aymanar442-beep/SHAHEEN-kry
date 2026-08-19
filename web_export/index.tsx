"use client";
import React, { useState, useEffect } from 'react';
import { Shield, Activity, Lock, Globe, ChevronRight, BarChart3, Zap, Terminal, Database, Server, Fingerprint, Eye, ArrowRight } from 'lucide-react';
import { motion } from 'framer-motion';

export default function Home() {
  const [mounted, setMounted] = useState(false);
  const [ticker, setTicker] = useState(40);

  useEffect(() => {
    setMounted(true);
    const interval = setInterval(() => setTicker(prev => (prev + Math.random() * 5 - 2.5)), 2000);
    return () => clearInterval(interval);
  }, []);

  if (!mounted) return null;

  return (
    <main className="flex min-h-screen flex-col items-center p-8 md:p-12 lg:p-24 relative overflow-hidden bg-[#020202] text-gray-300 selection:bg-shaheen-gold selection:text-black">
      {/* Immersive Background Matrix */}
      <div className="fixed inset-0 z-0 pointer-events-none opacity-[0.03]" 
           style={{ backgroundImage: 'linear-gradient(#fff 1px, transparent 1px), linear-gradient(90deg, #fff 1px, transparent 1px)', backgroundSize: '64px 64px' }}>
      </div>
      
      {/* Deep Ambient Glows */}
      <div className="fixed top-[-20%] left-[-10%] w-[800px] h-[800px] bg-shaheen-gold opacity-[0.02] blur-[150px] rounded-full pointer-events-none"></div>
      <div className="fixed bottom-[-20%] right-[-10%] w-[600px] h-[600px] bg-shaheen-neon opacity-[0.02] blur-[120px] rounded-full pointer-events-none"></div>

      {/* Premium Header */}
      <motion.header 
        initial={{ y: -20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.8, ease: "easeOut" }}
        className="z-10 w-full max-w-7xl flex justify-between items-center mb-24 border-b border-white/5 pb-6 backdrop-blur-md sticky top-0 pt-6"
      >
        <div className="flex items-center gap-4 cursor-pointer group">
          <div className="relative">
             <Shield className="w-8 h-8 text-shaheen-gold relative z-10 transition-transform duration-700 group-hover:rotate-180" />
             <div className="absolute inset-0 bg-shaheen-gold blur-lg opacity-20 group-hover:opacity-50 transition-opacity"></div>
          </div>
          <h1 className="text-2xl font-black tracking-[0.3em] text-white">SHAHEEN<span className="text-shaheen-gold animate-pulse">_</span></h1>
        </div>
        <nav className="hidden lg:flex gap-12 text-xs font-mono tracking-[0.2em] text-gray-500">
          {['ECOSYSTEM', 'TELEMETRY', 'NEURAL-NET', 'INFRASTRUCTURE'].map((item) => (
             <a key={item} href={`#${item.toLowerCase()}`} className="hover:text-white transition-colors relative group py-2">
                {item}
                <span className="absolute bottom-0 left-0 w-0 h-[1px] bg-shaheen-gold transition-all duration-300 group-hover:w-full"></span>
             </a>
          ))}
        </nav>
        <button className="px-6 py-2.5 border border-white/10 hover:border-shaheen-gold text-white hover:text-shaheen-gold hover:bg-shaheen-gold/5 transition-all text-xs tracking-widest font-mono flex items-center gap-2 group">
          <Fingerprint className="w-4 h-4 group-hover:scale-110 transition-transform" /> 
          INITIATE
        </button>
      </motion.header>

      {/* Cinematic Hero Section */}
      <section className="z-10 w-full max-w-7xl flex flex-col items-center text-center mt-12 mb-40">
        <motion.div 
          initial={{ scale: 0.9, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ duration: 1, delay: 0.2 }}
          className="inline-flex items-center gap-3 px-5 py-2 rounded-full border border-shaheen-neon/30 bg-[#00FF66]/5 backdrop-blur-sm mb-12 shadow-[0_0_30px_rgba(0,255,102,0.05)]"
        >
          <div className="relative flex h-2 w-2">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-shaheen-neon opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2 w-2 bg-shaheen-neon shadow-[0_0_8px_#00FF66]"></span>
          </div>
          <span className="text-[10px] font-mono tracking-[0.3em] text-shaheen-neon">GLOBAL NEURAL NETWORK ONLINE</span>
        </motion.div>
        
        <motion.h2 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.8, delay: 0.4 }}
          className="text-5xl md:text-7xl lg:text-8xl font-black tracking-tighter mb-8 leading-[1.05] text-white"
        >
          Sovereign Intelligence <br />
          <span className="text-transparent bg-clip-text bg-gradient-to-r from-gray-100 via-shaheen-gold to-gray-500 drop-shadow-sm">
            Financial Infrastructure.
          </span>
        </motion.h2>
        
        <motion.p 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.8, delay: 0.6 }}
          className="text-lg md:text-xl text-gray-400 max-w-3xl mb-16 leading-relaxed font-light"
        >
          Shaheen is not a platform; it is a <span className="text-white font-medium">Tier-1 Apex Entity</span>. 
          Powered by a proprietary Psycho-Bio Engine, we decode planetary market fear, map human greed indices, and execute absolute algorithmic sovereignty.
        </motion.p>
        
        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.8, delay: 0.8 }}
          className="flex flex-col sm:flex-row gap-6 w-full max-w-lg justify-center"
        >
          <button className="flex-1 flex items-center justify-center gap-3 px-8 py-4 bg-white text-black hover:bg-gray-200 transition-all font-bold tracking-[0.2em] text-sm shadow-[0_0_40px_rgba(255,255,255,0.15)] group">
            ENTER MATRIX <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>
          <button className="flex-1 flex items-center justify-center gap-3 px-8 py-4 border border-white/10 hover:border-white/40 bg-white/5 backdrop-blur-md transition-all font-bold tracking-[0.2em] text-white text-sm">
            <Eye className="w-4 h-4" /> OBSERVE
          </button>
        </motion.div>
      </section>

      {/* Palantir-Style Telemetry Matrix */}
      <motion.section 
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, margin: "-100px" }}
        transition={{ duration: 1 }}
        id="telemetry" 
        className="z-10 w-full max-w-7xl mb-40 relative"
      >
         {/* Decorative scanning line */}
         <div className="absolute top-0 left-0 w-full h-[1px] bg-gradient-to-r from-transparent via-shaheen-gold to-transparent opacity-50 animate-[scan_3s_ease-in-out_infinite]"></div>
         
         <div className="w-full border border-white/10 rounded-xl overflow-hidden bg-[#050505]/80 backdrop-blur-xl shadow-2xl relative">
            <div className="absolute top-0 right-0 w-64 h-64 bg-shaheen-neon/5 blur-[80px] pointer-events-none"></div>
            
            {/* Terminal Header */}
            <div className="flex items-center justify-between px-6 py-4 border-b border-white/5 bg-[#0A0A0A]">
              <div className="flex gap-2">
                <div className="w-2.5 h-2.5 rounded-full bg-white/20"></div>
                <div className="w-2.5 h-2.5 rounded-full bg-white/20"></div>
                <div className="w-2.5 h-2.5 rounded-full bg-shaheen-neon/80 shadow-[0_0_5px_#00FF66]"></div>
              </div>
              <div className="flex items-center gap-3 text-[10px] font-mono tracking-[0.2em] text-gray-500">
                <Terminal className="w-3 h-3 text-shaheen-gold" /> SHAHEEN_CORE_NODE_01
              </div>
            </div>
            
            {/* Terminal Body */}
            <div className="p-8 md:p-12 grid grid-cols-1 lg:grid-cols-12 gap-12">
               {/* Chart Area */}
               <div className="col-span-1 lg:col-span-8 space-y-8">
                 <div className="flex items-end justify-between border-b border-white/10 pb-4">
                    <div>
                       <p className="text-[10px] font-mono tracking-widest text-gray-500 mb-1">DATA STREAM</p>
                       <h3 className="text-white font-mono text-xl tracking-widest flex items-center gap-3">
                          <Activity className="w-5 h-5 text-shaheen-neon" />
                          PSYCHO-BIO MOMENTUM
                       </h3>
                    </div>
                    <div className="text-right">
                       <p className="text-[10px] font-mono tracking-widest text-gray-500 mb-1">GLOBAL TICKER</p>
                       <span className="text-shaheen-gold font-mono text-xl">{ticker.toFixed(2)} TH/s</span>
                    </div>
                 </div>
                 
                 {/* Advanced SVG Chart Simulation */}
                 <div className="h-64 w-full relative flex items-end pt-4 group">
                    <svg className="w-full h-full preserve-3d" viewBox="0 0 100 100" preserveAspectRatio="none">
                      <path d="M0,100 L0,50 Q25,30 50,60 T100,20 L100,100 Z" fill="url(#grad1)" opacity="0.1" className="group-hover:opacity-20 transition-opacity duration-1000"/>
                      <path d="M0,50 Q25,30 50,60 T100,20" fill="none" stroke="#D4AF37" strokeWidth="0.5" className="drop-shadow-[0_0_5px_rgba(212,175,55,0.5)]"/>
                      <defs>
                        <linearGradient id="grad1" x1="0%" y1="0%" x2="0%" y2="100%">
                          <stop offset="0%" stopColor="#D4AF37" stopOpacity="1" />
                          <stop offset="100%" stopColor="#D4AF37" stopOpacity="0" />
                        </linearGradient>
                      </defs>
                    </svg>
                    {/* Grid Overlay */}
                    <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.03)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.03)_1px,transparent_1px)] bg-[size:10%_20%] pointer-events-none"></div>
                 </div>
               </div>

               {/* Metrics Sidebar */}
               <div className="col-span-1 lg:col-span-4 space-y-6 font-mono">
                  <div className="p-6 border border-white/5 bg-[#0A0A0A] rounded-sm hover:border-white/20 transition-colors">
                     <p className="text-[10px] tracking-[0.2em] text-gray-500 mb-2 border-b border-white/5 pb-2">FEAR/GREED INDEX</p>
                     <p className="text-4xl font-black text-red-500 tracking-tighter mt-4">12 <span className="text-xs font-normal text-gray-500 tracking-widest ml-2 align-middle">PANIC</span></p>
                     <div className="w-full h-1 bg-white/5 mt-4 overflow-hidden">
                        <div className="h-full bg-red-500 w-[12%] shadow-[0_0_10px_red]"></div>
                     </div>
                  </div>

                  <div className="p-6 border border-shaheen-gold/20 bg-shaheen-gold/5 rounded-sm relative overflow-hidden group hover:bg-shaheen-gold/10 transition-colors">
                     <div className="absolute top-0 left-0 w-1 h-full bg-shaheen-gold group-hover:shadow-[0_0_15px_#D4AF37] transition-all"></div>
                     <p className="text-[10px] tracking-[0.2em] text-gray-400 mb-2 flex items-center gap-2">
                        <Zap className="w-3 h-3 text-shaheen-gold" /> AUTO-SWAP ENGAGED
                     </p>
                     <p className="text-sm font-bold text-white mt-4">ARBITRAGE DETECTED</p>
                     <p className="text-xs text-shaheen-gold mt-2">EXECUTING: BTC/ETH +1.8%</p>
                     <p className="text-[10px] text-gray-600 mt-4">LATENCY: 4ms</p>
                  </div>
               </div>
            </div>
         </div>
      </motion.section>

      {/* Expanded Architecture / Future Diverse Services */}
      <section id="ecosystem" className="z-10 w-full max-w-7xl mb-32">
        <div className="flex flex-col mb-20 text-center items-center">
          <span className="text-shaheen-gold text-[10px] font-mono tracking-[0.3em] mb-4 border border-shaheen-gold/30 px-3 py-1 rounded-full">THE APEX ECOSYSTEM</span>
          <h3 className="text-3xl md:text-5xl font-black tracking-tight text-white max-w-2xl leading-tight">
            Infinite Scale. Absolute Control.
          </h3>
          <p className="text-gray-400 mt-6 max-w-xl text-sm font-light leading-relaxed">
            Shaheen is expanding beyond trading. The architecture is designed to govern multiple sectors of algorithmic defense and intelligence.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {[
            { icon: Activity, title: "Psycho-Bio Engine", desc: "Core heuristic prediction algorithms mapping human emotion to market velocity.", color: "text-shaheen-gold" },
            { icon: Globe, title: "Global Auto-Swap", desc: "Instantaneous cross-exchange arbitrage executing at nanosecond latency.", color: "text-shaheen-neon" },
            { icon: Shield, title: "Hardware Sovereignty", desc: "Military-grade device locking. The intelligence is physically bound to silicon.", color: "text-white" },
            { icon: Database, title: "Quantum Data Hub", desc: "(Future) Decentralized dark-pool data lakes for predictive intelligence modeling.", color: "text-blue-400" }
          ].map((feature, i) => (
            <motion.div 
              key={i}
              initial={{ y: 20, opacity: 0 }}
              whileInView={{ y: 0, opacity: 1 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.1, duration: 0.5 }}
              className="p-8 border border-white/5 bg-[#050505] hover:bg-[#0A0A0A] hover:border-white/10 transition-all duration-300 group rounded-sm"
            >
              <feature.icon className={`w-8 h-8 ${feature.color} mb-8 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-500`} />
              <h4 className="text-lg font-bold mb-3 tracking-wide text-white">{feature.title}</h4>
              <p className="text-gray-500 text-xs leading-relaxed font-light">{feature.desc}</p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Footer */}
      <footer className="z-10 w-full max-w-7xl pt-8 border-t border-white/5 flex flex-col md:flex-row justify-between items-center gap-6 text-[10px] text-gray-600 font-mono tracking-widest">
        <p>© 2026 SHAHEEN CORP. // APEX PREDATOR CLASS</p>
        <div className="flex gap-6 items-center">
           <span className="flex items-center gap-2"><Server className="w-3 h-3" /> NODE ACTIVE</span>
           <span className="text-shaheen-gold px-2 py-1 border border-shaheen-gold/20 bg-shaheen-gold/5">RESTRICTED ACCESS</span>
        </div>
      </footer>
    </main>
  );
}
