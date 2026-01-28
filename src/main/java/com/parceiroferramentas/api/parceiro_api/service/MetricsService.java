package com.parceiroferramentas.api.parceiro_api.service;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class MetricsService {
    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void registrarPedidoCriado(String tipo) {
        Counter.builder("pedidos.criados.total")
            .tag("tipo", tipo)  // COMPRA ou ALUGUEL
            .description("Total de pedidos criados")
            .register(meterRegistry)
            .increment();
    }

    // Timer (tempo de execução)
    public void medirTempoCriacaoPedido(Runnable operacao) {
        Timer timer = Timer.builder("pedidos.criacao.tempo")
            .description("Tempo para criar um pedido")
            .register(meterRegistry);

        timer.record(operacao);
    }

    // Gauge (valor atual)
    public void atualizarUsuariosOnline(int quantidade) {
        Gauge.builder("usuarios.online", () -> quantidade)
            .description("Número de usuários online")
            .register(meterRegistry);
    }
}
