package poo.modelo;

import java.math.BigDecimal;

/** CC2008, Ejercicio 6. Construye doce conexiones ficticias al iniciar. */
public final class CargaInicial {
    private CargaInicial() { }

    public static CatalogoIntegraciones crearCatalogo() {
        CatalogoIntegraciones catalogo = new CatalogoIntegraciones();
        catalogo.agregar(new TarjetaCredito("TAR-01", "Tarjeta Maya", "https://tarjeta-maya.example/api", 120, true));
        catalogo.agregar(new TarjetaCredito("TAR-02", "Tarjeta Quetzal", "https://tarjeta-quetzal.example/api", 180, true));
        catalogo.agregar(new TransferenciaBancaria("TRA-01", "Banco del Valle Demo", "https://banco-valle.example/api", 250, true));
        catalogo.agregar(new TransferenciaBancaria("TRA-02", "Banco del Norte Demo", "https://banco-norte.example/api", 420, false));
        catalogo.agregar(new BilleteraDigital("BIL-01", "Billetera Ceiba", "https://billetera-ceiba.example/api", 85, true));
        catalogo.agregar(new BilleteraDigital("BIL-02", "Billetera Jade", "https://billetera-jade.example/api", 95, true));
        catalogo.agregar(new RastreoLogistico("LOG-01", "Rastreo Volcán", "https://rastreo-volcan.example/api", 160, true, new BigDecimal("20.00"), new BigDecimal("5.00")));
        catalogo.agregar(new RastreoLogistico("LOG-02", "Rastreo Petén", "https://rastreo-peten.example/api", 300, true, new BigDecimal("25.00"), new BigDecimal("6.00")));
        catalogo.agregar(new LogisticaDocumental("GUI-01", "Envíos Pacífico", "https://envios-pacifico.example/api", 210, true, new BigDecimal("18.00"), new BigDecimal("4.00")));
        catalogo.agregar(new LogisticaDocumental("GUI-02", "Envíos Altiplano", "https://envios-altiplano.example/api", 270, true, new BigDecimal("22.00"), new BigDecimal("5.50")));
        catalogo.agregar(new CertificadorFel("FEL-01", "FEL Central Demo", "https://fel-central.example/api", 140, true));
        catalogo.agregar(new CertificadorFel("FEL-02", "FEL Sierra Demo", "https://fel-sierra.example/api", 390, false));
        return catalogo;
    }
}
