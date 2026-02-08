package br.com.fiap.inteligencia_service.integrations;

import br.com.fiap.inteligencia_service.clients.EstabelecimentoSaudeServiceClient;
import br.com.fiap.inteligencia_service.dto.EstabelecimentoSaudeDto;
import br.com.fiap.inteligencia_service.integrations.interfaces.EstabelecimentoSaudeIntegration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EstabelecimentoSaudeIntegrationImpl implements EstabelecimentoSaudeIntegration {

    private final EstabelecimentoSaudeServiceClient client;

    public EstabelecimentoSaudeIntegrationImpl(EstabelecimentoSaudeServiceClient client) {
        this.client = client;
    }

    @Override
    public EstabelecimentoSaudeDto buscarEstabelecimentoPorId(UUID idEstabelecimento) {
        return client.buscarEstabelecimentoPorId(idEstabelecimento);
    }
}
