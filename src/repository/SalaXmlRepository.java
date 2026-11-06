package repository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import excepciones.PersistenciaException;
import mapper.SalaXmlMapper;
import modelo.Sala;

public class SalaXmlRepository implements Repository<Sala, Integer> {

	private final Path ruta;
	private final SalaXmlMapper mapper;

	public SalaXmlRepository(Path ruta) {
		this.ruta = ruta;
		this.mapper = new SalaXmlMapper();
	}

	@Override
	public List<Sala> findAll() throws PersistenciaException {
		Document documento = leerDocumento();
		NodeList nodos = documento.getDocumentElement().getElementsByTagName("sala");

		List<Sala> salas = new ArrayList<>();

		for (int i = 0; i < nodos.getLength(); i++) {
			Element elemento = (Element) nodos.item(i);

			try {
				salas.add(mapper.fromElement(elemento));
			} catch (IllegalArgumentException e) {
				System.err.printf("Error en la sala %d: %s%n", i + 1, e.getMessage());
			}
		}

		return salas;
	}

	@Override
	public Optional<Sala> findById(Integer id) throws PersistenciaException {
		return findAll().stream().filter(sala -> sala.getId() == id).findFirst();
	}

	@Override
	public Sala save(Sala sala) throws PersistenciaException {
		Document documento = leerDocumento();
		Element raiz = documento.getDocumentElement();
		Element nuevoElemento = mapper.toElement(documento, sala);
		Element existente = buscarElementoPorId(documento, sala.getId());

		if (existente == null) {
			raiz.appendChild(nuevoElemento);
		} else {
			raiz.replaceChild(nuevoElemento, existente);
		}

		guardarDocumento(documento);
		return sala;
	}

	@Override
	public boolean deleteById(Integer id) throws PersistenciaException {
		Document documento = leerDocumento();
		Element existente = buscarElementoPorId(documento, id);

		if (existente == null) {
			return false;
		}

		documento.getDocumentElement().removeChild(existente);
		guardarDocumento(documento);
		return true;
	}

	private Document leerDocumento() throws PersistenciaException {
		try {
			DocumentBuilder constructor = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			if (Files.exists(ruta)) {
				Document documento = constructor.parse(ruta.toFile());

				if (!"salas".equals(documento.getDocumentElement().getTagName())) {
					throw new PersistenciaException("La raíz del XML debe llamarse <salas>", null);
				}

				return documento;
			}

			Document documento = constructor.newDocument();
			documento.appendChild(documento.createElement("salas"));
			return documento;

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new PersistenciaException("No se pudo leer el archivo XML de salas: " + ruta, e);
		}
	}

	private void guardarDocumento(Document documento) throws PersistenciaException {
		try {
			Path carpeta = ruta.toAbsolutePath().getParent();

			if (carpeta != null) {
				Files.createDirectories(carpeta);
			}

			Transformer transformador = TransformerFactory.newInstance().newTransformer();
			transformador.setOutputProperty(OutputKeys.INDENT, "yes");
			transformador.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			try (OutputStream salida = Files.newOutputStream(ruta)) {
				transformador.transform(new DOMSource(documento), new StreamResult(salida));
			}

		} catch (TransformerException | IOException e) {
			throw new PersistenciaException("No se pudo guardar el archivo XML de salas: " + ruta, e);
		}
	}

	private Element buscarElementoPorId(Document documento, int id) {
		NodeList nodos = documento.getDocumentElement().getElementsByTagName("sala");

		for (int i = 0; i < nodos.getLength(); i++) {
			Element elemento = (Element) nodos.item(i);

			try {
				Sala sala = mapper.fromElement(elemento);

				if (sala.getId() == id) {
					return elemento;
				}
			} catch (IllegalArgumentException e) {
				System.err.printf("Error en la sala %d: %s%n", i + 1, e.getMessage());
			}
		}

		return null;
	}
}
