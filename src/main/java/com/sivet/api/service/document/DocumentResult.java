package com.sivet.api.service.document;

/**
 * Binario generado más el nombre de archivo sugerido para la descarga.
 */
public record DocumentResult(byte[] content, String filename) {
}
