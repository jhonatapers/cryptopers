package com.jhonatapers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ResourceUtils {

    public static File criaArquivo(String nomeArquivo) {
        try {
            URL resource = ResourceUtils.class.getClassLoader().getResource("");
            if (resource == null) {
                throw new IOException("Pasta resources não encontrada.");
            }
            Path path = Paths.get(resource.toURI()).resolve(nomeArquivo);

            return new File(path.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verificaSeArquivoExisteEmResources(String nomeArquivo) {
        return lerArquivo(nomeArquivo) != null;
    }

    public static String salvarObjetoComoJson(Object obj, String nomeArquivo) {
        try {
            final String _nomeArquivo = "%s.json".formatted(nomeArquivo);
            new ObjectMapper().writeValue(criaArquivo(_nomeArquivo), obj);
            return _nomeArquivo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T lerObjetoAPartirDeUmJson(Class<T> clazz, String nomeArquivo) {
        try {
            InputStream inputStream = lerArquivo(nomeArquivo);

            if (inputStream == null)
                return null;

            return new ObjectMapper().readValue(inputStream, clazz);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> arquivosComExtensao(String extensao) {

        try {
            Path DiretorioResource = Paths.get(ClassLoader.getSystemResource("").toURI());

            List<String> fileList = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(DiretorioResource, "*" + extensao)) {
                for (Path entry : stream)
                    fileList.add(entry.getFileName().toString());
            }

            return fileList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String lerArquivoTexto(String nomeArquivo) {

        try {
            InputStream inputStream = lerArquivo(nomeArquivo);
            if (inputStream == null)
                return null;

            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream lerArquivo(String nomeArquivo) {
        return ResourceUtils.class.getClassLoader().getResourceAsStream(nomeArquivo);
    }

}
