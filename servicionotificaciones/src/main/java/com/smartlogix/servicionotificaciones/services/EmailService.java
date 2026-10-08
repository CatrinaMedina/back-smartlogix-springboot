package com.smartlogix.servicionotificaciones.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        System.out.println("========== CORREO SIMULADO SMARTLOGIX ==========");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + cuerpo);
        System.out.println("================================================");
    }
}