"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Copy, Link, Loader2, Check, Sparkles, ArrowRight, BarChart2, Clock, Globe } from "lucide-react";

interface UrlStats {
  accessCount: number;
  originalUrl: string;
  createdAt: string;
  lastAccess: string | null;
}

export default function Home() {
  // Shortener State
  const [url, setUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [copied, setCopied] = useState(false);

  // Stats State
  const [statsCode, setStatsCode] = useState("");
  const [statsData, setStatsData] = useState<UrlStats | null>(null);
  const [statsLoading, setStatsLoading] = useState(false);
  const [statsError, setStatsError] = useState("");

  const handleShortenSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setShortUrl("");
    setCopied(false);

    try {
      const response = await fetch("/api/shortener", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ url }),
      });

      if (!response.ok) {
        throw new Error("Falha ao encurtar URL");
      }

      const data = await response.json();
      const currentOrigin = window.location.origin;
      setShortUrl(`${currentOrigin}/api/shortener/${data.urlCode}`);
    } catch (err) {
      setError("Erro ao encurtar. Verifique a URL e tente novamente.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleStatsSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatsLoading(true);
    setStatsError("");
    setStatsData(null);

    try {
      // Try to extract code if user pasted a full URL
      let code = statsCode.trim();
      const possibleUrlParts = code.split('/');
      if (possibleUrlParts.length > 1) {
          // Assuming the last part is the code
          code = possibleUrlParts[possibleUrlParts.length - 1];
      }

      const response = await fetch(`/api/shortener/${code}/stats`);

      if (!response.ok) {
        if (response.status === 404) {
            throw new Error("URL não encontrada.");
        }
        throw new Error("Falha ao buscar estatísticas.");
      }

      const data = await response.json();
      setStatsData(data);
    } catch (err: any) {
      setStatsError(err.message || "Erro ao buscar estatísticas.");
    } finally {
      setStatsLoading(false);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(shortUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const formatDate = (dateString: string | null) => {
    if (!dateString) return "Nunca acessado";
    return new Date(dateString).toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-teal-50 to-cyan-50 flex justify-center p-4 md:p-8 pt-20 md:pt-32">
      
      {/* Decorative background elements */}
      <div className="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none z-0">
         <div className="absolute top-[-10%] left-[-10%] w-96 h-96 bg-emerald-200/30 rounded-full blur-3xl" />
         <div className="absolute bottom-[-10%] right-[-10%] w-96 h-96 bg-teal-200/30 rounded-full blur-3xl" />
      </div>

      <div className="w-full max-w-2xl z-10 space-y-8">
        
        {/* Header Section */}
        <div className="text-center space-y-4 animate-in fade-in slide-in-from-bottom-4 duration-700">
          <div className="inline-flex items-center justify-center p-4 bg-white rounded-md shadow-sm mb-2 ring-1 ring-emerald-100">
            <div className="bg-gradient-to-br from-emerald-500 to-teal-600 text-white p-2 rounded-md">
              <Link className="h-6 w-6" />
            </div>
            <span className="ml-3 text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-emerald-600 to-teal-600">
              Curtin
            </span>
          </div>
          <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight text-slate-800">
            Encurte links com <span className="text-emerald-600">elegância</span>.
          </h1>
          <p className="text-lg text-slate-500 max-w-lg mx-auto">
            Transforme URLs longas e complexas em links curtos, compartilháveis e rastreáveis em segundos.
          </p>
        </div>

        <Tabs defaultValue="shorten" className="w-full">
          <div className="flex justify-center mb-6">
            <TabsList className="grid w-[300px] grid-cols-2 bg-white/50 p-1 rounded-lg border border-slate-200 shadow-sm">
              <TabsTrigger value="shorten" className="rounded-md">Encurtar</TabsTrigger>
              <TabsTrigger value="stats" className="rounded-md">Estatísticas</TabsTrigger>
            </TabsList>
          </div>

          <TabsContent value="shorten">
             {/* Shortener Card */}
            <Card className="border-none shadow-2xl shadow-emerald-900/5 bg-white/80 backdrop-blur-xl ring-1 ring-white/50 rounded-md">
              <CardContent className="p-8">
                <form onSubmit={handleShortenSubmit} className="space-y-6">
                  <div className="space-y-2">
                    <label htmlFor="url-input" className="text-sm font-medium text-slate-600 ml-1">
                      Cole sua URL longa aqui
                    </label>
                    <div className="relative group">
                      <Input
                        id="url-input"
                        type="url"
                        placeholder="https://exemplo.com/minha-url-super-longa"
                        required
                        value={url}
                        onChange={(e) => setUrl(e.target.value)}
                        className="h-14 px-5 rounded-md bg-white border-slate-200 focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 transition-all text-lg shadow-sm group-hover:border-emerald-300"
                      />
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400">
                        <Sparkles className="h-5 w-5 opacity-0 group-focus-within:opacity-100 transition-opacity text-emerald-500" />
                      </div>
                    </div>
                  </div>

                  <Button 
                    type="submit" 
                    className="w-full h-14 rounded-md text-lg font-semibold bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white shadow-lg shadow-emerald-500/25 transition-all hover:scale-[1.01] active:scale-[0.99] cursor-pointer" 
                    disabled={loading}
                  >
                    {loading ? (
                      <div className="flex items-center gap-2">
                        <Loader2 className="h-5 w-5 animate-spin" />
                        <span>Processando...</span>
                      </div>
                    ) : (
                      <div className="flex items-center gap-2">
                        <span>Encurtar Agora</span>
                        <ArrowRight className="h-5 w-5" />
                      </div>
                    )}
                  </Button>
                </form>

                {error && (
                  <div className="mt-6 p-4 bg-red-50 border border-red-100 rounded-md flex items-center gap-3 text-red-600 animate-in fade-in slide-in-from-top-2">
                    <div className="bg-red-100 p-1 rounded-full">
                      <span className="font-bold px-2">!</span>
                    </div>
                    <p className="text-sm font-medium">{error}</p>
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Shortener Result */}
            {shortUrl && (
              <div className="mt-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
                <Card className="border-emerald-100 bg-white shadow-xl shadow-emerald-900/5 overflow-hidden rounded-md">
                  <div className="bg-emerald-50/50 p-4 border-b border-emerald-100/50 flex justify-between items-center">
                    <span className="text-sm font-semibold text-emerald-700 flex items-center gap-2">
                        <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
                        Link Gerado com Sucesso
                    </span>
                  </div>
                  <CardContent className="p-6">
                    <div className="flex flex-col md:flex-row gap-4 items-center">
                      <div className="flex-1 w-full bg-slate-50 border border-slate-200 rounded-md p-4 font-mono text-slate-600 break-all text-center md:text-left select-all">
                        {shortUrl}
                      </div>
                      <div className="flex gap-2 w-full md:w-auto">
                        <Button
                            onClick={copyToClipboard}
                            className={`flex-1 md:flex-none h-12 px-6 rounded-md transition-all duration-300 cursor-pointer ${
                                copied 
                                ? "bg-emerald-100 text-emerald-700 hover:bg-emerald-200 border border-emerald-200" 
                                : "bg-slate-900 text-white hover:bg-slate-800"
                            }`}
                            variant={copied ? "outline" : "default"}
                        >
                            {copied ? (
                            <>
                                <Check className="h-4 w-4 mr-2" />
                                Copiado!
                            </>
                            ) : (
                            <>
                                <Copy className="h-4 w-4 mr-2" />
                                Copiar
                            </>
                            )}
                        </Button>
                        <Button
                            asChild
                            variant="outline"
                            className="h-12 px-4 rounded-md border-slate-200 hover:bg-slate-50 text-slate-600 cursor-pointer"
                        >
                            <a href={shortUrl} target="_blank" rel="noopener noreferrer">
                                <ArrowRight className="h-5 w-5" />
                            </a>
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
            )}
          </TabsContent>

          <TabsContent value="stats">
            {/* Stats Form */}
            <Card className="border-none shadow-2xl shadow-emerald-900/5 bg-white/80 backdrop-blur-xl ring-1 ring-white/50 rounded-md">
              <CardContent className="p-8">
                <form onSubmit={handleStatsSubmit} className="space-y-6">
                  <div className="space-y-2">
                    <label htmlFor="stats-input" className="text-sm font-medium text-slate-600 ml-1">
                      Insira o código ou a URL curta
                    </label>
                    <div className="relative group">
                      <Input
                        id="stats-input"
                        type="text"
                        placeholder="Ex: abc12345 ou curt.in/abc12345"
                        required
                        value={statsCode}
                        onChange={(e) => setStatsCode(e.target.value)}
                        className="h-14 px-5 rounded-md bg-white border-slate-200 focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 transition-all text-lg shadow-sm group-hover:border-emerald-300"
                      />
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400">
                        <BarChart2 className="h-5 w-5 opacity-0 group-focus-within:opacity-100 transition-opacity text-emerald-500" />
                      </div>
                    </div>
                  </div>

                  <Button 
                    type="submit" 
                    className="w-full h-14 rounded-md text-lg font-semibold bg-slate-900 hover:bg-slate-800 text-white shadow-lg transition-all hover:scale-[1.01] active:scale-[0.99] cursor-pointer" 
                    disabled={statsLoading}
                  >
                    {statsLoading ? (
                      <div className="flex items-center gap-2">
                        <Loader2 className="h-5 w-5 animate-spin" />
                        <span>Buscando...</span>
                      </div>
                    ) : (
                      <div className="flex items-center gap-2">
                        <span>Ver Estatísticas</span>
                        <ArrowRight className="h-5 w-5" />
                      </div>
                    )}
                  </Button>
                </form>

                {statsError && (
                  <div className="mt-6 p-4 bg-red-50 border border-red-100 rounded-md flex items-center gap-3 text-red-600 animate-in fade-in slide-in-from-top-2">
                    <div className="bg-red-100 p-1 rounded-full">
                      <span className="font-bold px-2">!</span>
                    </div>
                    <p className="text-sm font-medium">{statsError}</p>
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Stats Result */}
            {statsData && (
               <div className="mt-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                     <Card className="bg-white border-emerald-100 shadow-sm rounded-md p-4 flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                        <div className="p-3 bg-emerald-100 text-emerald-600 rounded-full mb-3">
                           <BarChart2 className="h-6 w-6" />
                        </div>
                        <span className="text-3xl font-bold text-slate-800">{statsData.accessCount}</span>
                        <span className="text-xs text-slate-500 uppercase tracking-wider font-semibold mt-1">Acessos Totais</span>
                     </Card>

                     <Card className="bg-white border-emerald-100 shadow-sm rounded-md p-4 flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                        <div className="p-3 bg-blue-100 text-blue-600 rounded-full mb-3">
                           <Clock className="h-6 w-6" />
                        </div>
                        <span className="text-sm font-medium text-slate-800">{formatDate(statsData.lastAccess)}</span>
                        <span className="text-xs text-slate-500 uppercase tracking-wider font-semibold mt-1">Último Acesso</span>
                     </Card>

                     <Card className="bg-white border-emerald-100 shadow-sm rounded-md p-4 flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                        <div className="p-3 bg-purple-100 text-purple-600 rounded-full mb-3">
                           <Globe className="h-6 w-6" />
                        </div>
                        <span className="text-sm font-medium text-slate-800">{formatDate(statsData.createdAt)}</span>
                        <span className="text-xs text-slate-500 uppercase tracking-wider font-semibold mt-1">Criado Em</span>
                     </Card>
                  </div>

                  <Card className="bg-white border-slate-200 shadow-sm rounded-md p-6">
                     <h3 className="text-sm font-semibold text-slate-500 uppercase tracking-wider mb-2">URL Original</h3>
                     <div className="bg-slate-50 p-3 rounded-md border border-slate-200 font-mono text-sm text-slate-700 break-all flex items-center gap-2">
                        <Link className="h-4 w-4 text-slate-400 shrink-0" />
                        {statsData.originalUrl}
                     </div>
                  </Card>
               </div>
            )}

          </TabsContent>
        </Tabs>
        
        <footer className="text-center pt-8 pb-4">
          <p className="text-sm text-slate-400">
            &copy; {new Date().getFullYear()} Curtin. Designed for efficiency.
          </p>
        </footer>
      </div>
    </div>
  );
}
