package mapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import modelo.Sala;

	public class SalaXmlMapper {

	    public Sala fromElement(Element elemento) {
	        try {
	            int id = Integer.parseInt(elemento.getAttribute("id"));
	            String nombre = obtenerTexto(elemento, "nombre");
	            int filas = Integer.parseInt(obtenerTexto(elemento, "filas"));
	            int asientosPorFila =
	                    Integer.parseInt(obtenerTexto(elemento, "asientosPorFila"));
	            String tipo = obtenerTexto(elemento, "tipo");

	            return new Sala(id, nombre, filas, asientosPorFila, tipo);

	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException(
	                    "El id, las filas o los asientos no son números", e);
	        }
	    }

	    public Element toElement(Document documento, Sala sala) {
	        Element elementoSala = documento.createElement("sala");
	        elementoSala.setAttribute("id", String.valueOf(sala.getId()));

	        agregarTexto(documento, elementoSala, "nombre", sala.getNombre());
	        agregarTexto(documento, elementoSala, "filas",
	                String.valueOf(sala.getFilas()));
	        agregarTexto(documento, elementoSala, "asientosPorFila",
	                String.valueOf(sala.getAsientosPorFila()));
	        agregarTexto(documento, elementoSala, "tipo", sala.getTipo());

	        return elementoSala;
	    }

	    private String obtenerTexto(Element padre, String etiqueta) {
	        NodeList elementos = padre.getElementsByTagName(etiqueta);

	        if (elementos.getLength() == 0) {
	            throw new IllegalArgumentException(
	                    "Falta el elemento <" + etiqueta + ">");
	        }

	        return elementos.item(0).getTextContent().trim();
	    }

	    private void agregarTexto(
	            Document documento,
	            Element padre,
	            String etiqueta,
	            String texto) {

	        Element elemento = documento.createElement(etiqueta);
	        elemento.setTextContent(texto);
	        padre.appendChild(elemento);
	    }
}
