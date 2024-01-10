package com.Analisador.fuzzy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.Analisador.fuzzy.model.Retorno;
import com.Analisador.fuzzy.repository.RetornoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Analisador.fuzzy.model.Mensagem;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProcessadorFuzzy {

    @Autowired
    private RetornoRepository repository;

    @PostMapping("/Analisando")
    public String resposta(@ModelAttribute("frase") Mensagem frase, RedirectAttributes attributes, Model model) {
        String resposta = avaliarSentimento(frase.getFrase());
        Retorno retorno = new Retorno(frase.getFrase(), resposta);
        repository.save(retorno);
        return "redirect:/Resposta";
    }

    @GetMapping("/Resposta")
    public ModelAndView exibirResposta(Model model) {
        Iterable<Retorno> retornos = repository.findAll();
        repository.deleteAll();
        model.addAttribute("retornos", retornos);
        return new ModelAndView("Resposta.html");
    }


   private static String avaliarSentimento(String texto) {

        //verificando se o texto esta vazio
        if (texto == null || texto.trim().isEmpty()) {
            return "Texto vazio. Nada para analisar.";
        }

        // Define as palavras-chave positivas
        String[] palavrasPositivas = {
                "perfeito", "excelente", "ótimo", "maravilhoso", "fantástico", "bomdemais", "atendente atencioso", "atendimento top", "estou muito feliz", "bom",
                "feliz", "felicidade", "está tudo bem", "alegria", "muitobom", "gostoso", "alegre", "tranquilo", "suave", "levando de boa",
                "satisfeito", "otimo", "bem", "extraordinário", "ótimo", "gratificante", "fenomenal", "radiante",
                "impressionante", "notável", "genial", "excelente qualidade", "rápido", "eficiente", "perfeitamente", "fabuloso", "encantador", "fantasia",
                "triunfante", "excelente escolha", "sorridente", "ótima experiência", "fácil", "encantado", "fenomenal", "fantasia", "incrível",
                "agradável", "divertido", "maravilha", "bem-vindo", "empolgado", "adorável", "fantástico",
                "agradável surpresa", "incrível oportunidade", "ótimo começo", "maravilhoso dia", "sucesso", "realização", "bem feito",
                "confortável", "apaixonado", "fascinante", "satisfação garantida", "melhoria", "excelente desempenho", "rápido",
                "muito bom", "sem problemas", "orgulhoso", "paz de espírito", "conquista", "sensacional", "vitória", "sucesso",
                "fantástico atendimento", "surpreendente", "amor", "carinho", "amigável",
                "muito satisfeito", "excelente", "maravilhoso", "ótimosuporte", "soluçãoperfeita", "recomendo",
                "superior", "impecável", "adoração", "elogios", "melhorescolha", "perfeição", "grande alegria",
                "celebração", "eficaz", "excelentequalidadedeservico", "prestativo", "criatividade",
                "inovação", "imaculado", "super feliz", "admirável", "incrível",
                "conforto", "muito satisfeito", "admirado", "melhordoqueoesperado", "muitobomdesempenho", "muitoeficaz",
                "ótimo desempenho", "surpreendenteatendimentoaocliente", "incrívelsuporte", "satisfeitoalemdasexpectativas", "eficiênciaincrível",
                "excepcional", "ótimovalor", "melhordoquenunca", "completamentesatisfeito", "otimofeedback", "recomendado",
                "incomparável", "granderealizacao", "fantasticaqualidade", "absolutamentemaravilhoso", "incrivelatendimentoaocliente",
                "surpreendentesuporte", "elogiável", "insuperável", "fantasticoresultado", "soluçãoincrivel", "perfeitoemtodososaspectos",
                "melhordoquequalqueroutro", "totalmenteimpressionado", "fenomenaldesempenho", "muitoeficiente", "otimotratamento", "bemfeito",
                "atendimentoimpecavel", "excelenteresposta", "maravilhososuporte", "melhorpreco", "incríveldesempenho", "grandeexperiencia", "absolutamente incrível",
                "ótimoproduto", "valorexcepcional", "altamenterecomendado", "excelentequalidadedeproduto", "superioridade", "admirávelrealizacao", "impressionanteserviço",
                "maravilhosoatendimentoaocliente", "perfeitamentesatisfeito", "altamenteeficaz", "fantásticodesempenho", "recomendacaomerecida", "fenomenalsuporte",
                "elogiosmerecidos", "excelenteassistência", "supersatisfeito", "magnífico", "eficáciaexcepcional", "ótimasrecomendações", "esplêndido", "excelenteresultadofinal",
                "qualidadesuperior", "superlativo", "fantásticoatendimentoaocliente", "tratamentoexcepcional", "supereficiente", "qualidadeimpecável",
                "serviçoexcepcional", "perfeitoemtodossentidos", "absolutamenteexcepcional", "inigualávelqualidade", "elogiosmerecidos", "satisfaçãocompleta",
                "realizaçãonotável", "desempenhoimpressionante", "excelentedesempenho", "absolutamentemaravilhoso", "completamenteeficaz", "fantásticotratamento", "recomendaçãobrilhante",
                "fantásticaqualidadedeserviço", "serviçosuperior", "muitoapreciado", "atendimentoaoclienteinacrível", "soluçãoexcelente", "valorimbatível", "altamenteeficiente",
                "impressionante", "incrível", "ótimosuportetécnico", "perfeitoproduto", "qualidadeexcepcional", "eficáciaimpressionante",
                "resultados", "maravilhoso", "altamenteeficaz", "desempenhoexcepcional", "altamenterecomendável", "ajudaincrível",
                "altamentesatisfeito", "suportebrilhante", "excepcionalmentebemfeito", "qualidadesuperior doproduto", "serviçoexcepcional", "recomendaçãoexcepcional",
                "excelentequalidadedeatendimento", "fantásticoserviçoaocliente", "tratamentoexcepcional", "supereficaz", "produtoexcepcional", "valorexcepcional",
                "altamenteeficaz", "desempenhoexcepcional", "recomendaçãojusta", "fantásticosuportetécnico", "qualidadeexcepcionaldoproduto", "ajudaincrível", "muitosatisfeito",
                "atendimentoincrível", "recomendaçãomerecida", "soluçãoexcepcional", "valorimensurável", "altamenteeficaz", "respostafenomenal", "excelenteassistênciatécnica",
                "produtoperfeito", "qualidadeinsuperável", "fantásticoresultado", "serviçoimpressionante", "qualidadeexcepcionaldeatendimento", "magníficoatendimentoaocliente", "tratamentoexcelente",
                "supereficaz", "produtoimpecável", "valorinigualável", "altamenteeficiente", "respostaimpressionante", "assistênciaincrível", "produtodequalidadesuperior",
                "serviçoexcepcional", "recomendaçãobrilhante", "fantásticaqualidadedeproduto", "atendimentoaoclientesuperior", "muitoapreciado", "ajudaexcepcional",
                "atendimentoaoclienteinacrível", "soluçãobrilhante", "valorincomparável", "altamenteeficaz", "respostafenomenal", "excelentesuportetécnico",
                "produtoexcepcional", "qualidadeinsuperável", "resultadosurpreendente", "serviçofenomenal", "qualidadeexcepcionaldeatendimentoaocliente", "atendimentoaoclientemagnífico", "tratamentoexcelente",
                "supereficaz", "produtoimpecável", "valorinestimável", "altamenteeficaz", "respostaincrível", "assistênciaexcepcional", "produtodequalidadesuperior",
                "serviçoexcepcional", "recomendaçãomerecida", "qualidadedeprodutoexcepcional", "atendimentoaoclientedefirstaclasse", "muitoapreciado", "ajudaexcepcional", "atendimentoaoclienteexcepcional",
                "soluçãoinovadora", "valorexcepcional", "altamenteeficaz", "respostaexcepcional", "suportebrilhante", "produtoperfeito", "qualidadeinigualável", "resultadoincrível",
                "serviçoexcepcional", "qualidadedeatendimentoexcepcional", "atendimentoaoclientesuperior", "muitoapreciado", "ajudaexcepcional", "atendimentoaoclienteinovador", "tratamentoexcelente",
                "supereficaz", "produtoimpecável", "valorinigualável", "altamenteficiente", "respostafenomenal", "assistênciabrilhante", "produtodequalidadesuperior", "serviçoexcepcional", "recomendaçãoinovadora", "qualidadedeprodutoexcepcional",
                "atendimentoaoclientedefirstaclasse", "muitoapreciado", "ajudaexcepcional", "atendimentoaoclienteexcepcional", "soluçãonotável", "valorexcepcional", "altamenteeficaz", "respostasurpreendente",
                "suporteinovador", "produtoperfeito", "qualidadeinsuperável", "resultadofenomenal", "serviçoexcepcional", "qualidadedeatendimentoexcepcional", "muitoapreciado", "ajudaexcepcional", "atendimentoaoclienteinovador", "tratamentoexcelente",
                "supereficaz", "produtoimpecável", "valorinigualável", "altamenteficiente", "respostaexcepcional", "assistênciainovadora", "produtodequalidadesuperior", "serviçoexcepcional", "recomendaçãoexcepcional", "qualidadedeprodutoexcepcional",
                "atendimentoaoclientedefirstaclasse", "muitoapreciado"};


        String[] palavrasNegativas = {
                "merda","horrivel", "naofunciona", "pessimo", "assustado", "naorecebinada", "Bloquearam",
                "atendenteburro", "atendenteburra", "servicolixo", "servicodemerda", "naoqueromais", "naoecerto",
                "naoavisa", "demora", "péssimo", "insatisfeito", "irritado", "estoumuitoirritado", "atendimentoruim", "naoresolvenada",
                "não", "triste", "ruim", "desapontado", "estresse", "estressante", "corrido", "horrível", "terrível", "desgastante",
                "aborrecido", "estressante", "inaceitavel", "desastroso", "incompetente", "frustrante", "desperdício", "inaceitável",
                "caótico", "irritante", "insuficiente", "lamentável", "problematico", "desanimador", "desconfortável",
                "péssima experiência", "desapontamento", "preocupante", "fiasco", "desconforto", "incomodado", "chateado", "chateação", "desprezo",
                "terrível", "insatisfatório", "lamentável", "ineficaz",
                "problemático", "desagradável", "desapontador", "inaceitável", "frustrante",
                "insatisfatório", "ineficiente", "insuportável", "desastroso", "irritante",
                "decepcionante", "inútil", "negligente", "desapontador", "ruim",
        };


        HashSet<String> setPositivas = new HashSet<>();
        HashSet<String> setNegativas = new HashSet<>();

        for (String palavra : palavrasPositivas) {
            setPositivas.add(palavra.toLowerCase());
        }

        for (String palavra : palavrasNegativas) {
            setNegativas.add(palavra.toLowerCase());
        }

        int contPositivas = 0;
        int contNegativas = 0;

        String[] palavras = texto.split("\\s+");

        for (String palavra : palavras) {
            if (setPositivas.contains(palavra.toLowerCase())) {
                contPositivas++;
            } else if (setNegativas.contains(palavra.toLowerCase())) {
                contNegativas++;
            }
        }
        System.out.println("Quantidade de palavras positivas-> "+contPositivas);
        System.out.println("Quantidade de palavras negativas-> "+contNegativas);

        // adiciona um valor aleatorio para simbolizar uma heuristica ou peso
        double valorPositivas = contPositivas * 3;
        double valorNegativas = contNegativas * -3.7;

        if(contNegativas > contPositivas) {

            double sentimento = valorNegativas;


            String descricaoSentimento = interpretarSentimento(sentimento);

            return descricaoSentimento;

        }else if(contNegativas < contPositivas) {
            double sentimento = valorPositivas;


            String descricaoSentimento = interpretarSentimento(sentimento);

            return descricaoSentimento;
        }

        else {

            String descricaoSentimento = "Neutro";
            return descricaoSentimento;

        }


    }


    // Função para interpretar o sentimento
    private static String interpretarSentimento(double sentimento) {

        String	sentimentoResultado =  FuzzificarSentimento(sentimento);

        return sentimentoResultado;
    }

    private static String FuzzificarSentimento(double sentimento) {
        double pertinenciaPositivo = funcaoPertinenciaPositivo(sentimento);
        double pertinenciaNegativo = funcaoPertinenciaNegativo(sentimento);
        double pertinenciaMuitoNegativo = funcaoPertinenciaMuitoNegativo(sentimento);

        System.out.println("sentimento->"+sentimento);
        System.out.println("pertinenciaPositivo ->" +pertinenciaPositivo+"%");
        System.out.println("pertinenciaNegativo ->" +pertinenciaNegativo+"%");
        System.out.println("pertinenciaMuitoNegativo ->" +pertinenciaMuitoNegativo+"%");

        // Verificando qual categoria tem pertinência não nula e retornando o sentimento correspondente
        if ((pertinenciaPositivo > 0.8 && pertinenciaPositivo > 0) && funcaoPertinenciaMuitoAlto(pertinenciaPositivo)) {
            return "Muito Feliz";
        }else if((pertinenciaPositivo < 0.6 && pertinenciaPositivo > 0) && funcaoPertinenciaAlto(pertinenciaPositivo)) {
            return "Feliz";
        }else if((pertinenciaPositivo < 0.4 && pertinenciaPositivo > 0)  &&  funcaoPertinenciaBaixo(pertinenciaPositivo)) {
            return "Bem";
        }


        else if (pertinenciaMuitoNegativo > -0.1  &&  funcaoPertinenciaNegativoMuitoAlto(pertinenciaMuitoNegativo) ){
            return "Muito Chateado /muito insatisfeito";
        } else if (pertinenciaNegativo > -0.2  && funcaoPertinenciaNegativoAlto(pertinenciaNegativo) ){
            return "Chateado / insatisfeito";
        }else if (pertinenciaNegativo > -0.9  && funcaoPertinenciaNegativoBaixo(pertinenciaNegativo)) {
            return "insatisfeito";
        }


        else {
            return "Sentimento Indefinido";
        }

    }





    private static double funcaoPertinenciaMuitoPositivo(double valor) {
        if (valor > 10) {
            return 1 / valor;
        }
        return 0;
    }

    private static double funcaoPertinenciaPositivo(double valor) {
        if (valor >= 0 && valor <= 10) {
            return 1 / valor;
        }
        return 0;
    }

    private static double funcaoPertinenciaNegativo(double valor) {

        if (valor < 0 && valor >= -19) {
            return (1 / valor);
        }

        return 0;
    }

    private static double funcaoPertinenciaMuitoNegativo(double valor) {
        if (valor <= -20) {
            return (1 / valor);
        }
        return 0;
    }

    /**********************************Positivo*********************************************************/
    private static boolean funcaoPertinenciaBaixo(double valor) {
        if (valor >= 0 && valor <= 0.5) {
            return true;
        }
        return false;
    }

    private static boolean funcaoPertinenciaAlto(double valor) {
        if (valor >= 0.5 && valor <= 0.8) {
            return true;
        }
        return false;
    }

    private static boolean funcaoPertinenciaMuitoAlto(double valor) {
        if (valor > 0.8 && valor <= 1) {
            return true;
        }
        return false;
    }

/******************************************************************************************/

    /**********************************Negativo*********************************************************/
    private static boolean funcaoPertinenciaNegativoBaixo(double valor) {
        if (valor > -0.5) {
            return true;
        }
        return false;
    }

    private static boolean funcaoPertinenciaNegativoAlto(double valor) {
        if (valor >= -0.2 && valor <= -0.4) {
            return true;
        }
        return false;
    }

    private static boolean funcaoPertinenciaNegativoMuitoAlto(double valor) {
        if (valor > -0.1) {
            return true;
        }
        return false;
    }





}
