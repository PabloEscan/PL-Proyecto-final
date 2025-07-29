package models;

public class ResultadosAlgoritmos {
    private String algoritmo;
    private int celdasRecorridas;
    private long tiempoMs;

    public ResultadosAlgoritmos(String algoritmo, int celdasRecorridas, long tiempoMs) {
        this.algoritmo = algoritmo;
        this.celdasRecorridas = celdasRecorridas;
        this.tiempoMs = tiempoMs;
    }
    public String getAlgoritmo() { 
        return algoritmo; 
    }
    public int getCeldasRecorridas() { 
        return celdasRecorridas; 
    }
    public long getTiempoMs() { 
        return tiempoMs; 
    }
}
