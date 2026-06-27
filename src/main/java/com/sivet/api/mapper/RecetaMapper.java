package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Receta;
import com.sivet.api.domain.entity.RecetaItem;
import com.sivet.api.dto.request.RecetaItemRequest;
import com.sivet.api.dto.response.RecetaItemResponse;
import com.sivet.api.dto.response.RecetaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapeo de {@link Receta} y sus líneas embebidas. El enlace con la atención y el
 * tenant los resuelve el servicio.
 */
@Component
public class RecetaMapper {

    public List<RecetaItem> toItemEntities(List<RecetaItemRequest> items) {
        return items.stream()
                .map(i -> new RecetaItem(
                        i.medicamento(),
                        i.dosis(),
                        i.via(),
                        i.duracion(),
                        i.indicaciones()))
                .toList();
    }

    public RecetaResponse toResponse(Receta r) {
        List<RecetaItemResponse> items = r.getItems().stream()
                .map(i -> new RecetaItemResponse(
                        i.getMedicamento(),
                        i.getDosis(),
                        i.getVia(),
                        i.getDuracion(),
                        i.getIndicaciones()))
                .toList();
        return new RecetaResponse(
                r.getId(),
                r.getAtencion() != null ? r.getAtencion().getId() : null,
                items
        );
    }
}
