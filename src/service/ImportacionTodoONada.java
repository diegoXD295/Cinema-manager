package service;

public class ImportacionTodoONada implements EstrategiaImportacion {

    @Override
    public boolean continuarConErrores(int errores) {
        return errores == 0;
    }
}
