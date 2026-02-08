package br.com.fiap.inteligencia_service.integrations;

import br.com.fiap.inteligencia_service.clients.InsumoServiceClient;
import br.com.fiap.inteligencia_service.dto.InsumoDto;
import br.com.fiap.inteligencia_service.integrations.interfaces.InsumoIntegration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InsumoIntegrationImpl implements InsumoIntegration {

    private final InsumoServiceClient client;

    public InsumoIntegrationImpl(InsumoServiceClient client) {
        this.client = client;
    }

    @Override
    public InsumoDto buscarInsumoPorId(UUID idInsumo) {
        return client.buscarInsumoPorId(idInsumo);
    }
}
