package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;

public interface ContratoAlquilerService {
    List<ContratoAlquiler> listar();
    ContratoAlquiler guardar(ContratoAlquiler contratoAlquiler);
    ContratoAlquiler buscarPorId(Integer id);
    void eliminar(Integer id);
    ContratoAlquiler actualizar(Integer id,ContratoAlquiler contrato);
    public void activarContrato(Integer id);
    public void finalizarContrato(Integer id);
    public byte[] generarPdf(Integer idContrato);
    List<ContratoAlquiler> buscarPorCliente(Integer idCliente);
    ContratoAlquiler buscarPorReserva(Integer idReserva);
    ContratoAlquiler generarDesdeReserva(Integer idReserva);
}
