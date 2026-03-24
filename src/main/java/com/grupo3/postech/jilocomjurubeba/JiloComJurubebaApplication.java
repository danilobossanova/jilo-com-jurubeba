package com.grupo3.postech.jilocomjurubeba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe JiloComJurubebaApplication.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@SpringBootApplication(scanBasePackages = "com.grupo3.postech.jilocomjurubeba")
public class JiloComJurubebaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiloComJurubebaApplication.class, args);
    }
}
