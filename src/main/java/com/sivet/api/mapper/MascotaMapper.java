package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.MascotaRequest;
import com.sivet.api.dto.response.MascotaResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Mascota} y sus DTOs. Dueño y tenant los resuelve el servicio.
 */
@Component
public class MascotaMapper {

    public Mascota toEntity(MascotaRequest req, Cliente cliente, Clinica clinica) {
        Mascota m = new Mascota();
        m.setNombre(req.nombre());
        m.setEspecie(req.especie());
        m.setRaza(req.raza());
        m.setSexo(req.sexo());
        m.setEdad(req.edad());
        m.setPeso(req.peso());
        m.setColor(req.color());
        m.setFoto(req.foto());
        m.setEsterilizada(Boolean.TRUE.equals(req.esterilizada()));
        m.setMicrochip(req.microchip());
        m.setVacunacion(req.vacunacion());
        m.setAlergias(req.alergias());
        m.setNotas(req.notas());
        m.setCliente(cliente);
        m.setClinica(clinica);
        return m;
    }

    public MascotaResponse toResponse(Mascota m) {
        return new MascotaResponse(
                m.getId(),
                m.getNombre(),
                m.getEspecie(),
                m.getRaza(),
                m.getSexo(),
                m.getEdad(),
                m.getPeso(),
                m.getColor(),
                m.getCliente().getId(),
                m.getFoto(),
                m.isEsterilizada(),
                m.getMicrochip(),
                m.getVacunacion(),
                m.getAlergias(),
                m.getNotas()
        );
    }
}
