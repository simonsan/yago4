package org.yago.yago4.converter.utils;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RDFBinaryFormatTest {

  @Test
  void readAndWrite() throws IOException {
    YagoValueFactory valueFactory = YagoValueFactory.getInstance();
    List<Statement> Statements = List.of(
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/url"), valueFactory.createIRI("http://bar")),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/url"), valueFactory.createIRI("http://www.wikidata.org/entity/Q12")),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/parent"), valueFactory.createBNode()),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/name"), valueFactory.createLiteral("foo")),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/name"), valueFactory.createLiteral("foo", "en")),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/height"), valueFactory.createLiteral("1", valueFactory.createIRI("xsd:integer"))),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/height"), valueFactory.createLiteral(BigInteger.valueOf(1))),
            valueFactory.createStatement(valueFactory.createIRI("http://foo"), valueFactory.createIRI("http://schema.org/latitude"), valueFactory.createLiteral(12.0d))
    );

    Path file = Files.createTempFile("test", "binary");
    RDFBinaryFormat.write(Statements.stream(), file);
    assertEquals(Statements, RDFBinaryFormat.read(valueFactory, file).collect(Collectors.toList()));
  }

  @Test
  void numericEncodedIriNamespace() {
    YagoValueFactory valueFactory = YagoValueFactory.getInstance();
    IRI iri = valueFactory.createIRI("http://www.wikidata.org/entity/Q42");
    assertEquals("http://www.wikidata.org/entity/", iri.getNamespace());
    assertEquals("Q42", iri.getLocalName());
  }

  @Test
  void someEqualities() {
    YagoValueFactory valueFactory = YagoValueFactory.getInstance();
    assertEquals(valueFactory.createLiteral("foo"), valueFactory.createLiteral("foo", XMLSchema.STRING));
    assertEquals(valueFactory.createLiteral(1.0), valueFactory.createLiteral("1.0", XMLSchema.DOUBLE));
    assertEquals(valueFactory.createLiteral(BigInteger.valueOf(1)), valueFactory.createLiteral("1", XMLSchema.INTEGER));
  }
}
