package com.parceiroferramentas.api.parceiro_api.model.pedido;

public enum STATUS_PEDIDO {
    //ALUGUEL E COMPRA
    PENDENTE, 
    APROVADO, 
    FINALIZADO, 
    CANCELADO, 
    DEVOLVIDO, 
    ESTRAVIADO, 
    ATRASADO,
    REEMBOLSADO, 

    //VENDA
    EM_ROTA, 
    PAGAMENTO_RECUSADO, 
    RECUSADO_CLIENTE, 

    //ALUGUEL
    RETIDO, 
    RESERVADO, 
    ALUGADO, 
    BLOQUEADO, 
    PENDENCIA_CLIENTE
}
