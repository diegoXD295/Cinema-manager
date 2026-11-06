package service;

public class ImportacionParcial implements EstrategiaImportacion {

    @Override
    public boolean continuarConErrores(int errores) {
        return true;
    }
}
