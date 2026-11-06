package modelo;

import java.math.BigDecimal;

public class EstadisticaPelicula {

    private final int sesionesRealizadas;
    private final int entradasVendidas;
    private final BigDecimal importeTotal;

    public EstadisticaPelicula(int sesionesRealizadas, int entradasVendidas,
            BigDecimal importeTotal) {
        this.sesionesRealizadas = sesionesRealizadas;
        this.entradasVendidas = entradasVendidas;
        this.importeTotal = importeTotal;
    }

    public int getSesionesRealizadas() {
        return sesionesRealizadas;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }
}
